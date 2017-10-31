package  qrypto.qommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import qrypto.exception.RuntimeQryptoException;



public class ServerDGConnection extends Object implements Runnable{


ServerSocket _serversocket = null;
Socket _socket = null;
ConnectionNotify _notifyTo = null;

OutputStream _outputStream = null;
InputStream _inputStream = null;
private boolean __connected = false;
@SuppressWarnings("unused")
private boolean __timeout = false;

    /**
     * This is a constructor for a server socket connection by
     * listening to a given port.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public ServerDGConnection(int port)throws IOException{
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

    public ServerDGConnection(int port, ConnectionNotify notifyTo)throws IOException{
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
	    System.out.println("GOT A DG...");
	    _outputStream = _socket.getOutputStream();
	    _inputStream = _socket.getInputStream();
	    if(_outputStream == null){
	       problem = true;
	       throw new RuntimeQryptoException("DG Connection broken...(outputstream)");
	    }
	    if(_inputStream == null){
		problem = true;
		throw new RuntimeQryptoException("DG Connection broken...(inputStream)");
	    }
	    if(!problem){
		//newConnection();
		if(_notifyTo != null){
		    System.out.println("NOTIFICATION");
		    _notifyTo.notifyNewConnection();
		    //newConnection();
		    System.out.println("CONNECTION WELL_ESTABLISHED");
		}
		newConnection();
	    }
	}catch(IOException io){
	    _socket = null;
	    throw new RuntimeQryptoException("Problem with connection:"+io.getMessage());
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

    public OutputStream getOutputStream(){
	return _outputStream;
    }

    /**
     * Returns the input stream associated with this socket pub connection.
     * Null is returned if the socket has not been initialised correctly.
     * @return the input stream.
     */

    public InputStream getInputStream(){
	return _inputStream;
    }
    

    /**
    * The server is launched and waits for a byte. Then it sends
    * byte 128. The connection is closed afterward.
    */
    public static void main(String[] args){
      try{
	ServerDGConnection ssc = new ServerDGConnection(Constants.DEF_PORT_REC_DG);
	System.out.println("Running the DG server.");
	Thread t = new Thread(ssc);
	t.start();
	//InetAddress ia = InetAddress.getLocalHost();
	//System.out.println("launching the DG client.");
	//ClientDGConnection spc = new ClientDGConnection(ia, Constants.DEFAULT_PORT);
	System.out.println("Waiting the client");
	ssc.waitConnection(0);
	System.out.println("Got a client!");
	//System.out.println("Sending 129");
	//ssc.getOutputStream().write(129);
	int bt = ssc.getInputStream().read();
	System.out.println("Received:"+bt);
	System.out.println("Server sends 128");
	ssc.getOutputStream().write(128);
	//bt = ssc.getInputStream().read();
	//System.out.println("Server receives:"+bt);
	//spc.closeConnection();
	ssc.closeConnection();
      }catch(IOException io){
	throw new RuntimeException(io.getMessage());
      }
    }


}
