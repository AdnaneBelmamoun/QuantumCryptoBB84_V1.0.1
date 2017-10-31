package qrypto.server;

import java.io.IOException;
import java.net.InetAddress;

import qrypto.exception.AcknowledgeException;
import qrypto.exception.InitException;
import qrypto.exception.QryptoException;
import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;
import qrypto.log.Log;
import qrypto.qommunication.Constants;
import qrypto.qommunication.QReceiver;
import qrypto.qommunication.QodingType;
import qrypto.qommunication.VirtualQReceiver;



public class VirtualResponderServer implements Runnable
{


//standard timeout while waiting for a client.
public long TIMEOUT = 0;


private String _protID = null;



@SuppressWarnings("unused")
private int _portc = 0;
@SuppressWarnings("unused")
private int _ports = 0;
@SuppressWarnings("unused")
private InetAddress _initserverAdd = null; 

private ResponderServerConnection _pc = null;
private QReceiver _qc = null;


private boolean _initiatorIsConnected = false;
@SuppressWarnings("unused")
private boolean _timeout = false;
@SuppressWarnings("unused")
private boolean _clientConnected = false;
@SuppressWarnings("unused")
private boolean _run = false;

private Thread _TS = null;
private Thread _TC = null;


private static String LOGFILENAME=Constants.RESPSERVERLOGFILE; 
private Log _logfile = null;
private boolean _internallogfile = true;


 
    
    /**
     * Creates a virtual server for one end of the quantum channel.
     * This server is responsible to send the data obtained from the
     * quantum transmission to one of the party involved. Upon instantiation,
     * this connects to the initiator server.
     *
     * @param qc is the quantum receiver used by the server.
     * @exception IOException if the responder server couldn't connect to the initiator server. 
     *
     */

    public VirtualResponderServer(QReceiver qc)throws IOException{
	this(qc.getPubConnection());
	_qc = qc;
    }
    
    
    /**
    * Constructor that launches the server-server connection.
    * The QReceiver is still undefined and therefore any subsequent
    * call to a method involving it will fail. The QReceiver must be
    * set with setQReceiver before the full functionality of that class
    * can be invoked. In particular any method relying upon the client
    * connection. No further test is implemented to test if the QReceiver
    * has been set. 
    * @param spc is the connection to  the initiator server.
    * @exception IOException if the responder server cannot connect to the 
    * initiator server.
    */
    
    public VirtualResponderServer(QSocketPubConnection spc)throws IOException{
	_logfile = new Log(LOGFILENAME, true, "qrypto.VirtualResponderServer");
	_internallogfile = true;
	_logfile.header();
	_TS = null;
	_TC = null;
	_run = false;
	_timeout = false;
	_initiatorIsConnected = false;
	_clientConnected = false;
	_pc = new ResponderServerConnection(spc,this);
    }

    public VirtualResponderServer(int i, VirtualQReceiver vqr, InetAddress ia, int i0, Log logrespserver) throws IOException {
        @SuppressWarnings("unused")
		VirtualResponderServer virtualResponderServer = new VirtualResponderServer(vqr);
    }// <editor-fold defaultstate="collapsed" desc="comment">
// </ed// <editor-fold defaultstate="collapsed" desc="comment">
   
    
    
    
    /**
    * Waits for the initiator server to make a quantum communication request.
    * This launches a thread. Nothing is done if the server was already expecting
    * the initiator server.
    */
    
    public void acceptInitiatorServerRequest(){
     if(_TS == null){
      _TS = new Thread(_pc);
      _TS.start();
     }
    }
    
    
    
    
    /**
    * This method is called to start the responder server to wait for a client.
    * Nothing is done if the server is already expecting a client.
    */
    
    public void acceptResponderClient(){
      if(_TC == null){
	_TC = new Thread(_qc.getClientConnection());
	_TC.start();
      }
    }

    /**
     * This method blocks until either the responder client connects or
     * a timeout occurs.
     * @param timeout is the maximum time to wait for a client. 0 means that no timeout
     * is used.
     * @return true iff a client connects before the timeout.
     */

    public boolean waitForClient(long timeout){
	 return _qc.getClientConnection().waitConnection(timeout);
    }


    /**
     * Returns whether or not the client is connected.
     * @return true iff the client is connected.
     */

    public boolean isClientConnected(){
	return _qc.getClientConnection().isConnected();
    }





    /**
     * Sets the quantum connection.
     * @param qc is the new quantum connection.
     */

    public void setQReceiver(QReceiver qc){
	_qc = qc;
    }



    /**
     * This is called by the Responder server connection whenever the initiator
     * server has sent a request to this responder initiator. Once this method is called
     * all waiting process in this class are notified.
     * @param protID is the protocol requested by the initiator
     * @param n is the length of the quantum transmission. 
     */
 

    public synchronized void initiatorHasCalled(String protID){
	Log.write(_logfile,"Initiator server has sent a request.",true);
	_initiatorIsConnected = true;
	_protID = protID;
	notifyAll();
    }



    /**
     * This method starts the server to listen for a connection.
     * The run stops after the first connection has been closed. This method
     * must be re-run  for the next connection to be accepted.
     * @exception qrypto.exception.RuntimeQryptoException is thrown whenever
     * a problem occured during while executing a new request.
     */

    public synchronized void run(){
	_run  = true;
	while( !_initiatorIsConnected){ 
	  try{
		wait(TIMEOUT);
	  }catch(InterruptedException ie){
	    Log.write(_logfile,"Error while expecting a request from the initiator server:"+ie.getMessage(),true);
	    _run = false;
	    throw new RuntimeQryptoException("Responder:waiting initiator's connection--"+ie.getMessage());
	  }
	}
	if(_initiatorIsConnected){
	    try{
		Log.write(_logfile,"Initiator server has contacted, ready to process the request",true);
		_processRequest();
	    }catch(QryptoException qe){
		_run = false;
		Log.write(_logfile,"request couldn't be completed:"+qe.getMessage(),true);
		throw new RuntimeQryptoException(qe.getMessage());
	    }
	}else{
	  _run = false;
	  Log.write(_logfile,"timeout reached while waiting for the initiator server to send a request.",true);
	  throw new RuntimeQryptoException("Responder:reached a timeout while waiting the initiator to make a request.");
	}
	_run = false;
    }


    /**
     * Process the request made by the client. Actually only BB84
     * quantum transmission is supported. This is initiated by
     * sending the byte containing BB84 followed by the length of the
     * transmission (the raw key).
     * @exception qrypto.exception.InitException whenever a problem occured while 
     * setting the parameters for the request. This exception is also thrown when
     * the client disconnects before receiving the outcome of the quantum transmission.
     * @exception qrypto.exception.TimeOutException whenever a message had not been received
     * before the timeout.
     * @exception qrypto.exception.AcknowledgeException whenever the ACK between the server and the client
     * couldn't be completed. 
     */

    @SuppressWarnings("rawtypes")
	private synchronized void _processRequest()throws InitException, TimeOutException, AcknowledgeException{
    try{
	    Log.write(_logfile,"processing request:"+_protID,true);
	    Class c = Class.forName(_protID);
	    QodingType proc = (QodingType)c.newInstance();
	    proc.getTransmissionRequest(_pc.getSocket());
	    proc.setQType(_qc.qType());
	    //proc.setBucketSize(_qc.getBucketSize());
	    Log.write(_logfile,"Got the parameters for the request.",true);
	    Log.write(_logfile,"Waiting for the client to connect.",true);
	    //attendre que le client repondeur soit branche.
	    boolean result = waitForClient(TIMEOUT);
	    if(!result){
		Log.write(_logfile,"Timeout occured while waiting for the client to connect.",true);
		throw new QryptoException("Timeout occured while waiting for the client");
	    } 
	    Log.write(_logfile,"Client is connected.",true);
	    proc.retransmitRequest(_qc.getClientConnection());
	    Log.write(_logfile,"The request has been retransmitted to the responder client.",true);
	    proc.readQTransmission(_qc);
	    Log.write(_logfile,"Quantum transmission done.",true);
	}catch(ClassNotFoundException cnf){
	    Log.write(_logfile,"Request couldn't be recognized:"+cnf.getMessage(),true);
	    throw new AcknowledgeException("Init server cannot recognize the request:"+cnf.getMessage());
	}catch(InstantiationException ie){
	    Log.write(_logfile,"Couldn't get the quantum protocol :"+_protID,true);
	    throw new AcknowledgeException("Init server couldn't acknowledge the request:"+_protID);
	}catch(IllegalAccessException ia){
	    Log.write(_logfile,"couldn't run the request:"+_protID,true);
	    throw new AcknowledgeException("Init server couldn't run the request:"+_protID);
	}catch(QryptoException qe){
	    Log.write(_logfile,"Quantum transmission couldn't be completed:"+qe.getMessage(),true);
	    throw new InitException("Init server couldn't complete the transmission:"+qe.getMessage());
	}
    }

    @SuppressWarnings("deprecation")
	public void close(){
	if(_TS != null){_TS.stop();}
	if(_TC != null){_TC.stop();}
	_qc.close();
	Log.write(_logfile,"Serveur arreté.",true);
	if(_internallogfile){_logfile.closeAnyway();}
    }



}
