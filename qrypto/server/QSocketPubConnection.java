package qrypto.server;


import java.io.IOException;
import java.net.InetAddress;
import qrypto.exception.*;
import qrypto.qommunication.*;


public class QSocketPubConnection extends SocketPubConnection
{


    private byte _mode = Constants.ERROR; 

     /**
     * This is a constructor for a client socket connection through
     * an IP address and a port number. After the connection is established
     * a byte describing the quantum transmission mode to be used is expected.
     * The call to this constructor returns only after the communicaiton mode
     * has been received and acknowledge.
     * @param ip is the ip address where to connect.
     * @param port is the port number where to connect, should be above 1024.
     *
     * @exception IOException if an I/O error occurs while creating the socket
     * or no agreement on the quantum transmission mode could have been made.
     */

    public QSocketPubConnection( InetAddress ip, int port)throws IOException{
	    super(ip,port);
	    try{
		_mode = receiveByte();
	    }catch(TimeOutException to){
		closeConnection();
		_mode = Constants.ERROR;
		throw new IOException("Did not receive the quantum transmission mode:"+to.getMessage());
	    }
	    if((_mode != Constants.REAL) && (_mode != Constants.VIRTUAL)){
		closeConnection();
		_mode = Constants.ERROR;
		throw new IOException("Couldn't agree on the quantum transmission mode.");
	    }
	    sendByte(Constants.OK);
    }
    
    /**
    * Returns the quantum transmission mode. 
    * @return the bytecode for the agreed quantum transmission mode. If no
    * transmission mode has been agreed upon then Constant.ERROR is returned.
    */
    
    public byte getTransmissionMode(){
	return _mode;
    }
    

}
