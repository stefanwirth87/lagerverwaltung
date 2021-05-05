package verwaltung;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import lipermi.handler.CallHandler;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;
import testcommon.TestService;

public class Kundenstamm extends javax.swing.JFrame {
    //Klassenweite variablen
    TestService myServiceCaller;
    String Benutzer;
    int akt = 0;
    String[][] alle;
    Einstellungen E;
    int recht, art = 0;
    boolean kundeschondrin;
    public String kdnr;
    Bestellung aufrufend = null;
    String [][] beleg_alt;
    
    //Konstruktor
    public Kundenstamm(String Benutzer, int recht) {
        
        //Verbindungsaufbau (siehe Einstellungen.java)
        E = new Einstellungen();
        this.myServiceCaller = E.connection();
        this.Benutzer = Benutzer;
        this.recht = recht;
        this.aufrufend=aufrufend;
        
        //Fensteraufbauen
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setIconImage(icon);
        initComponents();
        
        //JLabels befüllen
        jlbenutzer.setText(Benutzer);
        jbloeschen.setVisible(false);
        
       //Rechteverarbeitung
        if (recht == 3) //Recht praktikant
        {
            jbsave.setVisible(false);
            jbneu.setVisible(false);
            jbloeschen.setVisible(false);
            jmexport.setForeground(Color.GRAY);
            jmdrucken.setForeground(Color.GRAY);
        }
        else if(recht==2) //Recht Anwender
        {
            jbloeschen.setVisible(false);
            jbsave.setVisible(true);
            jbneu.setVisible(true);
        }
        else if (recht == 1) //Recht Administrator
        {
            jbloeschen.setVisible(true);
        }

        jbue.setVisible(false);

        alle = myServiceCaller.Alle();
        createTable();
        
        
    }
    //zweiter Konstruktor wenn dieses Fenster von der Bestellung aufgerufen wird (verarbeitung wie oben)
    public Kundenstamm(String Benutzer, int recht, Bestellung aufrufend, int art, String suche) {
        
        E = new Einstellungen();
        this.myServiceCaller = E.connection();
        this.Benutzer = Benutzer;
        this.recht = recht;
        this.aufrufend=aufrufend;
        this.art = art;
  
        //Fensteraufbauen
        Image icon = new ImageIcon("ic_lagerverwaltung32.gif").getImage();
        setSize(1000,1000);
        setIconImage(icon);
        setVisible(true);
        initComponents();
        
        jlbenutzer.setText(Benutzer);
        jbloeschen.setVisible(false);
        if(recht==1)
        {jbloeschen.setVisible(true);}
        
        //Rechteverarbeitung
        if (recht == 3)
        {
            jbloeschen.setVisible(false);
            jbsave.setVisible(false);
            jbneu.setVisible(false);
            jmexport.setForeground(Color.GRAY);
            jmdrucken.setForeground(Color.GRAY);
        }
        
        jbue.setVisible(true);

        //Benutzer anzeigen
        jlbenutzer.setText(Benutzer);
        
        if (art == 1) // wenn nach allen Kunden aus der Bestellung aus gesucht werden soll
        {
         alle = myServiceCaller.Alle();
        createTable();
        kundeschondrin = false;
        
        }
        else if (art == 2) //wenn ein bestehender Kunde geöffnet werden soll
        {
        kundeschondrin = true;   
        jtsuche.setText(suche);
        jbue.setVisible(false);
        
            //Suche Text abrufen
        String s = jtsuche.getText();
        //Suche aufrufen
        alle = myServiceCaller.einen_kudnen(s);
        
        createTable();
        }
    }
    
    // suchen Methode
    public final void suchen()
    {
        //Suche Text abrufen
        String suche = jtsuche.getText();
 
        //Suche aufrufen
        alle = myServiceCaller.search(suche);
   
       createTable();
    }
    
    // erstellen der Tabelle
    public void createTable()
    {
        //Tabelen Modell ziehen und neue Ergebnisse anzeigen
       DefaultTableModel model = (DefaultTableModel) jtkunden.getModel();
       model.setRowCount(0);
        
       Object[] row = new Object[10];
       
        for(int i=0; i< alle.length; i++)
        {
            row[0]= alle[i][0];
            row[1]= alle[i][1];
            row[2]= alle[i][2];
            row[3]= alle[i][3];
            row[4]= alle[i][4];
            row[5]= alle[i][5];
            row[6]= alle[i][6];
            row[7]= alle[i][7];
            row[8]= alle[i][8];
            row[9]= alle[i][9];
            
            model.addRow(row);
        }
        
        //Anzahl anzeigen
        jlrows.setText(alle.length + " Rows returned");
        //aktuelle Position setzen
        akt=0;
        //Textfelder und Combofelder bestücken - nur wenn etwas gefunden wurde
        if (alle.length > 0)
        {
        JTkdnummer.setText(alle[akt][0]);
        
        if ("Herr".equals((String) alle[akt][1]))
        {
            jcanrede.setSelectedIndex(0);
        }
        else
        {
            jcanrede.setSelectedIndex(1);
        }
        jtnachname.setText(alle[akt][2]);
        jtvorname.setText(alle[akt][3]);
        jtstrasse.setText(alle[akt][4]);
        jthausnummer.setText(alle[akt][5]);
        jtplz.setText(alle[akt][6]);
        jtort.setText(alle[akt][7]);
        jttel.setText(alle[akt][8]);
        jtemail.setText(alle[akt][9]);
        }
        else
        {
        JOptionPane.showMessageDialog(null, "Ihre Suche hat nichts ergeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
        jtsuche.setText("");
        suchen();
        }
    
    }

   //Beim ersten aufruf des Fenster wird diese Methode aufgerufen
    public void Benutzer_in_Tabelle()
    {   
        //Alle Kunden vom Server holen
        alle = myServiceCaller.Alle();
        //Tabelle füllen
        Object[] row = new Object[10];
        DefaultTableModel model = (DefaultTableModel) jtkunden.getModel();
        
        for (int i=0; i< alle.length; i++)
        {
            row[0]= alle[i][0];
            row[1]= alle[i][1];
            row[2]= alle[i][2];
            row[3]= alle[i][3];
            row[4]= alle[i][4];
            row[5]= alle[i][5];
            row[6]= alle[i][6];
            row[7]= alle[i][7];
            row[8]= alle[i][8];
            row[9]= alle[i][9];
            
            model.addRow(row);
        }
        
        //Anzahl der Zeilen anzeigen
        jlrows.setText(alle.length + " Rows returned");
    }
   
     public void neu_Tabelle()
    {   
       DefaultTableModel model = (DefaultTableModel) jtkunden.getModel();
       model.setRowCount(0);
        
       Benutzer_in_Tabelle();  
    }
      
      

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jlbenutzer1 = new javax.swing.JLabel();
        jlkundennr1 = new javax.swing.JLabel();
        JTkdnummer1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jtnachname1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jtvorname1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jtstrasse1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jthausnummer1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jtplz1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jtort1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jttel1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jtemail1 = new javax.swing.JTextField();
        jbzureuck1 = new javax.swing.JButton();
        jbvor1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtkunden1 = new javax.swing.JTable();
        jbneu1 = new javax.swing.JButton();
        jbloeschen1 = new javax.swing.JButton();
        jcanrede1 = new javax.swing.JComboBox<>();
        jtsuche1 = new javax.swing.JTextField();
        jlrows1 = new javax.swing.JLabel();
        jbsave1 = new javax.swing.JButton();
        jbue1 = new javax.swing.JButton();
        jbclear1 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jmprogramm1 = new javax.swing.JMenu();
        jmexport1 = new javax.swing.JMenuItem();
        jmdrucken1 = new javax.swing.JMenuItem();
        jmbeenden1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jlbenutzer = new javax.swing.JLabel();
        jlkundennr = new javax.swing.JLabel();
        JTkdnummer = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jtnachname = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtvorname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtstrasse = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jthausnummer = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtplz = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtort = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jttel = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jtemail = new javax.swing.JTextField();
        jbzureuck = new javax.swing.JButton();
        jbvor = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtkunden = new javax.swing.JTable();
        jbneu = new javax.swing.JButton();
        jbloeschen = new javax.swing.JButton();
        jcanrede = new javax.swing.JComboBox<>();
        jtsuche = new javax.swing.JTextField();
        jlrows = new javax.swing.JLabel();
        jbsave = new javax.swing.JButton();
        jbue = new javax.swing.JButton();
        jbclear = new javax.swing.JButton();
        jbbestehende = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmprogramm = new javax.swing.JMenu();
        jmexport = new javax.swing.JMenuItem();
        jmdrucken = new javax.swing.JMenuItem();
        jmbeenden = new javax.swing.JMenuItem();

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFrame1.setTitle("Kundenstamm");
        jFrame1.setLocation(new java.awt.Point(200, 100));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Kundenstamm");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.gif"))); // NOI18N
        jLabel4.setText("Benutzer:");

        jlbenutzer1.setText("jLabel3");

        jlkundennr1.setText("Kunden_Nr:");

        JTkdnummer1.setText("jTextField1");

        jLabel14.setText("Anrede:");

        jLabel15.setText("Nachname:");

        jtnachname1.setText("jTextField1");

        jLabel16.setText("Vorname");

        jtvorname1.setText("jTextField1");

        jLabel17.setText("Straße:");

        jtstrasse1.setText("jTextField1");

        jLabel18.setText("Hausnummer:");

        jthausnummer1.setText("jTextField1");

        jLabel19.setText("Postleitzahl:");

        jtplz1.setText("jTextField1");

        jLabel20.setText("Ort");

        jtort1.setText("jTextField1");

        jLabel21.setText("Telefon:");

        jttel1.setText("jTextField1");

        jLabel22.setText("E Mail:");

        jtemail1.setText("jTextField1");

        jbzureuck1.setText("<<");
        jbzureuck1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbzureuck1ActionPerformed(evt);
            }
        });

        jbvor1.setText(">>");
        jbvor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbvor1ActionPerformed(evt);
            }
        });

        jtkunden1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kundennummer", "Anrede", "Nachname", "Vorname", "Straße", "Hausnummer", "Postleitzahl", "Ort", "Telefon", "E-Mail"
            }
        ));
        jtkunden1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtkunden1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtkunden1);

        jbneu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.gif"))); // NOI18N
        jbneu1.setText("Neuer Kunde");
        jbneu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbneu1ActionPerformed(evt);
            }
        });

        jbloeschen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.gif"))); // NOI18N
        jbloeschen1.setText("Kunden löschen");
        jbloeschen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbloeschen1ActionPerformed(evt);
            }
        });

        jcanrede1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Herr", "Frau" }));

        jtsuche1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtsuche1KeyPressed(evt);
            }
        });

        jlrows1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table.gif"))); // NOI18N
        jlrows1.setText("jLabel14");

        jbsave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        jbsave1.setText("Speichern");
        jbsave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsave1ActionPerformed(evt);
            }
        });

        jbue1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/download-cloud-flat.gif"))); // NOI18N
        jbue1.setText("Übernahme");
        jbue1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbue1ActionPerformed(evt);
            }
        });

        jbclear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clear.gif"))); // NOI18N
        jbclear1.setText("clear");
        jbclear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbclear1ActionPerformed(evt);
            }
        });

        jmprogramm1.setText("Kundenstamm");

        jmexport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export.gif"))); // NOI18N
        jmexport1.setText("Exportieren");
        jmexport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmexport1ActionPerformed(evt);
            }
        });
        jmprogramm1.add(jmexport1);

        jmdrucken1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        jmdrucken1.setText("Drucken");
        jmdrucken1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmdrucken1ActionPerformed(evt);
            }
        });
        jmprogramm1.add(jmdrucken1);

        jmbeenden1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.gif"))); // NOI18N
        jmbeenden1.setText("beenden");
        jmbeenden1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmbeenden1ActionPerformed(evt);
            }
        });
        jmprogramm1.add(jmbeenden1);

        jMenuBar2.add(jmprogramm1);

        jFrame1.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16)
                                    .addComponent(jlkundennr1)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtvorname1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(jtnachname1)
                                    .addComponent(jcanrede1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JTkdnummer1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                                .addGap(48, 48, 48))
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(91, 91, 91)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel17))
                                .addGap(22, 22, 22)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtstrasse1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                                    .addComponent(jtplz1)
                                    .addComponent(jtort1)
                                    .addComponent(jthausnummer1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addComponent(jlbenutzer1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbneu1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22))
                                .addGap(18, 18, 18)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtemail1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jttel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 410, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrame1Layout.createSequentialGroup()
                                .addComponent(jbsave1)
                                .addGap(29, 29, 29)
                                .addComponent(jtsuche1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbclear1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jbue1)
                                    .addComponent(jbloeschen1)))))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addComponent(jbzureuck1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbvor1))
                    .addComponent(jlrows1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jlbenutzer1)
                        .addComponent(jbloeschen1))
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtsuche1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbneu1)
                        .addComponent(jbsave1)
                        .addComponent(jbclear1)))
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbzureuck1)
                            .addComponent(jbvor1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlkundennr1)
                            .addComponent(JTkdnummer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jtstrasse1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jbue1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jthausnummer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcanrede1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jtnachname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtplz1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(jttel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jtvorname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jtort1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jtemail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jlrows1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kundenstamm");
        setLocation(new java.awt.Point(200, 100));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Kundenstamm");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.gif"))); // NOI18N
        jLabel2.setText("Benutzer:");

        jlbenutzer.setText("jLabel3");

        jlkundennr.setText("Kunden_Nr:");

        JTkdnummer.setText("jTextField1");

        jLabel5.setText("Anrede:");

        jLabel6.setText("Nachname:");

        jtnachname.setText("jTextField1");

        jLabel7.setText("Vorname");

        jtvorname.setText("jTextField1");

        jLabel8.setText("Straße:");

        jtstrasse.setText("jTextField1");

        jLabel9.setText("Hausnummer:");

        jthausnummer.setText("jTextField1");

        jLabel10.setText("Postleitzahl:");

        jtplz.setText("jTextField1");

        jLabel11.setText("Ort");

        jtort.setText("jTextField1");

        jLabel12.setText("Telefon:");

        jttel.setText("jTextField1");

        jLabel13.setText("E Mail:");

        jtemail.setText("jTextField1");

        jbzureuck.setText("<<");
        jbzureuck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbzureuckActionPerformed(evt);
            }
        });

        jbvor.setText(">>");
        jbvor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbvorActionPerformed(evt);
            }
        });

        jtkunden.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kundennummer", "Anrede", "Nachname", "Vorname", "Straße", "Hausnummer", "Postleitzahl", "Ort", "Telefon", "E-Mail"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtkunden.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jtkunden.setMinimumSize(new java.awt.Dimension(150, 150));
        jtkunden.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtkundenMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtkunden);
        if (jtkunden.getColumnModel().getColumnCount() > 0) {
            jtkunden.getColumnModel().getColumn(0).setPreferredWidth(50);
            jtkunden.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jbneu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.gif"))); // NOI18N
        jbneu.setText("Neuer Kunde");
        jbneu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbneuActionPerformed(evt);
            }
        });

        jbloeschen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.gif"))); // NOI18N
        jbloeschen.setText("Kunden löschen");
        jbloeschen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbloeschenActionPerformed(evt);
            }
        });

        jcanrede.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Herr", "Frau" }));

        jtsuche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtsucheKeyPressed(evt);
            }
        });

        jlrows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/table.gif"))); // NOI18N
        jlrows.setText("jLabel14");

        jbsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        jbsave.setText("Speichern");
        jbsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsaveActionPerformed(evt);
            }
        });

        jbue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/download-cloud-flat.gif"))); // NOI18N
        jbue.setText("Übernahme");
        jbue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbueActionPerformed(evt);
            }
        });

        jbclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clear.gif"))); // NOI18N
        jbclear.setText("clear");
        jbclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbclearActionPerformed(evt);
            }
        });

        jbbestehende.setText("Bestehende Kundenbelege");
        jbbestehende.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbbestehendeActionPerformed(evt);
            }
        });

        jmprogramm.setText("Kundenstamm");

        jmexport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jmexport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export.gif"))); // NOI18N
        jmexport.setText("Exportieren");
        jmexport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmexportActionPerformed(evt);
            }
        });
        jmprogramm.add(jmexport);

        jmdrucken.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jmdrucken.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        jmdrucken.setText("Drucken");
        jmdrucken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmdruckenActionPerformed(evt);
            }
        });
        jmprogramm.add(jmdrucken);

        jmbeenden.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jmbeenden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.gif"))); // NOI18N
        jmbeenden.setText("beenden");
        jmbeenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmbeendenActionPerformed(evt);
            }
        });
        jmprogramm.add(jmbeenden);

        jMenuBar1.add(jmprogramm);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jlkundennr)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtvorname, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(jtnachname)
                                    .addComponent(jcanrede, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(JTkdnummer, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                                .addGap(48, 48, 48))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(91, 91, 91)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel8))
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtstrasse, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                                    .addComponent(jtplz)
                                    .addComponent(jtort)
                                    .addComponent(jthausnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jlbenutzer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbneu)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbsave)
                                .addGap(29, 29, 29)
                                .addComponent(jtsuche, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbclear))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jttel, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jbloeschen)
                                .addComponent(jbue))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jbbestehende)
                                .addGap(4, 4, 4)))
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbzureuck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbvor)))
                .addGap(12, 12, 12))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlrows, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jlbenutzer)
                        .addComponent(jbloeschen))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtsuche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbneu)
                        .addComponent(jbsave)
                        .addComponent(jbclear)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbzureuck)
                            .addComponent(jbvor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlkundennr)
                            .addComponent(JTkdnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jtstrasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jbue)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jthausnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcanrede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jtnachname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtplz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)
                        .addComponent(jttel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbbestehende)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtvorname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jtort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jlrows)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbzureuckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbzureuckActionPerformed
        // für den zurück Button
        
        if (akt>0)
        {
        akt=akt-1;
        }
        
        JTkdnummer.setText(alle[akt][0]); //JComboBox
        
        if ("Herr".equals((String) alle[akt][1]))
        {
            jcanrede.setSelectedIndex(0);
        }
        else
        {
            jcanrede.setSelectedIndex(1);
        }
       
        jtnachname.setText(alle[akt][2]);
        jtvorname.setText(alle[akt][3]);
        jtstrasse.setText(alle[akt][4]);
        jthausnummer.setText(alle[akt][5]);
        jtplz.setText(alle[akt][6]);
        jtort.setText(alle[akt][7]);
        jttel.setText(alle[akt][8]);
        jtemail.setText(alle[akt][9]);
        
    }//GEN-LAST:event_jbzureuckActionPerformed

    private void jbvorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbvorActionPerformed
        // für den vor Button

        if(akt+1 < alle.length )
        {
            akt=akt+1;
        }

        JTkdnummer.setText(alle[akt][0]); //JComboBox
        
        if ("Herr".equals((String) alle[akt][1]))
        {
            jcanrede.setSelectedIndex(0);
        }
        else
        {
            jcanrede.setSelectedIndex(1);
        }
    
        jtnachname.setText(alle[akt][2]);
        jtvorname.setText(alle[akt][3]);
        jtstrasse.setText(alle[akt][4]);
        jthausnummer.setText(alle[akt][5]);
        jtplz.setText(alle[akt][6]);
        jtort.setText(alle[akt][7]);
        jttel.setText(alle[akt][8]);
        jtemail.setText(alle[akt][9]);
   
    }//GEN-LAST:event_jbvorActionPerformed

    private void jtkundenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtkundenMouseClicked
        // angeklickte Zeile anzeigen
        //angeklickte Zeile auslesen und abspeichern
        jtkunden.setSelectionBackground(Color.blue);
        int i = jtkunden.getSelectedRow();
        akt = i;
        
        //Table modell holen und aus den Spalten holen und in die Textfelder / JComboboxen füllen
        TableModel model = jtkunden.getModel();
   
        JTkdnummer.setText((String) model.getValueAt(i, 0));

        if ("Herr".equals((String) model.getValueAt(i, 1))) //JComboBox
        {
            jcanrede.setSelectedIndex(0);
        }
        else
        {
            jcanrede.setSelectedIndex(1);
        }

        jtnachname.setText((String) model.getValueAt(i, 2));
        jtvorname.setText((String) model.getValueAt(i, 3));
        jtstrasse.setText((String) model.getValueAt(i, 4));
        jthausnummer.setText((String) model.getValueAt(i, 5));
        jtplz.setText((String) model.getValueAt(i, 6));
        jtort.setText((String) model.getValueAt(i, 7));
        jttel.setText((String) model.getValueAt(i, 8));
        jtemail.setText((String) model.getValueAt(i, 9));
        
        //Anzeigen ob auf den Kunden schon belege angelegt wurden (siehe Belege_Kunde.java)
        beleg_alt = myServiceCaller.BestellungeneinesKunden(JTkdnummer.getText());
        
        if (beleg_alt.length==0)
        {
            jbbestehende.setVisible(false);
        }
        else
        {
            jbbestehende.setVisible(true);
            jbbestehende.setText("Bestehende Kundenbelege: "+beleg_alt.length);
        }
  
    }//GEN-LAST:event_jtkundenMouseClicked

    private void jbneuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbneuActionPerformed
        // Button neu - alle Felder leeren
        JTkdnummer.setVisible(false);
        jlkundennr.setVisible(false);
        jbue.setVisible(false);
        JTkdnummer.setText("");
        jtnachname.setText("");
        jtvorname.setText("");
        jtstrasse.setText("");
        jthausnummer.setText("");
        jtplz.setText("");
        jtort.setText("");
        jttel.setText("");
        jtemail.setText("");
    }//GEN-LAST:event_jbneuActionPerformed

    private void jbloeschenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbloeschenActionPerformed
        // Loeschen Button
        // mit vorhergehender Abfrage ob es wirklich passieren soll
        int ok = JOptionPane.showConfirmDialog(null, "Möchten sie den Kunden wirklich löschen ?", "Hinweiß" , JOptionPane.YES_NO_OPTION);
        //nach bestätigung des Anwender wird der Datensatz gelöscht und die Tabelle erstellt
        if (ok == 0)
        {
            int kunden_id = Integer.parseInt(JTkdnummer.getText());
            String Nachname = jtnachname.getText();

            String r = myServiceCaller.loeschen_kunden(kunden_id, Nachname);
            
            //Die Tabelle wird erneut erstellt und die bestätigung angegeben
            neu_Tabelle();
            JOptionPane.showMessageDialog(null, r, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jbloeschenActionPerformed

    private void jmbeendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmbeendenActionPerformed
        // Menü: Programm - beenden
        E.connection_close();
        this.setVisible(false);
        
    }//GEN-LAST:event_jmbeendenActionPerformed

    private void jbsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsaveActionPerformed
        // Neuen Kunden anlegen mit prüfung ob die Kundenummer leer ist, weil diese trägt die Datenbank selbst ein
        //Variablen deklariert und intialisiert
            String anrede="", nachname="", vorname="", strasse="", ort="", tel="", mail="",hausnummer="";
            int plz=0, kunden_id=0;
            int err = 0; //errorcode - erst wenn der am ende 0 ist wird der Datensatz eingetragen
            
        //Prüfung ob die Kundennummer leer ist, wenn ja auch ein eintragen erfolgen, 
        //wenn nicht müssen die felder vorher gelöscht werden, da es sonst zu einer überschreinung kommen kann
        
        if (JTkdnummer.getText().equals(""))
        {
            //abfrage ob die neu eingetragenen Daten als Kundensatz angelegt werden soll
            int ok = JOptionPane.showConfirmDialog(null, "Kunden anlegen ?", "Fehler" , JOptionPane.YES_NO_OPTION);
            //ok==0 == ja
            if (ok == 0)
            {
                /*Vorgang bleibt gleich für alle Felder*/
                //Anrede Combo Box
                
                 int jc = jcanrede.getSelectedIndex();      //prüfen welches Feld gewählt wurde
                 if (jc == 0)                               
                 {
                      anrede = "Herr";                      //Text dementsprechend übernehmen
                 }
                 else
                 {
                     anrede = "Frau";
                 }

                //Nachname
                 if (jtnachname.getText().equals(""))       //Prüfen ob im Textfeld inhalt vorhanden ist
                {
                     //eventuell Fehler ausgeben und den Fehler status anzeigen
                 JOptionPane.showMessageDialog(null, "Kein Nachname Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                 else //Alternatib text übernehmen - bei den anderen Textfeldern gilt das gleiche Vorgehen
                {
                 nachname = jtnachname.getText();
                }

                //Vorname 
                if (jtvorname.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Vorname Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 vorname = jtvorname.getText();
                }

                //Strasse
                if (jtstrasse.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Strasse Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 strasse = jtstrasse.getText();
                }

                //hausnummer

                 if (jthausnummer.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Hausnummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 hausnummer = jthausnummer.getText();
                }

                // PLZ 
                if (jtplz.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Postleitzahl Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 plz = Integer.parseInt(jtplz.getText());
                }

                //Ort
                if (jtort.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Kein Ort Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 ort = jtort.getText();
                }

                //Telefon
                if (jttel.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine Telefonnummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 tel = jttel.getText();
                }

                if (jtemail.getText().equals(""))
                {
                 JOptionPane.showMessageDialog(null, "Keine E-Mail Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                 err = err + 1;
                }
                else
                {
                 mail = jtemail.getText();
                }

                if (err == 0)//nur wenn alle Felder gefüllt sind wird der Datensatz erstellt
                {
                    //aufruf der Methode insert_Kunde_neu auf dem Server
                    String r = myServiceCaller.insert_Kunde_neu(anrede, nachname, vorname, strasse, hausnummer, plz, ort, tel, mail);
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
            if (JTkdnummer.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine Kundenummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             kunden_id = Integer.parseInt(JTkdnummer.getText());
            } 
            
            //Anrede Combo Box - auslesen wie oben
            int jc = jcanrede.getSelectedIndex();
            if (jc == 0)
            {
                anrede = "Herr";
            }
            else
            {
                anrede = "Frau";
            }
  
            //Nachname
            if (jtnachname.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Kein Nachname Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             nachname = jtnachname.getText();
            }
            //Vorname 
            if (jtvorname.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Kein Vorname Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             vorname = jtvorname.getText();
            }
            //Strasse
            if (jtstrasse.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine Strasse Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             strasse = jtstrasse.getText();
            }
            //hausnummer
             if (jthausnummer.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine Hausnummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             hausnummer = jthausnummer.getText();
            } 
            // PLZ 
            if (jtplz.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine Postleitzahl Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             plz = Integer.parseInt(jtplz.getText());
            }
            //Ort
            if (jtort.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Kein Ort Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             ort = jtort.getText();
            }
            //Telefon
            if (jttel.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine Telefonnummer Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             tel = jttel.getText();
            }
            
            if (jtemail.getText().equals(""))
            {
             JOptionPane.showMessageDialog(null, "Keine E-Mail Vorhanden!", "Fehler", JOptionPane.ERROR_MESSAGE);
             err = err + 1;
            }
            else
            {
             mail = jtemail.getText();
            }
            
            if (err == 0)//nur wenn alle Felder gefüllt sind wird der Datensatz erstellt
            {
                String r = myServiceCaller.update_Kunden(kunden_id, anrede, nachname, vorname, strasse, hausnummer, plz, ort, tel, mail);

                JOptionPane.showMessageDialog(null, r, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                
                neu_Tabelle();
            }
  
           }
        }
        if (art != 0)
        {jbue.setVisible(true);}
        
        JTkdnummer.setVisible(true);
        jlkundennr.setVisible(true);
    }//GEN-LAST:event_jbsaveActionPerformed

    private void jmdruckenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmdruckenActionPerformed
        // Drucken (Print.java "druckeKunden") -> Druckt aktuelle Tabellen ansicht
        //kann nur ausgeführt werden wenn der Benutzer Administrator oder Anwender ist
        if ((recht == 1)||(recht ==2))
        {
        Print p = new Print("Print");
        p.druckeKunden(alle);
        }
    }//GEN-LAST:event_jmdruckenActionPerformed

    private void jbueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbueActionPerformed
        // Für den Übernahme Button in eine Bestellung
        if (JTkdnummer.getText().equals(""))
        {
           JOptionPane.showMessageDialog(null, "Bitte zu übernehmenden Kunden auswählen!", "Fehler", JOptionPane.ERROR_MESSAGE); 
        }
        else
        {
           if(kundeschondrin==false)
           {   
            aufrufend.kdnr = JTkdnummer.getText();
            aufrufend.nachname = jtnachname.getText();
            aufrufend.vorname = jtvorname.getText();
            aufrufend.kundefuellen(0); 
            this.setVisible(false);
           }
           else
           {  
           String r = myServiceCaller.update_KundenBestellung(aufrufend.Bestell_ID, JTkdnummer.getText());
           JOptionPane.showMessageDialog(null, r, "Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
           aufrufend.kdnr = JTkdnummer.getText();
            aufrufend.nachname = jtnachname.getText();
            aufrufend.vorname = jtvorname.getText();
            aufrufend.kundefuellen(1); 
           this.setVisible(false);
            }
        }
    }//GEN-LAST:event_jbueActionPerformed

    private void jtsucheKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtsucheKeyPressed
        // key Pressed Event - ruft suchen Methode auf bei tastendruck
        suchen();
    }//GEN-LAST:event_jtsucheKeyPressed

    private void jbclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbclearActionPerformed
        // clear suche
        jtsuche.setText("");
        suchen();
    }//GEN-LAST:event_jbclearActionPerformed

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

                    bw.write("kdnr;Anrede;Nachname;Vorname;Straße;Hausnummer;PLZ;Ort;Telefon;E-Mail");   
                    bw.newLine();  

                    for(int i=0;i<alle.length;i++)
                    {
                        bw.write(alle[i][0]+";");
                        bw.write(alle[i][1]+";");
                        bw.write(alle[i][2]+";");
                        bw.write(alle[i][3]+";");
                        bw.write(alle[i][4]+";");
                        bw.write(alle[i][5]+";");
                        bw.write(alle[i][6]+";");
                        bw.write(alle[i][7]+";");
                        bw.write(alle[i][8]+";");
                        bw.write(alle[i][9]);

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

    private void jbzureuck1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbzureuck1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbzureuck1ActionPerformed

    private void jbvor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbvor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbvor1ActionPerformed

    private void jtkunden1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtkunden1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtkunden1MouseClicked

    private void jbneu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbneu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbneu1ActionPerformed

    private void jbloeschen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbloeschen1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbloeschen1ActionPerformed

    private void jtsuche1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtsuche1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtsuche1KeyPressed

    private void jbsave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsave1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbsave1ActionPerformed

    private void jbue1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbue1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbue1ActionPerformed

    private void jbclear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbclear1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbclear1ActionPerformed

    private void jmexport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmexport1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmexport1ActionPerformed

    private void jmdrucken1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmdrucken1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmdrucken1ActionPerformed

    private void jmbeenden1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmbeenden1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jmbeenden1ActionPerformed

    private void jbbestehendeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbbestehendeActionPerformed
        // zum anzeigen der bestehenden Kundenbelege
        Belege_Kunde BK = new Belege_Kunde(myServiceCaller.BestellungeneinesKunden(JTkdnummer.getText()), recht, Benutzer, JTkdnummer.getText(), jtvorname.getText(), jtnachname.getText());
        BK.setVisible(true);
    }//GEN-LAST:event_jbbestehendeActionPerformed

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
            java.util.logging.Logger.getLogger(Kundenstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kundenstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kundenstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kundenstamm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
      
                new Kundenstamm("Test-Benutzer", 1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JTkdnummer;
    private javax.swing.JTextField JTkdnummer1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbbestehende;
    private javax.swing.JButton jbclear;
    private javax.swing.JButton jbclear1;
    private javax.swing.JButton jbloeschen;
    private javax.swing.JButton jbloeschen1;
    private javax.swing.JButton jbneu;
    private javax.swing.JButton jbneu1;
    private javax.swing.JButton jbsave;
    private javax.swing.JButton jbsave1;
    private javax.swing.JButton jbue;
    private javax.swing.JButton jbue1;
    private javax.swing.JButton jbvor;
    private javax.swing.JButton jbvor1;
    private javax.swing.JButton jbzureuck;
    private javax.swing.JButton jbzureuck1;
    private javax.swing.JComboBox<String> jcanrede;
    private javax.swing.JComboBox<String> jcanrede1;
    private javax.swing.JLabel jlbenutzer;
    private javax.swing.JLabel jlbenutzer1;
    private javax.swing.JLabel jlkundennr;
    private javax.swing.JLabel jlkundennr1;
    private javax.swing.JLabel jlrows;
    private javax.swing.JLabel jlrows1;
    private javax.swing.JMenuItem jmbeenden;
    private javax.swing.JMenuItem jmbeenden1;
    private javax.swing.JMenuItem jmdrucken;
    private javax.swing.JMenuItem jmdrucken1;
    private javax.swing.JMenuItem jmexport;
    private javax.swing.JMenuItem jmexport1;
    private javax.swing.JMenu jmprogramm;
    private javax.swing.JMenu jmprogramm1;
    private javax.swing.JTextField jtemail;
    private javax.swing.JTextField jtemail1;
    private javax.swing.JTextField jthausnummer;
    private javax.swing.JTextField jthausnummer1;
    private javax.swing.JTable jtkunden;
    private javax.swing.JTable jtkunden1;
    private javax.swing.JTextField jtnachname;
    private javax.swing.JTextField jtnachname1;
    private javax.swing.JTextField jtort;
    private javax.swing.JTextField jtort1;
    private javax.swing.JTextField jtplz;
    private javax.swing.JTextField jtplz1;
    private javax.swing.JTextField jtstrasse;
    private javax.swing.JTextField jtstrasse1;
    private javax.swing.JTextField jtsuche;
    private javax.swing.JTextField jtsuche1;
    private javax.swing.JTextField jttel;
    private javax.swing.JTextField jttel1;
    private javax.swing.JTextField jtvorname;
    private javax.swing.JTextField jtvorname1;
    // End of variables declaration//GEN-END:variables
}
