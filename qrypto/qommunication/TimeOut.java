package qrypto.qommunication;


import java.lang.InterruptedException;



public class TimeOut extends Thread{

private TimeOutNotify _target=null;
private long _delay =0;
  // This flag indicated whether or not to notify when the delay is expired.
private boolean _notify = false;


  /**
   * Constructor for a delay.
   * @param target is the class that is going to be notified
   *        when the delay is over.
   * @param millis is the length of the delay in milliseconds.
   */
  public TimeOut(TimeOutNotify target, long millis){
    super();
    _target = target;
    _delay  = millis;
    _notify = true;
  }


  /**
   * Tells the thread not to notify when the delay is expired.
   * This method is called usually when the timeout is no more
   * needed because the expected event occurs.
   */

  public synchronized void forget(){
    _notify = false;
  }


  /**
   * When run, the thread sleeps during a predefined delay and then
   * notify the calling class after the delay is expired.
   */  

  public synchronized void run(){
    _target.timeInit();
    try{this.wait(_delay); } catch (InterruptedException ie){}
    if(_notify){_target.timeNotify();}
  }

}





