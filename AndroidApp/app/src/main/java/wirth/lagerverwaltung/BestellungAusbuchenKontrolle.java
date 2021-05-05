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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class BestellungAusbuchenKontrolle extends AppCompatActivity {

    Button btnAusbuchen,btnMehr,btnWeniger;
    TextView tvArtikelNr,tvHersteller,tvModel,tvAnzahl,tvAnzahl_gesamt;
    String anzahl,artikelNr,bestellNr,nachname,vorname;
    String user_id = "";

    //BestellungenAusbuchenKontrolle-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestellung_ausbuchen_kontrolle);

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
        tvAnzahl_gesamt = (TextView) findViewById(R.id.tvanzahlgesamt);

        //Daten aus der vorherigen Activity übernehmen und in Variablen speichern
        Bundle zielkorbdetail = getIntent().getExtras();
        artikelNr = zielkorbdetail.getString("artikelNr");
        String hersteller = zielkorbdetail.getString("hersteller");
        String model = zielkorbdetail.getString("model");
        anzahl = zielkorbdetail.getString("anzahl");
        String anzahl_gesamt = zielkorbdetail.getString("anzahl_gesamt");
        bestellNr = zielkorbdetail.getString("BestNr");
        nachname = zielkorbdetail.getString("nachname");
        vorname = zielkorbdetail.getString("vorname");

        //Texte der TextViews setzen
        tvArtikelNr.setText("Artikel Nummer: "+artikelNr);
        tvHersteller.setText("Hersteller: "+hersteller);
        tvModel.setText("Model: "+model);
        tvAnzahl.setText(anzahl);
        tvAnzahl_gesamt.setText("Anzahl im Lager: "+anzahl_gesamt);

        btnAusbuchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int anzahl_int = Integer.parseInt(anzahl);
                int anzahl_gesamt_int = Integer.parseInt(anzahl_gesamt);
                //Prüfen ob eingegebene Anzahl größer ist als Gesamtanzahl in Datenbank
                if (anzahl_int <= anzahl_gesamt_int) {
                    //Class zum Ausbuchen aufrufen
                    new ConnAusbuchen().execute();
                } else {
                    Toast.makeText(BestellungAusbuchenKontrolle.this, "Es soll mehr ausgebucht werden, als im Lager vorhanden!", Toast.LENGTH_LONG).show();
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
            Intent i = new Intent(BestellungAusbuchenKontrolle.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_Menue_logout_menue) {
            Intent i = new Intent(BestellungAusbuchenKontrolle.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zurück-Button überschrieben und auf Bestellungen Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(BestellungAusbuchenKontrolle.this, Bestellungen.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung aufbauen und die Stored Procedure "Artikel_ausbuchen" ausführen
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
            int int_bestellNr = Integer.parseInt(bestellNr);

            //Verbindung zum Server aufbauen und Datenbankabfrage durchführen
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(ip, 1234, callHandler, new GZipFilter());
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                z = testService.Artikel_ausbuchen(int_user_id,int_art_nr,int_anzahl,int_bestellNr);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return z;
        }

        @Override
        protected void onPostExecute(String antwort) {
            if (antwort.equals("true")) {
                Toast.makeText(BestellungAusbuchenKontrolle.this, "Artikel erfolgreich ausgebucht", Toast.LENGTH_SHORT).show();
                //Datenübergabe an nächste Activity vorbereiten
                Bundle korbbestNr = new Bundle();
                korbbestNr.putString("bestellNr", bestellNr);
                korbbestNr.putString("nachname", nachname);
                korbbestNr.putString("vorname", vorname);
                //Nächste Activity aufrufen und Daten übergeben
                Intent i = new Intent(BestellungAusbuchenKontrolle.this, BestellungenDetail.class);
                i.putExtras(korbbestNr);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(BestellungAusbuchenKontrolle.this, "Fehler beim Ausbuchen!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
