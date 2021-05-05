package testserver;

/**
 *
 * @author Stefan
 */
import testcommon.AnotherObject;
import testcommon.ListenerTest;

public class AnotherObjectImpl implements AnotherObject {

  private static final long serialVersionUID = -1881637933978100698L;
  
  int myNumber;
  
  AnotherObjectImpl(int myNumber) {
    this.myNumber = myNumber;
  }
  
  public void test() {
    System.out.println("AnotherObjectImpl::test() .. myNumber=" + myNumber);
  }

  public int getNumber() {
    return myNumber;
  }

  public void gimmeYourListener(ListenerTest listener) {
    listener.makingSomeCallback("the listener works, dude.");
  }
  
 
  


  
}
