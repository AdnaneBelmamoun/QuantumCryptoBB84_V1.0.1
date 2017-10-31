package qrypto.qommunication;


import qrypto.exception.*;
import qrypto.log.Log;
import qrypto.server.QServerSocketConnection;
import java.io.IOException;
import javax.swing.*;
import java.lang.Thread;
import java.lang.Double;
import java.lang.Math;
import java.util.Hashtable;
import java.awt.Component;


 
   
public class VirtualQSender implements QSender, ConnectionNotify{

private static String LOGFILENAME = Constants.VIRTUALQSENDERLOGFILE;
private Log _logfile = null;


//Is the key associated to the configuration of the error 
//probability.
public static String ERROR_PROB_CONFIG_KEY = "Error probability";
//definir comme (1-sqrt(1-2*Constants.DEF_DETECTED_ERROR_PROB))/2
public static double ERROR_PROB_DEF_VALUE = (1-Math.sqrt(1-2*Constants.DEF_DETECTOR_PROB_ERROR))/2;
//The error probability for one transmitted qubit.  
public double errorProb = ERROR_PROB_DEF_VALUE;


private int _numberOfBucket = 0;  
private long _timeout = Constants.NO_TIMEOUT; 
private QServerSocketConnection _ssc = null;
private Thread _t = null;
@SuppressWarnings("unused")
private int _port = 0;
private boolean _connected = false; 
private JProgressBar _bar = null;
private ServerSocketConnection _ic = null;


   
      public VirtualQSender(int port) throws IOException{
	  _logfile = new Log(LOGFILENAME,true, "VirtualQSender");
	  _logfile.header();
	  _ssc = new QServerSocketConnection(port,Constants.VIRTUAL,this);
	  _port = port;
	  _connected = false;
	  _t = new Thread(_ssc);
	  _t.start(); 
      }
      
      
    /**
     * The constructor of a virtual sender with selected error probability. 
     * It launched a server listening
     * to the given port for the connection of a VirtualQReceiver.
     * 
     * @param port is the port where the servers are going to be connected.
     * @param pe is the error probability <= 0.5 for each transmission. That is
     * the probability to decode it as the opposite classical bit than the one sent.
     * @exception IOException whenever the server could not be set up properly.
     * @see ServerSocketConnection
     */

      public VirtualQSender(int port,double pe ) throws IOException{
	  this(port);
	  if((pe >=  0.0) && (pe <= 0.5)){
	    errorProb = pe;
	  }else{
	    errorProb = ERROR_PROB_DEF_VALUE;
	  }
      }
    
      
    /**
     * The constructor of a virtual sender with selected error probability. 
     * It launches a server listening
     * to the given port for the connection of a VirtualQReceiver.
     * 
     * @param port is the port where to listen for server-server connection.
     * @param pe is the error probability <= 0.5 for each transmission. That is
     * the probability to decode it as the opposite classical bit than the one sent.
     * @param ic is the client connection.
     * @exception IOException whenever the server could not be set up properly.
     * @see ServerSocketConnection
     */

      public VirtualQSender(int port,double pe, ServerSocketConnection ic ) throws IOException{
	  this(port,pe);
	  _ic =ic;
	  
      }
      
      
      
    /**
     * The constructor of a virtual sender with selected error probability. 
     * It launches a server listening
     * to the given port for the connection of a VirtualQReceiver.
     * 
     * @param port is the port where to listen for server-server connection.
     * @param ic is the client connection.
     * @exception IOException whenever the server could not be set up properly.
     * @see ServerSocketConnection
     */

      public VirtualQSender(int port,ServerSocketConnection ic ) throws IOException{
	  this(port);
	  _ic =ic;
	  
      }
 
       
       
  /**
   * Returns 1 since the bucket size of a virtual q-transmission
   * is 1.
   * @return 1.
   */ 
   
   public int getBucketSize(){
    return 1;
   }
   
  
   /**
   * Sends the number of buckets used back to the
   * client. 
   */
   
   public void sendNumberOfBucketsToClient(){
	_ic.sendInt(_numberOfBucket);
   }
   
   /**
   * Returns the number of buckets needed.
   * @return the number of buckets.
   */
   
   public int getNumberOfBuckets(){
	return _numberOfBucket;
   }
      
      
      
    /**
    * Applies the configuration to this instance. 
    * @param h is the hashable that can contain some configuration
    * values. If not then nothins is done. Otherwise this instance
    * is configured according to the selected parameter.
    * @return Constants.ERROR whenever the hashtable was null. Constants.OK
    * otherwise.
    */
    
    @SuppressWarnings("rawtypes")
	public byte appliesConfig(Hashtable h){
	byte out = Constants.OK;
	if(h != null){
	    Object o = h.get(ERROR_PROB_CONFIG_KEY);
	    if(o != null){
	     errorProb = ((Double)o).doubleValue();
	    }
	}else{
	    out = Constants.ERROR;
	}
	return out;
    }
	
	 
      
    /**
    * Sets the client connection for this transmission. To be used
    * when the outcome is retransmit back to the client.
    * @param ic is the client connection.
    */
    
    public void setClientConnection(ServerSocketConnection ic){
	_ic = ic;
    }
    
    
   /**
    * Returns the client connection.
    * @return the client connection or null if it has not been set.
    */
    
    public ServerSocketConnection getClientConnection(){
	return _ic;
    }

    /**
     * Implements the notification method called when the responder server connects.
     */
    public synchronized void notifyNewConnection(){
	_connected = true;
	notifyAll();
    }


    /**
     * Sets the timeout.
     * param timeout is the new timeout.
     */

    public void setTimeout(long timeout){
	_timeout = timeout;
    }



    /**
    * Sets the progress bar to be used.
    * @param bar is the progress bar. Null means that no progress
    * bar is used in the transmission.
    */
    
    public void setProgressBar(JProgressBar bar){
	_bar = bar;
    }


    /**
     * Returns the timeout.
     * @return the timeout.
     */

    public long getTimeOut(){
	return _timeout;
    }


    /**
     * Returns whether or not a connection has been established.
     * @return true iff a connection has been established.
     */

    public boolean isConnected(){
	return _ssc.isConnected();
    }
	
    /**
    * Returns the type of the quantum transmission.
    * @return Constants.VIRTUAL
    */
    
    public byte qType(){
	return Constants.VIRTUAL;
    }

    /**
     * This method sends a virtual qubit through a socket.
     * @param qbin is the qubit to be sent.
     * @exception InitException if no connection was established before the transmission.
     */
//    public void send(QuBit qbin) throws InitException{
//	QuBit qb = null;
//	if(_ssc.isConnected()){
//	    if(Math.random() < errorProb){
//		qb = qbin.neg();
//	    }else{
//		qb = qbin;
//	    }
//	    qb.sendMe(_ssc);
//	}else{
//	    throw new InitException("Couldn't send a qubit since no connection was established.");
//	}
//   }


   /**
    * This method is used to send a random qubit among
    * several possibilites.
    * @param alphabet is the set of possible state to be sent.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */
    public QuBit send(QuBit[] alphabet)throws TimeOutException,QuBitFormatException{
	QuBit qb = null;
	if(_ssc.isConnected()){
	    double rr = alphabet.length*Math.random();
	    int s = new Double(rr).intValue();
	    qb = alphabet[s].copyMe();
	    if(Math.random() < errorProb){
	         QuBit qbnoisy = qb.copyMe();
		 qbnoisy.compl();
		 qbnoisy.sendMe(_ssc);
	    }else{
		qb.sendMe(_ssc);
	    }
	}else{
	    throw new TimeOutException("Servers are not connected for sending.");
	}
	return qb;
    }
    
    
   /**
    * This method is used to send random qubits among
    * several possibilites.
    * @param alphabet is the set of possible state to be sent.
    * @param n is the number of qubits to be sent.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */   

    public QuBit[] send(QuBit[] alphabet,int n)throws TimeOutException, QuBitFormatException{
	QuBit[] qb = new QuBit[n];
	_numberOfBucket = 0;
	if(_bar != null){
	    _bar.setMaximum(n-1);
	    _bar.setValue(0);
	}
	Log.write(_logfile,"Sending Quantumly",true);
	for(int i=0; i<n; i++){
	    qb[i] = send(alphabet);
	    _numberOfBucket++;
	    if(qb[i] != null){
		if(_bar != null){_bar.setValue(i);}
		if(_ic != null){
		   qb[i].sendMe(_ic);
		}
	    }else{
		throw new QuBitFormatException("Bad qubit format for sending.");
	    }
	}
	Log.write(_logfile,"done",true);
	return qb;
    }
    
    /**
    * This method is used to send a random qubit among
    * several possibilites.The qubits are automatically forwarded
    * to the initiator client. The connection must be up and ready.
    * @param alphabet is the set of possible state to be sent.
    * @param n is the number of qubits to be sent.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */   

    public void sendAndForward(QuBit[] alphabet,int n)throws TimeOutException, QuBitFormatException{
	QuBit qb = null;
	_numberOfBucket = 0;
	if(_bar != null){
	    _bar.setMaximum(n-1);
	    _bar.setValue(0);
	}
	for(int i=0; i<n; i++){
	    qb = send(alphabet);
	    _numberOfBucket++;
	    if(qb != null){
		if(_bar != null){_bar.setValue(i);}
		qb.sendMe(_ic);
	    }else{
		throw new QuBitFormatException("Bad qubit format for sending.");
	    }
	}
    }   
   
    
    
   /**
    * This method is used to send a random qubit among
    * several possibilites. They are not returned to the server but
    * rather sent automatically to the init client.
    * The connection with the client must be set and working.
    * @param bytecode is the quantum coding to be used.
    * @param n is the number of qubits to be sent and received.
    */
    public void sendAndForward(byte bytecode, int n)throws TimeOutException,QuBitFormatException{
	switch(bytecode){
	    case Constants.BB84: 
			sendAndForward(BB84Qoding.BB84ALPHABET,n);
			break;
	    case Constants.B92 : 
			sendAndForward(B92Qoding.B92ALPHABET,n);
			break;
	    default: QryptoWarning.warning("Bad code for qantum coding",
	                                    "VirtualQSender", null);
	}
    }


    
    
    
    
    /**
    * Sends n random qubits from a qoding scheme.
    * @param bytecode is the code for the quantum encoding to be used.
    * The possible bytecodes are all in the Constants class.
    * @return the qubits sent. Null if a problem occured.
    */
    
    public QuBit[] send(byte bytecode, int n)throws TimeOutException, QuBitFormatException{
	QuBit[] qb = null;
	switch(bytecode){
	    case Constants.BB84: 
			qb = send(BB84Qoding.BB84ALPHABET,n);
			break;
	    case Constants.B92 : 
			qb = send(B92Qoding.B92ALPHABET,n);
			break;
	    default: QryptoWarning.warning("Bad code for qantum coding",
	                                    "VirtualQSender", null);
	}
	return qb;
    }


    /**
     * This method close the connection. 
     */

    @SuppressWarnings("deprecation")
	public void close() {
      if(_t != null){
	_t.stop();
      }
      if(_ssc != null){
	_ssc.closeConnection();
      }
      if(_ic != null){_ic.closeConnection();}
    }

    /**
     * This method waits for a resp. server connection  or timeout  
     * before returning the control.
     * @return true if the responder connects before the timeout
     */

    public synchronized boolean waitConnection()throws RuntimeQryptoException{
	boolean tout = false;
	try{
	    while(!_connected && !tout){
		wait(_timeout);
		if(!_connected){tout = true;}
	    }
	}catch(InterruptedException ie){
	    throw new RuntimeQryptoException("Problem while waiting connection:"+ie.getMessage());
	}
	return _connected;
    }
    
    /**
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QServerSocketConnection getPubConnection(){
	return _ssc;
    }

   /**
    * Allows to get the configuration for the quantum transmission.
    * The configuration can then be used to call the constructor
    * with the selected configuration.
    * @param h is a hashtable containing some configurations.
    * @param owner is the parent frame for the configuration dialog. 
    * @return a new configuration hashtable containing the original
    * configuration plus the new ones.
    */
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Hashtable configure(Hashtable h, Component owner){
     try{
	 String PREV_ERROR_PROB = new Double(ERROR_PROB_DEF_VALUE).toString();
	 Object prevval = h.get(ERROR_PROB_CONFIG_KEY);
	 try{
	    if(prevval != null){
	     PREV_ERROR_PROB = ((Double)prevval).toString();
	    }
	 }catch(RuntimeException et){
	   PREV_ERROR_PROB = new Double(ERROR_PROB_DEF_VALUE).toString(); 
	 }
	 String out = (String)JOptionPane.showInputDialog(owner, 
				    "Probabilitté d'érreur du Canal quantique virtuel :",
				    "configuration du Canal Quantique ",
				    JOptionPane.QUESTION_MESSAGE,
				    null,null,
				    PREV_ERROR_PROB);
	  if(out != null){
	    Double dval = Double.valueOf(out);
	    double val = dval.doubleValue();
	    if((val <0.5) && (dval.longValue() >= 0)){
		h.put(ERROR_PROB_CONFIG_KEY, new Double(val));
	    }else{
	       throw new NumberFormatException("Valeur hors limite");
	    }
	  }
      }catch(NumberFormatException nfe){
	    JOptionPane.showMessageDialog(owner,
		    "La valeur sélectionné est incorrecte.\n la valeur précedente "+
		     new Double(ERROR_PROB_DEF_VALUE).toString()+" réstants.",
		    "Mauvaise configuration",
		     JOptionPane.ERROR_MESSAGE);
      }
      return h;
    }
    



}
