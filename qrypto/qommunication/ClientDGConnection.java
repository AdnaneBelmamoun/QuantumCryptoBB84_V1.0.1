package qrypto.qommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;



public class ClientDGConnection {


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

    public ClientDGConnection( InetAddress ip, int port)throws IOException{
	    _socket = new Socket(ip,port);  
	    _inputStream = _socket.getInputStream();
	    _outputStream = _socket.getOutputStream();
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


}
