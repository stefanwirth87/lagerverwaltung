package testserver;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.Timer;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.handler.CallLookup;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.IServerListener;
import lipermi.net.Server;
import testcommon.TestService;


import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Limbo
 */
public class ServerGUI extends javax.swing.JFrame implements testcommon.TestService{

    CallHandler callHandler;
    String User;
    Timer timer = null;
    
    public String letsDoIt() {
    System.out.println("letsDoIt() done.");
    Socket clientSocket = CallLookup.getCurrentSocket();
    System.out.println("My client: " + clientSocket.getRemoteSocketAddress());
    return "server saying hi";
  }
    
    public int SeverStatus()
    {
       int r = 0;
       r=r+1;
       int DB = MySQLConnection.DatenbankStatus();
       r=r+(DB*10);
       return r; 
    }
  
   public String[][] Alle() 
   {
    String[][] alle = MySQLConnection.printlist();
    return alle;
  }
   
   public String[][] einen_kudnen(String suche) 
   {
    String[][] alle = MySQLConnection.search_einen_kunden(suche);
    return alle;
  }
   
    public String[][] search(String suche) 
   {
    String[][] gefunden = MySQLConnection.search(suche);

    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Methode search wurde aufgerufen. Parameter: "+suche;
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");

    return gefunden;
  }
    
    public String[][] searchManuellausbuchen(String von, String bis, String name, String kommentar) 
   {
    String[][] gefunden = MySQLConnection.sucheManuellAusgebucht(von, bis, name, kommentar);
    return gefunden;
  }
   
     public int Login(String user, String passwort) {
    int code = MySQLConnection.Login_abfrage(user, passwort);
    User = user;
    //Zeitstempel aufrufen
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Methode Login wurde von " +user+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return code;
  }
     
     public int LoginMD5(String user, String passwort) {
    int code = MySQLConnection.Login_md5(user, passwort);
    User = user;
    //Zeitstempel aufrufen
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Methode LoginMD5 wurde von " +user+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return code;
  }
     
    public int get_id(String user, String passwort) {
    int code = MySQLConnection.MySQL_id(user, passwort);
    return code;
  }
    
    public int get_idMD5(String user, String passwort) {
    int code = MySQLConnection.MySQL_idMD5(user, passwort);
    return code;
  }
    
     public int get_recht(String user, String passwort) {
    int code = MySQLConnection.MySQL_recht(user, passwort);
    return code;
  }
     
     public int get_rechtMD5(String user, String passwort) {
    int code = MySQLConnection.MySQL_rechtMD5(user, passwort);
    return code;
  }
      
  public  String insert_Kunde_neu(String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email)
  {
  
    String r = MySQLConnection.insert_kunde(Anrede, Nachname, Vorname, Strasse, Hausnummer, plz, Ort, Telefon, email);
      
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Neuer Kunde " +Nachname+" wurde angelegt";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
    return r;
   }
   
  public  String update_Kunden(int Kunden_id, String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email)
  {
    String r = MySQLConnection.update_kunde(Kunden_id, Anrede, Nachname, Vorname, Strasse, Hausnummer, plz, Ort, Telefon, email);
      
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Kunde " +Nachname+" wurde geändert";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
    return r;
   }
  
  /*Für die Artikel*/
  
  public String[][] AlleArtikel() 
   {
    String[][] alle = MySQLConnection.printlistArtikel();
    return alle;
  }
  
   public String[][] BestellungeneinesKunden(String kundennr) 
   {
    String[][] alle = MySQLConnection.printlistBestellungeneinesKunden(kundennr);
    return alle;
  }
  
  public  String insert_artikel_neu(String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid)
  {
  
    String r = MySQLConnection.insert_artikel(hersteller, model, beschreibung, anzahl, preis, lagerid);
      
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Neuer Artikel " +model+" wurde angelegt";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
    return r;
   }
  
  public  String update_artikel(int artnr, String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid)
  {
    //System.out.println(Kunden_id + Anrede + Nachname + Vorname + Strasse + Hausnummer + plz + Ort + Telefon + email);
  
    String r = MySQLConnection.update_artikel(artnr, hersteller, model, beschreibung, anzahl, preis, lagerid);
      
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Artikel " +artnr+" wurde geändert";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
    return r;
   }
  
  public String[][] searchArtikel(String suche) 
   {
    String[][] gefunden = MySQLConnection.searcharticle(suche);

    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Methode searchArtikel wurde aufgerufen. Parameter: "+suche;
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");

    return gefunden;
  }
  
    public String[][] AllePositionen(String bestellid) 
   {
    String[][] alle = MySQLConnection.printlistPositionen(bestellid);
    return alle;
  }
    
    public String[][] AlleManuellAusgebuchtenArtikel() 
   {
    String[][] alle = MySQLConnection.printlistManuellAusgebucht();
    return alle;
  }
    
    public int get_Status(String bestellnr)
    {
       int status = MySQLConnection.getStatus(bestellnr);
        return status; 
    }
  
    public  String update_KundenBestellung(String Bestell_ID, String Kunden_Nr)
  {
    String r = MySQLConnection.update_KundenBestellung(Bestell_ID, Kunden_Nr);
      
    return r;
   }
  
  
   public String[][] aktBestellungen(int auswahl) 
   {
    String[][] alle = MySQLConnection.printlistBestellungen(auswahl);
    
    
    return alle;
  }
   
   //Suche nach Bestellungen
   public String[][] sucheBestellung(String von, String bis, String suchbegriff) 
   {
    String[][] alle = MySQLConnection.searchBestellung(von, bis, suchbegriff);
    return alle;
  }
          
          
  
  public String[] freiLagerplaetze(){
    
       String[] alle = MySQLConnection.freiLager();
    return alle;
      
  }
  
    public String[] namendesManuellenausbuchen(){
    
    String[] alle = MySQLConnection.namenManuellausbuchen();
    return alle;  
  }
   
    public  String loeschen_kunden(int kunden_id, String Nachname)
  {
    String r = MySQLConnection.loeschen_kunde(kunden_id);
      
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Kunde " +Nachname+" wurde gelöscht";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
    return r;
   }
    
    /*-->Für Benutzerverwaltung<--*/
    
   public String[][] AlleBenutzer() 
   {
    String[][] alle = MySQLConnection.printlistUser();
    return alle;
  }
   
   public  String insertBenutzer(String Name, String Passwort, String Recht)
  {
    String r = MySQLConnection.insertUser(Name, Passwort, Recht);
    return r;
   }
   
     public  String updateBenutzer(String UserID, String Name, String Passwort, String Recht)
  {
  
    String r = MySQLConnection.updateUser(UserID, Name, Passwort, Recht);
    return r;
   }
     
        public  String loescheBenutzer(String UserID)
  {
  
    String r = MySQLConnection.deleteUser(UserID);
    return r;
   }
     
     
    /*-->Für App<--*/
    
    public ArrayList<String> Artikelbestand() {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Artikelbestand wurde von " +User+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return MySQLConnection.Artikelbestand();
  }
    
    public ArrayList<String> Bestellungen() {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Bestellungen wurde von " +User+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return MySQLConnection.Bestellungen();
  }
  
    public ArrayList<String> BestellungenDetail(String bestellNr) {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Es wurde die Bestellung "+bestellNr+" von " +User+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return MySQLConnection.BestellungenDetail(bestellNr);
  }
    
    public String Anzahl_gesamt(String art_nr) {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Es wurde die Gesamt-Anzahl des Artikels "+art_nr+" von " +User+ " aufgerufen";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    return MySQLConnection.Anzahl_gesamt(art_nr);
    }
    
    public String manuell_ausbuchen(int user_id, int art_nr, int menge, String kommentar) {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Es wurden "+menge+" Stück des Artikels "+art_nr+" von " +User+ " manuell ausgebucht";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    String alle = MySQLConnection.manuell_ausbuchen(user_id, art_nr, menge, kommentar);
    return alle;
    }
    
    public String Artikel_ausbuchen(int user_id, int art_nr, int menge, int best_nr) {
    String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
    String ausgabe = aktZeit + "Es wurden von der Bestellung "+best_nr+" "+menge+" Stück des Artikels "+art_nr+" von " +User+ " ausgebucht";
    ServerFunktionen.schreibe_Log(ausgabe);
    jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
    String alle = MySQLConnection.Artikel_ausbuchen(user_id, art_nr, menge, best_nr);
    return alle;
    }
    
    public String neue_Bestellung(String kdnr)
    {
       String alle = MySQLConnection.neueBestellung(kdnr);
       return alle; 
    }
    
    public String insert_Positin(String[][] positionen, String bestellnr, String [] zuloeschen, int status){
    
    String alle = MySQLConnection.insertPositionen(positionen, bestellnr, zuloeschen,status);
    return alle;
      
  }
    
    public ServerGUI() 
    {
    Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
    setIconImage(icon);
    initComponents();
    //ImageIcon bild = new ImageIcon("ic_lagerverwaltung32.gif"); 
    //this.setIconImage(bild.getImage());
  
    jTextArea1.setText("Creating Server\n");
    Server server = new Server();
    jTextArea1.setText(jTextArea1.getText()+"Creating CallHandler\n");
    callHandler = new CallHandler();
    try {
      jTextArea1.setText(jTextArea1.getText()+"Registrating implementation\n");
      callHandler.registerGlobal(TestService.class, this);
      jTextArea1.setText(jTextArea1.getText()+"Binding\n");
      String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
      String ausgabe = aktZeit + "Server ist gestartet";
      ServerFunktionen.schreibe_Log(ausgabe);
      jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
      
      server.addServerListener(new IServerListener() {
        public void clientConnected(Socket socket) {
            String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
            String ausgabe = aktZeit + "Client connected: "+ socket.getInetAddress();
            ServerFunktionen.schreibe_Log(ausgabe);
            jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
        }

        public void clientDisconnected(Socket socket) {
          String aktZeit = ServerFunktionen.getaktuelleZeit() + ": ";
          String ausgabe = aktZeit + "Client disconnected: "+ socket.getInetAddress() + " --> " + User +"\n";
          ServerFunktionen.schreibe_Log(ausgabe);
          jTextArea1.setText(jTextArea1.getText() +ausgabe+"\n");
          //jTextArea1.setText(jTextArea1.getText() +aktZeit+ "Client disconnected: " + socket.getInetAddress() + " --> " + User +"\n");
        }
      });
      server.bind(1234, callHandler, new GZipFilter());
      
      jLabel2.setText("Server Bereit");  
    } catch (LipeRMIException e) {
      // TODO Auto-generated catch block
      
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      JOptionPane.showMessageDialog(null, "Der Server wurde Neugestartet!", "ServerGUI", JOptionPane.INFORMATION_MESSAGE);
      //e.printStackTrace();
    }    
    }

 class Task extends TimerTask
{
  public void run()
  {
    int code;
     code =  MySQLConnection.MySQL_recht("Limbacher", "Limbacher");
     
     //System.out.println("Server aktiv - Datenbank aktiv: "+name); 
     
     String online = ServerFunktionen.getaktuelleZeit() + ": " + "Server aktiv - Datenbank aktiv: "+code;
     jtonline.setText(online+"\n");
  }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jbausschalten = new javax.swing.JButton();
        jtonline = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmNeustart = new javax.swing.JMenuItem();
        jmEinstellungen = new javax.swing.JMenuItem();
        jmAusschalten = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Anwendung");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Server Anwendung");
        jLabel1.setToolTipText("");

        jLabel2.setText("Login Aktiv:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jbausschalten.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ausschalten.gif"))); // NOI18N
        jbausschalten.setText("Ausschalten");
        jbausschalten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbausschaltenActionPerformed(evt);
            }
        });

        jtonline.setText("Online: ?");

        jMenu1.setText("ServerGUI");

        jmNeustart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.gif"))); // NOI18N
        jmNeustart.setText("Neustart");
        jmNeustart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmNeustartActionPerformed(evt);
            }
        });
        jMenu1.add(jmNeustart);

        jmEinstellungen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/settings.gif"))); // NOI18N
        jmEinstellungen.setText("Einstellungen");
        jmEinstellungen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmEinstellungenActionPerformed(evt);
            }
        });
        jMenu1.add(jmEinstellungen);

        jmAusschalten.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ausschalten.gif"))); // NOI18N
        jmAusschalten.setText("Ausschalten");
        jmAusschalten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmAusschaltenActionPerformed(evt);
            }
        });
        jMenu1.add(jmAusschalten);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtonline, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(90, 90, 90)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbausschalten)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(jLabel2))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtonline))
                    .addComponent(jbausschalten))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
    
     java.util.Timer timer = new java.util.Timer();

    // nach 10 Sek geht's los und dann alle 10 Sekunden
    timer.schedule( new Task(), 10000, 10000 );
        
    }//GEN-LAST:event_formWindowOpened

    private void jbausschaltenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbausschaltenActionPerformed
              // jbausschalten
    
      System.exit(0);
        
        
    }//GEN-LAST:event_jbausschaltenActionPerformed

    private void jmNeustartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmNeustartActionPerformed
        // TODO add your handling code here:
        
        int ok = JOptionPane.showConfirmDialog(null, "Möchten Sie den Server wirklich Neustarten?", "ServerGUI", JOptionPane.YES_NO_OPTION);
        if (ok == 0)
        {
        ServerGUI SG = new ServerGUI();
        this.setVisible(false);
        this.dispose();
        SG.setVisible(true);
        }
    }//GEN-LAST:event_jmNeustartActionPerformed

    private void jmAusschaltenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmAusschaltenActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jmAusschaltenActionPerformed

    private void jmEinstellungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmEinstellungenActionPerformed
        // TODO add your handling code here:
        // Öffnen des Einstellungen Fenster
      
        ServerEinstellungen SE = new ServerEinstellungen();
       
        JLabel l = new JLabel("Eingabe Master Passwort");
        JPasswordField jtpasswortfeld = new JPasswordField(10);
        
        JPanel JP = new JPanel();
        
        JP.setLayout((new GridLayout(2,1,10,20)));
        
        add(JP);
        JP.add(l);
        JP.add(jtpasswortfeld);
        
        JOptionPane.showMessageDialog(null,JP,"Passwort eingabe",JOptionPane.QUESTION_MESSAGE);

        char[] zeichen = jtpasswortfeld.getPassword();
        String ep = SE.build_md5(new String(zeichen));
        
        if (ep.equals(SE.auslesen_masterpw()))
        {
            SE.setVisible(true); 
            
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Eingabe ist nicht korrekt", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jmEinstellungenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerGUI().setVisible(true);
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbausschalten;
    private javax.swing.JMenuItem jmAusschalten;
    private javax.swing.JMenuItem jmEinstellungen;
    private javax.swing.JMenuItem jmNeustart;
    private javax.swing.JLabel jtonline;
    // End of variables declaration//GEN-END:variables
}
