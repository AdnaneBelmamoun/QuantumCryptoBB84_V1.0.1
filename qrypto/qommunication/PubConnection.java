package qrypto.qommunication;



import qrypto.exception.TimeOutException;




public interface PubConnection{


  /**
   * Method for sending a boolean (or bit).
   * @param b is the boolean to be sent.
   * @return true iff no problem occurs.
   */

  public boolean sendBit(boolean b);

  /**
   * Method for sending a bit stream.
   * @param b is the bit to be sent.
   * @return true iff no problem occured.
   */

  public boolean sendBits(boolean[] b);



    /**
     * Method for sending a byte.
     * @param b is the byte to be sent.
     * @return true iff no problem occured.
     */

    public boolean sendByte(byte b);




    /**
     * Method for sending an array of bytes.
     * @param b is the array of bytes.
     * @return true iff no problem occured.
     */

    public boolean sendBytes(byte[] b);



  /**
   * Method for sending a string.
   * @param s is the string to be sent.
   * @return true iff no problem occured.
   */
 

  public boolean sendString(String s);


 /**
   * Method for sending an integer.
   * @param i the integer to be sent.
   * @return true iff no problem occured.
   */


  public boolean sendInt(int i);



  /**
   * Method for receiving a boolean (bit).
   * @return the bit received.
   * @exception TimeOutException is thrown whenever nothing has been received
   * before the timeout.
   */

  public boolean receiveBit() throws TimeOutException;



  /**
   * Method for receiving a bit.
   * @return the bit if any.
   * @exception qrypto.exception.TimeOutException whenever a bit is expected 
   * and the timeout is expired.
   */

  public boolean[] receiveBits() throws TimeOutException;

    /**
     * Method for receiving a byte.
     * @return the byte received.
     * @exception qrypto.exception.TimeOutException is a timeout occured
     * before having received the byte.
     */  

    public byte receiveByte() throws TimeOutException;



    /**
     * Method for rceiving an array of bytes.
     * @return the received array.
     * @exception qrypto.exception.TimeOutException if a timeout occured
     * before the reception.
     */

    public byte[] receiveBytes() throws TimeOutException;





  /**
   * Method for receiving a string
   * @return the string if any.
   * @exception qrypto.TimeOutException whenever a string is expected 
   * and the timeout is expired.
   */

  public String receiveString() throws TimeOutException;

  /**
   * Method for receiving a string
   * @return the string if any.
   * @exception qrypto.TimeOutException whenever an int is expected 
   * and the timeout is expired.
   */

  public int receiveInt() throws TimeOutException;

  /**
   * Close a connection.
   */

  public void closeConnection();

} 


