package wirth.lagerverwaltung;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Einstellungen extends AppCompatActivity {

    String ip ="";
    Button speichern_ip, speichern_adminpw;
    EditText ipadresse, admin_passwort;

    //Einstellungen Activity erstellen und starten
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);
        //Implementieren der ActionBar und zuweisen des Icons
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        speichern_ip = (Button) findViewById(R.id.btnSpeichern);
        speichern_adminpw = (Button) findViewById(R.id.btnadminpw_speichern);
        ipadresse = (EditText) findViewById(R.id.edtIP);
        admin_passwort = (EditText) findViewById(R.id.edtAdminpw);

        auslesen_connection();

        speichern_ip.setOnClickListener(new View.OnClickListener() {
            //Speichern der IP-Adresse für die Datenbank-Verbindung
            @Override
            public void onClick(View v){
                speichern_connection();
            }
        });

        speichern_adminpw.setOnClickListener(new View.OnClickListener() {
            //Speichern des Masterpassworts
            @Override
            public void onClick(View v){
                speichern_admin_passwort();
            }
        });
    }

    //Zurück-Button überschrieben und auf Login Activity umgeleitet
    @Override
    public void onBackPressed() {
        // Standardaktion (App schließen)
        //super.onBackPressed();
        Intent i = new Intent(Einstellungen.this, Login.class);
        startActivity(i);
        finish();
    }

    //Neue IP-Adresse in File auf internen Speicher ablegen
    public void speichern_connection()
    {
        String FILENAME = "verbindung.txt";
        String string = ipadresse.getText().toString();
        try {
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
            Toast.makeText(Einstellungen.this,"Neue IP-Adresse erfolgreich gespeichert",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Einstellungen.this,"Speichern fehlgeschlagen",Toast.LENGTH_SHORT).show();
        }
    }

    //IP-Adresse aus File im internen Speicher auslesen und anzeigen
    public void auslesen_connection()
    {
        String FILENAME = "verbindung.txt";
        try {
            FileInputStream fin = openFileInput(FILENAME);
            int size;
            String neueIP = "";

            while ((size = fin.read()) != -1) {
                neueIP += Character.toString((char) size);
            }
            ipadresse.setText(neueIP);
            ip = neueIP;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Neues Masterpasswort in File auf internen Speicher ablegen
    public void speichern_admin_passwort()
    {
        String ADMIN = "passwort.txt";
        String stringadmin = admin_passwort.getText().toString();
        try {
            FileOutputStream fosadmin = openFileOutput(ADMIN, Context.MODE_PRIVATE);
            fosadmin.write(stringadmin.getBytes());
            fosadmin.close();
            Toast.makeText(Einstellungen.this,"Admin Passwort erfolgreich geändert",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Einstellungen.this,"Speichern fehlgeschlagen",Toast.LENGTH_SHORT).show();
        }
    }
}
