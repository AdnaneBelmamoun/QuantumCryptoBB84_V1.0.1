package qrypto.qommunication;

import qrypto.exception.TimeOutException;
import qrypto.exception.RuntimeQryptoException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.Socket;
import java.net.InetAddress;


@SuppressWarnings("unused")
public class SocketPubConnection implements PubConnection {


Socket _socket = null;
ObjectOutputStream _outputStream = null;
ObjectInputStream _inputStream = null;





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
	    _inputStream = new ObjectInputStream(_socket.getInputStream());  
	    _outputStream = new ObjectOutputStream(_socket.getOutputStream());
	    _outputStream.flush();
    }

   
    /**
     * It sends a byte to the client. 
     * @param b is the byte to be sent.
     * @return false iff an IOException occured.
     */


    public synchronized boolean sendByte(byte b){
	boolean res = true;
	try{
		_outputStream.writeByte((int)b);
		_outputStream.flush();
	}catch(IOException io){
		res = false;
	}
	return res;
    }

    /**
     * It sends an array of bytes to the client.
     * @param b is the array of bytes.
     * @return false iff an IOException occured.
     */

    public synchronized boolean sendBytes(byte[] b){
	boolean res = true;
	try{
	    sendInt(b.length);
	    //_outputStream.write(b,0,b.length);
	    _outputStream.write(b);
	    _outputStream.flush();
	}catch(IOException io){
	    res = false;
	}
	return res;
    }





    /**
     * It receives a byte from the client.
     * @return the byte received.
     * !!!!!error handling must be added.
     */

    public synchronized byte receiveByte()throws TimeOutException{
	byte number = 0;
       	try{
	    number = _inputStream.readByte();
       	}catch(IOException io){
     	    System.out.println("Problem occured when expecting a byte "+io.getMessage());
	    throw new TimeOutException("Broken connection while expecting a byte");
	}
	return number;
    }
    
    /**
    * Receives an unsigned byte.
    * @return an int encoding the received unsigned byte.
    */
    
    public synchronized int receiveUByte()throws TimeOutException{
	int number = 0;
       	try{
	    number = _inputStream.readUnsignedByte();
       	}catch(IOException io){
     	    System.out.println("Problem occured when expecting a byte "+io.getMessage());
	    throw new TimeOutException("Broken connection while expecting a byte");
	}
	return number;    
    }


    /**
     * It receives an array of bytes.
     * @return the array received.
     * !!error handling must be added.
     */

    public synchronized byte[] receiveBytes()throws TimeOutException{
      int size = receiveInt();
      byte[] out = new byte[size];
      try{
	int x = _inputStream.read(out,0,size);
	if( x == -1){
	    throw new TimeOutException("Did not receive all the byte arrays.");
	}
      }catch(IOException io){
	throw new TimeOutException(io.getMessage());
      }
      return out;
    }

   /**
   * Writes a boolean without flushing. It used to accelerate
   * the sendBits method by flushing only once.
   */
   
   private synchronized boolean sendBitNoFlush(boolean b)throws IOException{
	boolean res=false;
	_outputStream.writeBoolean(b);
	res = true;
	return res;
   }




  /**
   * Method for sending a boolean (or bit).
   * @param b is the boolean to be sent.
   * @return true iff no problem occurs.
   */

    public synchronized boolean sendBit(boolean b){
	boolean res=false;
	try{
	    _outputStream.writeBoolean(b);
	    _outputStream.flush();
	    res = true;
	}catch(IOException io){
	 
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
	try{
	    res = sendInt(b.length);
	    for(int i = 0; (i<b.length) && res; i++){
		res = sendBitNoFlush(b[i]);
	    }
	    _outputStream.flush();
	}catch(IOException io){
	    res = false;
	}
	return res;
    }

  /**
   * Method for sending a string.
   * @param s is the string to be sent.
   * @return true iff no problem occured.
   */
 

    public synchronized boolean sendString(String s){
	boolean res = false;
	try{
	    _outputStream.writeUTF(s);
	    _outputStream.flush();
	    res = true;
	}catch(IOException io){
	}
	return res;
    }


 /**
   * Method for sending an integer.
   * @param i the integer to be sent.
   * @return true iff no problem occured.
   */


    public synchronized boolean sendInt(int i){
      boolean res = false;
      try{
	_outputStream.writeInt(i);
	_outputStream.flush();
	res = true;
      }catch(IOException io){
      }
      return res;
    }



  /**
   * Method for receiving a boolean (bit).
   * @return the bit received.
   * @exception TimeOutException is thrown whenever nothing has been received
   * before the timeout.
   */

    public synchronized boolean receiveBit() throws TimeOutException{
     boolean out;
     try{
	    out = _inputStream.readBoolean();
     }catch(IOException io){
	throw new TimeOutException(io.getMessage());
     }
     return out;
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
	String s = null;
	try{
	    s = _inputStream.readUTF();
	}catch(IOException io){
	    throw new TimeOutException(io.getMessage());
	}
	return s;
    }

  /**
   * Method for receiving a integer.
   * @return the string if any.
   * @exception qrypto.TimeOutException whenever an int is expected 
   * and the timeout is expired.
   */

    public synchronized int receiveInt() throws TimeOutException{
	int out = 0;
	try{
	    out = _inputStream.readInt();
	}catch(IOException io){
	    throw new TimeOutException(io.getMessage());
	}
	return out;
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
