package wirth.lagerverwaltung;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class BestellungenDetail extends AppCompatActivity {
    ArrayList<String> antwortstringbestelldetail;
    String bestellNr;
    String artikelNr, hersteller, model, anzahl;
    TextView tvbestNr,tvNachname,tvVorname;
    Bundle korbdetail, korbdetail2;
    String antwortstringanzahlgesamt,nachname,vorname;

    //BestellungenDetail-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestellungen_detail);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        tvbestNr = (TextView) findViewById(R.id.tvBestellNr);
        tvNachname = (TextView) findViewById(R.id.tvNachname);
        tvVorname = (TextView) findViewById(R.id.tvVorname);
        //Daten aus der vorherigen Activity übernehmen und in Variablen speichern
        Bundle zielkorb = getIntent().getExtras();
        bestellNr = zielkorb.getString("bestellNr");
        nachname = zielkorb.getString("nachname");
        vorname = zielkorb.getString("vorname");
        //Texte der TextViews setzen
        tvbestNr.setText("Bestell Nummer: "+bestellNr);
        tvNachname.setText("Nachname: "+nachname);
        tvVorname.setText("Vorname: "+vorname);
        //Class zum Abrufen der Bestellungsdaten aufrufen
        Conn con = new Conn();
        con.execute();
    }

    //OptionsMenü erstellen und zuweisen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //gedrücktes Menü-Item auslesen und einer Aktivity oder Funktion zuweisen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_aktualisieren) {
            Conn con = new Conn();
            con.execute();
            return true;
        }
        if (id == R.id.menu_menue) {
            Intent i = new Intent(BestellungenDetail.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_bestellungen) {
            Intent i = new Intent(BestellungenDetail.this, Bestellungen.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_logout) {
            Intent i = new Intent(BestellungenDetail.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Anzeigen der Artikel in der Bestellungen aus der Datenbank in einem ListView
    public void BestellungenDetail_anzeigen() {
        //ListView zusammenbauen und Werte eintragen
        ArrayAdapter<String> mAktienlisteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,antwortstringbestelldetail);
        ListView bestelllisteListView = (ListView) findViewById(R.id.listview3);
        bestelllisteListView.setAdapter(mAktienlisteAdapter);

        bestelllisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String aktienInfo = (String) bestelllisteListView.getItemAtPosition(position);
                //Den String nach ' absuchen und zuschneiden
                String[] artikel = aktienInfo.split("'");
                artikelNr = artikel[1];
                hersteller = artikel[3];
                model = artikel[5];
                anzahl = artikel[9];
                //Puffer für Hersteller auf internen Speicher schreiben
                try {
                    FileOutputStream foshersteller = openFileOutput("HerstellerPuffer.txt", Context.MODE_PRIVATE);
                    foshersteller.write(hersteller.getBytes());
                    foshersteller.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Puffer für Model auf internen Speicher schreiben
                try {
                    FileOutputStream fosmodel = openFileOutput("ModelPuffer.txt", Context.MODE_PRIVATE);
                    fosmodel.write(model.getBytes());
                    fosmodel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Puffer für Anzahl auf internen Speicher schreiben
                try {
                    FileOutputStream fosanzahl = openFileOutput("AnzahlPuffer.txt", Context.MODE_PRIVATE);
                    fosanzahl.write(anzahl.getBytes());
                    fosanzahl.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Puffer für ArtikelNr  auf internen Speicher schreiben
                String stringadmin = artikelNr;
                try {
                    FileOutputStream fosadmin = openFileOutput("ArtikelNrPuffer.txt", Context.MODE_PRIVATE);
                    fosadmin.write(stringadmin.getBytes());
                    fosadmin.close();
                    Scanner_ausfuehren();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void anzahlgesamt_holen() {
        //Class zum Abrufen der Gesamtanzahl des Artikels aufrufen
        ConnAnzahl conAnzahl = new ConnAnzahl();
        conAnzahl.execute();
    }

    public void Kontrolle_anzeigen() {
        //Variable an nächste Activity weitergeben
        String[] Anzahl_gesamt = antwortstringanzahlgesamt.split(";");
        String anzahl_gesamt = Anzahl_gesamt[3];
        //Hersteller aus Puffer im internen Speicher abrufen
        try {
            FileInputStream fin = openFileInput("HerstellerPuffer.txt");
            int size;
            String herstellerPuffer = "";

            while ((size = fin.read()) != -1) {
                herstellerPuffer += Character.toString((char) size);
            }
            hersteller = herstellerPuffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Model aus Puffer im internen Speicher abrufen
        try {
            FileInputStream fin = openFileInput("ModelPuffer.txt");
            int size;
            String modelPuffer = "";

            while ((size = fin.read()) != -1) {
                modelPuffer += Character.toString((char) size);
            }
            model = modelPuffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Anzahl aus Puffer im internen Speicher abrufen
        try {
            FileInputStream fin = openFileInput("AnzahlPuffer.txt");
            int size;
            String anzahlPuffer = "";

            while ((size = fin.read()) != -1) {
                anzahlPuffer += Character.toString((char) size);
            }
            anzahl = anzahlPuffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Datenübergabe an nächste Activity vorbereiten
        korbdetail = new Bundle();
        korbdetail.putString("artikelNr", artikelNr);
        korbdetail.putString("hersteller", hersteller);
        korbdetail.putString("model", model);
        korbdetail.putString("anzahl", anzahl);
        korbdetail.putString("BestNr", bestellNr);
        korbdetail.putString("nachname", nachname);
        korbdetail.putString("vorname", vorname);
        korbdetail.putString("anzahl_gesamt", anzahl_gesamt);
        //Nächste Activity aufrufen und Daten übergeben
        Intent i = new Intent(BestellungenDetail.this, BestellungAusbuchenKontrolle.class);
        i.putExtras(korbdetail);
        startActivity(i);
    }

    //Scanner ausführen
    public void Scanner_ausfuehren() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    //Daten vom Scanner abrufen und weiter bearbeiten
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //Puffer für ArtikelNr auf internen Speicher schreiben
            try {
                FileInputStream fin = openFileInput("ArtikelNrPuffer.txt");
                int size;
                String artikelNrPuffer = "";

                while ((size = fin.read()) != -1) {
                    artikelNrPuffer += Character.toString((char) size);
                }
                artikelNr = artikelNrPuffer;
            } catch (IOException e) {
                e.printStackTrace();
            }

            String scanContent = scanningResult.getContents();
            if (scanContent != null) {
                //Daten vom Scanner mit vorhandener ArtikelNr auf gleichheit Prüfen
                if (scanContent.equals(artikelNr)) {
                    //Methode anzahlgesamt_holen aufrufen
                    anzahlgesamt_holen();
                } else {
                    Toast.makeText(BestellungenDetail.this, "Artikelnummern sind verschieden", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Zurück-Button überschrieben und auf Bestellungen Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(BestellungenDetail.this, Bestellungen.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung für abrufen der Artikel in der Bestellungen aufbauen und Daten holen
    public class Conn extends AsyncTask<String, String, ArrayList<String>> {

        ArrayList<String> antwortbestelldetail;

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
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
                TestService testService2 = null;
                testService2 = (TestService) client.getGlobal(TestService.class);
                antwortbestelldetail = testService2.BestellungenDetail(bestellNr);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return antwortbestelldetail;
        }

        @Override
        protected void onPostExecute(ArrayList<String> antwortbestelldetail) {
            int Anzahl;
            if (antwortbestelldetail != null) {
                Anzahl = antwortbestelldetail.size();
                //Prüfen ob Bestellung keine Artikel mehr enthält, um zu Bestellungen zurück zu leiten
                if (Anzahl == 0) {
                    Toast.makeText(BestellungenDetail.this, "Bestellung abgeschlossen", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(BestellungenDetail.this, Bestellungen.class);
                    startActivity(i);
                    finish();
                }else{
                    antwortstringbestelldetail = antwortbestelldetail;
                    //Methode BestellungenDetail_anzeigen aufrufen
                    BestellungenDetail_anzeigen();
                }
            }
        }
    }

    //Datenbankverbindung für abrufen der Anzahl des Artikels aufbauen und Daten holen
    public class ConnAnzahl extends AsyncTask<String, String, String> {

        String antwortanzahlgesamt;

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
                TestService testService2 = null;
                testService2 = (TestService) client.getGlobal(TestService.class);
                antwortanzahlgesamt = testService2.Anzahl_gesamt(artikelNr);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return antwortanzahlgesamt;
        }

        @Override
        protected void onPostExecute(String antwortanzahlgesamt) {
            if (antwortanzahlgesamt != null) {
                antwortstringanzahlgesamt = antwortanzahlgesamt;
                //Methode Kontrolle_anzeigen aufrufen
                Kontrolle_anzeigen();
            }
        }
    }
}
