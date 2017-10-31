package qrypto.qommunication;



public interface TimeOutNotify{
  
  /**
   * This method is called to initialised the timemout flag.
   * Class TimeOut calls this method first when a delay is started.
   * @see qrypto.TimeOut
   */

  public void timeInit();

  /**
   * This method is called by TimeOut to notify the expiration of
   * a delay.
   * @see qrypto.TimeOut
   */

  public void timeNotify();

}
