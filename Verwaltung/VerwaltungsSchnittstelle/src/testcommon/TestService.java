package testcommon;

import java.util.ArrayList;

/**
 *
 * @author Stefan
 */
public interface TestService {

  public String letsDoIt();
  
  public int SeverStatus();
  
  public String[][] Alle();
  
   public String[][] search(String suche);
  
  public int Login(String user, String passwort);
  
  public int LoginMD5(String user, String passwort);
  
  public int get_id(String user, String passwort);
  
  public int get_idMD5(String user, String passwort);
  
  public int get_recht(String user, String passwort);
  
  public int get_rechtMD5(String user, String passwort);
  
  public  String insert_Kunde_neu(String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email);
  
  public  String loeschen_kunden(int kunden_id, String Nachname);
  
  public  String update_Kunden(int Kunden_id, String Anrede, String Nachname, String Vorname, String Strasse, String Hausnummer, int plz, String Ort, String Telefon, String email);
  
  public ArrayList<String> Artikelbestand();
  
  public ArrayList<String> BestellungenDetail(String bestellNr);
  
  public ArrayList<String> Bestellungen();
  
  public String Anzahl_gesamt(String art_nr);
  
  public String manuell_ausbuchen(int user_id, int art_nr, int menge, String kommentar);
  
  public String Artikel_ausbuchen(int user_id, int art_nr, int menge, int best_nr);
  
  public String[][] AlleArtikel();
  
  public String[][] AlleManuellAusgebuchtenArtikel();
  
  public  String insert_artikel_neu(String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid);
  
  public String[] freiLagerplaetze();
  
  public  String update_artikel(int artnr, String hersteller, String model, String beschreibung, int anzahl, Double preis, int lagerid);
  
  public String[][] searchArtikel(String suche);
  
   public String[][] aktBestellungen(int auswahl);
  
  public String[][] einen_kudnen(String suche);
  
  public String neue_Bestellung(String kdnr);
  
  public String insert_Positin(String[][] positionen, String bestellnr, String [] zuloeschen, int status);
  
  public String[][] AllePositionen(String bestellid);
  
  public  String update_KundenBestellung(String Bestell_ID, String Kunden_Nr);
  
  public int get_Status(String bestellnr);
  
  public String[][] sucheBestellung(String von, String bis, String suchbegriff);
  
  public String[][] searchManuellausbuchen(String von, String bis, String name, String kommentar);
  
  public String[] namendesManuellenausbuchen();
  
  public String[][] BestellungeneinesKunden(String kundennr);
  
  public String[][] AlleBenutzer();
  
  public  String insertBenutzer(String Name, String Passwort, String Recht);
  
  public  String updateBenutzer(String UserID, String Name, String Passwort, String Recht);
  
  public  String loescheBenutzer(String UserID);
 
}

