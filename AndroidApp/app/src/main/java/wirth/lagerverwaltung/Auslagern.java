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
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.FileInputStream;
import java.io.IOException;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class Auslagern extends AppCompatActivity {
    Button btnBarcode, btnManuell;
    String scanContent;
    String antwortstringanzahlgesamt;

    //Auslagern-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auslagern);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        btnBarcode = (Button) findViewById(R.id.btnBarcode);
        btnManuell = (Button) findViewById(R.id.btnManuell);

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Auf Kamera wechseln und Scanner ausführen
                scanner_ausfuehren();
            }
        });

        btnManuell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ManuellAusbuchen-Activity aufrufen
                Intent i = new Intent(Auslagern.this, ManuellAusbuchen.class);
                startActivity(i);
                finish();
            }
        });
    }


    //Zu Kamera wechseln und Scanner ausführen
    public void scanner_ausfuehren(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    //Wenn Scannen erfolgreich die abgerufenen Daten speichern und an BarcodeAusbuchen-Activity übergeben
    public void BarcodeAusbuchen_ausfuehren() {
        String[] Anzahl_gesamt = antwortstringanzahlgesamt.split(";");  //Daten-String zerteilen um einzellne Datensätze zu bekommen und in ein Array speichern
        //Array der reihe nach durchgehen und einzellne Datensätze in Variablen abspeichern
        String artikelNr = Anzahl_gesamt[0];
        String hersteller = Anzahl_gesamt[1];
        String model = Anzahl_gesamt[2];
        String anzahl_gesamt = Anzahl_gesamt[3];

        //Datenübergabe an nächste Activity vorbereiten
        Bundle korbbarcode = new Bundle();
        korbbarcode.putString("Barcode_artikelNr", artikelNr);
        korbbarcode.putString("Barcode_hersteller", hersteller);
        korbbarcode.putString("Barcode_model", model);
        korbbarcode.putString("Barcode_anzahlDB", anzahl_gesamt);

        //Activity BarcodeAusbuchen aufrufen und Daten übergeben
        Intent i = new Intent(Auslagern.this, BarcodeAusbuchen.class);
        i.putExtras(korbbarcode);
        startActivity(i);
        finish();
    }

    //Daten vom Scanner entnehmen und weiter bearbeiten
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            if (scanContent != null) {
                //Datenbankverbindung aufbauen
                Conn con = new Conn();
                con.execute();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
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
            Intent i = new Intent(Auslagern.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.menu_Menue_logout_menue) {
            Intent i = new Intent(Auslagern.this, Menue.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zurück-Button überschrieben und auf Menü Activity umgeleitet
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Auslagern.this, Menue.class);
        startActivity(i);
        finish();
    }

    //Datenbankverbindung für Gesamtanzahl des gesannten Artikels aufbauen und Daten abrufen
    public class Conn extends AsyncTask<String, String, String> {

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
                TestService testService = null;
                testService = (TestService) client.getGlobal(TestService.class);
                antwortanzahlgesamt = testService.Anzahl_gesamt(scanContent);
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
                //Methode BarcodeAusbuchen aufrufen
                BarcodeAusbuchen_ausfuehren();
            }
        }
    }
}
