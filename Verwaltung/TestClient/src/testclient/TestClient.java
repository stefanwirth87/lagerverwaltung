package testclient;

/**
 *
 * @author Stefan
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import lipermi.handler.CallHandler;
import lipermi.exception.LipeRMIException;
import lipermi.handler.filter.GZipFilter;
import lipermi.net.Client;

import testcommon.AnotherObject;
import testcommon.ListenerTest;
import testcommon.TestService;

/**
 * Sample client
 *  
 *   1. create a CallHandler
 *   2. call a method
 *   3. call a method which throws a exception
 *   4. call a method which returns another exported object
 *   5. call a method in the "another exported object"
 *   6. create a listener
 *   7. export a listener
 *   8. register the listener in the "another exported object" (which immediately call a listener method)  
 *   
 *   
 * @author lipe
 *
 */
public class TestClient {
  
  @SuppressWarnings("serial")
  public static void main(String... args) {

    long init = new Date().getTime();
    System.out.println("Creating CallHandler");
    CallHandler callHandler = new CallHandler();
    try {
      System.out.println("Creating Client");
      Client client = new Client("localhost", 1234, callHandler, new GZipFilter());
      
      
      System.out.println("Getting proxy");
      TestService myServiceCaller = (TestService) client.getGlobal(TestService.class);
      
      System.out.println("Calling the method letsDoIt():");
      System.out.println("return: " + myServiceCaller.letsDoIt());
      
      System.out.println("ListView return:");
      System.out.println((myServiceCaller.Artikelbestand()));
      
      System.out.println("Bestellung return:");
      System.out.println((myServiceCaller.Bestellungen()));

      
/*
      System.out.println("Calling the method throwAExceptionPlease():");
      try {
        myServiceCaller.throwAExceptionPlease();
      }
      catch (AssertionError e) {
        System.out.println("Catch! " + e);
      }

      System.out.println("Calling the method getAnotherObject():");
      AnotherObject ao = myServiceCaller.getAnotherObject();
      System.out.println("return: " + ao);

      System.out.println("AnotherObject::getNumber(): " + ao.getNumber());      

      
      System.out.println("----");
      System.out.println("ok, listener tests:");
      System.out.println("----");
      

      
      try {
        System.out.println("Creating listener");
        ListenerTest myListener = new ListenerTest() {
          public void makingSomeCallback(String str) {
            System.out.println("Server make me print this: " + str);
          };
        };
        
        System.out.println("Exporting listener");
        callHandler.exportObject(ListenerTest.class, myListener);
        
        System.out.println("Testing listener");
        ao.gimmeYourListener(myListener);
        
      } catch (LipeRMIException e) {
        System.out.println("Oops:");
        e.printStackTrace();
      }
     */
      client.close();
      
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    System.out.println("Time: " + (new Date().getTime() - init));

  }
}
