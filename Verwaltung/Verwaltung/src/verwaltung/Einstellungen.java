/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltung;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

/**
 *
 * @author limbo
 * Fenster um Einstellungen (Verbindung und Master-Passwort) vorzunehmen
 */
public class Einstellungen extends javax.swing.JFrame {
        
        //Klassenweite variablen
        Client client;
        String ip ="";
        
        //Konstruktor
    public Einstellungen() {   
    Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
    setIconImage(icon);
    initComponents();
    auslesen_connection();   
    }

    //Methode um eine Server Connection aufgebaut wird (nutzt jedes Fenster)
    public TestService connection()
    {
        //liest die verbindung.dat aus
        auslesen_connection();
        //erstellet einen Handler (lipermi) für die ServerConnection
        CallHandler callHandler = new CallHandler();
        //erstellt die variable für den ServiceCaller
        TestService myServiceCaller = null;  
        //Verbindungsaufbau
        try
        {
            //bildet aus dem Port und der IP adresse die Verbindung zum Server
            client = new Client(ip, 1234, callHandler, new GZipFilter());
            myServiceCaller = (TestService) client.getGlobal(TestService.class);           
        }
        catch (Exception ex) //Fehler abfangen
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Keine Verbindung zur Datenbank möglich.\nMögliche Fehler:\n-Die Verbindung zum Server wurde beendet\n-Die Netzwerkverbindung wurde getrennt\n\nDas Programm wurde beendet!", "Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        //this.myServiceCaller=myServiceCaller;
      return myServiceCaller;
    }
    
    //wird selten genutzt (nicht zwingend nötig da der server die Connection 
    //schließt nach beenden des Programms)
    public void connection_close()
    {   
        try
        { 
          client.close();          
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    //speichern der neuen Connection Daten
    public void speichern_connection ()
    {
         try {
             //evtl. vorhandene Datei wird einfach überschrieben
            BufferedWriter bw = new BufferedWriter(new FileWriter("verbindung.txt",false));
            bw.write(jtverbindung.getText());   
            bw.close();
            JOptionPane.showMessageDialog(null, "Änderungen wurden übernommen!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } 
         catch(IOException error) 
         {
            JOptionPane.showMessageDialog(null, "Fehler"+error, "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //auslesen aus der verbindung.txt
    public void auslesen_connection()
    {
         try {
           BufferedReader br = new BufferedReader(new FileReader("verbindung.txt")); 
          ip = br.readLine();
          jtverbindung.setText(ip);
          br.close();
        } 
         catch(IOException error) 
         {
            //falls keine vorhanden ist wird eine angelegt und mit standartwerten gefüttert
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("verbindung.txt",true));
                bw.write("127.0.0.1");   
                //bw.newLine();              
                bw.close();
                JOptionPane.showMessageDialog(null, "Verbindungsdaten nicht gefunden.\nStandartwert: 127.0.0.1 wurde angelegt", "Erfolgreich", JOptionPane.OK_OPTION);
   
            } 
                catch(IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Fehler"+e, "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    
    //auslesen des master-passwortes (mast.dat)
    public String auslesen_masterpw()
    {
        String master="l";
         try {
          BufferedReader br = new BufferedReader(new FileReader("master.dat")); 
          master = br.readLine();
          br.close();
  
        } catch(IOException error) {
 
            try {
                //bei nicht vorhanden sein wird eine neue mast.dat angelegt und mit standart wert gefüttert
                BufferedWriter bw = new BufferedWriter(new FileWriter("master.dat",true));
                bw.write(build_md5("geheim"));                
                bw.close();
                JOptionPane.showMessageDialog(null, "Initialpasswort gesetzt: geheim.\nFür eine bessere Sicherheit des Systems\nsollte dieses geändert werden!", "Erfolgreich", JOptionPane.OK_OPTION);
   
            } 
                catch(IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Fehler"+e, "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
         
         return (master);
    }
    
    //speichern der neuen master Passwortes
    public void schreiben_masterpw()
    {
         try {

            //Message Box erzeugen - anwenden muss nochmal das aktuelle Kennwort eingeben
            JLabel l = new JLabel("Aktuelles Passwort eingaben");
            JPasswordField jtpasswortfeld = new JPasswordField(10);
            JPanel JP = new JPanel();
            JP.setLayout((new GridLayout(2,1,10,20)));
            add(JP);
            JP.add(l);
            JP.add(jtpasswortfeld);

            JOptionPane.showMessageDialog(null,JP,"Passwort eingabe",JOptionPane.QUESTION_MESSAGE);
            
            //eingebenes Passwort auslesen und in MD5 wandeln
            char[] pwabfrage = jtpasswortfeld.getPassword();
            String ep = build_md5(new String(pwabfrage));

            if (auslesen_masterpw().equals(ep))
            {
                //Passwort feld auslesen
                char[] neupw = jpmaster.getPassword();
                BufferedWriter bw = new BufferedWriter(new FileWriter("master.dat",false));
                bw.write(build_md5(new String(neupw)));   
                bw.close();
                JOptionPane.showMessageDialog(null, "Änderungen wurden übernommen!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Eingabe ist nicht korrekt", "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        } catch(IOException error) {
            JOptionPane.showMessageDialog(null, "Fehler"+error, "Erfolgreich", JOptionPane.OK_OPTION);
        }
    }
    
        //bilden der des md5 Hashwertes
    public String build_md5(String insert)
    {
    //variablen deklarieren
    MessageDigest md;
    byte byteData[]=null;
    //Byte mit md5 bilden und abspeichern
    try {
         md = MessageDigest.getInstance("MD5");
         md.update(insert.getBytes());
         byteData = md.digest();
    } catch (NoSuchAlgorithmException ex) {
         Logger.getLogger(Einstellungen.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //byte code in hex umwandeln
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
      sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }
    //hashwert in einen String wandeln und ausgeben - wert durch den return zurückliefern
    String convert = sb.toString();
    System.out.println(convert);
    return (convert);
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
        jtverbindung = new javax.swing.JTextField();
        jtspeichern = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jbclose = new javax.swing.JButton();
        jlmasterpw = new javax.swing.JLabel();
        jpmaster = new javax.swing.JPasswordField();
        jbmasterspeichern = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Einstellungen");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Einstellungen");

        jLabel2.setText("Serververbingung:");

        jtspeichern.setText("Speichern");
        jtspeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtspeichernActionPerformed(evt);
            }
        });

        jLabel3.setText("Hier können Programm einstellungen vorgenommen werden");

        jbclose.setText("Schließen");
        jbclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbcloseActionPerformed(evt);
            }
        });

        jlmasterpw.setText("Master Kennwort:");

        jbmasterspeichern.setText("Passwort änderung speichern");
        jbmasterspeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbmasterspeichernActionPerformed(evt);
            }
        });

        jLabel4.setText("Aktuelles Master Kennwort ändern");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbmasterspeichern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtverbindung, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jbclose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtspeichern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlmasterpw)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpmaster, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtverbindung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtspeichern)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlmasterpw)
                    .addComponent(jpmaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbmasterspeichern)
                .addGap(41, 41, 41)
                .addComponent(jbclose)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbcloseActionPerformed
        // jbclose
        this.dispose();
    }//GEN-LAST:event_jbcloseActionPerformed

    private void jtspeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtspeichernActionPerformed
        // Speichern verbindung
        speichern_connection ();
    }//GEN-LAST:event_jtspeichernActionPerformed

    private void jbmasterspeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbmasterspeichernActionPerformed
        // Schreiben eines neuen Master Passwort
        schreiben_masterpw();

    }//GEN-LAST:event_jbmasterspeichernActionPerformed

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
            java.util.logging.Logger.getLogger(Einstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Einstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Einstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Einstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Einstellungen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jbclose;
    private javax.swing.JButton jbmasterspeichern;
    private javax.swing.JLabel jlmasterpw;
    private javax.swing.JPasswordField jpmaster;
    private javax.swing.JButton jtspeichern;
    private javax.swing.JTextField jtverbindung;
    // End of variables declaration//GEN-END:variables
}
