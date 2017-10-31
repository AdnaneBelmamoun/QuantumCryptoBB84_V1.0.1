package qrypto.qommunication.BAK;


import qrypto.exception.TimeOutException;
import qrypto.qommunication.PubConnection;




public class VirtualPubConnection implements PubConnection{


public static final long _DELAY = 1000;

private ComVector _in=null;
private ComVector _out=null;




  /**
   * Constructor for a new virtual public connection.
   * @param in is the vector where the ingoing messages are packed
   * @param out is the vector where the outgoing messages are packed.
   */

  public VirtualPubConnection(ComVector in, ComVector out){
    _in = in;
    _out = out;
  }


 

  /**
   * Method for sending a boolean (bit).
   * @param b is the bit to be sent
   * @return true iff everything went fine
   */

  public boolean sendBit(boolean b){
    _out.setNextElement(new Boolean(b));
    return true;
  }


  /**
   * Method for sending a sequence of bits.
   * @param b is the array of bits to be sent.
   * @return true iff no problem occured.
   */

  public boolean sendBits(boolean[] b){
    sendInt(b.length);
    for(int i=0; i<b.length; i++){
      sendBit(b[i]);
    }
    return true;
  }



    /**
     * Method for sending a byte.
     * @param b is the byte to be sent.
     * @return true iff no problem occured (always true in this case).
     */

    public boolean sendByte(byte b){
	_out.setNextElement(new Byte(b));
	return true;
    }




    /**
     * Method for sending an array of bytes.
     * @param b is the array of bytes.
     * @return true iff no problem occured (always true in this case).
     */

    public boolean sendBytes(byte[] b){
	sendInt(b.length);
	for(int i = 0;i<b.length;i++){
	    sendByte(b[i]);
	}
	return true;
    }






  /**
   * Method for sending a string.
   * @param s is the string to be sent.
   * @return true iff no problem occured (always true in this case).
   */
 

  public boolean sendString(String s){
    _out.setNextElement(s);
    return true;
  }


 /**
   * Method for sending an integer.
   * @param i the integer to be sent.
   * @return true iff no problem occured.
   */


  public boolean sendInt(int i){
    _out.setNextElement(new Integer(i));
    return true;
  }

  /**
   * Method for receiving a boolean (bit).
   * @return the received boolean 
   * @exception TimeOutException is thrown when the timeout occurs before reception.
   */

  public boolean receiveBit() throws TimeOutException{
    Boolean  b=null;
    Object o=null;
    o = _in.getNextElement();
    if(o != null){
	b = (Boolean)o;
    }else{
	throw new TimeOutException("Time out while expecting a bit");
    }
    return b.booleanValue();
  }

  /**
   * Method for receiving a bit.
   * @return the bit if any.
   * @exception TimeOutException is thrown when the timeout occurs before the reception.
   */

  public boolean[] receiveBits() throws TimeOutException{
    int number = 0;
    boolean[] res;
    number = receiveInt();
    res = new boolean[number];
    for(int i = 0; i<number; i++){
	    res[i] = receiveBit();
    }
    return res;
  }


    /**
     * Method for receiving a byte.
     * @return the byte received.
     * @exception qrypto.exception.TimeOutException is a timeout occured
     * before having received the byte.
     */  

    public byte receiveByte() throws TimeOutException{
	@SuppressWarnings("unused")
	byte rb = 0;
	    Object ro = _in.getNextElement();
	    Byte rbb = (Byte)ro;
	return rbb.byteValue();
    }



    /**
     * Method for rceiving an array of bytes.
     * @return the received array.
     * @exception qrypto.exception.TimeOutException if a timeout occured
     * before the reception.
     */

    public byte[] receiveBytes() throws TimeOutException{
	int number = 0;
	number = receiveInt();
	byte b[] = new byte[number];
	for(int i = 0; i<number; i++){
	    b[i] = receiveByte();
	}
	return b;
    }







  /**
   * Method for receiving a string
   * @return the string if any.
   * @exception TimeOutException is thrown when the timeout occurs before the reception.
   */

  public String receiveString() throws TimeOutException{
    Object o=null;
    String s=null;
    try{
      o = _in.getNextElement();
      if(o != null){
	s = (String)o;
      }else{
	s=null;
      }
    }catch(TimeOutException te){
      throw new TimeOutException("Timeout while expecting a string");
    }
    return s;
  }

  /**
   * Method for receiving an integer.
   * @return the integer if any.
   * @exception TimeOutException is thrown when the timeout occurs before the reception.
   */

  public int receiveInt() throws TimeOutException{
    Object o;
    Integer i=null;
    o = _in.getNextElement();
    if(o != null){
	i = (Integer)o;
    }else{
	i = null;
    }
    return i.intValue();
  }



  /**
   * Close a connection.
   */

  public void closeConnection(){
    _in = null;
    _out = null;
  }

}


