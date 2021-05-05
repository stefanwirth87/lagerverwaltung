package testserver;


//Classe: MySQLConnection

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
public class MySQLConnection{
    
  ServerEinstellungen SE = new ServerEinstellungen();
   String[] verbindung = SE.connection();
    
  /*
  private static Connection con = null;
  private static String dbHost = "localhost";       // Hostname
  private static String dbPort = "3306";                    // Port -- Standard: 3306
  private static String dbName = "projekt";                 // Datenbankname
  private static String dbUser = "lagerverwaltung";        // Datenbankuser
  private static String dbPass = "ProjektFJS";            // Datenbankpasswort
  */
   
  private static Connection con = null;
  private  String dbHost = verbindung[0]; // Hostname
  private  String dbPort = verbindung[1];      // Port -- Standard: 3306
  private  String dbName = verbindung[2];   // Datenbankname
  private  String dbUser = verbindung[3];     // Datenbankuser
  private  String dbPass = verbindung[4];      // Datenbankpasswort
   
  //private static String dbHost = "wirths-server.familyds.com"; // Hostname
  //private static String dbPort = "3306";      // Port -- Standard: 3306
  //private static String dbName = "Projekt";   // Datenbankname
  //private static String dbUser = "Schnittstelle";     // Datenbankuser
  //private static String dbPass = "ProjektFJS";      // Datenbankpasswort
  
  //private static String bestellNr;
 
  private MySQLConnection(){
      
    try {
      Class.forName("com.mysql.jdbc.Driver"); // Datenbanktreiber f�r JDBC Schnittstellen laden.
      
      // Verbindung zur JDBC-Datenbank herstellen.
      con = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+ dbPort+"/"+dbName+"?"+"user="+dbUser+"&"+"password="+dbPass);  
    } catch (ClassNotFoundException e) {
      System.out.println("Treiber nicht gefunden");
    } catch (SQLException e) {
      System.out.println("Verbindung nicht moglich");
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    }
  }
 
  private static Connection getInstance(){
    if(con == null)
      new MySQLConnection();
    return con;
  }
  
  public static int DatenbankStatus(){
    con = getInstance();
    int count = 0;
   
    
    if(con != null){
      // Abfrage-Statement erzeugen.

      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "select count(User_ID) from User limit 1";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        
        result.last();
        count = result.getRow();
        result.beforeFirst();

        return(count); //R�ckgabe
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        return (0);
      }
    }

    return (0);

  }
  
  public static String insert_kunde(String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        query.executeUpdate("INSERT INTO Kunden (Anrede, Nachname, Vorname, Straße, Hausnummer, Postleitzahl, Ort, Telefon, EMail)" + " VALUES ('"+Anrede+"','"+Nachname+"','"+Vorname+"','"+Strasse+"','"+Hausnummer+"','"+plz+"','"+Ort+"','"+Telefon+"','"+email+"')");
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        
      }
      
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("Datensatz wurde erstellt!");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
    
  } //ende insert
  
  
  public static String loeschen_kunde(int kunden_id)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
       
        query.executeUpdate("DELETE FROM Kunden WHERE Kunden_ID = '"+kunden_id+"'");
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        
      }
      
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("Datensatz wurde gelöscht!");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
    
  } //kunden löschen
  
  
    public static String update_kunde(int kunden_id, String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
       
       query.executeUpdate("UPDATE Kunden SET Anrede='"+Anrede+"', Nachname='"+Nachname+"', Vorname='"+Vorname+"', Straße='"+Strasse+"', Hausnummer='"+Hausnummer+"', Postleitzahl='"+plz+"' , Ort='"+Ort+"', Telefon='"+Telefon+"', EMail='"+email+"' WHERE Kunden_ID = '"+kunden_id+"' "); 
        
        
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        
      }  
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("änderungen wurden gespeichert!");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
    
  } //ende kunden updaten
  
  
  public static String[][] printlist(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei ein statement zu setzen!");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "SELECT * FROM Kunden";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][10];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
          info[i][7] = result.getString(8);
          info[i][8] = result.getString(9);
          info[i][9] = result.getString(10);

          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);

  }
    
  public static String[][] search(String suche){
    con = getInstance();
    
    String speicher = "%"+suche+"%";

    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei die Suche auszuführen!");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "Select * from Kunden "
                + "where Nachname like '"+speicher+"' "
                + "or Vorname like '"+speicher+"'"
                + "or Kunden_ID like '"+speicher+"'"
                + "or Straße like '"+speicher+"'"
                + "or Postleitzahl like '"+speicher+"'"
                + "or Ort like '"+speicher+"'";

        ResultSet result = query.executeQuery(sql);
   
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][10];

        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {

          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
          info[i][7] = result.getString(8);
          info[i][8] = result.getString(9);
          info[i][9] = result.getString(10);
 
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);

  }
    
    
  public static int MySQL_id (String user, String passwort){
    //con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "SELECT User_ID FROM User WHERE Name='"+user+"' AND Passwort = md5('"+passwort+"')";
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          int id = result.getInt("User_ID");  
          
          return(id); //r�ckgabe des gewandelten
        }
        
        //        return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return (100);
    
  }
  
  public static int MySQL_idMD5 (String user, String passwort){
    //con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "SELECT User_ID FROM User WHERE Name='"+user+"' AND Passwort = '"+passwort+"'";
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          int id = result.getInt("User_ID");  
          
          return(id); //r�ckgabe des gewandelten
        }
        
        //        return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return (100);
    
  }
    
  public static int MySQL_recht (String user, String passwort){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "SELECT Berechtigung FROM User WHERE Name='"+user+"' AND Passwort = md5('"+passwort+"')";
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          int recht = result.getInt("Berechtigung");
          
          return(recht); //r�ckgabe des gewandelten
        }
        
        //        return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return (100);
    
  }
  
  public static int MySQL_rechtMD5 (String user, String passwort){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "SELECT Berechtigung FROM User WHERE Name='"+user+"' AND Passwort = '"+passwort+"'";
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          int recht = result.getInt("Berechtigung");
          
          return(recht); //r�ckgabe des gewandelten
        }
        
        //        return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return (100);
    
  }

  public static int Login_abfrage(String user, String passwort){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = " SELECT COUNT(User_ID) as correct FROM User WHERE Name='" + user +"' AND Passwort = md5('" + passwort + "')";
        
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          //          String id = result.getString("correct");  
          
          return(Integer.parseInt(result.getString("correct")));
        }
        //return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return (100);
    
    
  }
  
  
   public static int Login_md5(String user, String passwort){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "SELECT COUNT(User_ID) as correct FROM User WHERE Name='"+user+"' AND Passwort = '"+passwort+"'";
        
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          //          String id = result.getString("correct");  
          
          return(Integer.parseInt(result.getString("correct")));
        }
        //return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e);
      }
    }
    //System.out.print(info);
    return (100);
    
    
  }
  
  public static String test()
  {
      
     con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      String info;
      
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = " select name from User where Name = \"Limbacher\" limit 1";
        
        
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          //          String id = result.getString("correct");  
          
          return result.getString("Name");
        }
        //return(info);
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    return ("Nicht gefunden");
  }
  
   /*-----------Für die App------------------------*/
  
  public static ArrayList<String> Artikelbestand(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "SELECT * FROM Artikel";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 4 ist Anzahl der Spalten, kann fest gesetzt werden
        String[] info = new String[count];
        ArrayList<String> list = new ArrayList<String>();
        
        //Z�hler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {        
          info[i] = (i+1)+". "+"Hersteller: "+result.getString(2)+" \n";
          info[i] = info[i]+"Model: "+result.getString(3)+" \n";
          info[i] = info[i]+"Beschreibung: "+result.getString(4)+" \n";
          info[i] = info[i]+"Anzahl: "+result.getString(5)+" \n";
          info[i] = info[i]+"Lagerplatz: "+result.getString(7);
          list.add(info[i]);
          i++;
        }
        return(list); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(list);
    ArrayList<String> ende = new ArrayList<String>();
    ende.add("Error!!!");
    return (ende);
  }
  
  //public static ArrayList<String> Bestellungen(){
  public static ArrayList<String> Bestellungen(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "select Bestellungen.Bestell_ID, Nachname, Vorname, sum(Positionen_Bestellt.Anzahl)\n" +
        "from Bestellungen,Positionen_Bestellt,Kunden \n" +
        "where Bestellungen.Status_ID = 1\n" +
        "and Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr \n" +
        "and Kunden.Kunden_ID = Bestellungen.KundenNr\n" +
        "group by Bestellungen.Bestell_ID\n" +
        "order by Bestell_ID ASC";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 4 ist Anzahl der Spalten, kann fest gesetzt werden
        String[] info = new String[count];
        ArrayList<String> list = new ArrayList<String>();  
        
        //Z�hler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben

        while (result.next()) { 
        
          info[i] = "Bestell Nr: '"+result.getString(1)+"' \n";
          info[i] = info[i]+"Nachname: '"+result.getString(2)+"' \n";
          info[i] = info[i]+"Vorname: '"+result.getString(3)+"' \n";
          info[i] = info[i]+"Anzahl Artikel: '"+result.getString(4)+"'";
          list.add(info[i]);
          
          i++;
        }
        //return(info); //R�ckgabe
        return(list); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(list);
    ArrayList<String> ende = new ArrayList<String>();
    ende.add("Error!!!");
    //String[] ende = {"Error"};
    return (ende);
  }
  
  public static ArrayList<String> BestellungenDetail(String bestellNr){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "Select Bestell_ID,Artikel.ArtikelNr,Hersteller,Model,Beschreibung,Positionen_Bestellt.Anzahl - Positionen_Bestellt.Anzahl_Ausgebucht from Bestellungen,Positionen_Bestellt,Artikel where Bestell_ID = '"+ bestellNr +"' and Ausgebucht = \"false\" and Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr and Positionen_Bestellt.ArtikelNr = Artikel.ArtikelNr order by Bestell_ID ASC;";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 4 ist Anzahl der Spalten, kann fest gesetzt werden
        String[] info = new String[count];
        ArrayList<String> list = new ArrayList<String>();
        
        //Z�hler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {        
          info[i] = "Artikel Nr: '"+result.getString(2)+"' \n";
          info[i] = info[i]+"Hersteller: '"+result.getString(3)+"' \n";
          info[i] = info[i]+"Model: '"+result.getString(4)+"' \n";
          info[i] = info[i]+"Beschreibung: '"+result.getString(5)+"' \n";
          info[i] = info[i]+"Anzahl: '"+result.getString(6)+"'";
          list.add(info[i]);
          i++;
        }
        return(list); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(list);
    ArrayList<String> ende = new ArrayList<String>();
    ende.add("Error!!!");
    return (ende);
  }
  
  public static String Anzahl_gesamt(String art_nr)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        String sql =    "select ArtikelNr,Hersteller,Model,Artikel.Anzahl\n" +
                        "from Artikel\n" +
                        "where ArtikelNr ='"+ art_nr +"'";
        ResultSet result = query.executeQuery(sql);
        
        // Ergebnisstabelle durchforsten
        while (result.next()) {
          
          String ant = result.getString(1) + ";"; 
          ant = ant + result.getString(2) + ";";
          ant = ant + result.getString(3) + ";"; 
          ant = ant + result.getString(4); 
          
          return(ant); //r�ckgabe des gewandelten
        }
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    }
    return ("Error!!");
  }
  
  public static String manuell_ausbuchen(int user_id, int art_nr, int menge, String kommentar)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      CallableStatement query;
      try 
      {
        String sql = "CALL Manuelles_ausbuchen ('"+user_id+"','"+art_nr+"','"+menge+"','"+kommentar+"')";
        query = con.prepareCall(sql);
        //Verbindung aufbauen
        query.execute();
        return ("true");
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    }
    return ("Error!!");
  }
  
  public static String Artikel_ausbuchen(int user_id, int art_nr, int menge, int best_nr)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      CallableStatement query;
      try 
      {
        String sql = "CALL Position_ausbuchen ('"+art_nr+"','"+best_nr+"','"+menge+"','"+user_id+"')";
        query = con.prepareCall(sql);
        //Verbindung aufbauen
        query.execute();
        return ("true");
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    }
    return ("Error!!");
  }
  
  /*-----------Für die Artikel------------------------*/
  
  public static String[][] printlistArtikel(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Artikel aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "SELECT * FROM Artikel";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][7];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
          

          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  }
  
  public static String[][] printlistManuellAusgebucht(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Manuell Ausgebuchten Artikel aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
            "select Manuelles_Ausbuchen.Datum, Name, Hersteller, Model, Manuelles_Ausbuchen.Menge, Kommentar\n" +
            "from Manuelles_Ausbuchen,Artikel,User\n" +
            "where Manuelles_Ausbuchen.User_ID = User.User_ID\n" +
            "and Hersteller = (select Hersteller from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)\n" +
            "and Model = (select Model from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)"+
            "order by Manuelles_Ausbuchen.Datum ASC";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][6];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  }
  
  public static String[][] sucheManuellAusgebucht(String von, String bis, String name, String kommentar){
    con = getInstance();
    
    String uevon = von+"%";
    String uebis = bis+"%";
    String uename = "%"+name+"%";
    String uekommentar = "%"+kommentar+"%";
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Es wird in manuellen ausbuchen gesucht");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
            "select Manuelles_Ausbuchen.Datum, Name, Hersteller, Model, Manuelles_Ausbuchen.Menge, Kommentar\n"+
            "from Manuelles_Ausbuchen,Artikel,User\n"+
            "where Manuelles_Ausbuchen.User_ID = User.User_ID\n"+
            "and Hersteller = (select Hersteller from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)\n"+
            "and Model = (select Model from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)\n"+
            "and Datum between '"+uevon+"' and '"+uebis+"'\n"+
            "and Name like '"+uename+"'\n"+
            "and Kommentar like '"+uekommentar+"'"+
            "order by Manuelles_Ausbuchen.Datum ASC";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][6];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  }
  
  public static String insert_artikel(String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        query.executeUpdate("INSERT INTO Artikel (Hersteller, Model, Beschreibung, Anzahl, Preis, LagerID)" 
                + " VALUES ('"+hersteller+"','"+model+"','"+beschreibung+"','"+anzahl+"','"+preis+"','"+lagerid+"')");
        //SQL befehl absetzen
        query.executeUpdate("Update Lager set Zustand = 1 where LagerID= '"+lagerid+"'");
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("Datensatz wurde erstellt!");
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
  } //ende insert
 
  public static String[] freiLager(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Artikel aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "select LagerID from Lager where Zustand = 0";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[] info = new String[count];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i] = result.getString(1); 
    
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[] ende = new String[1];
    ende[0]="Error";

    return (ende);
  }
  
  public static String[] namenManuellausbuchen(){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Artikel aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "select Name from Manuelles_Ausbuchen,Artikel,User\n"+
        "where Manuelles_Ausbuchen.User_ID = User.User_ID\n"+
        "and Hersteller = (select Hersteller from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)\n"+
        "and Model = (select Model from Artikel where Artikel.ArtikelNr = Manuelles_Ausbuchen.ArtikelNr)\n"+
        "group by name";

        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[] info = new String[count];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i] = result.getString(1); 
    
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[] ende = new String[1];
    ende[0]="Error";

    return (ende);
  }
  
   public static String update_artikel(int artnr, String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid)
  {
    con = getInstance();
    int ulagerid = lagerid;
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        if(lagerid==0)
        {   
           String [] lagerfrei = freiLager(); 
           ulagerid=Integer.parseInt(lagerfrei[0]);
           query.executeUpdate("Update Lager set Zustand = 1 where LagerID= '"+ulagerid+"'");
        }
       
       query.executeUpdate("UPDATE Artikel "
               + "SET ArtikelNr='"+artnr+"', "
               + "Hersteller='"+hersteller+"', "
               + "Model='"+model+"', "
               + "Beschreibung='"+beschreibung+"', "
               + "Anzahl='"+anzahl+"', "
               + "Preis='"+preis+"' , "
               + "LagerID='"+ulagerid+"' "
               + "where ArtikelNr='"+artnr+"' "); 
        
        
        
        return ("änderungen wurden gespeichert!"); 
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }  
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("änderungen wurden nicht gespeichert");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/  
  } //ende kunden updaten
   
   
   public static String[][] searcharticle(String suche){
    con = getInstance();
    
    String speicher = "%"+suche+"%";

    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei die Suche auszuführen!");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "Select * from Artikel "
                + "where ArtikelNr like '"+speicher+"' "
                + "or Hersteller like '"+speicher+"'"
                + "or Model like '"+speicher+"'"
                + "or Beschreibung like '"+speicher+"'"
                + "or Preis like '"+speicher+"'"
                + "or LagerID like '"+speicher+"'";

        ResultSet result = query.executeQuery(sql);
   
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][7];

        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {

          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
         
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  } 

  public static String[][] search_einen_kunden(String suche){
    con = getInstance();
    
    String speicher = suche;

    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei die Suche auszuführen!");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql = "Select * from Kunden "
                + "where Nachname = '"+speicher+"' "
                + "or Vorname = '"+speicher+"'"
                + "or Kunden_ID = '"+speicher+"'";

        ResultSet result = query.executeQuery(sql);
   
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][10];

        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {

          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
          info[i][7] = result.getString(8);
          info[i][8] = result.getString(9);
          info[i][9] = result.getString(10);
 
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);

  }
  
  
  /*Für die Bestellungen*/
  
  public static String neueBestellung(String kundennr)
  {
    con = getInstance();
    String info="";
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        query.executeUpdate("insert into Bestellungen (KundenNr, Status_ID, Zeit) values ('"+kundennr+"','1', now())");
 
        String sql = "Select Bestell_ID from Bestellungen where KundenNr='"+kundennr+"' group by Bestell_ID order by Bestell_ID DESC Limit 1";
        ResultSet result = query.executeQuery(sql);
        
        
        while (result.next()) {

          info = result.getString(1); 
  
        }
        
        return (info);
 
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    info="Error";
    return (info);
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
  } //ende insert
  
  
  public static String insertPositionen(String[][] positionen, String bestellnr, String zuloeschen[], int status)
  {
    con = getInstance();
    String info="";
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      String sql;
      ResultSet result;
      int speicher=0;
      int ID=0;
      try 
      {
        query = con.createStatement();

        for (int i=0; i<positionen.length; i++)
        {
            sql = "select count(Anzahl) as vorhanden, ID from Positionen_Bestellt where ArtikelNr='"+positionen[i][0]+"' and BestellNr='"+bestellnr+"'";
            result = query.executeQuery(sql);
            while (result.next()) 
            {
                speicher = Integer.parseInt(result.getString(1));
                System.out.println("speicher: "+speicher);
                if (speicher > 0)
                {
                    ID = Integer.parseInt(result.getString(2));
                    System.out.println("ID: "+ID);
                }
            }
            if (speicher == 0)
            {
                query.executeUpdate("insert into Positionen_Bestellt (ArtikelNr, Anzahl, BestellNr) values ('"+positionen[i][0]+"','"+positionen[i][5]+"','"+bestellnr+"')"); 
            }
            else
            {
                query.executeUpdate("Update Positionen_Bestellt set ArtikelNr='"+positionen[i][0]+"', Anzahl='"+positionen[i][5]+"' where ID='"+ID+"'");
            }
        }//Positionen aktualiert
        
        //zu löschenden noch entfernen
        for (int i=0;i<zuloeschen.length;i++)
        {
            //prüfen ob noch etwas im Array steht - wenn ja ist der ausstieg erreicht
            if (zuloeschen[i] == null)
            {
                System.out.println("Ich bin mit dem löschen fertig");
                break;
            }
            else
            {
                //prüfen ob der eintrag existiert
                sql = "select count(Anzahl) as vorhanden, ID from Positionen_Bestellt where ArtikelNr='"+zuloeschen[i]+"' and BestellNr='"+bestellnr+"' limit 1";
                result = query.executeQuery(sql);
                while (result.next()) 
                {
                    speicher = Integer.parseInt(result.getString(1));
                    System.out.println("zu löschen speicher: "+speicher);
                    if (speicher > 0)
                    {
                    ID = Integer.parseInt(result.getString(2));
                    System.out.println("zu löschen ID: "+ID);
                    }
                }
                if (speicher > 0)
                {
                    query.executeUpdate("DELETE FROM Positionen_Bestellt WHERE ID ='"+ID+"' and ArtikelNr='"+zuloeschen[i]+"' and BestellNr='"+bestellnr+"'"); 
                }
            System.out.println("Ich habe gelöscht "+i);
            }
        }
        //aktuellen Status eintragen
        query.executeUpdate("Update Bestellungen set Status_ID='"+status+"' where Bestell_ID='"+bestellnr+"'");
        //Züählen wieviele aktuelle Positionen in der Bestellung vorhanden sind
        sql = "Select count(ID) as anzahl from Positionen_Bestellt where BestellNr='"+bestellnr+"' group by BestellNr";
        result = query.executeQuery(sql);

        while (result.next()) 
        {
          info = result.getString(1); 
        }
        return (info);
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        return (e.toString());
      }
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    info="Error";
    return (info);
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
  } //ende insert
  
  
  public static String[][] printlistBestellungen(int auswahl){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle aktuellen Bestellungen aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        String sql;
        
        if (auswahl == 0)
        {
        sql =
        "select Zeit, Bestell_ID, Status, Kunden_ID, Nachname, Vorname, sum(Positionen_Bestellt.Anzahl) as SummeArtikel \n" +
        "from Bestellungen,Positionen_Bestellt,Kunden, status \n" +
        "where Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr \n" +
        "and Kunden.Kunden_ID = Bestellungen.KundenNr \n" +
        "and Bestellungen.Status_ID=status.Status_ID\n"+
        "group by Bestell_ID order by Zeit DESC;";
        
        }
        else
        {
        
        sql =
        "select Zeit, Bestell_ID, Status, Kunden_ID, Nachname, Vorname, sum(Positionen_Bestellt.Anzahl) as SummeArtikel \n" +
        "from Bestellungen,Positionen_Bestellt,Kunden, status \n" +
        "where Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr \n" +
        "and Kunden.Kunden_ID = Bestellungen.KundenNr \n" +
        "and Bestellungen.Status_ID=status.Status_ID\n"+
        "and Bestellungen.Status_ID='"+auswahl+"'\n"+
        "group by Bestell_ID order by Zeit DESC;";
        }
          
        
        
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][7];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
           
            //Datum bekommt irgendwie einen punkt - substring wird nur der teil gefiltert den wir wollen
           String date = result.getString(1);
           info[i][0] = date.substring(0, 19);  
    
          // info[i][0] = result.getString(1); 
          
                  
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  }
  
  public static String[][] printlistPositionen(String bestellnr){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Positionen einer Bestellung aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "Select Artikel.ArtikelNr,Hersteller,Model,Beschreibung,Preis,Positionen_Bestellt.Anzahl,ID \n" +
        "from Bestellungen,Positionen_Bestellt,Artikel \n" +
        "where Bestell_ID = '"+bestellnr+"' \n" +
        "and Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr \n" +
        "and Positionen_Bestellt.ArtikelNr = Artikel.ArtikelNr \n" +
        "order by Bestell_ID ASC;";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][8];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          //mit berechnung des Gesamtpreises
          info[i][6] = Double.toString(Double.parseDouble(result.getString(5))*Double.parseDouble(result.getString(6)));
          info[i][7] = result.getString(7);

          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  }
  
  public static String update_KundenBestellung(String Bestell_ID, String Kunden_Nr)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
       
       query.executeUpdate("Update Bestellungen set KundenNr='"+Kunden_Nr+"' where Bestell_ID='"+Bestell_ID+"'"); 

        //SQL befehl absetzen
        return ("änderungen wurden gespeichert!");      
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }  
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("änderungen wurden nicht gespeichert");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/  
  } //ende kunden updaten
  
  
  public static String[][] printlistBestellungeneinesKunden(String kundennr){
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei alle Positionen einer Bestellung aufzulisten");
  
      Statement query;
      try {
        query = con.createStatement();
        
        //Suchen der Bestellungen
        String sql =
        "select Bestell_ID, Zeit, Status\n"+
        "from Bestellungen, status\n"+
        "where Bestellungen.Status_ID = status.Status_ID\n"+
        "and KundenNr = '"+kundennr+"'";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][4];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);

          i++;  
        }
        
        //Einfügen der jeweiligen Anzahl der Bestellungen
        for (i=0;i<info.length;i++)
        {
        
        sql =
        "select sum(Positionen_Bestellt.Anzahl) as SummeArtikel\n" +
        "from Bestellungen,Positionen_Bestellt,Kunden, status\n" +
        "where Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr\n" +
        "and Kunden.Kunden_ID = Bestellungen.KundenNr\n" +
        "and Bestellungen.Status_ID=status.Status_ID\n" +
        "and Bestell_ID = '"+info[i][0]+"'\n" +
        "group by Bestell_ID";
        
        result = query.executeQuery(sql);
 
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) 
        {
          info[i][3] = result.getString(1);  
        }
        
        }
        
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }

    String[][] ende = {
    {"Error","Datenbank"}};

    return (ende);
  }
  
  public static int getStatus(String bestellnr){
    con = getInstance();
    
    if(con != null){

      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "Select Status_ID from Bestellungen where Bestell_ID='"+bestellnr+"';";
        ResultSet result = query.executeQuery(sql);

        int info = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
          info = Integer.parseInt(result.getString(1)); 
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    int ende = 100;

    return (ende);
  }
  
   public static String[][] searchBestellung(String Datevon, String Datebis, String begriff){
    con = getInstance();
    //Variablen ünerbnehmen und bearbeiten für die Suche
    String suche = "%"+begriff+"%";
    String von = Datevon+"%";
    String bis = Datebis+"%";
    
    System.out.println(suche+" "+von+" "+bis);

    if(con != null){
      // Abfrage-Statement erzeugen.
      System.out.println("Ich bin dabei die Suche auszuführen!");
  
      Statement query;
      try {
        query = con.createStatement();

        String sql = "select Zeit, Bestell_ID, Status, Kunden_ID, Nachname, Vorname, sum(Positionen_Bestellt.Anzahl) as SummeArtikel \n" +
                 "from Bestellungen,Positionen_Bestellt,Kunden,status \n" +
                 "where Bestellungen.Bestell_ID = Positionen_Bestellt.BestellNr \n" +
                 "and Kunden.Kunden_ID = Bestellungen.KundenNr \n" +
                 "and Bestellungen.Status_ID=status.Status_ID \n" +
                 "and Bestellungen.Zeit between '"+von+"' and '"+bis+"' \n" + 
                 "and Bestellungen.Bestell_ID like '"+suche+"' \n" +
                 "group by Bestell_ID \n" + 
                 "order by Zeit DESC";

        ResultSet result = query.executeQuery(sql);
   
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        String[][] info = new String[count][7];

        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {

          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
          info[i][4] = result.getString(5);
          info[i][5] = result.getString(6);
          info[i][6] = result.getString(7);
         
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);
  } 
   
   
   /*--> Für die Benutzerverwaltung <--*/
   
   public static String[][] printlistUser(){
    con = getInstance();
    
    if(con != null){

      Statement query;
      try {
        query = con.createStatement();
        
        String sql =
        "select User_ID, Name, Passwort, Berechtigung from User;";
        ResultSet result = query.executeQuery(sql);
        
        //Anzahl der Datens�tze ermitteln um die Richtige Array gr��e zu bekommen
        int count = 0;
        result.last();
        count = result.getRow();
        result.beforeFirst();
        
        //Array mit den Datens�tzen erzeugen
        //Die 10 ist Anzahl der Spalten, kann fest gesetzt werden
        String[][] info = new String[count][4];
        
        //Zähler
        int i = 0;
        
        //Datens�tze Zeile f�r Zeile auslesen und an Array �bergeben
        while (result.next()) {
   
          info[i][0] = result.getString(1); 
          info[i][1] = result.getString(2);
          info[i][2] = result.getString(3);
          info[i][3] = result.getString(4);
   
          i++;  
        }
        return(info); //R�ckgabe
      } 
      catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //System.out.print(info);
    
    String[][] ende = {
    {"Error","Code"}};

    return (ende);

  }
   
   public static String insertUser(String Name, String Passwort, String Recht)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
        
        query.executeUpdate("insert into User (Name,Passwort,Berechtigung) values ('"+Name+"','"+Passwort+"','"+Recht+"')");
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        
      }
      
    }

    return ("Datensatz wurde erstellt!");
 
  } //ende insert
   
   
   public static String updateUser(String UserID, String Name, String Passwort, String Recht)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
       
       query.executeUpdate("update User set Name='"+Name+"', Passwort='"+Passwort+"', Berechtigung='"+Recht+"' where User_ID = '"+UserID+"'"); 
        
        
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
        
      }  
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("änderungen wurden gespeichert!");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
    
  } //ende kunden updaten
   
   
   public static String deleteUser(String UserID)
  {
    con = getInstance();
    
    if(con != null){
      // Abfrage-Statement erzeugen.
      Statement query;
      try 
      {
        query = con.createStatement();
        //Verbindung aufbauen
       
        query.executeUpdate("DELETE FROM User WHERE User_ID = '"+UserID+"'");
        //SQL befehl absetzen
        
      } 
      catch (SQLException e) 
      {
          e.printStackTrace();
        return("Fehler beim löschen");
      }
      
    }
    
    //int error = SQLException.getErrorCode();
    //r�ckmeldung �ber die Erstellung
    return ("Datensatz wurde gelöscht!");
    
    /*Kann man das mit dem Fehler auslesen auch sch�ner machen ???????*/
    
  } //kunden löschen
   
}
    
    
    
  