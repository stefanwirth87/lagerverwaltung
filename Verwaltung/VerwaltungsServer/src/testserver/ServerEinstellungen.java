/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

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
 * @author Acer
 */
public class ServerEinstellungen extends javax.swing.JFrame {

        Client client;
        //TestService myServiceCaller = null;
        String ip ="", benutzer="", passwort="", port="", dbname="";
        
    public ServerEinstellungen() {   
    Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
    setIconImage(icon);
    initComponents();
    auslesen_connection();   
    }

    public String [] connection()
    {
       String [] verbindung = new String [5];
       
       try {
          BufferedReader br = new BufferedReader(new FileReader("verbindung.dat")); 
          ip = br.readLine();
          port = br.readLine();
          dbname = br.readLine();
          benutzer = br.readLine();
          passwort = br.readLine();

          verbindung[0] = ip;
          verbindung[1] = port;
          verbindung[2] = dbname;
          verbindung[3] = benutzer;
          verbindung[4] = passwort;
          br.close();
  
        } 
         catch(IOException error) 
         {
            
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("verbindung.dat",true));
                bw.write("127.0.0.1");   
                bw.newLine();
                bw.write("root"); 
                bw.newLine();
                bw.write(""); 
                bw.close();
                JOptionPane.showMessageDialog(null, "Verbindungsdaten nicht gefunden.\nStandartwert: 127.0.0.1 wurde angelegt", "Fehler", JOptionPane.OK_OPTION);
   
            } 
                catch(IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Fehler"+e, "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        }
       
       //Rückgabe eines Arrays mit den Verbindungsdaten
       return verbindung;
    }

    public void speichern_connection ()
    {
         try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("verbindung.dat",false));
          
      
           bw.write(jtdatenbank.getText());
           bw.newLine();
           bw.write(jtport.getText());
           bw.newLine();
           bw.write(jtdbname.getText());
           bw.newLine();
           bw.write(jtbenutzer.getText());
           bw.newLine();
           bw.write(jtpasswort.getText());

           bw.flush();
           bw.close();
            
            JOptionPane.showMessageDialog(null, "Änderungen wurden übernommen!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } 
         catch(IOException error) 
         {
            JOptionPane.showMessageDialog(null, "Fehler"+error, "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void auslesen_connection()
    {
         try {
          BufferedReader br = new BufferedReader(new FileReader("verbindung.dat")); 

          ip = br.readLine();
          port = br.readLine();
          dbname = br.readLine();
          benutzer = br.readLine();
          passwort = br.readLine();
          
          jtdatenbank.setText(ip);
          jtport.setText(port);
          jtdbname.setText(dbname);
          jtbenutzer.setText(benutzer);
          jtpasswort.setText(passwort);

          br.close();
  
        } 
         catch(IOException error) 
         {
            
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("verbindung.dat",true));
                bw.write("127.0.0.1");   
                bw.newLine();
                bw.write("3306"); 
                bw.newLine();
                bw.write("Projekt"); 
                bw.newLine();
                bw.write("root");
                bw.close();
                JOptionPane.showMessageDialog(null, "Verbindungsdaten nicht gefunden.\nStandartwert: 127.0.0.1 wurde angelegt", "Fehler", JOptionPane.OK_OPTION);
   
            } 
                catch(IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Fehler"+e, "Fehler", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    
    public String auslesen_masterpw()
    {
        String master="";
         try {
          BufferedReader br = new BufferedReader(new FileReader("master.dat")); 
          master = br.readLine();
          br.close();
  
        } catch(IOException error) {
 
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("master.dat",true));
                bw.write(build_md5("geheim"));   
                //bw.newLine();              
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
    
    public void schreiben_masterpw()
    {

         try {

            //Message Box erzeugen - anwenden muss nochmal das aktuelle Kennwort eingeben
            JLabel l = new JLabel("Aktuelles Passwort eingeben");
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
         Logger.getLogger(ServerEinstellungen.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel3 = new javax.swing.JLabel();
        jbclose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtdatenbank = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtpasswort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtbenutzer = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtspeichern = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jtport = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtdbname = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jlmasterpw = new javax.swing.JLabel();
        jpmaster = new javax.swing.JPasswordField();
        jbmasterspeichern = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Einstellungen");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Einstellungen");

        jLabel3.setText("Hier können Programmeinstellungen vorgenommen werden");

        jbclose.setText("Schließen");
        jbclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbcloseActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Datenbank-Server:");

        jLabel6.setText("Benutzer-Kennwort:");

        jLabel5.setText("Datenbank-Benutzer:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Datenbank");

        jtspeichern.setText("Speichern");
        jtspeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtspeichernActionPerformed(evt);
            }
        });

        jLabel8.setText("Port:");

        jLabel9.setText("Datenbank-Name:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtspeichern, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel5))
                                .addGap(72, 72, 72)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jtbenutzer, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtpasswort, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtdatenbank, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtport)
                                    .addComponent(jtdbname))))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtdatenbank, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtdbname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtbenutzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtpasswort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jtspeichern)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Aktuelles Master Kennwort ändern");

        jlmasterpw.setText("Master Kennwort:");

        jbmasterspeichern.setText("Passwort änderung speichern");
        jbmasterspeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbmasterspeichernActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jbmasterspeichern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(67, 67, 67))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jlmasterpw)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpmaster, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlmasterpw)
                    .addComponent(jpmaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbmasterspeichern)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbclose, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jbclose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(ServerEinstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerEinstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerEinstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerEinstellungen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerEinstellungen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbclose;
    private javax.swing.JButton jbmasterspeichern;
    private javax.swing.JLabel jlmasterpw;
    private javax.swing.JPasswordField jpmaster;
    private javax.swing.JTextField jtbenutzer;
    private javax.swing.JTextField jtdatenbank;
    private javax.swing.JTextField jtdbname;
    private javax.swing.JTextField jtpasswort;
    private javax.swing.JTextField jtport;
    private javax.swing.JButton jtspeichern;
    // End of variables declaration//GEN-END:variables
}
