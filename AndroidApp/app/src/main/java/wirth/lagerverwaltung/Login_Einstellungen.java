package wirth.lagerverwaltung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

public class Login_Einstellungen extends AppCompatActivity {

    String adminpw ="";
    String eingabe_adminpw = "";
    Button enter;
    EditText admin_passwort;

    //Masterpasswort Abfrage Activity erstellen und starten
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__einstellungen);
        enter = (Button) findViewById(R.id.btnlogin_admin);
        admin_passwort = (EditText) findViewById(R.id.edtadminpw);

        auslesen_admin_passwort();
        //Passwort mit Passworteingabe vergleichen und Einstellungen aufrufen
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                eingabe_adminpw = admin_passwort.getText().toString();
                if (eingabe_adminpw.equals (adminpw)) {
                    Intent i = new Intent(Login_Einstellungen.this, Einstellungen.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(Login_Einstellungen.this,"Falsches Passwort",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Zurück-Button überschrieben und auf Login Activity umgeleitet
    @Override
    public void onBackPressed() {
        // Standardaktion (App schließen)
        //super.onBackPressed();
        Intent i = new Intent(Login_Einstellungen.this, Login.class);
        startActivity(i);
        finish();
    }

    //Auslesen des Master-Passworts aus einem File im internen Speicher
    public void auslesen_admin_passwort()
    {
        String FILENAME = "passwort.txt";
        try {
            FileInputStream fin = openFileInput(FILENAME);
            int size;
            String apw = "";

            while ((size = fin.read()) != -1) {
                apw += Character.toString((char) size);
            }
            adminpw = apw;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Login_Einstellungen.this,"Fehler beim lesen der Datei",Toast.LENGTH_SHORT).show();
        }
    }
}
