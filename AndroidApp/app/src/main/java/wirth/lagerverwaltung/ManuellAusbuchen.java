package wirth.lagerverwaltung;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class ManuellAusbuchen extends AppCompatActivity {

    Button btnAusbuchen,btnMehr,btnWeniger,btnSuchen;
    TextView tvHersteller,tvModel,tvAnzahl,tvAnzahl_gesamt;
    EditText etArtNr, etBeschreibung;
    String anzahl, anzahl_gesamt, ArtNr, antwortstringArtNr, hersteller, model, antwortstringAusbuchen, kommentar;
    String user_id = "";

    //ManuellAusbuchen-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuell_ausbuchen);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        btnAusbuchen = (Button) findViewById(R.id.btnkontrolleAusbuchen);
        btnSuchen = (Button) findViewById(R.id.btnsearch);
        btnMehr = (Button) findViewById(R.id.btnmehr);
        btnWeniger = (Button) findViewById(R.id.btnweniger);
        etArtNr = (EditText) findViewById(R.id.editText);
        etBeschreibung = (EditText) findViewById(R.id.etbeschreibung);
        tvHersteller = (TextView) findViewById(R.id.tvHersteller);
        tvModel = (TextView) findViewById(R.id.tvModel);
        tvAnzahl = (TextView) findViewById(R.id.tvAnzahl);
        tvAnzahl_gesamt = (TextView) findViewById(R.id.tvanzahlgesamt);
        etArtNr.requestFocus();

        //Texte der TextViews setzen
        anzahl = "1";
        tvHersteller.setText("Hersteller: ");
        tvModel.setText("Model: ");
        tvAnzahl.setText(anzahl);
        tvAnzahl_gesamt.setText("Anzahl im Lager: ");

        btnAusbuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int anzahl_int = Integer.parseInt(anzahl);
                int anzahl_gesamt_int = Integer.parseInt(anzahl_gesamt);
                kommentar = etBeschreibung.getText().toString();
                String ArtNrkontrolle = etArtNr.getText().toString();
                //Überprüfen ob ein Artikel gewählt wurde
                if (ArtNrkontrolle.equals("")) {
                    Toast.makeText(ManuellAusbuchen.this, "Es wurde kein Artikel ausgewaehlt!", Toast.LENGTH_LONG).show();
                } else {
                    //Überprüfen ob angegebene Anzahl weniger als Gesamt-Anzahl in der Datenbank ist
                    if (anzahl_int <= anzahl_gesamt_int) {
                        //Überprüfen ob ein Kommentar eingetragen wurde
                        if (kommentar.equals("")) {
                            Toast.makeText(ManuellAusbuchen.this, "Es wurde kein Grund für das Ausbuchen angegeben!", Toast.LENGTH_LONG).show();
                        } else {
                            //Class zum Ausbuchen aufrufen
                            new ConnAusbuchen().execute();
                        }
                    } else {
                        Toast.makeText(ManuellAusbuchen.this, "Es soll mehr ausgebucht werden, als im Lager vorhanden!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnSuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtNr = etArtNr.getText().toString();
                //Class zum Suchen der eingebebenen Artikelnummer ausführen
                new ConnArtSuche().execute();
            }
        });

        btnMehr.setOnClickListener(new View.OnClickListener() {
            //Setzt die Anzahl um 1 nach oben
            @Override
            public void onClick(View v) {
                int anzahlInt = Integer.parseInt(anzahl);
                anzahlInt = (anzahlInt + 1);
                anzahl = Integer.toString(anzahlInt);
                tvAnzahl.setText(anzahl);
            }
        });

        btnWeniger.setOnClickListener(new View.OnClickListener() {
            //Setzt die Anzahl um 1 nach unten
            @Override
            public void onClick(View v) {
                int anzahlInt = Integer.parseInt(anzahl);
                if (anzahlInt > 1) {
                    anzahlInt = (anzahlInt - 1);
                }
                anzahl = Integer.toString(anzahlInt);
                tvAnzahl.setText(anzahl);
            }
        });

        // User_ID aus internem Speicher lesen
        String User_ID = "userid.txt";
        try {
            FileInputStream fin = openFileInput(User_ID);
            int size;
            String getid = "";

            while ((size = fin.read()) != -1) {
                getid += Character.toString((char) size);
            }
            user_id = getid;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Suche_anzeigen() {
        //Antwort der Datenbank überprüfen
        if (antwortstringArtNr.equals("Error!!")) {
            Toast.makeText(ManuellAusbuchen.this, "Artikel Nummer gibt es nicht", Toast.LENGTH_LONG).show();
        } else {
            //Antwort-String zurecht schneiden und in TextViews einsetzen
            String[] ArtNr_Suche = antwortstringArtNr.split(";");
            hersteller = ArtNr_Suche[1];
            model = ArtNr_Suche[2];
            anzahl_gesamt = ArtNr_Suche[3];

            tvHersteller.setText("Hersteller: " + hersteller);
            tvModel.setText("Model: " + model);
            tvAnzahl_gesamt.setText("Anzahl im Lager: " + anzahl_gesamt);
        }
    }

    //OptionsMenü erstellen und zuweisen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menue_logout, menu);
        return true;
    }

    //gedrücktes Menü-Item auslesen und einer Aktivity oder Funktion zuweisen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_Menue_logout_logout) {
            Intent i = new Intent(ManuellAusbuchen.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_Menue_logout_menue) {
            Intent i = new Intent(ManuellAusbuchen.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zurück-Button überschrieben und auf Auslagern Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ManuellAusbuchen.this, Auslagern.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung für Artikel-Suche aufbauen und Daten holen
    public class ConnArtSuche extends AsyncTask<String, String, String> {

        String antwortArtNr;

        @Override
        protected String doInBackground(String... strings) {
            String ip = "";
            String FILENAME = "verbindung.txt";
            //Server-IP aus internem Speicher lesen
            try {
                FileInputStream fin = openFileInput(FILENAME);
                int size;
                String neueIP = "";

                while ((size = fin.read()) != -1) {
                    neueIP += Character.toString((char) size);
                }
                ip = neueIP;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Verbindung zum Server aufbauen und Datenbankabfrage durchführen
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(ip, 1234, callHandler, new GZipFilter());
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                antwortArtNr = testService.Anzahl_gesamt(ArtNr);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return antwortArtNr;
        }

        @Override
        protected void onPostExecute(String antwort) {
            if (antwort != null) {
                antwortstringArtNr = antwort;
                //Methode Suche_anzeigen aufrufen
                Suche_anzeigen();
            }
        }
    }

    //Datenbankverbindung aufbauen und die Stored Procedure "manuell_ausbuchen" ausführen
    public class ConnAusbuchen extends AsyncTask<String, String, String> {

        String z;

        @Override
        protected String doInBackground(String... strings) {
            String ip = "";
            String FILENAME = "verbindung.txt";
            //Server-IP aus internem Speicher lesen
            try {
                FileInputStream fin = openFileInput(FILENAME);
                int size;
                String neueIP = "";

                while ((size = fin.read()) != -1) {
                    neueIP += Character.toString((char) size);
                }
                ip = neueIP;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Strings in Integer umwandeln
            int int_user_id = Integer.parseInt(user_id);
            int int_art_nr = Integer.parseInt(ArtNr);
            int int_anzahl = Integer.parseInt(anzahl);

            //Verbindung zum Server aufbauen und Datenbankabfrage durchführen
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(ip, 1234, callHandler, new GZipFilter());
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                z = testService.manuell_ausbuchen(int_user_id,int_art_nr,int_anzahl,kommentar);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return z;
        }

        @Override
        protected void onPostExecute(String antwort) {
            //Wenn die Antwort der Datenbank "true" ist wird ein Toast angezeigt und alle TextViews wieder zurück gesetzt
            if (antwort.equals("true")) {
                antwortstringAusbuchen = antwort;
                Toast.makeText(ManuellAusbuchen.this, "Artikel erfolgreich ausgebucht", Toast.LENGTH_LONG).show();
                etArtNr.setText("");
                etBeschreibung.setText("");
                anzahl = "1";
                tvHersteller.setText("Hersteller: ");
                tvModel.setText("Model: ");
                tvAnzahl.setText(anzahl);
                tvAnzahl_gesamt.setText("Anzahl im Lager: ");
                etArtNr.requestFocus();
            } else {
                Toast.makeText(ManuellAusbuchen.this, "Fehler beim Ausbuchen!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
