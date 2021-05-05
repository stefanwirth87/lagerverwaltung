import java.util.*;

class Task extends TimerTask

{
  public void run()
  
  {
    System.out.println( "Make my day." );
  }
}

  public class TimerTaskDemo
{
  public static void main( String args[] )
  {
    Timer timer = new Timer();
    
    // nach 2 Sek geht's los
    timer.schedule( new Task(), 2000 );
    
    // nach 1 Sek geht's los und dann alle 5 Sekunden
    timer.schedule( new Task(), 1000, 5000 );
  }
}


