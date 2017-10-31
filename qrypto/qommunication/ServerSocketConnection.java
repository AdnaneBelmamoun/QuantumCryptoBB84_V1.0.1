package  qrypto.qommunication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;


public class ServerSocketConnection extends Object implements  PubConnection, 
		                                               Runnable{


protected static int[] __div = new int[4];





ServerSocket _serversocket = null;
Socket _socket = null;
ConnectionNotify _notifyTo = null;

ObjectOutputStream _outputStream = null;
ObjectInputStream _inputStream = null;
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

    public void run(){
	try{
	    boolean problem = false;
	    _socket = _serversocket.accept();
	    _outputStream = new ObjectOutputStream(_socket.getOutputStream());
	    _outputStream.flush();
	    _inputStream = new ObjectInputStream(_socket.getInputStream());
	    if(_outputStream == null){
	       problem = true;
	       throw new RuntimeQryptoException("Connection broken...(outputstream)");
	    }
	    if(_inputStream == null){
		problem = true;
		throw new RuntimeQryptoException("Connection broken...(inputStream)");
	    }
	    if(!problem){
		//newConnection(); new modif
		if(_notifyTo != null){
		    _notifyTo.notifyNewConnection();
		  //  newConnection(); new modif
		}
		newConnection();//new modif
	    }
	}catch(IOException io){
	    _socket = null;
	    throw new RuntimeQryptoException("The server couldn't accept a request.");
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
		_outputStream.writeByte((int)b);
		_outputStream.flush();
	    }catch(IOException io){
		res = false;
	    }
	}else{res = false;}
	return res;
    }
    
    
    
    /**
     * It sends an unsigned byte to the client. 
     * @param b is the byte to be sent.
     * @return false iff an IOException occured.
     */


    public synchronized boolean sendUByte(int b){
	boolean res = true;
	if(__connected){
	    try{
		_outputStream.writeByte(b);
		_outputStream.flush();
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
	  try{
	    sendInt(b.length);
	    //_outputStream.write(b,0,b.length);
	    _outputStream.write(b);
	    _outputStream.flush();
	  }catch(IOException io){
	    res = false;
	  }
	}else{res = false;}
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
     byte[] out = null;
     try{
	if(__connected){
	    int size = receiveInt();
	    out = new byte[size];
	    int x = _inputStream.read(out,0,size);
	    if( x == -1){
		throw new TimeOutException("Did not receive all the byte arrays.");
	    }
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

    public ObjectOutputStream whatOutputStream(){
	return _outputStream;
    }

    /**
     * Returns the input stream associated with this socket pub connection.
     * Null is returned if the socket has not been initialised correctly.
     * @return the input stream.
     */

    public ObjectInputStream whatInputStream(){
	return _inputStream;
    }
    
    public static void main(String[] args){
      try{
	ServerSocketConnection ssc = new ServerSocketConnection(Constants.DEFAULT_PORT);
	System.out.println("Running the server.");
	Thread t = new Thread(ssc);
	t.start();
	InetAddress ia = InetAddress.getLocalHost();
	System.out.println("launching the client.");
	SocketPubConnection spc = new SocketPubConnection(ia, Constants.DEFAULT_PORT);
	System.out.println("Waiting the client");
	ssc.waitConnection(0);
	System.out.println("Sending byte :"+129);
	ssc.sendByte((byte)129);
	ssc.sendString("Criss");
	ssc.sendBit(false);
	ssc.sendInt(-689023);
	byte r = spc.receiveByte();
	String s = spc.receiveString();
	boolean b = spc.receiveBit();
	int i = spc.receiveInt();
	System.out.println("Received:"+r+","+s+","+b+","+i);
	System.out.println("Testing array");
	byte[] bs = {(byte)0,(byte)-1,(byte)1,(byte)-128};
	boolean[] bools = {true,true,false,true};
	ssc.sendBytes(bs);
	ssc.sendBits(bools);
	byte[] bytes = spc.receiveBytes();
	boolean[] rbs = spc.receiveBits();
	for(int j = 0; j<4;j++){
	    System.out.println("received at pos j="+j+":"+bytes[j]+","+rbs[j]);
	}
	spc.sendByte((byte)129);
	spc.sendString("Criss");
	spc.sendBit(false);
	spc.sendInt(-689023);
	r = ssc.receiveByte();
	s = ssc.receiveString();
	b = ssc.receiveBit();
	i = ssc.receiveInt();
	System.out.println("Received:"+r+","+s+","+b+","+i);
	System.out.println("Testing array");
	spc.sendBytes(bs);
	spc.sendBits(bools);
	bytes = ssc.receiveBytes();
	rbs = ssc.receiveBits();
	for(int j = 0; j<4;j++){
	    System.out.println("received at pos j="+j+":"+bytes[j]+","+rbs[j]);
	}
	spc.closeConnection();
	ssc.closeConnection();
      }catch(IOException io){
	throw new RuntimeException(io.getMessage());
      }catch(TimeOutException to){
	throw new RuntimeException(to.getMessage());  
      }
    }

  
}
