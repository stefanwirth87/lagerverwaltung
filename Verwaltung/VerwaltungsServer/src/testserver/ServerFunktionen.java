/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Acer
 */
public class ServerFunktionen {
    
    public static String getaktuelleZeit()
    {
        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date d = new Date();
        //System.out.println(s.format(d));
        return(s.format(d));
    }
    
    public static void schreibe_Log (String Log)
    {
        try {
        
        BufferedWriter bw = new BufferedWriter(new FileWriter("ServerLog.txt",true));
        
        bw.write(Log);   
        bw.newLine(); 
        
        bw.close();
        
         } catch(IOException error) {
          System.out.println(error);
        }
        
        
    }
    
}
