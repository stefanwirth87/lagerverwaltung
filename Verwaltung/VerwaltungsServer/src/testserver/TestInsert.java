/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

/**
 *
 * @author Acer
 */
public class TestInsert {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //String r = MySQLConnection.insert_kunde("Mann", "Limbacher", "Fabian", "Wichernstraße", 14, 91052, "Erlangen", "091611230", "interressiert@niemanden.de");
        
        //System.out.println(r);
        
       //String r =  MySQLConnection.update_kunde(5,"Mann", "Limbacher", "Fabian", "Wichernstraße", 14, 91052, "Erlangen", "091611230", "interressiert@niemanden.de");
        
        //System.out.println(r);
        
        
//      String [][] alle = MySQLConnection.printlistBestellungen(2);
//      
//      for (int i = 0; i<alle.length; i++)
//      {
//          for(int j=0; j<alle[i].length; j++)
//          {
//              System.out.print(alle[i][j]+", ");
//          }
//      
//      System.out.print("\n");
//      }
      
//       String [][] alle = MySQLConnection.printlistPositionen("10000010");
//      
//      for (int i = 0; i<alle.length; i++)
//      {
//          for(int j=0; j<alle[i].length; j++)
//          {
//              System.out.print(alle[i][j]+", ");
//          }
//      
//      System.out.print("\n");
//      }

 String [][] alle = MySQLConnection.printlistBestellungeneinesKunden("22");
      
      for (int i = 0; i<alle.length; i++)
      {
          for(int j=0; j<alle[i].length; j++)
          {
              System.out.print(alle[i][j]+", ");
          }
      
      System.out.print("\n");
      }
      
    
      
        
       
        
      
        
    }
    
}
