package wirth.lagerverwaltung;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class Bestand extends AppCompatActivity {
    ArrayList<String> antwortstring;

    //Bestand-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestand);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);
        //Class zum Abrufen der Artikelliste aufrufen
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
            Intent i = new Intent(Bestand.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_bestellungen) {
            Intent i = new Intent(Bestand.this, Bestellungen.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_logout) {
            Intent i = new Intent(Bestand.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Anzeigen der Artikelliste aus der Datenbank in einem ListView
    public void ListView_anzeigen() {
        //ListView zusammenbauen und Werte eintragen
        ArrayAdapter<String> mAktienlisteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,antwortstring);
        ListView antwortlisteListView = (ListView) findViewById(R.id.listview1);
        antwortlisteListView.setAdapter(mAktienlisteAdapter);
    }

    //Zurück-Button überschrieben und auf Menue Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Bestand.this, Menue.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung für abrufen der Artikelliste aufbauen und Daten holen
    public class Conn extends AsyncTask<String, String, ArrayList<String>> {

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

            ArrayList<String> antwort = new ArrayList<String>();
            //Verbindung zum Server aufbauen und Datenbankabfrage durchführen
            try {
                CallHandler callHandler = new CallHandler();
                Client client = new Client(ip, 1234, callHandler, new GZipFilter());
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                antwort = testService.Artikelbestand();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return antwort;
        }

        @Override
        protected void onPostExecute(ArrayList<String> antwort) {
            if (antwort != null) {
                antwortstring = antwort;
                //Methode ListView_anzeigen aufrufen
                ListView_anzeigen();
            }
        }
    }
}
