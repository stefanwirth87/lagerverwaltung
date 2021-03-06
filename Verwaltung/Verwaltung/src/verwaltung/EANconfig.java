/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltung;

import com.keepdynamic.barcode.generator.BarCode;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 *
 * @author Limbo
 * Klasse der sich um den Barcode kümmert
 */
public class EANconfig extends javax.swing.JFrame {

    //Konstruktor
    public EANconfig() {
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setIconImage(icon);
        initComponents();
    }
    
    //Methode der einen Barcode erzeugt
    public static ImageIcon createBar(String eingabe)
    {
        ImageIcon image = null;
     try
     {
        BarCode barcode = new BarCode(); 
        // set barcode Eigenschaften 
        barcode.setSymbologyType(BarCode.CODE128); 
        barcode.setCodeText(eingabe);
        //Barcode als Byte Rendern und dem Icon übergeben
         image = new ImageIcon(barcode.drawBarCode2ByteArray());
        System.out.println("Code erstellt!");
     }
     catch(Exception ex)
     {
         ex.printStackTrace();
     }
    
    return image;
    }
    
    //generiert einen Barcode und liefert ihn als Byte Array zurück (wird zum erstellen des Image genutzt)
    public static byte [] createBarbyte(String eingabe)
    {
    
    byte [] ausgabe = null;
     try
     {
        BarCode barcode = new BarCode(); 
        // set barcode properties 
        barcode.setSymbologyType(BarCode.CODE128); 
        barcode.setCodeText(eingabe);
        //barcode.setX(1);
        barcode.setY(30);
        //barcode.renderBarcode("BarCode128.gif");
         ausgabe = barcode.drawBarCode2ByteArray();
        System.out.println("Byte-Code erstellt!");
      
     }
     catch(Exception ex)
     {
         ex.printStackTrace();
     }
    
    return ausgabe;
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtnummer = new javax.swing.JTextField();
        jberstellen = new javax.swing.JButton();
        jlcode = new javax.swing.JLabel();
        jbclose = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EANconfig");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("EAN");

        jLabel2.setText("Nummer:");

        jberstellen.setText("Erstellen");
        jberstellen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jberstellenActionPerformed(evt);
            }
        });

        jlcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ean/BarCode128.gif"))); // NOI18N

        jbclose.setText("schließen");
        jbclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbcloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jberstellen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbclose, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlcode, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jberstellen)
                .addGap(18, 18, 18)
                .addComponent(jlcode, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jbclose)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jberstellenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jberstellenActionPerformed
        // Erstellen Button
        jlcode.setIcon(createBar(jtnummer.getText()));
    }//GEN-LAST:event_jberstellenActionPerformed

    private void jbcloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbcloseActionPerformed
        // schließen des Fensters:
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jbcloseActionPerformed

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
            java.util.logging.Logger.getLogger(EANconfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EANconfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EANconfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EANconfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EANconfig().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbclose;
    private javax.swing.JButton jberstellen;
    private javax.swing.JLabel jlcode;
    private javax.swing.JTextField jtnummer;
    // End of variables declaration//GEN-END:variables
}
