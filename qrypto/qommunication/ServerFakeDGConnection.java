package  qrypto.qommunication;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import qrypto.exception.RuntimeQryptoException;




public class ServerFakeDGConnection extends ServerSocket 
				    implements Runnable{

Socket _socket = null;
ConnectionNotify _notifyTo = null;
private boolean __connected = false;
@SuppressWarnings("unused")
private int _port = 0;
private byte _bytecode = 0; //is the bytecode for the quantum transmission.
private int _bucketsize = 0; //is the bucket size for the seession.


    /**
     * This is a constructor for a server socket connection by
     * listening to a given port.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public ServerFakeDGConnection(int port, 
				  ConnectionNotify notifyTo)throws IOException{
	    super(port);
	    __connected = false;
	    _port = port;
	    _notifyTo = notifyTo;
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
	  //accepts a new connection with a server.
	  _socket = accept();
	  _bytecode = (byte)_socket.getInputStream().read();
	  _bucketsize = Constants.readSpecialInt(_socket.getInputStream());
	  //System.out.println("SERVERFAKEDGCONNECTION BUCKETSIZE:"+_bucketsize);
	  if((_bucketsize < Constants.MAX_BUCKET_SIZE)&&(_bucketsize>0)){
	    _socket.getOutputStream().write(Constants.OK);
	    _notifyTo.notifyNewConnection();
	  }else{
	    _socket.getOutputStream().write(Constants.ERROR);
	  }
      }catch(IOException io){
	  _socket = null;
	  throw new RuntimeQryptoException("ServerFakeDG:Problem with DG connection:"+io.getMessage());
      }
    }
    
    /**
    * Returns the bucket size as established after connection.
    * @return the bucket size.
    */
    
    public int bucketSize(){
	return _bucketsize;
    }
    
    
    /**
    * Returns the coding type for the next quantum transmission.
    * @return the bytcode for the coding.
    */
    
    public byte qCode(){
	return _bytecode;
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

    public void closeConnection(){
	try{
	    if(_socket != null){
		_socket.close();
		_socket = null;
	    }
	    close();
	    __connected = false;
	}catch(IOException io){
	    System.out.println("Problem occured while trying to close the public connection.");
	}
    }
    
    /**
    * Returns the output stream of the current socket.
    * For the method to work, the socket must have been determined.
    * It means that a client is connected.
    * @return the output stream.
    * @exception IOException if the stream is not available but the
    * socket is defined.
    */
    
    public OutputStream getOutputStream()throws IOException{
      if(_socket.getOutputStream() == null){
	throw new IOException("Undefined output stream...");
      }
      return _socket.getOutputStream();
    }
    
    /**
    * Returns the input stream of the current socket.
    * Same remark as for getOuputStream().
    * @return the input stream.
    * @exception IOException if the streamm is not available but the
    * socket is defined.
    */
    
    public InputStream getInputStream()throws IOException{
      if(_socket.getInputStream() == null){
	throw new IOException("Undefined input stream...");
      }
      return _socket.getInputStream();
    }

}
