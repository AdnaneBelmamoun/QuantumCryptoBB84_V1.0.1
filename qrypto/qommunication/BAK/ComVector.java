package qrypto.qommunication.BAK;

import qrypto.exception.*;

import java.util.Vector;
import java.lang.InterruptedException;


@SuppressWarnings("rawtypes")
public class ComVector extends Vector{


/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private long _DELAY = 1000;


  /**
   * Constructor.
   */

  public ComVector(){
    super();
  }


  /**
   * Sets the delay allowed for waiting for a new symbol.
   * @param millis the time after a timeout is thrown while
   * waiting for a message from the peer.
   */

  public void setTimeOut(long millis){
    _DELAY = millis;
  }

  /**
   * Returns the delay before a timeout is launched.
   * @return the actual duration of waiting for a new message
   * before a timeout occurs.
   */

  public long getTimeOut(){
    return _DELAY;
  }


  /**
   * Sets the next element in the vector.
   * @param o is the new object to be added at the end of the vector.
   */

  @SuppressWarnings("unchecked")
public synchronized void setNextElement(Object o){
    this.addElement(o);
    if (this.size()==1){
      notifyAll();
    }
  }

 
  public synchronized Object getNextElement() throws TimeOutException{
    Object o=null;
    boolean ok = !this.isEmpty();
    boolean timeout = false;
    while(!ok && !timeout){
      try{
	wait(_DELAY,0);
      }catch(InterruptedException ie){}
      ok = !this.isEmpty();
      if (!ok){timeout = true;} 
    }
    if(ok){
      o = this.elementAt(0);
      this.removeElementAt(0);
    }else{
      throw new TimeOutException("timeout occured..");
    }
    return o;
  }

}
