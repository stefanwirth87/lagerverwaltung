
package verwaltung;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import testcommon.TestService;

/**
 *
 * @author Limbo
 * eigenständiger Task der von der Verwaltung gestartet wird
 * um den Status der Datenbank / Server zu erfahren (unten rechts)
 */

public class Status extends Thread  {
    //Klassenweite variable
    boolean aktiv = true;
    JLabel server, uhrzeit, datenbank;
    SimpleDateFormat s = new SimpleDateFormat("HH:mm");
    TestService myServiceCaller = null;
    int r = 0;
    JFrame aufrufende;

  //konstruktor mit den Übergaben der Objekte aus dem JFRAME
  public Status(String name, JLabel server, JLabel uhrzeit, JLabel datenbank, TestService myServiceCaller, JFrame aufrufende) 
  { 
    super(name);
    this.server=server;
    this.uhrzeit=uhrzeit;
    this.datenbank=datenbank;
    this.myServiceCaller=myServiceCaller;
    this.aufrufende=aufrufende;
  } 
  //äußerliche aufrufbare Methode um den task zu beenden --> wird zurzeit nicht verwendet
  public void setaktiv(boolean aktiv)
  {
      this.aktiv=aktiv;
  }
  // run-Methode - start für den task
  public void run() 
  {
    while(aktiv) 
    {
      try 
      {
       //Uhrzeityp ausgeben
       Date d = new Date();
       uhrzeit.setText(s.format(d));
       //Prüfen des aktuellen Status
       //nachfrage über die vorgegebene Methode
       try
       {
       r = myServiceCaller.SeverStatus();       // Eigene Methode am Server wird dafür verwendet
       }
       catch (Exception e) //auffangen der Verbindungsfehler
       {
           System.out.println("Fehler bei der Übermittlung "+r);
           System.out.println(e);
           r=0;
       }
       
       //auswertung der Rückgabewerte
       switch(r)
       {
           //es kommt garkeine Antwort vom Server
           case 0: 
               server.setText("Server offline"); 
               server.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/offline.gif")));
               datenbank.setText("Datenbank offline");
               datenbank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/offline.gif")));
               JOptionPane.showMessageDialog(null, "Das Programm wurde unerwartet beendet.\nMögliche Fehler:\n-Die Verbindung zum Server wurde beendet\n-Die Netzwerkverbindung wurde getrennt\n\nBitte versuchen sie sich erneut anzumelden.", "Fehler", JOptionPane.ERROR_MESSAGE);
               Login L = new Login();
               aufrufende.dispose();
               L.setVisible(true);
               aktiv=false;
               break;
           //der Server ist Online aber die Verbindung zur Datenbank ist nicht möglich
           case 1:
               server.setText("Server online");
               server.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/online.gif")));
               datenbank.setText("Datenbank offline");
               datenbank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/offline.gif")));
               break;
           //Server und Datenbank ist Online
           case 11:
               server.setText("Server online"); 
               server.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/online.gif")));
               datenbank.setText("Datenbank online"); 
               datenbank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/online.gif")));
               break;
           default: server.setText("Fehler"); datenbank.setText("Fehler");
       }
       sleep(60000); //eine Minute schlafen und erneut ausführen
      } 
      catch (InterruptedException e) 
      {
        System.out.println(e);
      }
    }
  } 
}
