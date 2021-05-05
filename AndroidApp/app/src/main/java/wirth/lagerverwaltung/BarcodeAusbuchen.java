package wirth.lagerverwaltung;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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

public class BarcodeAusbuchen extends AppCompatActivity{

    Button btnAusbuchen,btnMehr,btnWeniger;
    TextView tvArtikelNr,tvHersteller,tvModel,tvAnzahl,tvanzahlgesamt;
    EditText etkommentar;
    String anzahl, artikelNr, hersteller, model, anzahl_gesamt, user_id, kommentar;

    //BarcodeAusbuchen-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_ausbuchen);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        btnAusbuchen = (Button) findViewById(R.id.btnkontrolleAusbuchen);
        btnMehr = (Button) findViewById(R.id.btnmehr);
        btnWeniger = (Button) findViewById(R.id.btnweniger);
        tvArtikelNr = (TextView) findViewById(R.id.tvartikelNr);
        tvHersteller = (TextView) findViewById(R.id.tvHersteller);
        tvModel = (TextView) findViewById(R.id.tvModel);
        tvAnzahl = (TextView) findViewById(R.id.tvAnzahl);
        tvanzahlgesamt = (TextView) findViewById(R.id.tvanzahlgesamt);
        etkommentar = (EditText)  findViewById(R.id.editText2);

        //Daten aus vorhergehender Activity empfangen und in Variablen speichern
        Bundle zielkorbbarcode = getIntent().getExtras();
        artikelNr = zielkorbbarcode.getString("Barcode_artikelNr");
        hersteller = zielkorbbarcode.getString("Barcode_hersteller");
        model = zielkorbbarcode.getString("Barcode_model");
        anzahl_gesamt = zielkorbbarcode.getString("Barcode_anzahlDB");

        //Texte der TextViews setzen
        anzahl = "1";
        tvArtikelNr.setText("Artikel Nummer: "+artikelNr);
        tvHersteller.setText("Hersteller: "+hersteller);
        tvModel.setText("Model: "+model);
        tvanzahlgesamt.setText("Anzahl im Lager: "+anzahl_gesamt);
        tvAnzahl.setText(anzahl);

        btnAusbuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int anzahl_int = Integer.parseInt(anzahl);
                int anzahl_gesamt_int = Integer.parseInt(anzahl_gesamt);
                kommentar = etkommentar.getText().toString();
                //Wenn ausgewählte Anzahl kleiner ist als Gesamt-Anzahl in der Datenbank, wird die Class zum Ausbuchen aufgerufen
                if (anzahl_int <= anzahl_gesamt_int) {
                    new ConnAusbuchen().execute();
                } else {
                    Toast.makeText(BarcodeAusbuchen.this, "Es soll mehr ausgebucht werden, als im Lager vorhanden!", Toast.LENGTH_LONG).show();
                }
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
                if (anzahlInt > 1){
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
            Intent i = new Intent(BarcodeAusbuchen.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_Menue_logout_menue) {
            Intent i = new Intent(BarcodeAusbuchen.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zurück-Button überschrieben und auf Auslagern Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(BarcodeAusbuchen.this, Auslagern.class);
        startActivity(i);
        finish();
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
            int int_art_nr = Integer.parseInt(artikelNr);
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
            //Wenn die Antwort der Datenbank "true" ist wird ein Toast angezeigt und die Auslagern-Activity aufgerufen
            if (antwort.equals("true")) {
                Toast.makeText(BarcodeAusbuchen.this, "Artikel erfolgreich ausgebucht", Toast.LENGTH_LONG).show();
                Intent i = new Intent(BarcodeAusbuchen.this, Auslagern.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(BarcodeAusbuchen.this, "Fehler beim Ausbuchen!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
