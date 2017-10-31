package qrypto.qommunication;


import java.awt.Component;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import qrypto.exception.QryptoWarning;
import qrypto.exception.TimeOutException;
import qrypto.log.Log;
import qrypto.server.QSocketPubConnection;





public class VirtualQReceiver implements QReceiver{

private static String LOGFILENAME = Constants.VIRTUALQRECEIVERLOGFILE;
private Log _logfile = null;



private QSocketPubConnection _ssp = null;
private JProgressBar _bar = null;
private ServerSocketConnection _ic = null;
private int _numberOfBucket = 0;


//Is the key associated to the configuration of the error 
//probability.
public static String ERROR_PROB_CONFIG_KEY = "Error probability";
public static double ERROR_PROB_DEF_VALUE = (1-Math.sqrt(1-2*Constants.DEF_DETECTOR_PROB_ERROR))/2;
//The error probability for one transmitted qubit.  
public double errorProb = ERROR_PROB_DEF_VALUE;





    /**
     * Constructor for a new quantum receiver. It connects to a quantum sender.
     * @param ip is the IP address of the initiator server.
     * @param port is the port number where to connect.
     * @exception IOEXception if the connection couldn't be completed.
     * @see SocketPubConnection
     */

	public VirtualQReceiver(InetAddress ip, int port) throws IOException{
	      _logfile = new Log(LOGFILENAME,true, "VirtualQReceiver");
	      _logfile.header(); 
	      _ssp = new QSocketPubConnection(ip,port);
	}



    /**
     * Constructor for a new quantum receiver. It connects to the initiator server.
     * @param ip is the IP address of the initiator server..
     * @param port is the port number where to connect.
     * @param ic is the connection with the responder client.
     * @exception IOException if the connection couldn't be completed.
     * @see SocketPubConnection
     */

	public VirtualQReceiver(InetAddress ip, int port,ServerSocketConnection ic) throws IOException{
	      this(ip,port);
	      _ic = ic;
	}
	
     /**
     * Constructor for a new quantum receiver. It connects to the initiator server.
     * @param ssc is an alreay established  server-server connection.
     * @param ic is the connection with the responder client.
     * @exception IOException if the connection couldn't be completed.
     * @see SocketPubConnection
     */

	public VirtualQReceiver(QSocketPubConnection ssc,ServerSocketConnection ic) throws IOException{
	  _logfile = new Log(LOGFILENAME,true, "VirtualQReceiver");
	  _logfile.header(); 
	  _ssp = ssc;
	  _ic = ic;
	}
	
	
    /**
    * Returns 1 sincxe the bucket size of a virtual transmission
    * is always 1.
    * @return 1
    */
    
    public int getBucketSize(){
	return 1;
    }
    
  /**
   * Does nothing since the bucket size of virtual transmission
   * is always 1.
   * @param bs is the new bucket size.
   */
   
   public void setBucketSize(int bs){
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
     * Returns the type of the q-transmission.
     * @return Constants.VIRTUAL
     */
     
     public byte qType(){
	return Constants.VIRTUAL;
     }
	
	
    /**
    * Applies the configuration to this obect.
    * @param h is an hashtable of configuration parameters.
    * The ones that refer to this object will be set. If none appear then
    * nothing is done.
    */
    
    public byte appliesConfig(@SuppressWarnings("rawtypes") Hashtable h){
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
    * Sets the client connection.
    * @param ic is the new client connection.
    */
    
    public void setClientConnection(ServerSocketConnection ic){
	_ic = ic;
    }
    
    /**
    * Returns the client connection associated to this receiver.
    * @return the client connection or null is it has not been set.
    */
    
    public ServerSocketConnection getClientConnection(){
	return _ic;
    }
 


     /**
      * Sets the progress for showing progress. If the progress bar
      * is not set then nothing is shown.
      * @param bar is the progress bar.
      */
   
   public void setProgressBar(JProgressBar bar){
	_bar = bar;
   }


    /**
     * This methos allows to receive a qubit. For the time being
     * the measurement is a random BB84 measurement. Next version
     * will allow different kind of measurement.
     * @return the received qubit (measured).
     * @exception TimeOutException if nothing has been received before the timeout.
     * Possibly because the connection is broken.
     * @exception QuBitFormatException if what has been received did not encode a QuBit.
     */

//    private QuBit read()throws TimeOutException, QuBitFormatException{
//	QuBit measure = null;
//	QuBit qb = QuBit.receiveQuBit(_ssp);
//	measure = new BB84QuBit(BB84QuBit.B0,randBasis());
//	boolean outcome = qb.measureVN(measure);
//	if(!outcome){measure.compl();}
//	if(Math.random() < errorProb){
//	    measure.compl();
//	}
//	return measure;
//    }
    
     /**
     * This methos allows to receive a bunch of qubits. The resulting
     * array of qubits contains the outcome of the measurement. 
     * @return the received qubit (not measured).
     * @exception TimeOutException if nothing has been received before the timeout.
     * Possibly because the connection is broken.
     * @exception QuBitFormatException if what has been received did not encode a QuBit.
     */
//    public QuBit[] read(int n) throws TimeOutException, QuBitFormatException{
//	QuBit[] qb = new QuBit[n];
//	_numberOfBucket = 0;
//	if(_bar != null){
//	    _bar.setMaximum(n-1);_bar.setValue(0);
//	}
//	Log.write(_logfile,"Receiving quantumly.",true);
//	for(int i = 0; i<n; i++){
//	    qb[i] = read();
//	    _numberOfBucket++;
//	    //Log.write(_logfile,qb[i].toString(),true);
//	    if(_ic != null){
//		QuBit.sendQuBit(qb[i], _ic);
//	    }
//	    if(_bar != null){_bar.setValue(i);}
//	}
//	Log.write(_logfile,"done.",true);
//	return qb;
 //   }
    
    
    /**
    * This method allows to receive a qubit from an alphabet.
    * The returned qubit will be in the same class than
    * the qubit in the alphabet.
    * @param alphabet is the alphabet of possible states.
    * @param obs is the observable for each possible measurement.
    * obs[i] is an observable for he i-th possible measurement.
    * the measurement is choosen at random among the possible.
    * If obs[i] is the outcome obs[i] is returned otherwise
    * obs[i].negMe() is returned. For bb84 obs[0]=|0> and 
    * obs[1]=|0>+|1> do the job.
    */
    
    public QuBit read(QuBit[] alphabet,QuBit[] obs)throws TimeOutException{
	QuBit measure = null;
	QuBit qb = alphabet[0].receiveLikeMe(_ssp);
	measure = obs[new Double(obs.length * Math.random()).intValue()].copyMe();
	boolean outcome = qb.measureVN(measure);
	if(!outcome){measure.compl();}
	if(Math.random() < errorProb){
	    measure.compl();
	}
	return measure;	
    }
    
    
    /**
    * This methos allows to receive a qubit from an alphabet and
    * send them back to the responder player.
    * @param alphabet is the possible qubit to be received.
    * The alphabet should contain only qubits of the same subclass.
    * @param obs is the list of possible observables which are
    * selected randomly. See read(alphabet,obs).
    * @param n is the number of qubits expected.
    */
    
    public QuBit[] read(QuBit[] alphabet, QuBit[] obs, int n)throws TimeOutException{
      QuBit[]  qb = new QuBit[n];
      _numberOfBucket = 0;
      if(_bar != null){
	_bar.setMaximum(n-1);
	_bar.setValue(0);
      }
      for(int i = 0; i< n; i++){
	qb[i] = read(alphabet,obs);
	_numberOfBucket++;
	if(_ic != null){
		qb[i].sendMe(_ic);
	}
	if(_bar != null){_bar.setValue(i);}
      }
      return qb;
    }
    
    
    /**
    * This methos allows to receive qubits from an alphabet and
    * send them back to the responder player automatically.
    * The connection with the player must be up and ready
    * and the result of the transmission is not saved.
    * @param alphabet is the possible qubit to be received.
    * The alphabet should contain only qubits of the same subclass.
    * @param obs is the list of possible observables which are
    * selected randomly. See read(alphabet,obs).
    * @param n is the number of qubits expected.
    */
    
    public void readAndForward(QuBit[] alphabet, QuBit[] obs, int n)throws TimeOutException{
      QuBit qb = null;
      _numberOfBucket = 0;
      if(_bar != null){
	_bar.setMaximum(n-1);
	_bar.setValue(0);
      }
      for(int i = 0; i< n; i++){
	qb = read(alphabet,obs);
	_numberOfBucket++;
	qb.sendMe(_ic);
	if(_bar != null){_bar.setValue(i);}
      }
    }
    
   /**
    * Receives qubits according to some coding with automatic forwarding
    * without storing the received qubits.
    * @param bytecode is the code coresponding to the coding.
    * @param n is the number of qubits expected.
    * @return the array of qubits received.
    */
    
    public void readAndForward(byte bytecode, int n)throws TimeOutException{
       @SuppressWarnings("unused")
	QuBit[] qb = null;
	switch(bytecode){
	    case Constants.BB84: 
			readAndForward(BB84Qoding.BB84ALPHABET,BB84Qoding.BB84OBS,n);
			break;
	    case Constants.B92: 
			readAndForward(B92Qoding.B92ALPHABET, BB84Qoding.BB84OBS,n);
			break;
	    default: QryptoWarning.warning("Bad code for qantum coding",
	                                    "VirtualQSender", null);
	}
    }
    
    
    
    /**
    * Receives qubits according to some coding.
    * @param bytecode is the code coresponding to the coding.
    * @param n is the number of qubits expected.
    * @return the array of qubits received.
    */
    
    public QuBit[] read(byte bytecode, int n)throws TimeOutException{
       QuBit[] qb = null;
	switch(bytecode){
	    case Constants.BB84: 
			qb = read(BB84Qoding.BB84ALPHABET,BB84Qoding.BB84OBS,n);
			break;
	    case Constants.B92: 
			qb = read(B92Qoding.B92ALPHABET, BB84Qoding.BB84OBS,n);
			break;
	    default: QryptoWarning.warning("Bad code for qantum coding",
	                                    "VirtualQSender", null);
	}
	return qb;
    }
    


    /**
     * This method close the connection.
     */

    public void close(){
	if(_ssp != null){
	    _ssp.closeConnection();
	}
	if(_ic != null){
	    _ic.closeConnection();
	}
	_ssp = null;
	_ic = null;
    }
    
      
    /**
    * This method indicates whether or not this receiver is connected
    * with the sender.
    * @return true all the time since the constructor established the
    * connection.
    */
    
    public boolean isConnected(){
	return true;
    }
   
    
   /**
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QSocketPubConnection getPubConnection(){
	return _ssp;
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
				    "Virtual QC error probability:",
				    "Virtual QC configuration",
				    JOptionPane.QUESTION_MESSAGE,
				    null,null,
				    PREV_ERROR_PROB);
	try{
	    if(out != null){
	        Double dval = Double.valueOf(out);
		double val = dval.doubleValue();
		if((val <1.0) && (dval.longValue() >= 0)){
		    h.put(ERROR_PROB_CONFIG_KEY, new Double(val));
		}else{
		   throw new NumberFormatException("Value out of bound");
		}
	    }
	}catch(NumberFormatException nfe){
		JOptionPane.showMessageDialog(owner,
		    "The selected value was not set properly.\n The previous value "+
		     new Double(ERROR_PROB_DEF_VALUE).toString()+" remains.",
		    "Bad setting",
		     JOptionPane.ERROR_MESSAGE);
	}//finally{
	    owner.repaint();
	//}
	return h;
    }
    
    
    /**
    * Picks a random BB84 basis.
    * @return the BB84 basis.
    */
    
    @SuppressWarnings("unused")
	private boolean randBasis(){
	boolean r;
	if(Math.random()<0.5d){
	  r=BB84QuBit.RECTILINEAR;
	}else{
	  r=BB84QuBit.DIAGONAL;
	}
	return r;
    }
    
    


}
