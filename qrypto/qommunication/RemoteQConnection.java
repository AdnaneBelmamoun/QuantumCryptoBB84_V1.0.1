package qrypto.qommunication;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import qrypto.exception.AcknowledgeException;
import qrypto.exception.InitException;
import qrypto.exception.QryptoException;
import qrypto.exception.RuntimeQryptoException;
import qrypto.log.Log;


public class RemoteQConnection implements QConnection, Runnable {

//Progress inidication on the player panel.
private JLabel _label = null;
private JProgressBar _bar = null;
public static final String PROGRESS_TXT = "Quantum transmission";

private SocketPubConnection _socket = null;
private QodingType _proc = null;
private boolean _initiator = false;
private QuBit[] _outQuBit = null;
private boolean _success = false; //indicates if the last q-transmission was successfull
private boolean _done = false; 
private InetAddress _ip = null;
private int _port = 0;

private Log _logfile = null;

    /**
     * Constructor for a remote quantum connection.
     * @param ip is the internet address of the quantum channel server.
     * @param port is the port where to connect.
     * @param identity is the identity of this client.
     * @exception IOException is thrown when a problem occured while trying
     * to establish the connection.
     */

    public RemoteQConnection( InetAddress ip, int port)throws IOException{
	_logfile = null;
	_proc = null;
	_outQuBit = null;
	_port = port;
	_ip = ip;
	_socket = new SocketPubConnection(_ip,_port);
    }
    
    
      /**
     * Constructor for a remote quantum connection.
     * @param ip is the internet address of the quantum channel server.
     * @param port is the port where to connect.
     * @param identity is the identity of this client.
     * @param logfile is the logfile.
     * @exception IOException is thrown when a problem occured while trying
     * to establish the connection.
     */

    public RemoteQConnection( InetAddress ip, int port, Log logfile)throws IOException{
	this(ip,port);
	_logfile = logfile;
    }
     
    
    
    /**
    * This allows to set both a progress bar and a label for indication of
    * progress on the player panel. If one is null then no modification is applied.
    * @param bar is the progress bar to update during quantum transmission.
    * @param label is the label to be  changed for indicating that a quantum transmission
    * is executing.
    */
    
    public void setProgressTools(JProgressBar bar, JLabel label){
	_bar = bar;
	_label = label;
    }


    /**
     * This method is called by the run() method to indicate the 
     * end of the quantum transmission.
     */


    private void done(boolean success){
	Log.write(_logfile, "Quantum transmission done with success:", false);
	Log.write(_logfile, success, true);
	_done = true;
	_success = success;
	notifyAll();
    }


    /**
     * Sets the procedure to be used for the next quantum transmission executed
     * from a call to run() method.
     * @param procid is the qoding type for the protocol.
     * @param initiator is true if the connection is on the initiator side.
     * It is false if the client is responder.
     */

    public void setProc(QodingType proc, boolean initiator){
	_proc  = proc;
	_initiator = initiator;
    }
    
    /**
    * Returns the IP address of the quantum channel server.
    * @return the IP address of the quantum channel server. 
    */
    
    public InetAddress qServerIPAddress(){
	return _ip;
    }
    
    /**
    * Returns the port number for the connection to the quantum
    * channel server.
    * @return the port number.
    */

    public int qServerPort(){
	return _port;
    }

    /**
     * This method calls the send method if this client is initiator and calls the read
     * method if the client is responder. Prior to the execution of this method, setProtID
     * must have been called.
     * @exception RuntimeQryptoException whenever the quantum transmission couldn't have
     * been completed successfully.
     */

    public synchronized void run(){
	boolean success = false;
	_outQuBit = null;
	_done = false;
	try{
	    if(_label != null){
		_label.setText(PROGRESS_TXT);
	    }
	    if(_initiator){
		Log.write(_logfile, "Sender asks the initiator server to start QT.", true); 
		_outQuBit = send(_proc);
		success = true;
	    }else{
		Log.write(_logfile, "Receiver expects a QT.",true);
		_outQuBit = read(_proc);
		success = true;
	    }
	}catch(QryptoException qe){
	    Log.write(_logfile, "Quantum transmission not completed:"+qe.getMessage(),true);
	    throw new RuntimeQryptoException(qe.getMessage());
	}finally{
	    done(success);
	}
    }
 


    /**
     * This method waits for the end of the current quantum transmission.
     * It returns whether or not the transmission has been completed with success.
     * @return true iff the quantum transmission has been completed.
     */

    public synchronized boolean waitForCompletion(){
    Log.write(_logfile, "Waiting for completion of a QT",true);
     try{
	    while(!_done){
		wait();
	    }
    }catch(InterruptedException ie){
	Log.write(_logfile,"Problem while waiting completion:"+ie.getMessage(),true);
	throw new RuntimeQryptoException("Problem while waiting for completion:"+ie.getMessage());
    }
    Log.write(_logfile, "QT completed with success.",true);
    return _success;
    }
    
    
    /**
    * Returns the sucess flag for the last quantum transmission.
    * @return true iff the last quantum transmission was a success.
    */

    public boolean success(){
	return _success;
    }
    

    /**
     * Returns the result of the quantum transmission. Must be called
     * after a waitForCompletion() has been done.
     * @return the resulting array of qubits. It returns null if the last transmission
     * was not completed with success.
     */

    public QuBit[] getResult(){
	QuBit[] out = null;
	if(_success){ out = _outQuBit;}
	return out;
    }


    /**
     * This closes the connection with the server (and therefore with the peer).
     */

    public void closeConnection(){
      if(_socket != null){
	_socket.closeConnection();
	_socket = null;
      }
    }



    /**
     * This method allows to send qubits according to a particular quantum transmission
     * procedure. The request is sent to the server and the output is waited.
     * @param proc is the quantum procedure.
     * @exception qrypto.exception.QryptoException whenever a problem occurs.
     */

    public QuBit[] send(QodingType proc)throws QryptoException{
	QuBit[] qb = null;
	Log.write(_logfile, "Client the send command.",true); 
	try{
	    Log.write(_logfile, "Starting discussion with the initiator server.",true);
	    proc.makeInitiatorRequest(_socket);
	    Log.write(_logfile, "Client ready to get the transmission result.",true);
	    proc.setPlayerProgressBar(_bar);
	    qb = proc.getResult(_socket);
	    Log.write(_logfile,"Transmission result obtained.",true);
	}catch(AcknowledgeException ae){
	    Log.write(_logfile, "Error during quantum transmission:"+ae.getMessage(),true);
	    throw new QryptoException(ae.getMessage());
	}catch(QryptoException qe){
	    Log.write(_logfile,"Error during quantum transmission:"+qe.getMessage(),true);
	    throw new QryptoException(qe.getMessage());
	}
	return qb;
    }


    /**
     * This method allows to receive a quantum transmission according to a particular procedure.
     * 
     * @param proc is the quantum procedure.
     * @exception qrypto.exception.QryptoException
     */

    public QuBit[] read(QodingType proc)throws QryptoException{
	QuBit[] qb = null;
	Log.write(_logfile,"Responder client reads a quantum transmission.",true);
	try{
	    Log.write(_logfile,"Responder client waiting for the responder server.",true);
	    proc.waitForResponderServer(_socket);
	    Log.write(_logfile,"Responder client got the resp. server to connect.",true);
	    Log.write(_logfile,"Tries now to get the result",true);
	    proc.setPlayerProgressBar(_bar);
	    qb = proc.getResult(_socket);
	    Log.write(_logfile,"Got the result...",true);
	}catch(AcknowledgeException ae){
	    Log.write(_logfile,"Problem while reading:"+ae.getMessage(),true);
	    throw new QryptoException(ae.getMessage());
	}catch(QryptoException qe){
	    Log.write(_logfile,"Problem while reading:"+qe.getMessage(),true);
	    throw new QryptoException(qe.getMessage());
	}
	return qb;
    }


   /**
     * This method allows to receive a remote quantum transmission by specifiying
     * the classname of a qrypto.qoding.QodingType. 
     * @param protID is the classsname for the QodingType to be executed.
     * @return the received qubits.
     * @exception qrypto.exception.
     */

    public QuBit[] send(String protID)throws QryptoException{
	QuBit[] qb = null;
	try{
	    @SuppressWarnings("rawtypes")
		Class c = Class.forName(protID);
	    QodingType proc = (QodingType)c.newInstance();
	    qb = send(proc);
	}catch(ClassNotFoundException cnf){
	    throw new AcknowledgeException("Quantum coding not defined:"+cnf.getMessage());
	}catch(InstantiationException ie){
	    throw new AcknowledgeException("Quantum coding not properly defined:"+protID);
	}catch(IllegalAccessException ia){
	    throw new AcknowledgeException("Quantum coding not accessible:"+protID);
	}catch(QryptoException qe){
	    throw new InitException("Resp client couldn't get transmission back:"+qe.getMessage());
	}
	return qb;
    }







    /**
     * This method allows to receive a remote quantum transmission by specifiying
     * the classname of a qrypto.qoding.QodingType. 
     * @param protID is the classsname for the QodingType to be executed.
     * @return the received qubits.
     * @exception qrypto.exception.
     */

    @SuppressWarnings("rawtypes")
	public QuBit[] read(String protID)throws QryptoException{
	QuBit[] qb = null;
	try{
	    Class c = Class.forName(protID);
	    QodingType proc = (QodingType)c.newInstance();
	    qb = read(proc);
	}catch(ClassNotFoundException cnf){
	    throw new AcknowledgeException("Quantum coding not defined:"+cnf.getMessage());
	}catch(InstantiationException ie){
	    throw new AcknowledgeException("Quantum coding not properly defined:"+protID);
	}catch(IllegalAccessException ia){
	    throw new AcknowledgeException("Quantum coding not accessible:"+protID);
	}catch(QryptoException qe){
	    throw new InitException("Resp client couldn't get transmission back:"+qe.getMessage());
	}
	return qb;
    }
    
    
    public void finalize(){
	_outQuBit = null;
    }



}
