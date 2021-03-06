/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltung;

import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import testcommon.TestService;

/**
 *
 * @author Stefan
 * Jframe der auskunft gibt über die ausgebuchten Artikel
 */
public class ManuellAusgebucht extends javax.swing.JFrame {
    //Klassenweite variablen
    TestService myServiceCaller;
    Einstellungen E;
    String[][] alle;
    String [] name;
    //Konstruktor
    public ManuellAusgebucht() {
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setIconImage(icon);
        initComponents();
        //Connection bilden
        E = new Einstellungen();
        this.myServiceCaller = E.connection();
        
        name = myServiceCaller.namendesManuellenausbuchen(); 
        
        for (int i=0; i< name.length; i++)
        {
          jcname.addItem(name[i]);
        }
        
        //Alle Vorgänge vom Server holen
        alle = myServiceCaller.AlleManuellAusgebuchtenArtikel();
        //Tabelle auffüllen
        Tabelle_fuellen();
    }
    //Tabelle füllen - Vorgänge wie in den anderen FRAMES's
    public void Tabelle_fuellen()
    {   
        //Tabelle füllen
        Object[] row = new Object[10];
        DefaultTableModel model = (DefaultTableModel) jtartikel.getModel();
        model.setRowCount(0);
        
        for (int i=0; i< alle.length; i++)
        {
            row[0]= alle[i][0];
            row[1]= alle[i][1];
            row[2]= alle[i][2];
            row[3]= alle[i][3];
            row[4]= alle[i][4];
            row[5]= alle[i][5];
 
            model.addRow(row);
        }
        //Anzahl der Zeilen anzeigen
        jlrow.setText(alle.length + " Rows returned");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jtartikel = new javax.swing.JTable();
        jlrow = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jdvon = new com.toedter.calendar.JDateChooser();
        jdbis = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jcname = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jtkommentar = new javax.swing.JTextField();
        jbsearch = new javax.swing.JButton();
        jbclear = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmbeenden = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manuell ausgebuchte Artikel");
        setLocation(new java.awt.Point(200, 100));
        setLocationByPlatform(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Manuell ausgebuchte Artikel");

        jtartikel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Datum", "Name", "Hersteller", "Model", "Anzahl", "Kommentar"
            }
        ));
        jScrollPane1.setViewportView(jtartikel);
        if (jtartikel.getColumnModel().getColumnCount() > 0) {
            jtartikel.getColumnModel().getColumn(0).setPreferredWidth(120);
            jtartikel.getColumnModel().getColumn(4).setPreferredWidth(20);
            jtartikel.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        jlrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table.gif"))); // NOI18N
        jlrow.setText("Rows");

        jPanel1.setName(""); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Filter:");

        jLabel3.setText("von:");

        jLabel4.setText("bis:");

        jLabel5.setText("Benutzer:");

        jcname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alle" }));

        jLabel6.setText("Kommentar:");

        jbsearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.gif"))); // NOI18N
        jbsearch.setText("Filter");
        jbsearch.setToolTipText("");
        jbsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsearchActionPerformed(evt);
            }
        });

        jbclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clear.gif"))); // NOI18N
        jbclear.setText("Filter Aufheben");
        jbclear.setToolTipText("");
        jbclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbclearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jdbis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jdvon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcname, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jtkommentar)
                    .addComponent(jbsearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbclear, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdvon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdbis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtkommentar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbsearch)
                .addGap(18, 18, 18)
                .addComponent(jbclear)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jMenu1.setText("Beenden");

        jmbeenden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.gif"))); // NOI18N
        jmbeenden.setText("beenden");
        jmbeenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmbeendenActionPerformed(evt);
            }
        });
        jMenu1.add(jmbeenden);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jlrow, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(620, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addGap(221, 221, 221)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jlrow)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(53, 53, 53)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .addGap(37, 37, 37)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmbeendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmbeendenActionPerformed
        //Beim schliesen wird die Connection geschlossen und das Fenster zerstört
        E.connection_close();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jmbeendenActionPerformed

    private void jbsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsearchActionPerformed
        // Filtern der anzeige - gleich wie im Verwaltungsfenster
        String Datevon="", Datebis="", selectname="";
         //Simple date für die Formatierung wählen
         SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
         Date d = new Date();
        //überprüfen ob der kalender gesetzt ist
        if ((jdvon.getCalendar() != null)&&(jdbis.getCalendar() != null))
        {
            //wandlung der Millisekunden in Zeitformate (Datebis wird noch um einen Tag hochgezählt)
            Datevon = s.format(jdvon.getCalendar().getTimeInMillis());
            Datebis = s.format(jdbis.getCalendar().getTimeInMillis()+10000000);
        }
        else if ((jdvon.getCalendar() == null)&&(jdbis.getCalendar() == null))
        {
            //bei keiner auswahl zur suche den gesamten Programmzeitraum angeben
            Datevon = "2016-01-01";
            Datebis = s.format(d);
        }  
        
        if (jcname.getSelectedIndex() != 0)
        {
            selectname = jcname.getItemAt(jcname.getSelectedIndex());
        }

        alle = myServiceCaller.searchManuellausbuchen(Datevon, Datebis, selectname, jtkommentar.getText());
        
        Tabelle_fuellen();
 
    }//GEN-LAST:event_jbsearchActionPerformed

    private void jbclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbclearActionPerformed
        // clearButton
        jtkommentar.setText("");
        jcname.setSelectedIndex(0);
        jdvon.setDate(null);
        jdbis.setDate(null);
        alle = myServiceCaller.AlleManuellAusgebuchtenArtikel();
        Tabelle_fuellen();
    }//GEN-LAST:event_jbclearActionPerformed

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
            java.util.logging.Logger.getLogger(ManuellAusgebucht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManuellAusgebucht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManuellAusgebucht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManuellAusgebucht.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManuellAusgebucht().setVisible(true);
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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbclear;
    private javax.swing.JButton jbsearch;
    private javax.swing.JComboBox<String> jcname;
    private com.toedter.calendar.JDateChooser jdbis;
    private com.toedter.calendar.JDateChooser jdvon;
    private javax.swing.JLabel jlrow;
    private javax.swing.JMenuItem jmbeenden;
    private javax.swing.JTable jtartikel;
    private javax.swing.JTextField jtkommentar;
    // End of variables declaration//GEN-END:variables

}
