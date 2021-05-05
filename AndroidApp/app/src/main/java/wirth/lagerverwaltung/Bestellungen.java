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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class Bestellungen extends AppCompatActivity {
    ArrayList<String> antwortstringbestell;
    String bestellNr;

    //Bestellungen-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestellungen);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

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
            Intent i = new Intent(Bestellungen.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_bestellungen) {
            Intent i = new Intent(Bestellungen.this, Bestellungen.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_logout) {
            Intent i = new Intent(Bestellungen.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Anzeigen der Bestellungen aus der Datenbank in einem ListView
    public void Bestellungen_anzeigen() {
        //ListView zusammenbauen und Werte eintragen
        ArrayAdapter<String> mAktienlisteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,antwortstringbestell);
        ListView bestelllisteListView = (ListView) findViewById(R.id.listview2);
        bestelllisteListView.setAdapter(mAktienlisteAdapter);

        bestelllisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String aktienInfo = (String) bestelllisteListView.getItemAtPosition(position);
                //Den String nach ' absuchen und zuschneiden
                String[] artikel = aktienInfo.split("'");
                bestellNr = artikel[1];
                String nachname = artikel[3];
                String vorname = artikel[5];
                //Datenübergabe an nächste Activity vorbereiten
                Bundle korb = new Bundle();
                korb.putString("bestellNr", bestellNr);
                korb.putString("nachname", nachname);
                korb.putString("vorname", vorname);
                //Nächste Activity aufrufen und Daten übergeben
                Intent i = new Intent(Bestellungen.this, BestellungenDetail.class);
                i.putExtras(korb);
                startActivity(i);
                finish();
            }
        });
    }

    //Zurück-Button überschrieben und auf Menue Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Bestellungen.this, Menue.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung für abrufen der Bestellungen aufbauen und Daten holen
    public class Conn extends AsyncTask<String, String, ArrayList<String>> {

        ArrayList<String> antwortbestell;

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
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                antwortbestell = testService.Bestellungen();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return antwortbestell;
        }

        @Override
        protected void onPostExecute(ArrayList<String> antwortbestell) {
            if (antwortbestell != null) {
                antwortstringbestell = antwortbestell;
                //Methode Bestellungen_anzeigen aufrufen
                Bestellungen_anzeigen();
            }
        }
    }
}
