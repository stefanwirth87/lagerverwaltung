package wirth.lagerverwaltung;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class Login extends Activity {

    EditText edtuserid, edtpass;
    Button btnlogin, btneinstellungen;
    ProgressBar pbbar;

    //Login Activity erstellen und starten
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btneinstellungen = (Button) findViewById(R.id.btnEinstellung);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        edtuserid.requestFocus();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            //Benutzername und Passwort mit Datenbank abgleichen und Login ausführen
            @Override
            public void onClick(View v) {
                new Conn().execute();
            }
        });

        btneinstellungen.setOnClickListener(new View.OnClickListener() {
            //Masterpasswort-Eingabe aufrufen
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Login_Einstellungen.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Menü erstellen und zuweisen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    //Funktionen für Menüpunkte zuweisen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_einstellung) {
            Intent i = new Intent(Login.this, Login_Einstellungen.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Serververbindung aufbauen und Datenbankabfragen ausführen
    public class Conn extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        //Wenn Login erfolgreich werden die Rechte des Benutzers abgerufen
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Login.this,r,Toast.LENGTH_SHORT).show();
            //Class zum auslesen der Userrechte aufrufen
            if(isSuccess) {
                new Recht().execute();
            }
        }

        //Zugangsdaten für Datenbank aus File auslesen und Connenction aufbauen
        //Benutzername und Passwort aus Eingabefelder an Datenbank schicken und auf Antwort warten
        @Override
        protected String doInBackground(String... params) {
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
                //Client client = new Client("172.18.1.58", 1234, callHandler, new GZipFilter());
                TestService testService = (TestService) client.getGlobal(TestService.class);
                int antwort = testService.Login(userid,password);
                //Wenn die Datenbank eine "1" zurück liefert, ist der Login erfolgreich ansonsten gibt es eine Fehlermeldung
                if(antwort == 1)
                {
                    z = "Login erfolgreich";
                    isSuccess=true;
                }
                else
                {
                    z = "Benutzername oder Passwort falsch";
                    isSuccess = false;
                }
                client.close();
            } catch (Exception error) {
                z = "Fehler beim Verbindungsaufbau mit Server";
            }
            return z;
        }
    }

    //Datenbankverbindung für Benutzerrechte aufbauen und Daten holen
    public class Recht extends AsyncTask<String, String, String> {
        String r = "";
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        //Class zum auslesen der User-ID aufrufen
        @Override
        protected void onPostExecute(String r) {
            new User_ID().execute();
        }

        @Override
        protected String doInBackground(String... params) {
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
                TestService testService = (TestService) client.getGlobal(TestService.class);
                int antwort = testService.get_recht(userid,password);
                String Recht = "recht.txt";
                String recht = Integer.toString(antwort);
                //Benutzerrechte in ein File auf dem internen Speicher ablegen
                try {
                    FileOutputStream fos = openFileOutput(Recht, Context.MODE_PRIVATE);
                    fos.write(recht.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.close();
            } catch (Exception error) {
                r = "Fehler beim Verbindungsaufbau mit Server";
            }
            return r;
        }
    }

    //Datenbankverbindung für Benutzer-ID aufbauen und Daten holen
    public class User_ID extends AsyncTask<String, String, String> {
        String r = "";
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();

        @Override
        protected void onPostExecute(String r) {
            Intent i = new Intent(Login.this, Menue.class);
            startActivity(i);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
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
                //Client client = new Client("172.18.1.58", 1234, callHandler, new GZipFilter());
                TestService testService = (TestService) client.getGlobal(TestService.class);
                int antwort = testService.get_id(userid,password);
                String User_ID = "userid.txt";
                String user_id = Integer.toString(antwort);
                //Benutzer-ID in ein File auf dem internen Speicher ablegen
                try {
                    FileOutputStream fos = openFileOutput(User_ID, Context.MODE_PRIVATE);
                    fos.write(user_id.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.close();
            } catch (Exception error) {
                r = "Fehler beim Verbindungsaufbau mit Server";
            }
            return r;
        }
    }
}
