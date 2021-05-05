package wirth.lagerverwaltung;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import java.io.FileInputStream;
import java.io.IOException;

public class Menue extends AppCompatActivity {

    Button btnauslagern, btnauftrag, btnbestand;
    String recht;

    //Menü-Activity erstellen und aufrufen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.ic_lagerverwaltung);

        btnauslagern = (Button) findViewById(R.id.btnauslagern);
        btnauftrag = (Button) findViewById(R.id.btnauftrag);
        btnbestand = (Button) findViewById(R.id.btnbestand);

        //User-Recht aus internem Speicher abrufen
        recht = "";
        String Recht = "recht.txt";
        try {
            FileInputStream fin = openFileInput(Recht);
            int size;
            String getrecht = "";

            while ((size = fin.read()) != -1) {
                getrecht += Character.toString((char) size);
            }
            recht = getrecht;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //User-Recht überprüfen und Button zuschalten
        if (recht.equals("1")) {
            btnauslagern.setEnabled(true);
        } else if (recht.equals("2")) {
            btnauslagern.setEnabled(true);
        } else {
            btnauslagern.setEnabled(false);
        }

        btnauslagern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Auslagern-Activity aufrufen
                Intent i = new Intent(Menue.this, Auslagern.class);
                startActivity(i);
                finish();
            }
        });

        btnauftrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bestellungen-Activity aufrufen
                Intent i = new Intent(Menue.this, Bestellungen.class);
                startActivity(i);
                finish();
            }
        });

        btnbestand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bestand-Activity aufrufen
                Intent i = new Intent(Menue.this, Bestand.class);
                startActivity(i);
                finish();
            }
        });
    }

    //OptionsMenü erstellen und zuweisen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    //gedrücktes Menü-Item auslesen und einer Aktivity oder Funktion zuweisen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout_logout) {
            Intent i = new Intent(Menue.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zurück-Button überschrieben und auf Menü Activity umgeleitet
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(Menue.this, Menue.class);
        startActivity(i);
    }
}
