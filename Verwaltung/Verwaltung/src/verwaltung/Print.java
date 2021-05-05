package verwaltung;

/* PrintWithJDK11.java: Drucken mit JDK-1.1-Funktionen */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import testcommon.TestService;
import verwaltung.EANconfig;

public class Print extends Frame
{
  public static void main( String[] args )
  {
    Print wnd = new Print( "Drucken mit JDK-1.1-Funktionen" );
    wnd.setSize( 500, 150 );
    wnd.setVisible( true );
  }
    //Testdruck - Testklasse, nicht in verwendung
  public Print( String sFensterTitel )
  {
    super( sFensterTitel );
    Button bttn = new Button( "Button betätigen, um Ausdruck einer Testseite zu starten ..." );
    add( BorderLayout.CENTER, bttn );
    bttn.addActionListener(
    new ActionListener() {
      public void actionPerformed( ActionEvent ev ) {
          Einstellungen E = new Einstellungen();
          TestService myServiceCaller = E.connection();
      druckeKunden(myServiceCaller.Alle()); } } );
    addWindowListener(
    new WindowAdapter() {
      public void windowClosing( WindowEvent ev ) {
        dispose();
      System.exit( 0 ); } } );
  }
  
  //Drucken der Artikel
  public void druckeArtikelseite(String[][] becomeA)
  {
    String[][] artikel = becomeA;
     //Printjob starten  
    PrintJob prjob = getToolkit().getPrintJob( this, "Liste von Artikeln", null );
    //geamt schleife um den ganzen printjob - ein druchlauf = eine Seite
     for (int i=0; i < artikel.length;i++)
     {
     //wenn der Printjob noch besteht
    if( null != prjob )
    {
      final int iScreenResol           = getToolkit().getScreenResolution();
      final int iPageResol             = prjob.getPageResolution();
      final Dimension dimScreenSize    = getToolkit().getScreenSize();
      final Dimension dimPageDimension = prjob.getPageDimension();
      Graphics pg = prjob.getGraphics();
    
      if( null != pg && 0 < iPageResol )
      {
        int iAddY = 15;
        int iRand = (int)Math.round( iPageResol * 2. / 2.54 );  // 2 cm Rand
        int iPosX = iRand + iRand/4;                            // Textposition
        int iPosY = iPosX - iAddY/2;
        int iWdth = dimPageDimension.width  - iRand * 2;        // innere Breite
        int iMidY = dimPageDimension.height / 2;
        //beim ersten druchlauf wird 
        if (i==0)
        {
        //die schriftgöße verändert
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*3/3 ) );
        //ein Überschrift eingefügt
        pg.drawString("Artikelliste" , iPosX, iPosY);
        }
        //Die schrift wieder verkleinert
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );
        //dann werden so viele Artikel hinzugefügt bis die Seite voll ist
          try {
              
             for (int j=0; j<6;j++)
             {
                 if(i+j < artikel.length)
                 {
             
                      byte [] bar = EANconfig.createBarbyte(artikel[i+j][0]);
                      BufferedImage img = ImageIO.read(new ByteArrayInputStream(bar));
                      //jede Zeile wird mit einem Punkt angegen wo die Zeile beginnt und hochgezählt
                      pg.drawImage(img, iPosX+250, iPosY+=iAddY, this );
                      pg.drawString("Hersteller: " +artikel[i+j][1], iPosX, iPosY+=iAddY);
                      pg.drawString("Model: "+artikel[i+j][2], iPosX, iPosY+=iAddY );
                      pg.drawString("Beschreibung: "+artikel[i+j][3], iPosX, iPosY+=iAddY );
                      pg.drawString("Anzahl Lagernd: "+artikel[i+j][4], iPosX, iPosY+=iAddY );
                      pg.drawString("Preis: "+artikel[i+j][5], iPosX, iPosY+=iAddY );
                      pg.drawString("Lager-ID: "+artikel[i+j][6], iPosX, iPosY+=iAddY );
              
                 }
             }
             
             i = i+5;

          } catch (IOException ex) {
              Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
          }

        pg.dispose();
      }
     
    }
     
    }
     prjob.end(); // wenn alles ruchlaufen ist, der Printjob beendet
  }
  
  //Funktion für das Drucken von Kundeninformationen - vorgehen wie oben
  public void druckeKunden(String[][] becomeA)
  {
    String[][] artikel = becomeA;

    PrintJob prjob = getToolkit().getPrintJob( this, "Liste von Kunden", null );
    
     for (int i=0; i < artikel.length;i++)
     {

    if( null != prjob )
    {
      final int iScreenResol           = getToolkit().getScreenResolution();
      final int iPageResol             = prjob.getPageResolution();
      final Dimension dimScreenSize    = getToolkit().getScreenSize();
      final Dimension dimPageDimension = prjob.getPageDimension();
      Graphics pg = prjob.getGraphics();
    
      if( null != pg && 0 < iPageResol )
      {
        int iAddY = 15;
        int iRand = (int)Math.round( iPageResol * 2. / 2.54 );  // 2 cm Rand
        int iPosX = iRand + iRand/4;                            // Textposition
        int iPosY = iPosX - iAddY/2;
        int iWdth = dimPageDimension.width  - iRand * 2;        // innere Breite
        int iMidY = dimPageDimension.height / 2;
       
        
        if (i==0)
        {
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*3/3 ) );
    
        pg.drawString("Kundenliste" , iPosX, iPosY);

        try {
            BufferedImage img = ImageIO.read(new File("ic_lagerverwaltung96.jpg"));
    
            pg.drawImage(img, iPosX+350, iPosY, this );
        } catch (IOException ex) {
            Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
        }

        //leerzeile
        pg.drawString(" ", iPosX, iPosY+=iAddY );
        }
        
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );

          try {
              
             for (int j=0; j<6;j++)
                 //<13 //j=j+2
             {
                 if(i+j < artikel.length)
                 {
                    pg.drawString("Kundennummer: " +artikel[i+j][0], iPosX, iPosY+=iAddY);
                    pg.drawString(artikel[i+j][1]+" "+artikel[i+j][2]+" "+artikel[i+j][3], iPosX, iPosY+=iAddY );
                    pg.drawString(artikel[i+j][4]+" "+artikel[i+j][5], iPosX, iPosY+=iAddY );
                    pg.drawString(artikel[i+j][6]+" "+artikel[i+j][7], iPosX, iPosY+=iAddY );
                    pg.drawString("Telefon: "+artikel[i+j][8], iPosX, iPosY+=iAddY );
                    pg.drawString("E-Mail: "+artikel[i+j][9], iPosX, iPosY+=iAddY );
                    //leerzeile
                    pg.drawString(" ", iPosX, iPosY+=iAddY );
                 }
             }
             i = i+5;

          } catch (Exception ex) {
              System.out.print(ex);
          }
  
        pg.dispose();
      }
    }
     
    }
     prjob.end();
  }

  //Funktion für das Drucken von Bestellungen - vorgehen wie oben
  public void druckeBestellung(String[][] becomeA)
  {
    String[][] bestellung = becomeA;
 
    PrintJob prjob = getToolkit().getPrintJob( this, "Bestellung mit den Positionen", null );
    
     for (int i=0; i < bestellung.length;i++)
     {

    if( null != prjob )
    {
      final int iScreenResol           = getToolkit().getScreenResolution();
      final int iPageResol             = prjob.getPageResolution();
      final Dimension dimScreenSize    = getToolkit().getScreenSize();
      final Dimension dimPageDimension = prjob.getPageDimension();
      Graphics pg = prjob.getGraphics();
    
      if( null != pg && 0 < iPageResol )
      {
        int iAddY = 15;
        int iRand = (int)Math.round( iPageResol * 2. / 2.54 );  // 2 cm Rand
        int iPosX = iRand + iRand/4;                            // Textposition
        int iPosY = iPosX - iAddY/2;
        int iWdth = dimPageDimension.width  - iRand * 2;        // innere Breite
        int iMidY = dimPageDimension.height / 2;
        
        if (i==0)
        {
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*3/3 ) );
    
        pg.drawString("Bestelluebersicht" , iPosX, iPosY);
        }

        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );

          try {
              
             for (int j=0; j<7;j++)
             {
                 if(i+j < bestellung.length)
                 {
             
                    byte [] bar = EANconfig.createBarbyte(bestellung[i+j][1]);
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(bar));

                    pg.drawImage(img, iPosX+250, iPosY+=iAddY, this );
                    pg.drawString("Bestelldatum: " +bestellung[i+j][0], iPosX, iPosY+=iAddY);
                    pg.drawString("Bestellstatus: "+bestellung[i+j][2], iPosX, iPosY+=iAddY );
                    pg.drawString("Kundennummer: "+bestellung[i+j][3], iPosX, iPosY+=iAddY );
                    pg.drawString("Nachname: "+bestellung[i+j][4], iPosX, iPosY+=iAddY );
                    pg.drawString("Vorname: "+bestellung[i+j][5], iPosX, iPosY+=iAddY );
                    pg.drawString("Artikelanzahl: "+bestellung[i+j][6], iPosX, iPosY+=iAddY );
              
                 }
             }
             
             i = i+7;

          } catch (IOException ex) {
              Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
          }
        
    
        pg.dispose();
      }
     
    }
     
    }
     prjob.end();
  }

  //Funktion für das Drucken einer einzelnen Bestellung - vorgehen wie oben
   public void druckeAuftragsbestaetigung(String[][] Artikel, String BestellID, String[][] Kunde, JTable pos) throws IOException
  {
    String[][] artikelpos = Artikel;
    String Kundedetail[][] = Kunde;
 
    PrintJob prjob = getToolkit().getPrintJob( this, "Auftragsbestätigung mit den Positionen", null );
    
     for (int i=0; i < artikelpos.length;i++)
     {

    if( null != prjob )
    {
      final int iScreenResol           = getToolkit().getScreenResolution();
      final int iPageResol             = prjob.getPageResolution();
      final Dimension dimScreenSize    = getToolkit().getScreenSize();
      final Dimension dimPageDimension = prjob.getPageDimension();
      Graphics pg = prjob.getGraphics();
    
      if( null != pg && 0 < iPageResol )
      {
        int iAddY = 15;
        int iRand = (int)Math.round( iPageResol * 2. / 2.54 );  // 2 cm Rand
        int iPosX = iRand + iRand/4;                            // Textposition
        int iPosY = iPosX - iAddY/2;
        int iWdth = dimPageDimension.width  - iRand * 2;        // innere Breite
        int iMidY = dimPageDimension.height / 2;
        
        if (i==0)
        {
             try {
            BufferedImage img = ImageIO.read(new File("ic_lagerverwaltung96.jpg"));
    
            pg.drawImage(img, iPosX+350, iPosY-25, this );
        } catch (IOException ex) {
            Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
        }    
            
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );
        
        
        pg.drawString("Kundennummer: " +Kundedetail[0][0], iPosX, iPosY);
        pg.drawString(Kundedetail[0][1], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][3] +" "+Kundedetail[0][2], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][4] +" "+Kundedetail[0][5], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][6] +" "+Kundedetail[0][7], iPosX, iPosY+=iAddY);
       
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*3/3 ) );
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("Auftragsbestätigung: "+BestellID , iPosX, iPosY+=iAddY);
        
        byte [] bar = EANconfig.createBarbyte(BestellID);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bar));
        pg.drawImage(img, iPosX+270, iPosY-=iAddY, this );
        
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY+35);
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );
        pg.drawString("Art Nr", iPosX, iPosY+=iAddY);
        pg.drawString("Hersteller", iPosX+70, iPosY);
        pg.drawString("Model", iPosX+185, iPosY);
        pg.drawString("Anzahl", iPosX+300, iPosY);
        pg.drawLine(iPosX-15, iPosY+=iAddY, iPosX+470, iPosY);
        
        }


        for (int j=0; j<artikelpos.length;j++)
        {

        pg.drawString(artikelpos[j][0], iPosX, iPosY+=iAddY);
        pg.drawString(artikelpos[j][1], iPosX+70, iPosY);
        pg.drawString(artikelpos[j][2], iPosX+185, iPosY);
        pg.drawString(artikelpos[j][5], iPosX+300, iPosY);
      }
        pg.drawLine(iPosX-15, iPosY+=iAddY, iPosX+470, iPosY);
        pg.dispose();
     
    }
     
    }
     prjob.end();
  }
 
}
     //Funktion für das Drucken einer Rechnung - vorgehen wie oben
   public void druckeRechnung(String[][] Artikel, String BestellID, String[][] Kunde, JTable pos) throws IOException
  {
    String[][] artikelpos = Artikel;
    String Kundedetail[][] = Kunde;
    double bruttopreis = 0.0;
 
    PrintJob prjob = getToolkit().getPrintJob( this, "Rechnung mit den Positionen", null );
    
     for (int i=0; i < artikelpos.length;i++)
     {

    if( null != prjob )
    {
      final int iScreenResol           = getToolkit().getScreenResolution();
      final int iPageResol             = prjob.getPageResolution();
      final Dimension dimScreenSize    = getToolkit().getScreenSize();
      final Dimension dimPageDimension = prjob.getPageDimension();
      Graphics pg = prjob.getGraphics();
    
      if( null != pg && 0 < iPageResol )
      {
        int iAddY = 15;
        int iRand = (int)Math.round( iPageResol * 2. / 2.54 );  // 2 cm Rand
        int iPosX = iRand + iRand/4;                            // Textposition
        int iPosY = iPosX - iAddY/2;
        int iWdth = dimPageDimension.width  - iRand * 2;        // innere Breite
        int iMidY = dimPageDimension.height / 2;
        
        if (i==0)
        {
             try {
            BufferedImage img = ImageIO.read(new File("ic_lagerverwaltung96.jpg"));
    
            pg.drawImage(img, iPosX+350, iPosY-25, this );
        } catch (IOException ex) {
            Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
        }    
            
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );
        
        
        pg.drawString("Kundennummer: " +Kundedetail[0][0], iPosX, iPosY);
        pg.drawString(Kundedetail[0][1], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][3] +" "+Kundedetail[0][2], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][4] +" "+Kundedetail[0][5], iPosX, iPosY+=iAddY);
        pg.drawString(Kundedetail[0][6] +" "+Kundedetail[0][7], iPosX, iPosY+=iAddY);
       
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*3/3 ) );
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("Rechnung: "+BestellID , iPosX, iPosY+=iAddY);
        
        byte [] bar = EANconfig.createBarbyte(BestellID);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bar));
        pg.drawImage(img, iPosX+270, iPosY-=iAddY, this );
        
        pg.drawString("", iPosX, iPosY+=iAddY);
        pg.drawString("", iPosX, iPosY+=iAddY+35);
        pg.setFont( new Font( "Tahoma", Font.PLAIN, iAddY*2/3 ) );
        pg.drawString("Art Nr", iPosX, iPosY+=iAddY);
        pg.drawString("Hersteller", iPosX+70, iPosY);
        pg.drawString("Model", iPosX+185, iPosY);
        pg.drawString("Anzahl", iPosX+300, iPosY);
        pg.drawString("Einzelpreis", iPosX+340, iPosY);
        pg.drawString("Preis gesamt", iPosX+400, iPosY);
        pg.drawLine(iPosX-15, iPosY+=iAddY, iPosX+470, iPosY);
        
        }


        for (int j=0; j<artikelpos.length;j++)
        {

        pg.drawString(artikelpos[j][0], iPosX, iPosY+=iAddY);
        pg.drawString(artikelpos[j][1], iPosX+70, iPosY);
        pg.drawString(artikelpos[j][2], iPosX+185, iPosY);
        pg.drawString(artikelpos[j][5], iPosX+300, iPosY);
        pg.drawString(artikelpos[j][4], iPosX+340, iPosY);
        pg.drawString(artikelpos[j][6], iPosX+400, iPosY);
        String bp = artikelpos[j][6];
        bruttopreis = bruttopreis + Double.parseDouble(bp);
      }
        pg.drawLine(iPosX-15, iPosY+=iAddY, iPosX+470, iPosY);
        //Nettopreis und Mwst ausrechnen
        double nettopreis = Math.round((bruttopreis / 1.19) * 100.0) / 100.0;
        double Mwst = Math.round((bruttopreis - nettopreis) * 100.0) / 100.0;
        pg.drawString("  Preis netto: "+nettopreis, iPosX+340, iPosY+=iAddY);
        pg.drawString("+19% Mwst: "+Mwst, iPosX+340, iPosY+=iAddY);
        pg.drawString(" Preis brutto: "+bruttopreis, iPosX+340, iPosY+=iAddY);
        pg.dispose();
     
    }
     
    }
     prjob.end();
  }
 
}
}
