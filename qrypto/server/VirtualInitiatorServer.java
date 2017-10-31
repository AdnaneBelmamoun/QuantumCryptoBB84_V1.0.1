package qrypto.server;


import javax.swing.JLabel;
import java.awt.Color;


import java.io.IOException;

import qrypto.exception.QryptoException;
import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.InitException;
import qrypto.exception.TimeOutException;
import qrypto.exception.AcknowledgeException;
import qrypto.qommunication.*;
import qrypto.log.Log;



public class VirtualInitiatorServer implements Runnable 
{


/**
* waiting time for a new client to connect.
*/
public long TIMEOUT  = Constants.NO_TIMEOUT;


private JLabel _infoLabel = null;

private boolean _run = false;
@SuppressWarnings("unused")
private int _portc = 0;
@SuppressWarnings("unused")
private int _ports = 0;
private QSender _qc = null;
@SuppressWarnings("unused")
private boolean _clientConnected = false;
private Thread _tssc = null;

private String LOGFILENAME = Constants.INITSERVERLOGFILE;
private Log _logfile = null;
private boolean _internallogfile = true;



    /**
    * This constructor assumes that the QSender object contains a client
    * connection that is not active. That is the state one gets after a call
    * to the ServerSocketConnection constructor. This constructor starts
    * waiting for a client and sets the resulting client connection 
    * in the QSender input.
    * @param qc is the quantum sender from which the next transmission
    * should take place.
    * @exception IOException whenever the client connection couldn't be setup
    * properly.
    */
    
    public VirtualInitiatorServer(QSender qc)throws IOException{
	_logfile = new Log(LOGFILENAME,true, "qrypto.VirtualInitiatorServer");
	_internallogfile = true;
	_logfile.header();
	_clientConnected = false;
	_run = false;
	_qc = qc;
	_tssc = new Thread(_qc.getClientConnection());
	_tssc.start();
    }

    public VirtualInitiatorServer(int i, VirtualQSender vqs, int i0, Log loginitserver) throws IOException {
        @SuppressWarnings("unused")
		VirtualInitiatorServer virtualInitiatorServer = new VirtualInitiatorServer(vqs);
    }// <editor-fold defaultstate="collapsed" desc="comment">
// </editor-fold>


  
    
    /**
    * Returns the quantum sender associated to the nex quantum transmission.
    * @return the quantum sender.
    */
    
    public QSender getQSender(){
	return _qc;
    }
    
    
    /**
    * Sets the timeout for waiting for a client.
    * @param t is the timeout. t=0 means that no timeout occurs.
    */
    
    public void setClientTimeout(int t){
	TIMEOUT = t;
    }
    
    
    /**
    * Allows to set a label to be modified as this server
    * is performing some quantum transfer. If this is set to null
    * then this functionality is ignored.
    * @param l is the label where the information will be shown.
    */
    
    public void setInfoLabel(JLabel l){
	_infoLabel = l;
    }

    /**
     * Returns whether or not the connection with the responder server has been
     * established successfully.
     * @return true iff the connection is alive.
     */

    public boolean  serversConnected(){
	return _qc.isConnected();
    }


    /**
     * Returns whether or not the client is connected.
     * @return true iff the client is connected.
     */
     
     public boolean isClientConnected(){
	return _qc.getClientConnection().isConnected();
     }
	
    
    /**
    * Waits for a time period for a client to connect to this server.
    * @param timeout is the waiting time before a timeout occurs.
    * @return true iff a connection occured before the timeout.
    */
    
    public synchronized boolean waitForClient(long timeout){
	return _qc.getClientConnection().waitConnection(timeout);
    }
    
    

    /**
     * This method starts the server to listen for a connection.
     * It first waits for the responder sever to connect to this initiator
     * server. Once the connection has been made, a request from a client
     * is expected. The request is then process and the result is sent back 
     * to the client
     * The run stops after the first connection has been closed. This method
     * must be re-run  for the next connection to be accepted.
     * @exception qrypto.RuntimeQryptoExceptiob whenever the communication between
     * this server and the client couldn't be completed normally.
     */

    public void run(){
	_run = true;
	try{ 
	    _processRequest();
	}catch(QryptoException to){
	    Log.write(_logfile,"Error while running initiator server:"+to.getMessage(),true);
	    throw new RuntimeQryptoException(to.getMessage());
	}finally{
	    _run = false;
	}
    }




    /**
     * Process the request made by the client. Actually only BB84
     * quantum transmission is supported. This is initiated by
     * sending the byte  for a BB84 transfer. A request has the following shape
     * right after the <ACK>: <LI>
     * Client: <PROTID> -------> Server <LI>
     * @see qrypto.Constants
     * @return true if the request has been accepted and processed with success.
     */

    @SuppressWarnings("rawtypes")
	private void _processRequest()throws InitException, TimeOutException, AcknowledgeException{
	String protID = "Undefined";
	QodingType proc = null;
	Class c = null;
	try{
	    Log.write(_logfile,"Wait for a client.",true);
	    if(!waitForClient(TIMEOUT)){
	    	Log.write(_logfile,"Timeout or problem occurs before a client connects to initiator server.",true);
	    	throw new TimeOutException("Timeout or problem occurs before a client connect to the initiator server.");
	    }
	    Log.write(_logfile,"got a client",true);
	    Log.write(_logfile,"Receiving the request:",false);
	    protID   = _qc.getClientConnection().receiveString();
	    Log.write(_logfile,protID,true);
	    c = Class.forName(protID);
	    proc = (QodingType)c.newInstance();
	    proc.setQType(_qc.qType());
	    proc.setBucketSize(_qc.getBucketSize());
	    Log.write(_logfile,"Accept a request:"+protID,true);
	    writeInfoLabel(proc.getCodingName(),Color.black);
	    proc.acceptRequest(_qc.getClientConnection());
	    Log.write(_logfile,"Request accepted and is being retransmit to responder server.",true);
	    proc.setTransmissionRequest(_qc.getPubConnection());
	    Log.write(_logfile,"About to send qubits",true);
	    proc.sendQTransmission(_qc);
	    writeInfoLabel("("+proc.getCodingName()+")",Color.darkGray);
	    Log.write(_logfile,"Request is processed...done.",true);
	}catch(TimeOutException to){
	    Log.write(_logfile,"timeout while processing the request.",true);
	    throw new TimeOutException(to.getMessage());
	}catch(ClassNotFoundException cnf){
	    Log.write(_logfile,"Couldn't recognize the request:"+cnf.getMessage(),true);
	    throw new AcknowledgeException("Init server cannot recognize the request:"+cnf.getMessage());
	}catch(InstantiationException ie){
	    Log.write(_logfile,"Request unknown.",true); 
	    throw new AcknowledgeException("Init server couldn't acknowledge the request:"+protID);
	}catch(IllegalAccessException ia){
	    Log.write(_logfile,"Doesn't know how to process the request:"+protID,true);
	    throw new AcknowledgeException("Init server couldn't run the request:"+protID);
	}catch(QryptoException qe){
	    Log.write(_logfile,"Quantum transmission couldn't be completed:"+qe.getMessage(),true);
	    throw new InitException("Init server couldn't complete the transmission:"+qe.getMessage());
	}catch(RuntimeException rt){
	    Log.write(_logfile,"Probably broken connection:"+rt.getMessage(),true);
	    throw new InitException("Probable broken connection:"+rt.getMessage());
	}finally{
	    proc = null;
	    c = null;
	}
    }
    
    
    private void writeInfoLabel(String s, Color c){
	if(_infoLabel != null){
	    _infoLabel.setForeground(c);
	    _infoLabel.setText(s);
	}else{
	    System.out.println("WOOOPS! InfoLabel is NULL");
	}
    }



    /**
     * Returns whether or not this server is actually running.
     * @return true if this server is running.
     */

    public synchronized boolean running(){
	return _run;
    }
    
    /**
    * This method close all connections.
    * 
    */
    @SuppressWarnings("deprecation")
	public void close(){
	if(_tssc != null){_tssc.stop();}
	_qc.close();
	if(_internallogfile){_logfile.closeAnyway();}
    }
    
    /**
    * finalization.
    */
    
    public void finalize(){
	_qc = null;
	_tssc = null;
    }

   }
