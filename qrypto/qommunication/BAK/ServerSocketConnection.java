package qrypto.qommunication.BAK;

import qrypto.exception.TimeOutException;
import qrypto.exception.RuntimeQryptoException;


import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;


import java.net.ServerSocket;
import java.net.Socket;
import qrypto.qommunication.ConnectionNotify;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;



public class ServerSocketConnection extends Object implements  PubConnection, 
		                                               Runnable{


protected static int[] __div = new int[4];





ServerSocket _serversocket = null;
Socket _socket = null;
ConnectionNotify _notifyTo = null;

OutputStream _outputStream = null;
InputStream _inputStream = null;
private boolean __connected = false; 
@SuppressWarnings("unused")
private boolean __timeout = false;


    static{
	__div[3] = 128*128*128;
	__div[2] = 128*128;
	__div[1] = 128;
	__div[0] = 1;
    }

    /**
     * This is a constructor for a server socket connection by
     * listening to a given port. A Socket Pub channel returns one
     * ClientSocketConnection and one ServerSocketConnection.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public ServerSocketConnection(int port)throws IOException{
	    _serversocket = new ServerSocket(port);
	    __connected = false;
	    __timeout = false;
	    _notifyTo = null;
    }


    /**
     * This is a constructor for a client socket connection through
     * an IP address and a port number. A Socket Pub channel returns one
     * ClientSocketConnection and one ServerSocketConnection.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @param notifyTo is the class that is going to be notified once a connection is obtained.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public ServerSocketConnection(int port, ConnectionNotify notifyTo)throws IOException{
	    _serversocket = new ServerSocket(port);
	    __connected = false;
	    __timeout = false;
	    _notifyTo = notifyTo;
    }
    
    
    /**
    * Sets a class to be notified when a connection occurs.
    * @param target is the target class.
    */
    
    public void setNotification(ConnectionNotify target){
	_notifyTo  = target;
    }

    /**
     * Returns whether or not the server is connected
     * to a client.
     * @return true iff the server is connected.
     */

    public boolean isConnected(){
	return __connected;
    }



    /**
     * This method is used to start the server. It then starts listening for a connection.
     */

    public synchronized void run(){
	try{
	    boolean problem = false;
	    _socket = _serversocket.accept();
	    _outputStream = _socket.getOutputStream();
	    _inputStream = _socket.getInputStream();
	    if(_outputStream == null){
	       problem = true;
	       throw new RuntimeQryptoException("Connection broken...(outputstream)");
	    }
	    if(_inputStream == null){
		problem = true;
		throw new RuntimeQryptoException("Connection broken...(inputStream)");
	    }
	    if(!problem){
		newConnection();
		if(_notifyTo != null){
		    _notifyTo.notifyNewConnection();
		    newConnection();
		}
	    }
	}
	catch(IOException io){
	    _socket = null;
	    throw new RuntimeQryptoException("Problem when running the server.");
       }
    }
    
    
    /**
    * Is called when a new connection is established.
    */
    private synchronized void newConnection(){
	__connected = true;
	notifyAll();
    }
    
    /**
    * Waits a new for a new connection.
    * @param timeout is the maximum time to wait for a connection. 0 means
    * no timeout.
    * @return true if a connection has been established. Returns
    * false if a problem occurs.
    */
    
    public synchronized boolean waitConnection(long timeout){
      boolean out = true;
      try{
 	 while(!__connected && out){
	     wait(timeout);
	     if(!__connected){out = false;}
	 }
      }catch(InterruptedException ie){out = false;}
      return out;
    }



    /**
     * It sends a byte to the client. 
     * @param b is the byte to be sent.
     * @return false iff an IOException occured.
     */


    public synchronized boolean sendByte(byte b){
	boolean res = true;
	if(__connected){
	    try{
		_outputStream.write((int)b);
	    }catch(IOException io){
		res = false;
	    }
	}else{res = false;}
	return res;
    }

    /**
     * It sends an array of bytes to the client.
     * @param b is the array of bytes.
     * @return false iff an IOException occured.
     */

    public synchronized boolean sendBytes(byte[] b){
	boolean res = true;
	if(__connected){
	    res = sendInt(b.length);
	    for(int i = 0; (i<b.length)&& res; i++){
		res  = sendByte(b[i]);
	    }
	}else{res = false;}
	return res;
    }





    /**
     * It receives a byte from the client.
     * @return the byte received.
     * !!!!!error handling must be added.
     */

    @SuppressWarnings("unused")
	public synchronized byte receiveByte()throws TimeOutException{
	byte b;
	int number = 0;
       	try{
	    number = _inputStream.read();
       	}catch(IOException io){
     	    System.out.println("Problem occured when expecting a byte "+io.getMessage());
	    throw new TimeOutException("Broken connection while expecting a byte");
	}
	return (byte)number;
    }


    /**
     * It receives an array of bytes.
     * @return the array received.
     * !!error handling must be added.
     */

    public synchronized byte[] receiveBytes()throws TimeOutException{
	int size = receiveInt();
	byte[] out = new byte[size];
	for(int i = 0; i< size; i++){
	    out[i]  = receiveByte();
	}
	return out;
    }






  /**
   * Method for sending a boolean (or bit).
   * @param b is the boolean to be sent.
   * @return true iff no problem occurs.
   */

    public synchronized boolean sendBit(boolean b){
	boolean res=false;
	if(b){
	    res = sendByte(Constants.BYTE1);
	}else{
	    res = sendByte(Constants.BYTE0);
	}
	return res;
    }




  /**
   * Method for sending a bit stream.
   * @param b is the bit to be sent.
   * @return true iff no problem occured.
   */

    public synchronized boolean sendBits(boolean[] b){
	boolean res = true;
	res = sendInt(b.length);
	for(int i = 0; (i<b.length) && res; i++){
	    res = sendBit(b[i]);
	}
	return res;
    }

  /**
   * Method for sending a string.
   * @param s is the string to be sent.
   * @return true iff no problem occured.
   */
 

    public synchronized boolean sendString(String s){
	byte[] bs = s.getBytes();
	boolean res = sendBytes(bs);
	return res;
    }


 /**
   * Method for sending an integer.
   * @param i the integer to be sent.
   * @return true iff no problem occured.
   */


    public synchronized boolean sendInt(int i){
	int posi = i;
	/** Setting of the sign bit */
	if(i>=0){sendByte((byte)0);}
	else{sendByte((byte)(1));
	     posi  = -posi;
	}
	byte step = 0;
	boolean res= false;
	for(int j=3;j>=0;j--){
	    step =(byte)(posi / __div[j]);
	    sendByte(step);
	    posi = (posi % __div[j]);
	}
	res = true;
	return res;
    }



  /**
   * Method for receiving a boolean (bit).
   * @return the bit received.
   * @exception TimeOutException is thrown whenever nothing has been received
   * before the timeout.
   */

    public synchronized boolean receiveBit() throws TimeOutException{
	byte b = receiveByte();
	boolean res = false;
	if(b==0){ res = false;}
	else{ res = true;}
	return res;
    }



  /**
   * Method for receiving a bit.
   * @return the bit if any.
   * @exception qrypto.TimeOutException whenever a bit is expected 
   * and the timeout is expired.
   */

    public synchronized boolean[] receiveBits() throws TimeOutException{
	int size = receiveInt();
	boolean[] bool = new boolean[size];
	for(int i = 0; i<size; i++){
	    bool[i] = receiveBit();
	}
	return bool;
    }



  /**
   * Method for receiving a string
   * @return the string if any.
   * @exception qrypto.TimeOutException whenever a string is expected 
   * and the timeout is expired.
   */

    public synchronized String receiveString() throws TimeOutException{
	byte[] bytestring = receiveBytes();
	return new String(bytestring);
    }

  /**
   * Method for receiving a integer.
   * @return the string if any.
   * @exception qrypto.TimeOutException whenever an int is expected 
   * and the timeout is expired.
   */

    public synchronized int receiveInt() throws TimeOutException{
	int finali = 0;
	byte sign = receiveByte();
	for(int j = 3; j>=0;j--){
	    finali = finali + __div[j]*(int)receiveByte();
	}
	if(sign == (byte)1){finali = -finali;}
	return finali;
    }



    /**
     * This returns the socket returned after a connection.
     * @return the socket. Null is returned if no connection occured.
     * 
     */

    public Socket whatSocket(){
	return _socket;
    }




    /**
     * Close the socket public connection.
     * @exception IOException when an error occurs while closing the socket.
     */

    public synchronized void closeConnection(){
	try{
	    _serversocket.close();
	    if(_socket != null){
		_socket.close();
		_outputStream.close();
		_inputStream.close();
		_socket = null;
		_outputStream = null;
		_inputStream = null;
	    }
	    __connected = false;
	}catch(IOException io){
	    System.out.println("Problem occured while trying to close the public connection.");
	}
    }

    /**
     * Returns the output stream associated with this socket pub connection.
     * @return the output stream. Null is returned if the output stream
     * is not initialised appropriately.
     */

    public OutputStream whatOutputStream(){
	return _outputStream;
    }

    /**
     * Returns the input stream associated with this socket pub connection.
     * Null is returned if the socket has not been initialised correctly.
     * @return the input stream.
     */

    public InputStream whatInputStream(){
	return _inputStream;
    }

  
}
