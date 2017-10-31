package qrypto.server;


import java.io.IOException;

import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;
import qrypto.log.Log;
import qrypto.qommunication.Constants;
import qrypto.qommunication.SocketPubConnection;



public class ResponderServerConnection implements Runnable {

private QSocketPubConnection _sc = null;
private String  _protID = null;//the protocol ID (the class name of an implementation of QodingType).

private boolean _run = false;
private VirtualResponderServer _vs = null;

private static String LOGFILENAME = Constants.RESPONDERSERVERCONNECTIONLOGFILE;
private Log _logfile = null;



    /**
     * Constructor given the server connection already established.
     * @param servercon is the connection with the initiator server.
     */

    public ResponderServerConnection(QSocketPubConnection servercon, VirtualResponderServer vs)throws IOException{
	_logfile = new Log(LOGFILENAME, true, "qrypto.server.ResponderServerConnection");
	_sc = servercon;
	_protID = null;
	_run = false;
	_vs = vs;
    }



    /**
     * This methos waits for the initiator server to send the description of the next quantum transmission
     * 
     * Ex. <qrypto.qommunication.BB84QodingType> means that the initiator server is ready
     *     to execute a BB84 transsmission.
     * 
     */

    public synchronized void run(){
	_run = true;
	try{
	    Log.write(_logfile,"waiting for the protocol description to be sent by the initiator server.",true);
	    _protID = _sc.receiveString();
	    Log.write(_logfile,"got it, contact established for:"+_protID,true);
	}catch(TimeOutException to){
	    _protID = null;
	    throw new 
		RuntimeQryptoException("Responder:timeout occured while expecting initiator resquest--"+to.getMessage());
	}catch(RuntimeException rt){
	    _protID = null;
	    throw new 
		RuntimeQryptoException("Responder:probably a broken connection--"+rt.getMessage());
	}finally{
	    _run = false;
	}
	_vs.initiatorHasCalled(_protID);
    }



    /**
     * This tells whether or not this connection is waiting for an ACKNOWLEDGEMENT.
     * @return true iff the server is actually waiting for an ACK coming from the
     * initiator server.
     */

    public synchronized boolean isWaiting(){
	return _run;
    }

    /**
     * For compatibility. It is the !isWaiting().
     */

    public synchronized boolean isConnected(){
	return !isWaiting();
    }

	/**
	 * This returns the protocol ID sent by the initiator server.
	 * @return the protocol decription. The value 0 means that no protocol
	 * has been determined yet.
	 */

	public synchronized String protID(){
	    return _protID;
	}




    /**
     * Returns the socket connection.
     * @return the socket.
     */

    public SocketPubConnection getSocket(){
	return _sc;
    }
    
    
    /**
    * This method close the connection.
    */
    
    public void closeConnection(){
	_sc.closeConnection();
    }


}
