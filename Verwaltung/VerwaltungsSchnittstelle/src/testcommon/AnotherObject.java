package testcommon;

/**
 *
 * @author Stefan
 */
import java.io.Serializable;

public interface AnotherObject extends Serializable {
  
  public void test();
  
  public int getNumber();
  
  public void gimmeYourListener(ListenerTest listener);
  
 
  
}
