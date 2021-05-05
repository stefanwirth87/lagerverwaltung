/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltung;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import testcommon.TestService;

/**
 * @author Limbo
 * Klasse der Artikel
 */
public class Artikelstamm extends javax.swing.JFrame {

    //Klassen weite variablen
   TestService myServiceCaller;
   Einstellungen E;
   String Benutzer;
   int recht;
   int akt = 0, art =0;
   String[][] alle;
   Bestellung aufrufend = null;
   
   //Konstruktor
    public Artikelstamm(String Benutzer, int recht) {
        //Fenster bauen
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setIconImage(icon);
        initComponents();
        //uebnahme Button ausblenden
        jbue.setVisible(false);
        //Übergabe Parameter abspeichern
        this.Benutzer=Benutzer;
        this.recht=recht;
        //Benutzerrechte beachten
        if (recht == 3)
        {
            jbsave.setVisible(false);
            jbneu.setVisible(false);
            jmmanuellausgebucht.setForeground(Color.GRAY);
            jmexport.setForeground(Color.GRAY);
            jmdrucken.setForeground(Color.GRAY);
        }
        //Verbindung aufbauen
        E = new Einstellungen();
        this.myServiceCaller = E.connection();
        //Benutzer anzeigen
        jlbenutzer.setText(jlbenutzer.getText() + " " + Benutzer);
        //Artikel in die Tabelle füllen
        Artikel_in_Tabelle();
 
        //Textfelder / Combofelder bestücken
        jtanr.setText(alle[akt][0]);
        jthersteller.setText(alle[akt][1]);
        jtmodel.setText(alle[akt][2]);
        jtbeschreibung.setText(alle[akt][3]);
        jtanzahl.setText(alle[akt][4]);
        jtpreis.setText(alle[akt][5]);
        jtlager.setText(alle[akt][6]);
        //Standart EAN anzeigen
        jlean.setIcon(EANconfig.createBar(jtanr.getText()));
    }
    
    //zweiter Konstruktor wenn dieses Fenster von einem anderen aufgerufen wird
    public Artikelstamm(String Benutzer, int recht, Bestellung aufrufend, int art, String suche) {
        
        E = new Einstellungen();
        this.myServiceCaller = E.connection();
        this.Benutzer = Benutzer;
        this.recht = recht;
        this.aufrufend=aufrufend;
        this.art=art;
        
  
        //Fensteraufbauen
        initComponents();
        ImageIcon bild = new ImageIcon("ic_lagerverwaltung32.gif"); 
        this.setIconImage(bild.getImage());
        
        jlbenutzer.setText(Benutzer);
        
        if (recht == 3)
        {
            jbsave.setVisible(false);
            jbneu.setVisible(false);
            jmmanuellausgebucht.setForeground(Color.GRAY);
            jmexport.setForeground(Color.GRAY);
            jmdrucken.setForeground(Color.GRAY);
        }
       
        
        jbue.setVisible(true);

        //Benutzer anzeigen
        jlbenutzer.setText(Benutzer);
        
        if (art == 1) // wenn nach allen Artikel aus der Bestellung aus gesucht werden soll
        {
         alle = myServiceCaller.AlleArtikel();
        Artikel_in_Tabelle();
        
        }
        else if (art == 2) //wenn ein bestehender Artikel geöffnet werden soll
        {
        jbue.setVisible(false);
        jtsuche.setText(suche);
        
            //Suche Text abrufen
        String s = jtsuche.getText();

        //Suche aufrufen
        alle = myServiceCaller.searchArtikel(s);
        suchen();
        }
    }
    
    
    public void Artikel_in_Tabelle()
    {   
        //Alle Artikel vom Server holen
        alle = myServiceCaller.AlleArtikel();
        //Tabelle füllen
        Object[] row = new Object[10];
        DefaultTableModel model = (DefaultTableModel) jtartikel.getModel();
        
        for (int i=0; i< alle.length; i++)
        {
            row[0]= alle[i][0];
            row[1]= alle[i][1];
            row[2]= alle[i][2];
            row[3]= alle[i][3];
            row[4]= alle[i][4];
            row[5]= alle[i][5];
            row[6]= alle[i][6];
 
            model.addRow(row);
        }
        
        //Anzahl der Zeilen anzeigen
        jlrow.setText(alle.length + " Rows returned");
    }
    
      public void neu_Tabelle()
    {   
       DefaultTableModel model = (DefaultTableModel) jtartikel.getModel();
       model.setRowCount(0);
        
       Artikel_in_Tabelle();   
    }
      
      public void suchen()
      {
          //Suche Text abrufen
       // String suche = jtsuche.getText();
          System.out.println(jtsuche.getText());
        //Suche aufrufen
        alle = myServiceCaller.searchArtikel(jtsuche.getText());
 
        //Tabellen Modell ziehen und neue Ergebnisse anzeigen
       DefaultTableModel model = (DefaultTableModel) jtartikel.getModel();
       model.setRowCount(0);
        
       Object[] row = new Object[10];
       
        for (int i=0; i< alle.length; i++)
        {
            row[0]= alle[i][0];
            row[1]= alle[i][1];
            row[2]= alle[i][2];
            row[3]= alle[i][3];
            row[4]= alle[i][4];
            row[5]= alle[i][5];
            row[6]= alle[i][6];
            
            model.addRow(row);
        }
        
        if (alle.length > 0)
        {
        
        //Anzahl anzeigen
        jlrow.setText(alle.length + " Rows returned");
        //aktuelle Position setzen
        akt=0;
        //Textfelder / Combofelder bestücken
        jtanr.setText(alle[akt][0]);
        jthersteller.setText(alle[akt][1]);
        jtmodel.setText(alle[akt][2]);
        jtbeschreibung.setText(alle[akt][3]);
        jtanzahl.setText(alle[akt][4]);
        jtpreis.setText(alle[akt][5]);
        jtlager.setText(alle[akt][6]);
        
        jlean.setIcon(EANconfig.createBar(jtanr.getText()));
        }
        else
        {
        JOptionPane.showMessageDialog(null, "Ihre Suche hat nichts ergeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
        jtsuche.setText("");
        suchen();
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

        jtsuche = new javax.swing.JTextField();
        jtanr = new javax.swing.JTextField();
        jthersteller = new javax.swing.JTextField();
        jtmodel = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtartikel = new javax.swing.JTable();
        jtanzahl = new javax.swing.JTextField();
        jtpreis = new javax.swing.JTextField();
        jbsave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jlbenutzer = new javax.swing.JLabel();
        jlanr = new javax.swing.JLabel();
        jlhersteller = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtlager = new javax.swing.JTextField();
        jlrow = new javax.swing.JLabel();
        jbneu = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jlean = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtbeschreibung = new javax.swing.JTextArea();
        jbclear = new javax.swing.JButton();
        jbue = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmmanuellausgebucht = new javax.swing.JMenuItem();
        jmexport = new javax.swing.JMenuItem();
        jmdrucken = new javax.swing.JMenuItem();
        jmbeenden = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Artikelstamm");
        setLocation(new java.awt.Point(200, 100));

        jtsuche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtsucheKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtsucheKeyReleased(evt);
            }
        });

        jtartikel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ArtikelNr", "Hersteller", "Model", "Beschreibung", "Anzahl", "Preis", "LagerID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtartikel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtartikelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtartikel);
        if (jtartikel.getColumnModel().getColumnCount() > 0) {
            jtartikel.getColumnModel().getColumn(0).setResizable(false);
            jtartikel.getColumnModel().getColumn(0).setPreferredWidth(20);
            jtartikel.getColumnModel().getColumn(1).setResizable(false);
            jtartikel.getColumnModel().getColumn(1).setPreferredWidth(100);
            jtartikel.getColumnModel().getColumn(2).setResizable(false);
            jtartikel.getColumnModel().getColumn(2).setPreferredWidth(100);
            jtartikel.getColumnModel().getColumn(3).setPreferredWidth(300);
            jtartikel.getColumnModel().getColumn(4).setResizable(false);
            jtartikel.getColumnModel().getColumn(4).setPreferredWidth(30);
            jtartikel.getColumnModel().getColumn(5).setResizable(false);
            jtartikel.getColumnModel().getColumn(5).setPreferredWidth(30);
            jtartikel.getColumnModel().getColumn(6).setResizable(false);
            jtartikel.getColumnModel().getColumn(6).setPreferredWidth(10);
        }

        jbsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        jbsave.setText("Speichern");
        jbsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsaveActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Artikelstamm");

        jlbenutzer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.gif"))); // NOI18N
        jlbenutzer.setText("Benutzer:");

        jlanr.setText("Artikel_Nr:");

        jlhersteller.setText("Hersteller:");

        jLabel4.setText("Model:");

        jLabel5.setText("Anzahl:");

        jLabel6.setText("Preis");

        jLabel7.setText("Lager_ID:");

        jLabel8.setText("Beschreibung");

        jlrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table.gif"))); // NOI18N
        jlrow.setText("Rows");

        jbneu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.gif"))); // NOI18N
        jbneu.setText("Neuer Artikel");
        jbneu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbneuActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("EAN");

        jtbeschreibung.setColumns(20);
        jtbeschreibung.setRows(5);
        jScrollPane2.setViewportView(jtbeschreibung);

        jbclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clear.gif"))); // NOI18N
        jbclear.setText("clear");
        jbclear.setToolTipText("");
        jbclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbclearActionPerformed(evt);
            }
        });

        jbue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/download-cloud-flat.gif"))); // NOI18N
        jbue.setText("Übernehmen");
        jbue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbueActionPerformed(evt);
            }
        });

        jMenu1.setText("Artikelstamm");

        jmmanuellausgebucht.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jmmanuellausgebucht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.gif"))); // NOI18N
        jmmanuellausgebucht.setText("Manuell Ausgebucht");
        jmmanuellausgebucht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmmanuellausgebuchtActionPerformed(evt);
            }
        });
        jMenu1.add(jmmanuellausgebucht);

        jmexport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jmexport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export.gif"))); // NOI18N
        jmexport.setText("Exportieren");
        jmexport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmexportActionPerformed(evt);
            }
        });
        jMenu1.add(jmexport);

        jmdrucken.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jmdrucken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        jmdrucken.setText("Drucken");
        jmdrucken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmdruckenActionPerformed(evt);
            }
        });
        jMenu1.add(jmdrucken);

        jmbeenden.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jbneu)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlanr)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtanzahl, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtanr, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jlbenutzer, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbue))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jbsave)
                                        .addGap(118, 118, 118)
                                        .addComponent(jtsuche, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jlhersteller)
                                            .addComponent(jLabel6))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jthersteller, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtpreis, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(77, 77, 77)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel7))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtlager, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jtmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel8)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jbclear))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlean, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jlrow, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jlbenutzer)
                            .addComponent(jbue))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtsuche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbclear)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbneu)
                            .addComponent(jbsave))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlanr)
                                    .addComponent(jtanr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlhersteller)
                                    .addComponent(jthersteller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jtmodel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jtanzahl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(jtpreis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(jtlager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlean, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlrow)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmbeendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmbeendenActionPerformed
        //beim beenden - connection schliesen und dieses Fenster zerstören
        E.connection_close();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jmbeendenActionPerformed

    private void jtartikelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtartikelMouseClicked
        // Klichen auf die Tabellen Spalte
        jtartikel.setSelectionBackground(Color.blue);
        
        //angeklickte Zeile auslesen und abspeichern
        int i = jtartikel.getSelectedRow();
        akt = i;
        
        //Table modell holen und aus den Spalten holen und in die Textfelder / JComboboxen füllen
        TableModel model = jtartikel.getModel();
   
        //Textfelder / Combofelder bestücken
        jtanr.setText((String) model.getValueAt(i, 0));
        jthersteller.setText((String) model.getValueAt(i, 1));
        jtmodel.setText((String) model.getValueAt(i, 2));
        jtbeschreibung.setText((String) model.getValueAt(i, 3));
        jtanzahl.setText((String) model.getValueAt(i, 4));
        jtpreis.setText((String) model.getValueAt(i, 5));
        jtlager.setText((String) model.getValueAt(i, 6));
        //EAN erstellen
        jlean.setIcon(EANconfig.createBar(jtanr.getText()));
    }//GEN-LAST:event_jtartikelMouseClicked

    private void jbneuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbneuActionPerformed
        // Neuen Kunden anlegen
        jbue.setVisible(false);
        jtanr.setVisible(false);
        jlanr.setVisible(false);
        
        String [] frei  = myServiceCaller.freiLagerplaetze();
        
        jtanr.setText("");
        //jtanr.setBackground(Color.DARK_GRAY);
        jthersteller.setText("");
        jtmodel.setText("");
        jtbeschreibung.setText("");
        jtanzahl.setText("");
        jtpreis.setText("");
        jtlager.setText(frei[0]); //ersten freien nehmen
    }//GEN-LAST:event_jbneuActionPerformed

    private void jbsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsaveActionPerformed
        // Speichern Button
        //Variablen deklariert und intialisiert
        String Hersteller="", Model="", Beschreibung="";
        int Anzahl=0, LagerID=0, artnr=0;
        Double preis=0.0;
        int err = 0; //errorcode - erst wenn der am ende 0 ist wird der Datensatz eingetragen
            
        //Prüfung ob die Artikelnummer leer ist, wenn ja auch ein eintragen erfolgen, 
        //wenn nicht müssen die felder vorher gelöscht werden, da es sonst zu einer überschreibung kommen kann
        
        if (jtanr.getText().equals(""))
        {
            //abfrage ob die neu eingetragenen Daten als Kundensatz angelegt werden soll
            int ok = JOptionPane.showConfirmDialog(null, "Artikel anlegen ?", "Fehler" , JOptionPane.YES_NO_OPTION);
            //ok==0 == ja
            if (ok == 0)
            {
                /*Vorgang bleibt gleich für alle Felder*/
                //Hersteller
                 if (jthersteller.getText().equals(""))       //Prüfen ob im Textfeld inhalt vorhanden ist
                {
                     //eventuell Fehler ausgeben und den Fehler status anzeigen
                 JOptionPane.showMessageDialog(null, "Kein Hersteller Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                 else //Alternatib text übernehmen - bei den anderen Textfeldern gilt das gleiche Vorgehen
                {
                 Hersteller = jthersteller.getText();
                }

                //Modell 
                if (jtmodel.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Model Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Model = jtmodel.getText();
                }

                //Beschreibung
                if (jtbeschreibung.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Beschreibung Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Beschreibung = jtbeschreibung.getText();
                }

                //Anzahl

                 if (jtanzahl.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Anzahl Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Anzahl = Integer.parseInt(jtanzahl.getText());
                }

                // Preis 
                if (jtpreis.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Preis Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 preis = Double.parseDouble(jtpreis.getText());
                }

                //Lager
                if (jtlager.getText().equals("0"))
                {
                 JOptionPane.showMessageDialog(null, "Keine LagerID Vergeben\nneuer Lagerplatz wird automatisch hinzugefügt", "Leerer Lagerplatz", JOptionPane.INFORMATION_MESSAGE);
                 //err = err + 1;
                 LagerID=0;
                }
                else
                {
                 LagerID = Integer.parseInt(jtlager.getText());
                }

                

                if (err == 0)//nur wenn alle Felder gefüllt sind wird der Datensatz erstellt
                {
                    //aufruf der Methode insert_Kunde_neu auf dem Server
                    String r = myServiceCaller.insert_artikel_neu(Hersteller, Model, Beschreibung, Anzahl, preis, LagerID);
                    //Rückmeldung vom Server wird ausgefangen und ausgeben
                    JOptionPane.showMessageDialog(null, r, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                    //die Tabelle wird neu erzeugt - eigene Methode
                    neu_Tabelle();
                }
            }

        }
        else
        {
            
            //Für den Fall der änderung
           int ok = JOptionPane.showConfirmDialog(null, "Möchten Sie die änderungen Speichern ?", "Fehler" , JOptionPane.YES_NO_OPTION); 
           //die Frage taucht bei einem Update des Datensatzes auf - da es hier schon eine Kundenummer gibt
           if (ok == 0)
           {
               /*Vorgang bleibt wieder gleich für alle abfragen - gleich wie oben*/
               // Kunen_id 
                if (jtanr.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Artikelnummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 artnr = Integer.parseInt(jtanr.getText());
                } 
                //Hersteller
                 if (jthersteller.getText().equals(""))       //Prüfen ob im Textfeld inhalt vorhanden ist
                {
                     //eventuell Fehler ausgeben und den Fehler status anzeigen
                 JOptionPane.showMessageDialog(null, "Kein Hersteller Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                 else //Alternatib text übernehmen - bei den anderen Textfeldern gilt das gleiche Vorgehen
                {
                 Hersteller = jthersteller.getText();
                }

                //Model 
                if (jtmodel.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Model Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Model = jtmodel.getText();
                }
                //Beschreibung
                if (jtbeschreibung.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Beschreibung Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Beschreibung = jtbeschreibung.getText();
                }

                //Anzahl

                 if (jtanzahl.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Anzahl Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 Anzahl = Integer.parseInt(jtanzahl.getText());
                }

                // Preis 
                if (jtpreis.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Preis Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 preis = Double.parseDouble(jtpreis.getText());
                }

                //Lager
                if (jtlager.getText().equals("0"))
                {
                 JOptionPane.showMessageDialog(null, "Keine LagerID Vergeben\nneuer Lagerplatz wird automatisch hinzugefügt", "Leerer Lagerplatz", JOptionPane.INFORMATION_MESSAGE);
                 //err = err + 1;
                 LagerID=0;
                }
                else
                {
                 LagerID = Integer.parseInt(jtlager.getText());
                }
            
            if (err == 0)//nur wenn alle Felder gefüllt sind wird der Datensatz erstellt
            {
                String r = myServiceCaller.update_artikel(artnr, Hersteller, Model, Beschreibung, Anzahl, preis, LagerID);

                JOptionPane.showMessageDialog(null, r, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                
                neu_Tabelle();
            }
           }
        }
        if(art !=0)
        {jbue.setVisible(true);}
        
        jtanr.setVisible(true);
        jlanr.setVisible(true);
    }//GEN-LAST:event_jbsaveActionPerformed

    private void jmdruckenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmdruckenActionPerformed
        // Drucken Menüeintrag - Drucken der akuell Angezeigten Artikel
        //drucken nur möglich wenn man als Administrator als Anwender
        if ((recht ==1)||(recht == 2))
        {
        Print p = new Print("Print");
        p.druckeArtikelseite(alle);
        } 
    }//GEN-LAST:event_jmdruckenActionPerformed

    private void jtsucheKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtsucheKeyPressed
        // key pressed event suchen
        //suchen();
    }//GEN-LAST:event_jtsucheKeyPressed

    private void jbclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbclearActionPerformed
        // clear Button
        jtsuche.setText("");
        suchen();
    }//GEN-LAST:event_jbclearActionPerformed

    private void jbueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbueActionPerformed
        // für die übergabe an die bestellung zurück
        aufrufend.Artikelfuellen(jtanr.getText());
        this.setVisible(false);
    }//GEN-LAST:event_jbueActionPerformed

    private void jtsucheKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtsucheKeyReleased
        // Suchen Event ausführen bei key released
        suchen();
    }//GEN-LAST:event_jtsucheKeyReleased

    private void jmexportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmexportActionPerformed
        // Exportieren in eine CSV
        //nur möglich als Administrator
        if (recht == 1)
        {
            String pfad = null; 

            JFileChooser chooser; 
            if (pfad == null) 
              pfad = System.getProperty("user.home"); 
            File file = new File(pfad.trim()); 

            chooser = new JFileChooser(pfad); 
            chooser.setDialogType(JFileChooser.SAVE_DIALOG); 
            FileNameExtensionFilter plainFilter = new FileNameExtensionFilter( 
            "Dateityp: csv", "csv"); 
            chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter()); 
            chooser.setFileFilter(plainFilter);  
            chooser.setDialogTitle("Speichern unter..."); 
            chooser.setVisible(true); 

            int result = chooser.showSaveDialog(this); 

            if (result == JFileChooser.APPROVE_OPTION) { 

              pfad = chooser.getSelectedFile().toString()+".csv";; 
              file = new File(pfad); 
              if (plainFilter.accept(file)) 
              {
                System.out.println(pfad + " kann gespeichert werden."); 

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(pfad,false));

                    bw.write("ArtikelNr;Hersteller;Model;Beschreibung;Anzahl;Preis;LagerID");   
                    bw.newLine();  

                    for(int i=0;i<alle.length;i++)
                    {
                        bw.write(alle[i][0]+";");
                        bw.write(alle[i][1]+";");
                        bw.write(alle[i][2]+";");
                        bw.write(alle[i][3]+";");
                        bw.write(alle[i][4]+";");
                        bw.write(alle[i][5]+";");
                        bw.write(alle[i][6]);

                        bw.newLine(); 
                    }

                    bw.close();

                    JOptionPane.showMessageDialog(null, "Die Datei wurde gespeichert unter\n"+pfad, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);

                } 
                 catch(IOException error) 
                 {
                    JOptionPane.showMessageDialog(null, "Fehler"+error, "Fehler", JOptionPane.ERROR_MESSAGE);
                }  

              }    
              else 
              {
                JOptionPane.showMessageDialog(null, "Bitte Dateityp angeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
              } 
              chooser.setVisible(false); 
              //return true; 
            } 
            chooser.setVisible(false); 
            //return false; 
        }
        
    }//GEN-LAST:event_jmexportActionPerformed

    private void jmmanuellausgebuchtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmmanuellausgebuchtActionPerformed
       //anzeige der Manuell ausgebuchten Artikel
       //nur möglich als Administrator
        if (recht == 1)
        {
        ManuellAusgebucht MA = new ManuellAusgebucht();
        MA.setVisible(true);
        }

    }//GEN-LAST:event_jmmanuellausgebuchtActionPerformed

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
            java.util.logging.Logger.getLogger(Artikelstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Artikelstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Artikelstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Artikelstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Artikelstamm("Test-Umgebung",0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbclear;
    private javax.swing.JButton jbneu;
    private javax.swing.JButton jbsave;
    private javax.swing.JButton jbue;
    private javax.swing.JLabel jlanr;
    private javax.swing.JLabel jlbenutzer;
    private javax.swing.JLabel jlean;
    private javax.swing.JLabel jlhersteller;
    private javax.swing.JLabel jlrow;
    private javax.swing.JMenuItem jmbeenden;
    private javax.swing.JMenuItem jmdrucken;
    private javax.swing.JMenuItem jmexport;
    private javax.swing.JMenuItem jmmanuellausgebucht;
    private javax.swing.JTextField jtanr;
    private javax.swing.JTextField jtanzahl;
    private javax.swing.JTable jtartikel;
    private javax.swing.JTextArea jtbeschreibung;
    private javax.swing.JTextField jthersteller;
    private javax.swing.JTextField jtlager;
    private javax.swing.JTextField jtmodel;
    private javax.swing.JTextField jtpreis;
    private javax.swing.JTextField jtsuche;
    // End of variables declaration//GEN-END:variables
}
