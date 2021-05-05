/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltung;

import com.sun.prism.paint.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import lipermi.net.Client;
import testcommon.TestService;

/**
 * @author Limbo
 * Fenster für die Bestellungen
 */
public class Bestellung extends javax.swing.JFrame {

    //Klassenweite variablen
    String kdnr;
    String vorname;
    String nachname;
    int id;
    int recht;
    String name;
    TestService myServiceCaller = null;
    Client client;
    //String [][] alle;
    String [] zuloeschen = new String [100];
    int anzzuloeschen=0;
    String[][] artikel = new String [1][7];
    double pgesamt=0.0;
    String Bestell_ID;
    String status;
    
    int akt = 0;
    int artakt = 0;
    int anzpos =0;
    Kundenstamm k = null;
   
    //Konstruktor für eine neue Bestellung
    public Bestellung(int id, int recht, String name) {
        //Fenster erstellen
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setIconImage(icon);
        initComponents();
        //übergabe variablen abspeichern
        this.id=id;
        this.recht=recht;
        this.name=name;
        jcstatus.setSelectedIndex(1);
        Einstellungen E = new Einstellungen();
        myServiceCaller = E.connection();
        //an dieser Stelle keine Rechte prüfung. 
        //Neue Belege können nur mit genügend Recht geöffnet werden
    }
    
    //Konstruktor für eine schon bestehende Bestellung
    public Bestellung(int recht, String name, String Bestell_ID, String kdnr, String vorname, String nachname, String status) {
        //Fenster erzeugen
        initComponents();
        ImageIcon bild = new ImageIcon("ic_lagerverwaltung32.gif"); 
        this.setIconImage(bild.getImage());
        //Übergabe variabken abspeichern
        this.recht=recht;
        this.name=name;
        this.Bestell_ID=Bestell_ID;
        this.kdnr=kdnr;
        this.vorname=vorname;
        this.nachname=nachname;
        this.status=status;
        Einstellungen E = new Einstellungen();
        myServiceCaller = E.connection();
      
        //Komponenten füllen
        jlbstlnr.setText(Bestell_ID);
        jtkdnr.setText(kdnr);
        jtvorname.setText(vorname);
        jtnachname.setText(nachname);
        jlstatus.setText(status);
        jcstatus.setSelectedIndex(myServiceCaller.get_Status(Bestell_ID));
        PositionenHolen();
        //Rechte abfrage - der Praktikant darf hier nicht viel :D
        if (recht == 3)
        {
            jbposhinzu.setVisible(false);
            jbdelete.setVisible(false);
            jbkundensuchen.setVisible(false);
            jbsuchenartikel.setVisible(false);
            jbspeichern.setVisible(false);
            jLabel14.setVisible(false);
            jcstatus.setVisible(false);
            jmdrucken.setForeground(java.awt.Color.GRAY);
        }
    }
    

    public void kundefuellen(int status)
    {
        jtkdnr.setText(kdnr);
        jtnachname.setText(nachname);
        jtvorname.setText(vorname);
        
        if (status == 0)
        {
           Bestell_ID = myServiceCaller.neue_Bestellung(jtkdnr.getText());
            jlbstlnr.setText(Bestell_ID);
        }
    }
    
    //holt die schon zur Bestellung hinzugefügten Artikel ab und zeigt sie an
    public void PositionenHolen()
    {
       artikel = myServiceCaller.AllePositionen(Bestell_ID);
       //Tabelle neu füllen
        DefaultTableModel model = (DefaultTableModel) jtpositionen.getModel();
        Object[] row = new Object[7];

        for (int i=0; i< artikel.length; i++)   
        {
            row[0]= artikel[i][0];
            row[1]= artikel[i][1];
            row[2]= artikel[i][2];
            row[3]= artikel[i][3];
            row[4]= artikel[i][4];
            row[5]= artikel[i][5];
            row[6]= artikel[i][6];
            //Platz 7 wird nicht angezeigt - nur für verarbeitung
            model.addRow(row);
        }
        showRow(0);
        sumArtikel();
    }
    
    //show row zeigt die aktuell angeklickte Zeile an
    public void showRow(int clicked)
    {
        akt = clicked;

        TableModel model = jtpositionen.getModel();

        //aktuelle auswahl dem Benutzer anzeigen
        jtartnr.setText((String) model.getValueAt(akt, 0));
        jthersteller.setText((String) model.getValueAt(akt, 1));
        jtmodel.setText((String) model.getValueAt(akt, 2));
        jtbeschreibung.setText((String) model.getValueAt(akt, 3));
        jtpreis.setText((String) model.getValueAt(akt, 4));
        jtmenge.setText((String) model.getValueAt(akt, 5));
        jlean.setIcon(EANconfig.createBar(jtartnr.getText()));
    }
    
    //berechnet die Summe aller Artikel in der Bestellung
    public void sumArtikel()
    {
       pgesamt=0.0;
       for (int i = 0; i<artikel.length;i++)
       {
           pgesamt = pgesamt + Double.parseDouble(artikel[i][6]);
       }
      
      jlpreisg.setText("gesamt: "+Double.toString(Math.round(pgesamt*100)/100.0)); 
      jlrow.setText(artikel.length + " Rows returned");
      anzpos=artikel.length;
      
      System.out.println("länge der artikel "+artikel.length);
    }

    //wird im Artikelstamm aufgerufen wenn ein Artikel hinzugefügt werden soll
    public void Artikelfuellen(String RueckgabeArt)
    {
        //anlegen eines Array für die schon aktuellen Artikel
        String [][] aktuelleartikel;
        //wenn es noch keine Artikel gibt wird direkt das Array mit dem neuen Artikel befüllt
        if (anzpos==0) //bei der ersten position
        {
            //neueen artikel in ein übergabefeld schreiben (könnte ja eine position schon drin sein die überschrieben wird)
            String [][] ue  = myServiceCaller.searchArtikel(RueckgabeArt);
            //ein feld anlegen wo alle bisherigen positionen übergeben werden 
            aktuelleartikel = new String [artikel.length][7];
            //bisherrige artikel abspeichern
            for (int i = 0; i<artikel.length; i++)
            {
                aktuelleartikel[i][0] = artikel[i][0];
                aktuelleartikel[i][1] = artikel[i][1];
                aktuelleartikel[i][2] = artikel[i][2];
                aktuelleartikel[i][3] = artikel[i][3];
                aktuelleartikel[i][4] = artikel[i][4];
                aktuelleartikel[i][5] = artikel[i][5];   
            }
            //die neue Position hinzufügen
            aktuelleartikel[anzpos][0] = ue[0][0];
            aktuelleartikel[anzpos][1] = ue[0][1];
            aktuelleartikel[anzpos][2] = ue[0][2];
            aktuelleartikel[anzpos][3] = ue[0][3];
            aktuelleartikel[anzpos][4] = ue[0][5];
            
            //das Artikel objekt neu anlegen und direkt vergrößern (sind jetzt mehr daten)
            artikel = new String [aktuelleartikel.length][7];

            //artikel mit den bisherigen werten befüllen aus dem Objekt in dem alles zusammengführt wurde
            for (int j = 0; j<artikel.length; j++)
            {  
                artikel[j][0] = aktuelleartikel[j][0];
                artikel[j][1] = aktuelleartikel[j][1];
                artikel[j][2] = aktuelleartikel[j][2];
                artikel[j][3] = aktuelleartikel[j][3];
                artikel[j][4] = aktuelleartikel[j][4];
                artikel[j][5] = aktuelleartikel[j][5];
            }
            //aktuelle auswahl dem Benutzer anzeigen
            jtartnr.setText(artikel[anzpos][0]);
            jthersteller.setText(artikel[anzpos][1]);
            jtmodel.setText(artikel[anzpos][2]);
            jtbeschreibung.setText(artikel[anzpos][3]);
            jtpreis.setText(artikel[anzpos][4]);
    
            jlean.setIcon(EANconfig.createBar(jtartnr.getText()));
            
            jtmenge.setText("");
  
        }  
        else //ab der zweiten position
        {  
            //artikel in ein übergabefeld schreiben
            String [][] ue  = myServiceCaller.searchArtikel(RueckgabeArt);
            //ein feld anlegen wo alle bisherigen positionen übergeben werden 
            aktuelleartikel = new String [artikel.length+1][7];
            //bisherrige artikel abspeichern
            for (int i = 0; i<artikel.length; i++)
            {
                aktuelleartikel[i][0] = artikel[i][0];
                aktuelleartikel[i][1] = artikel[i][1];
                aktuelleartikel[i][2] = artikel[i][2];
                aktuelleartikel[i][3] = artikel[i][3];
                aktuelleartikel[i][4] = artikel[i][4];
                aktuelleartikel[i][5] = artikel[i][5]; 
                aktuelleartikel[i][6] = artikel[i][6];  
            }
            //die neue Position hinzufügen
            aktuelleartikel[anzpos][0] = ue[0][0];
            aktuelleartikel[anzpos][1] = ue[0][1];
            aktuelleartikel[anzpos][2] = ue[0][2];
            aktuelleartikel[anzpos][3] = ue[0][3];
            aktuelleartikel[anzpos][4] = ue[0][5];

            //das Artikel objekt neu anlegen und direkt vergrößern (sind jetzt mehr daten)
            artikel = new String [aktuelleartikel.length][7];

            //artikel mit den bisherigen werten befüllen aus dem Onjekt indem alles zusammengführt wurde
            for (int j = 0; j<artikel.length; j++)
            {  
                artikel[j][0] = aktuelleartikel[j][0];
                artikel[j][1] = aktuelleartikel[j][1];
                artikel[j][2] = aktuelleartikel[j][2];
                artikel[j][3] = aktuelleartikel[j][3];
                artikel[j][4] = aktuelleartikel[j][4];
                artikel[j][5] = aktuelleartikel[j][5];
                artikel[j][6] = aktuelleartikel[j][6];
            }
            //aktuelle auswahl dem Benutzer anzeigen
            jtartnr.setText(artikel[anzpos][0]);
            jthersteller.setText(artikel[anzpos][1]);
            jtmodel.setText(artikel[anzpos][2]);
            jtbeschreibung.setText(artikel[anzpos][3]);
            jtpreis.setText(artikel[anzpos][4]);
    
            jlean.setIcon(EANconfig.createBar(jtartnr.getText()));
            
            jtmenge.setText("");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jbkundensuchen = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jtkdnr = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtnachname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtvorname = new javax.swing.JTextField();
        jbkdoffen = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlbstlnr = new javax.swing.JLabel();
        jlstatus = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jbsuchenartikel = new javax.swing.JButton();
        jbartoeffnen = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jtartnr = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jthersteller = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbeschreibung = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jtmodel = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtpreis = new javax.swing.JTextField();
        jlmenge = new javax.swing.JLabel();
        jtmenge = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jlean = new javax.swing.JLabel();
        jbposhinzu = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtpositionen = new javax.swing.JTable();
        jlrow = new javax.swing.JLabel();
        jlpreisg = new javax.swing.JLabel();
        jbspeichern = new javax.swing.JButton();
        jbdelete = new javax.swing.JButton();
        jcstatus = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jmdrucken = new javax.swing.JMenuItem();
        beenden = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Bestellung");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(200, 100));
        setMaximumSize(new java.awt.Dimension(1210, 645));
        setMinimumSize(new java.awt.Dimension(1210, 645));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 102));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/kunden.gif"))); // NOI18N
        jLabel2.setText("Kunden");

        jbkundensuchen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.gif"))); // NOI18N
        jbkundensuchen.setText("Kunden suchen");
        jbkundensuchen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbkundensuchenActionPerformed(evt);
            }
        });

        jLabel3.setText("Kunden_Nr:");

        jLabel4.setText("Nachname:");

        jLabel5.setText("Vorname:");

        jbkdoffen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/kunden.gif"))); // NOI18N
        jbkdoffen.setText("Kunden öffnen");
        jbkdoffen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbkdoffenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtnachname)
                            .addComponent(jtvorname, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jtkdnr))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbkundensuchen)
                            .addComponent(jbkdoffen, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 13, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtkdnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbkundensuchen))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtnachname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbkdoffen))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtvorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Bestellung");

        jlbstlnr.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        jlstatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jlbstlnr)
                .addGap(18, 18, 18)
                .addComponent(jlstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jlbstlnr)
                    .addComponent(jlstatus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/article.gif"))); // NOI18N
        jLabel6.setText("Artikel");

        jbsuchenartikel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.gif"))); // NOI18N
        jbsuchenartikel.setText("Artikel suchen");
        jbsuchenartikel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsuchenartikelActionPerformed(evt);
            }
        });

        jbartoeffnen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/article.gif"))); // NOI18N
        jbartoeffnen.setText("Artikel öffnen");
        jbartoeffnen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbartoeffnenActionPerformed(evt);
            }
        });

        jLabel7.setText("Artikel_Nr:");

        jLabel8.setText("Hersteller");

        jLabel10.setText("Beschreibung:");

        jtbeschreibung.setColumns(20);
        jtbeschreibung.setRows(5);
        jScrollPane1.setViewportView(jtbeschreibung);

        jLabel9.setText("Modell:");

        jLabel11.setText("Preis:");

        jlmenge.setText("Menge:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("EAN");

        jlean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ean/BarCode128.gif"))); // NOI18N

        jbposhinzu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.gif"))); // NOI18N
        jbposhinzu.setText("Position hinzufügen");
        jbposhinzu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbposhinzuActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Positionen");

        jtpositionen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Artikel_Nr", "Hersteller", "Model", "Beschreibung", "Preis", "Menge", "Preis Gesamt"
            }
        ));
        jtpositionen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtpositionenMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtpositionen);

        jlrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table.gif"))); // NOI18N
        jlrow.setText("jLabel14");

        jlpreisg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlpreisg.setForeground(new java.awt.Color(255, 0, 0));
        jlpreisg.setText("Preis Gesamt");

        jbspeichern.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        jbspeichern.setText("Bestellung speichern");
        jbspeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbspeichernActionPerformed(evt);
            }
        });

        jbdelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.gif"))); // NOI18N
        jbdelete.setText("Position löschen");
        jbdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbdeleteActionPerformed(evt);
            }
        });

        jcstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "in Bearbeitung", "Abgeschlossen", "Cancel" }));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Status ändern:");

        jMenu1.setText("Bestellung");

        jmdrucken.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jmdrucken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        jmdrucken.setText("Drucken");
        jmdrucken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmdruckenActionPerformed(evt);
            }
        });
        jMenu1.add(jmdrucken);

        beenden.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        beenden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.gif"))); // NOI18N
        beenden.setText("beenden");
        beenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beendenActionPerformed(evt);
            }
        });
        jMenu1.add(beenden);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(18, 18, 18)
                                        .addComponent(jcstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jbdelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jbposhinzu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(89, 89, 89)
                                        .addComponent(jbspeichern, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(jtartnr, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel10)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(32, 32, 32)
                                                .addComponent(jbsuchenartikel)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jlmenge)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jtmenge, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jtpreis, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jtmodel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jbartoeffnen)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(18, 18, 18)
                                                .addComponent(jthersteller, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(64, 64, 64)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jlean, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jlrow, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlpreisg, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbspeichern)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jbsuchenartikel)
                            .addComponent(jbartoeffnen)
                            .addComponent(jLabel13))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jthersteller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jtmodel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jtpreis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlmenge, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jtmenge, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jtartnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlean, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbposhinzu)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jcstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbdelete)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlrow)
                    .addComponent(jlpreisg))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbkundensuchenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbkundensuchenActionPerformed
        // Kunden suchen - ruft den Kundenstamm auf:
        if (jlbstlnr.getText().equals(""))
        {
            k = new Kundenstamm(name, recht, this,1,""); //wenn noch kein Kunden hinzugefügt wurde
        }
        else
        {
            k = new Kundenstamm(name, recht, this,2, jtkdnr.getText()); //wenn es schon ein Kunden hinzugefügt wurde
        }
        k.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        k.setVisible(true);
    }//GEN-LAST:event_jbkundensuchenActionPerformed

    private void jbkdoffenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbkdoffenActionPerformed
        // schon bestehenden Kunden aus der Bestellung öffnen:
        k = new Kundenstamm(name, recht, this,2, jtkdnr.getText());
        k.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        k.setVisible(true);
    }//GEN-LAST:event_jbkdoffenActionPerformed

    private void jbsuchenartikelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsuchenartikelActionPerformed
        // Nach Artikel suchen - hinzufügen
        Artikelstamm A = new Artikelstamm(name, recht, this, 1, jtartnr.getText());
        A.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        A.setVisible(true);
    }//GEN-LAST:event_jbsuchenartikelActionPerformed

    private void jbposhinzuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbposhinzuActionPerformed
        // Speichern einer Position
        boolean enthalten=false;
        //Prüfen ob schon eine menge angegeben wurde
        if (jtmenge.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Es wurde keine Menge eingegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            //Prüfen ob der artikel mit dieser artikelnummer schon enthalten ist
            for (int i =0; i<artikel.length;i++)
            {
                //nur wenn die anzahl der artikel größer 0 ist
                if (anzpos > 0)
                {
                    //wenn ja schleife beenden
                    if ((artikel[i][0].equals(jtartnr.getText()))&&(i != anzpos))
                    {
                        enthalten = true;
                        break;
                    }
                }
            }
            if (enthalten == true) // wenn ein schon enthaltener Artikel gefunden wurde
            {
                JOptionPane.showMessageDialog(null, "Dieser Artikel wurde schon hinzugefügt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                // ja ganz richtig, das Srray wird nun wieder verkleinert (der Platz wurde vorher schon reserviert und wird wieder freigegeben)
                System.out.println("anzpos: "+anzpos+" Länge der artikel: "+artikel.length);
                //Artikel manipulieren - letzte zeile wieder aus dem Array löschen
                String [][] aktuelleartikel = new String [artikel.length-1][7];
                //bisherrige artikel abspeichern
                for (int i = 0; i<aktuelleartikel.length; i++)
                {
                aktuelleartikel[i][0] = artikel[i][0];
                aktuelleartikel[i][1] = artikel[i][1];
                aktuelleartikel[i][2] = artikel[i][2];
                aktuelleartikel[i][3] = artikel[i][3];
                aktuelleartikel[i][4] = artikel[i][4];
                aktuelleartikel[i][5] = artikel[i][5]; 
                aktuelleartikel[i][6] = artikel[i][6];
                }
                artikel =  new String [aktuelleartikel.length][7];
                
                 for (int j =0; j<artikel.length; j++)
                {
                artikel[j][0] = aktuelleartikel[j][0];
                artikel[j][1] = aktuelleartikel[j][1];
                artikel[j][2] = aktuelleartikel[j][2];
                artikel[j][3] = aktuelleartikel[j][3];
                artikel[j][4] = aktuelleartikel[j][4];
                artikel[j][5] = aktuelleartikel[j][5];
                artikel[j][6] = aktuelleartikel[j][6]; 
                }
                 
                //Tabelle löschen
                DefaultTableModel model = (DefaultTableModel) jtpositionen.getModel();
                model.setRowCount(0);

                //Tabelle neu füllen
                Object[] row = new Object[7];

                for (int i=0; i< artikel.length; i++)
                {
                    row[0]= artikel[i][0];
                    row[1]= artikel[i][1];
                    row[2]= artikel[i][2];
                    row[3]= artikel[i][3];
                    row[4]= artikel[i][4];
                    row[5]= artikel[i][5];
                    row[6]= artikel[i][6];

                    model.addRow(row);
                }
                sumArtikel();
            }
            else
            {
                //Tabelle löschen
                DefaultTableModel model = (DefaultTableModel) jtpositionen.getModel();
                model.setRowCount(0);

                //Die Menge wird mit dem Preis mal genommen und angezeigt
                artikel[anzpos][5] = jtmenge.getText();
                double gesamt = Double.parseDouble(artikel[anzpos][5]) * Double.parseDouble(artikel[anzpos][4]);
                artikel[anzpos][6] = Double.toString(gesamt);

                //Tabelle neu füllen
                Object[] row = new Object[7];

                for (int i=0; i< artikel.length; i++)
                {
                    row[0]= artikel[i][0];
                    row[1]= artikel[i][1];
                    row[2]= artikel[i][2];
                    row[3]= artikel[i][3];
                    row[4]= artikel[i][4];
                    row[5]= artikel[i][5];
                    row[6]= artikel[i][6];

                    model.addRow(row);
                }

                sumArtikel();
            }
            
            jtartnr.setText("");
            jthersteller.setText("");
            jtmodel.setText("");
            jtpreis.setText("");
            jtmenge.setText("");
            
        }

    }//GEN-LAST:event_jbposhinzuActionPerformed

    private void jbartoeffnenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbartoeffnenActionPerformed
        // Artikel nochmal öffnen
        Artikelstamm A = new Artikelstamm(name, recht, this, 2, jtartnr.getText());
        A.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        A.setVisible(true);
    }//GEN-LAST:event_jbartoeffnenActionPerformed

    private void jbspeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbspeichernActionPerformed
        // Bestellung speichern und eintragen
       int yes = JOptionPane.showConfirmDialog(null, "Möchten sie wirklich speichern?", "Erfolgreich", JOptionPane.YES_NO_OPTION);
       if (yes==0)
       {
        
       if (artikel[0][0]==null)
                {
                    JOptionPane.showMessageDialog(null, "Es wurde keine Position hinzugefügt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                String result = myServiceCaller.insert_Positin(artikel, jlbstlnr.getText(), zuloeschen, jcstatus.getSelectedIndex());
        
                if (0 < Integer.parseInt(result))
                {
                    JOptionPane.showMessageDialog(null, "Die Bestellung wurde gespeichert \nund es sind "+result+" Artikel enthalten!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                    //this.setVisible(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                
                this.dispose();
                }
       }           
    }//GEN-LAST:event_jbspeichernActionPerformed

    private void jtpositionenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtpositionenMouseClicked
        // Event wenn auf die Tabelle gedrückt wird
       jtpositionen.setSelectionBackground(java.awt.Color.blue);
       //angeklickte Zeile auslesen und abspeichern
        int i = jtpositionen.getSelectedRow();
        showRow(i);
    }//GEN-LAST:event_jtpositionenMouseClicked

    private void jbdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbdeleteActionPerformed
        // Löschen Button
        //löschende stelle suchen
        int i=0;
        for (i=0; i<artikel.length;i++)
        {
            if (artikel[i][0].equals(jtartnr.getText()))
            {
                //Stelle finden
                System.out.println("zu löschende stelle:"+i);
                //löschende artnr abspeichern
                zuloeschen[anzzuloeschen]=artikel[i][0];
                //schleifen beenden
                break;
            }
        }
        //anzahl der zu löschenden speichern
        anzzuloeschen++;
        //löschende stelle überscheiben
       for(i=i;i<artikel.length;i++)
       {
           if (i == artikel.length-1)
           {
               System.out.println(i);
               break;

           }
           else
           {
           artikel[i][0]= artikel[i+1][0];
           artikel[i][1]= artikel[i+1][1];
           artikel[i][2]= artikel[i+1][2];
           artikel[i][3]= artikel[i+1][3];
           artikel[i][4]= artikel[i+1][4];
           artikel[i][5]= artikel[i+1][5];
           artikel[i][6]= artikel[i+1][6];
           }
       }
        
        //neuen übergabe array bilden und hinkopieren
        String [][] aktuelleartikel = new String [artikel.length-1][7];
        //bisherrige artikel abspeichern

        for (int j =0; j<artikel.length-1; j++)
        {
                aktuelleartikel[j][0] = artikel[j][0];
                aktuelleartikel[j][1] = artikel[j][1];
                aktuelleartikel[j][2] = artikel[j][2];
                aktuelleartikel[j][3] = artikel[j][3];
                aktuelleartikel[j][4] = artikel[j][4];
                aktuelleartikel[j][5] = artikel[j][5]; 
                aktuelleartikel[j][6] = artikel[j][6]; 
        }
        
        //artikel array überschreiben und neu bilden
        artikel = new String [aktuelleartikel.length][7];
        
         for (int j =0; j<artikel.length; j++)
        {
            artikel[j][0] = aktuelleartikel[j][0];
            artikel[j][1] = aktuelleartikel[j][1];
            artikel[j][2] = aktuelleartikel[j][2];
            artikel[j][3] = aktuelleartikel[j][3];
            artikel[j][4] = aktuelleartikel[j][4];
            artikel[j][5] = aktuelleartikel[j][5];
            artikel[j][6] = aktuelleartikel[j][6]; 
        }
        
        //anzahl der positionen verringern
        anzpos--;
       //Tabelle löschen
        DefaultTableModel model = (DefaultTableModel) jtpositionen.getModel();
        model.setRowCount(0);
            
       //Tabelle neu füllen
        Object[] row = new Object[7];

        for (i=0; i< artikel.length; i++)   
        {
            row[0]= artikel[i][0];
            row[1]= artikel[i][1];
            row[2]= artikel[i][2];
            row[3]= artikel[i][3];
            row[4]= artikel[i][4];
            row[5]= artikel[i][5];
            row[6]= artikel[i][6];

            model.addRow(row);
        }
        
        jtartnr.setText("");
        jthersteller.setText("");
        jtmodel.setText("");
        jtbeschreibung.setText("");
        jtpreis.setText("");
        jtmenge.setText("");
    
        
    sumArtikel();
    
     if (artikel.length == 0)
     {  
        artikel = new String [1][7];
     }
    }//GEN-LAST:event_jbdeleteActionPerformed

    private void jmdruckenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmdruckenActionPerformed
        //menüeintrag drucken
        //nur möglich als Anwender oder Administrator
        if ((recht == 1)||(recht == 2))
        {
        try {
            // Drucken Button in Bestellungen
            if (jcstatus.getSelectedIndex()==2){
                Print p = new Print("Print");
                p.druckeRechnung(artikel,Bestell_ID, myServiceCaller.einen_kudnen(kdnr), jtpositionen);
            }
            //drucken einer Auftragsbestätigung
            if (jcstatus.getSelectedIndex()==1){
                Print p = new Print("Print");
                p.druckeAuftragsbestaetigung(artikel,Bestell_ID, myServiceCaller.einen_kudnen(kdnr), jtpositionen);
            }
        } catch (IOException ex) {
            Logger.getLogger(Bestellung.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

    }//GEN-LAST:event_jmdruckenActionPerformed

    private void beendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beendenActionPerformed
      if (jtkdnr.getText().equals(""))
        {
            this.dispose();
        }
        else
        {
            int yes = JOptionPane.showConfirmDialog(null, "Möchten Sie den Vorgang beenden?", "Frage", JOptionPane.YES_NO_OPTION);
            if (yes==0)
            {
                if (artikel[0][0]==null)
                {
                    JOptionPane.showMessageDialog(null, "Es wurde keine Position hinzugefügt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                String result = myServiceCaller.insert_Positin(artikel, jlbstlnr.getText(), zuloeschen, jcstatus.getSelectedIndex());
        
                if (0 < Integer.parseInt(result))
                {
                    JOptionPane.showMessageDialog(null, "Die Bestellung wurde gespeichert \nund es sind "+result+" Artikel enthalten!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                    //this.setVisible(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                
                this.dispose();
                }
            } 
        }
    }//GEN-LAST:event_beendenActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Aktion für das schliessen
        if (jtkdnr.getText().equals(""))
        {
            this.dispose();
        }
        else
        {
            int yes = JOptionPane.showConfirmDialog(null, "Möchten Sie den Vorgang beenden?", "Frage", JOptionPane.YES_NO_OPTION);
            if (yes==0)
            {
                if (artikel[0][0]==null)
                {
                    JOptionPane.showMessageDialog(null, "Es wurde keine Position hinzugefügt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                String result = myServiceCaller.insert_Positin(artikel, jlbstlnr.getText(), zuloeschen, jcstatus.getSelectedIndex());
        
                if (0 < Integer.parseInt(result))
                {
                    JOptionPane.showMessageDialog(null, "Die Bestellung wurde gespeichert \nund es sind "+result+" Artikel enthalten!", "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                    //this.setVisible(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Ein Fehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                
                this.dispose();
                }
            } 
        }
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(Bestellung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bestellung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bestellung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bestellung.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bestellung(00,1,"Test-Umgebung").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem beenden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbartoeffnen;
    private javax.swing.JButton jbdelete;
    private javax.swing.JButton jbkdoffen;
    private javax.swing.JButton jbkundensuchen;
    private javax.swing.JButton jbposhinzu;
    private javax.swing.JButton jbspeichern;
    private javax.swing.JButton jbsuchenartikel;
    private javax.swing.JComboBox<String> jcstatus;
    private javax.swing.JLabel jlbstlnr;
    private javax.swing.JLabel jlean;
    private javax.swing.JLabel jlmenge;
    private javax.swing.JLabel jlpreisg;
    private javax.swing.JLabel jlrow;
    private javax.swing.JLabel jlstatus;
    private javax.swing.JMenuItem jmdrucken;
    private javax.swing.JTextField jtartnr;
    private javax.swing.JTextArea jtbeschreibung;
    private javax.swing.JTextField jthersteller;
    private javax.swing.JTextField jtkdnr;
    private javax.swing.JTextField jtmenge;
    private javax.swing.JTextField jtmodel;
    private javax.swing.JTextField jtnachname;
    private javax.swing.JTable jtpositionen;
    private javax.swing.JTextField jtpreis;
    private javax.swing.JTextField jtvorname;
    // End of variables declaration//GEN-END:variables
}
