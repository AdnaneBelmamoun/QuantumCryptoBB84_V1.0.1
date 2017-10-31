package qrypto.server;

import qrypto.exception.*;
import qrypto.qommunication.*;
import java.io.IOException;






public class QServerSocketConnection extends ServerSocketConnection
{

    byte _mode = 0;

    /**
     * This is a constructor for a server socket connection
     * listening to a given port.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @param mode is the quantum transmission mode. The bytecode for the
     * available mode can be found in the qrypto.qommunication.Constants class.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public QServerSocketConnection(int port, byte mode)throws IOException{
	super(port);
	_mode  = mode;
    }
    
    /**
     * This is a constructor for a server socket connection by
     * listening to a given port. A Socket Pub channel returns one
     * ClientSocketConnection and one ServerSocketConnection. It also
     * notifies upon connection.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @param mode is the quantum transmission mode. The bytecode for the
     * available mode can be found in the qrypto.qommunication.Constants class.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public QServerSocketConnection(int port, byte mode, ConnectionNotify notifyTo)throws IOException{
	super(port,notifyTo);
	_mode = mode;
    }
    
    
    
    /**
    * This method runs the server socket connection and upon connection
    * sends the quantum transmission mode to be used in forthcoming connection.
    * After connection: Initiator --mode---> Responder
    *                   Initiator <----OK--- Responder.
    */
    
    public void run(){
	super.run();
	try{
	    sendByte(_mode);
	    byte ok = receiveByte();
	    if(ok != Constants.OK){
		closeConnection();
		throw new RuntimeQryptoException("The responder server did not accept the transmission mode.");
	    }
	}catch(TimeOutException to){
	    throw new RuntimeQryptoException("Initiator server couldn't send the transmission mode:"+to.getMessage());
	}
    }
}
