package qrypto.qommunication.BAK;

import qrypto.exception.TimeOutException;
import qrypto.exception.RuntimeQryptoException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;


import java.net.Socket;
import java.net.InetAddress;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;


public class SocketPubConnection implements PubConnection {


Socket _socket = null;
OutputStream _outputStream = null;
InputStream _inputStream = null;





    /**
     * This is a constructor for a client socket connection through
     * an IP address and a port number. A Socket Pub channel returns one
     * ClientSocketConnection and one ServerSocketConnection.
     * @param ip is the ip address where to connect.
     * @param port is the port number where to connect, should be above 1024.
     *
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public SocketPubConnection( InetAddress ip, int port)throws IOException{
	    _socket = new Socket(ip,port);
	    _outputStream = _socket.getOutputStream();
	    _inputStream = _socket.getInputStream();
    }

    /**
     * It sends a byte to the peer. 
     * @param b is the byte to be sent.
     * @return false iff an IOException occured.
     */


    public synchronized boolean sendByte(byte b){
	boolean res = true;
	try{
	    _outputStream.write((int)b);
	}catch(IOException io){
	    res = false;
	}
	return res;
    }

    /**
     * It sends an array of bytes to the peer.
     * @param b is the array of bytes.
     * @return false iff an IOException occured.
     */

    public synchronized boolean sendBytes(byte[] b){
	boolean res = true;
	res = sendInt((int)b.length);
	for(int i = 0; (i<b.length)&& res; i++){
	    res  = sendByte(b[i]);
	}
	return res;
    }





    /**
     * It receives a byte from the peer.
     * @return the byte received.
     * !!!!!error handling must be added.
     */

    @SuppressWarnings("unused")
	public synchronized byte receiveByte() throws TimeOutException{
	byte b;
	int number = 0;
       	try{
	    number = _inputStream.read();
       	}catch(IOException io){
     	    System.out.println("Problem occured when expecting a byte"+io.getMessage());
	    throw new TimeOutException("Broken connection while expecting a byte.");
	}
	return (byte)number;
    }


    /**
     * It receives an array of bytes.
     * @return the array received.
     * @exception TimeOutException if nothing has been received before the timeout.
     * @exception RuntimeQryptoException is the array received was not well formatted.
     * !!error handling must be added.
     */

    public synchronized byte[] receiveBytes()throws TimeOutException{
	int size = receiveInt();
	if(size<1){
	    throw new RuntimeQryptoException("Negative or 0 length array received:"+size);
	}
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
	res = sendInt((int)b.length);
	for(int i = 0; (i<b.length) && res; i++){
	    res = sendBit(b[i]);
	}
	return res;
    }

  /**
   * Method for sending a string.
   * @param s is the string to be sent.
   * @return true iff no problem occured.
   * @exception RuntimeQryptoException if the string was null.
   */
 

    public synchronized boolean sendString(String s){
	if(s == null){
	    throw new RuntimeQryptoException("The string to be sent was null");
	}
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
	    step =(byte)(posi / ServerSocketConnection.__div[j]);
	    sendByte(step);
	    posi = (posi % ServerSocketConnection.__div[j]);
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
	if(b == Constants.BYTE0){ res = false;}
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
	    byte rb = receiveByte();
	    finali = finali + ServerSocketConnection.__div[j]*(int)rb;
	}
	if(sign == (byte)1){finali = -finali;}
	return finali;
    }



    /**
     * This returns the socket created for implementing the conection.
     * @return the socket. Null is returned if the socket has not been initialised
     * correctly.
     */

    public Socket whatSocket(){
	return _socket;
    }


    /**
     * Close the socket public connection.
     * @exception IOException when an error occurs while closing the socket.
     */

    public void closeConnection(){
	try{
	    _socket.close();
	    _outputStream.close();
	    _inputStream.close();
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
