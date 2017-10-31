package qrypto.qommunication;


import java.awt.Component;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import qrypto.exception.QryptoWarning;
import qrypto.exception.QuBitFormatException;
import qrypto.exception.TimeOutException;
import qrypto.server.QServerSocketConnection;


public class RealQSender implements QSender, ConnectionNotify
{


    public static final String _LOGFILENAME = "reals.txt"; 


    public static final String DG_PORT_NUMBER_KEY = "DG port number";
    public static final String BUCKET_SIZE_KEY = "Bucket size";
    public static final String DG_IP_ADDRESS_KEY = "DG IP address";
  
    private ServerSocketConnection _ic  = null;
    private QServerSocketConnection _ssc = null;
    private DGConnection _dgc = null; 
    
    private Thread _tssc = null;
    private boolean _respServerConnected = false;
    private JProgressBar _bar = null;
 
    private int _bucketSize = Constants.DEF_BUCKET_SIZE;
    private int _numberOfBuckets = 0;
 
    private int _portDG = Constants.DEF_PORT_SEND_DG;
    private String _DGIP;
    
    /**
    * Creates a new instance. 
    * @param DGIP is the ip address of the initiator DG.
    * @param ports  is the port number for pub connection with the responder server.
    * @param portdg is the port number where to connect to the DG.
    * @param ic is the connection with the initiator client. If null
    * then qubit sent are not automatically retransmitted to the client.
    */

    public RealQSender(InetAddress DGIP, int portdg,int ports, ServerSocketConnection ic)throws IOException{
       this(ports,ic);
       _portDG = portdg;
       _DGIP = DGIP.toString();
       try{
	   _dgc = new DGConnection(DGIP,portdg);
       }catch(IOException io){
	    if(_ssc != null){_ssc.closeConnection();}
	    QryptoWarning.warning("Couldn't establish the connection with DG \n"+
	                          "at "+DGIP.toString()+" port "+portdg,"RealQSender",null);
	    throw new IOException(io.getMessage());
       }
    }
    
    
   /**
    * Creates a new instance with unspecified dg connection.
    * Before using the functionality of this class, one must
    * call appliesConfig(h).
    * @param ports  is the pub connection with the responder server.
    * @param portdg is the connection with the data grabber.
    * @param ic is the connection with the initiator client. If null
    * then qubit sent are not automatically retransmitted to the client.
    */

    
    public RealQSender(int ports, ServerSocketConnection ic)throws IOException{
	   _numberOfBuckets = 0;
	   _portDG = Constants.DEF_PORT_SEND_DG;
	   _DGIP =   Constants.DEF_INIT_DG_IP;
	   //_logfile = new Log(_LOGFILENAME, true, "RealQSender");
	   _respServerConnected = false;
	   _ssc = new QServerSocketConnection(ports,Constants.REAL, this);
	   _ic = ic;
	   _tssc = new Thread(_ssc);
	   _tssc.start();
    }
    
   /**
    * Creates a new instance with unspecified client connection.
    * @param DGIP is the IP address of the DG.
    * @param ports  is the pub connection with the responder server.
    * @param portdg is the connection with the data grabber.
    */

    public RealQSender(InetAddress DGIP, int portdg,int ports)throws IOException{
       this(DGIP, portdg, ports,null);
    }
    
    
    /**
    * Sets the bucket size for communication with the DG.
    * If it is set to N then each interrogation of the DG returns
    * N time slots.
    * @param s is the bucket size. If s is smaller than 1 than nothing
    * is done. s should also be smaller than (256*255)+255.
    */
    
    
    public void setBucketSize(int s){
     if( (s<(256*255)+255)&&(s>0)){
	_bucketSize = s;
     }
    }
    
    /**
    * Returns the bucket size.
    * @return the bucket size.
    */
    
    public int getBucketSize(){
	return _bucketSize;
    }
    
  /**
   * Sends the number of buckets used back to the
   * client. 
   * @return the number of bucket.
   */
   
   public void sendNumberOfBucketsToClient(){
	_ic.sendInt(_numberOfBuckets);
   }
   
   /**
   * Returns the number of buckets needed.
   * @return the number of buckets.
   */
   
   public int getNumberOfBuckets(){
	return _numberOfBuckets;
   }


     
    /**
    * Sets the client connection for this transmsission.
    * @param ic is the new connection to the client. Should be already
    * waiting for a connection.
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
    * Sets as progress bar to be updated as the quantum transmission
    * progress.
    * @param bar is the progress bar.
    */
    
    public void setProgressBar(JProgressBar bar){
	_bar = bar;
    }
    
    /**
    * Is called by the server socket connection when a new  connection
    * with the responder server occurs.
    */
    
    public synchronized void notifyNewConnection(){
      if(!_respServerConnected){
	_respServerConnected = true;
      }
      notifyAll();
    }
    
    /**
    * Returns the type of this q-transmission.
    * @return Constants.REAL
    */
    
    public byte qType(){
	return Constants.REAL;
    }


   /**
    * This method is used to send a random qubit among
    * several possibilites. For the progress bar to be handled 
    * correctly, the maximum must be set before calling this method.
    * @param qodingtype indicates the coding used.
    * @param thend is true if the next qubit is the last want to this
    * connection.
    * @return the qubit that has been sent. Null if nothing has been
    * sent or a problem occured.
    */
//    private QuBit[] send2()throws IOException, TimeOutException, QuBitFormatException{
//       int peerGoodTimeSlot = 0;
//       byte[] qb = null;
//       byte[] temp = new byte[_bucketSize];
//       QuBit[] qbs = null;
//       Vector goodIndices = new Vector();
//       _dgc.getOutputStream().write((int)Constants.OK);
//       int status = _dgc.getInputStream().read(temp); 
//       if(status != _bucketSize){throw new IOException("RealQSender: only "+status+" bytes received in a bucket");}
//       qb = temp;
//       temp = null; 
//       _ssc.sendByte(Constants.OK);
//       byte answ = _ssc.receiveByte();
//       if(answ == Constants.OK){
//	  peerGoodTimeSlot = _ssc.receiveInt();
//	  for(int i = 0; i<peerGoodTimeSlot;i++){
//	    int goodPos = _ssc.receiveInt();
//	    if(qb[goodPos] != Constants.UNDETECTED){
//		_ssc.sendByte(Constants.OK);
//		goodIndices.addElement(new Integer(goodPos));
//	    }else{
//		_ssc.sendByte(Constants.UNDETECTED);
//	    }
//	  } 
//	}else{
//		throw new QuBitFormatException("Responder server indicates a problem with DG.");
//	}
//	int s = goodIndices.size();
//	qbs = new QuBit[s];
//	for(int i = 0; i<s; i++){
//	    qbs[i] = mapToQuBit(qb[((Integer)goodIndices.elementAt(i)).intValue()]);
//	}
//	return qbs;
//    }

   /**
    * This method is used to send a random qubit among
    * several possibilites. For the progress bar to be handled 
    * correctly, the maximum must be set before calling this method.
    * @param qodingtype indicates the coding used. This is a more
    * efficient implementation than send2(). It receives from the DG only
    * the positions for which the receiver got a detected signal.
    * @param thend is true if the next qubit is the last want to this
    * connection.
    * @return the qubit that has been sent. Null if nothing has been
    * sent or a problem occured.
    */
//    private QuBit[] send()throws IOException, TimeOutException, QuBitFormatException{
//       int numberGoodSlots = 0;
//       int[] pos = null;
//       byte[] qb = null;
//       QuBit[] qbs = null;
//       _dgc.getOutputStream().write((int)Constants.OK);
//       _ssc.sendByte(Constants.OK);
//       byte answ = _ssc.receiveByte();
//       if(answ == Constants.OK){
//	  numberGoodSlots = _ssc.receiveInt();
//	  pos = new int[numberGoodSlots];
//	  for(int i = 0; i<numberGoodSlots; i++){
//	    pos[i] = _ssc.receiveInt();
//	  }
//	  qb = new byte[numberGoodSlots];
//	  Constants.sendSpecialInt(numberGoodSlots,_dgc.getOutputStream());
//	  for(int i = 0; i<numberGoodSlots;i++){
//	    Constants.sendSpecialInt(pos[i], _dgc.getOutputStream());
//	  }
//	  if(numberGoodSlots>0){
//	    _dgc.getInputStream().read(qb);
//	  }
//	}else{
//		throw new QuBitFormatException("Responder server indicates a problem with DG.");
//	}
//	qbs = new QuBit[numberGoodSlots];
//	for(int i = 0; i<numberGoodSlots; i++){
//	    qbs[i] = mapToQuBit(qb[i]);
//	}
//	return qbs;
//    }
    
   /**
   * Sends qubits. New version...
   */
     private byte[] send()throws IOException, TimeOutException, QuBitFormatException{
       int numberGoodSlots = 0;
       int[] pos = null;
       byte[] qb = null;
       //QuBit[] qbs = null;
       _dgc.getOutputStream().write((int)Constants.OK);
       _ssc.sendByte(Constants.OK);
       byte answ = _ssc.receiveByte();
       if(answ == Constants.OK){
	  numberGoodSlots = _ssc.receiveInt();
	  pos = new int[numberGoodSlots];
	  for(int i = 0; i<numberGoodSlots; i++){
	    pos[i] = _ssc.receiveInt();
	    //System.out.println("GOOD POS:"+pos[i]);
	  }
	  qb = new byte[numberGoodSlots];
	  Constants.sendSpecialInt(numberGoodSlots,_dgc.getOutputStream());
	  for(int i = 0; i<numberGoodSlots;i++){
	    Constants.sendSpecialInt(pos[i], _dgc.getOutputStream());
	  }
	  //if(numberGoodSlots>0){
	 //   _dgc.getInputStream().read(qb);
	  //}
	  for(int i =0;i<numberGoodSlots;i++){
	    qb[i] = (byte)_dgc.getInputStream().read();
	    //System.out.println("REALQSENDER pos:"+pos[i]+" sent:"+qb[i]);
	  }
	}else{
		throw new QuBitFormatException("Responder server indicates a problem with DG.");
	}
	//qbs = new QuBit[numberGoodSlots];
	//for(int i = 0; i<numberGoodSlots; i++){
	//    qbs[i] = mapToQuBit(qb[i]);
	//}
	return qb;
    }      
    
    /**
    * This method is used to send a random qubit among
    * several possibilites.
    * @param bytecode is the quantum coding to be used.
    * @param n is the number of qubits to be sent and received.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */
    public QuBit[] send(byte bytecode, int n)throws TimeOutException,QuBitFormatException{
	byte[] qbs = null;
	BB84QuBit[] qb = null;
	_numberOfBuckets = 0;
	if(!waitConnection()){throw new TimeOutException("Couldn't get resp server connection.");}
	if(_bar != null){_bar.setMaximum(n-1);_bar.setValue(0);}
	//_ssc.sendInt(_bucketSize);
	byte rsaccept = _ssc.receiveByte();
	try{
	    if(rsaccept == Constants.OK){
		qb = new BB84QuBit[n];
		//sends to the DG the bytecode and the bucket size for the
		//quantum transmission.
		_dgc.getOutputStream().write((int)bytecode);
		Constants.sendSpecialInt(_bucketSize, _dgc.getOutputStream());
		int intro = _dgc.getInputStream().read();
		if((byte)intro == Constants.OK){
		  int i = 0;
		  while(i<n){
		    qbs = send();
		    _numberOfBuckets++;
		    for(int j = 0; (j<qbs.length) && (i<n); j++){
		       //qb[i] = mapToQuBit(qbs[j]);
		       qb[i] = BB84QuBit.convert(qbs[j]);
		       if(_ic != null){
		           //sends back to the initiator player.
			   qb[i].sendMe(_ic);
		       }
		       if(_bar != null){_bar.setValue(i);}
		       i++;
		    }
		  }
		  _dgc.getOutputStream().write(Constants.STOP);
		}
	    }
	}catch(IOException io){
	    throw new TimeOutException("IO/ERR send-RealQSender "+io.getMessage());
	}finally{
	    qbs = null;
	}
	return qb;
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
	byte[] qbs = null;
	//BB84QuBit qb = null;
	_numberOfBuckets = 0;
	if(!waitConnection()){throw new TimeOutException("Couldn't get resp server connection.");}
	if(_bar != null){_bar.setMaximum(n-1);_bar.setValue(0);}
	//System.out.println("The bucket size to the init server:"+_bucketSize);
	//_ssc.sendInt(_bucketSize);
	//byte rsaccept = _ssc.receiveByte();
	try{
	  //  if(rsaccept == Constants.OK){
		//qb = new BB84QuBit[n];
		//sends to the DG the bytecode and the bucket size for the
		//quantum transmission.
		_dgc.getOutputStream().write((int)bytecode);
		Constants.sendSpecialInt(_bucketSize, _dgc.getOutputStream());
		int intro = _dgc.getInputStream().read();
		if((byte)intro == Constants.OK){
		  int i = 0;
		  while(i<n){
		    qbs = send();
		    _numberOfBuckets++;
		    for(int j = 0; (j<qbs.length) && (i<n); j++){
		       //qb[i] = mapToQuBit(qbs[j]);
		       //qb = BB84QuBit.convert(qbs[j]);
		       //sends back to the initiator player.
		       //qb.sendMe(_ic);
		       BB84QuBit.sendIt(qbs[j],_ic);
		       if(_bar != null){_bar.setValue(i);}
		       i++;
		    }
		  }
		  _dgc.getOutputStream().write(Constants.STOP);
		}
	    //}
	}catch(IOException io){
	    throw new TimeOutException("IO/ERR send-RealQSender "+io.getMessage());
	}finally{
	    qbs = null;
	}
    }   
    
    
    
    
    /**
    * This method is used to send a random qubit among
    * several possibilites.
    * @param bytecode is the quantum coding to be used.
    * @param n is the number of qubits to be sent and received.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */
//    public QuBit[] send(byte bytecode, int n)throws TimeOutException,QuBitFormatException{
//	QuBit[] qbs = null;
//	QuBit[] qb = null;
//	_coding = bytecode;
//	_numberOfBuckets = 0;
//	if(!waitConnection()){throw new TimeOutException("Couldn't get resp server connection.");}
//	if(_bar != null){_bar.setMaximum(n-1);_bar.setValue(0);}
//	_ssc.sendInt(_bucketSize);
//	byte rsaccept = _ssc.receiveByte();
//	try{
//	    if(rsaccept == Constants.OK){
//		qb = new QuBit[n];
//		//sends to the DG the bytecode and the bucket size for the
//		//quantum transmission.
//		_dgc.getOutputStream().write((int)bytecode);
//		Constants.sendSpecialInt(_bucketSize, _dgc.getOutputStream());
//		int intro = _dgc.getInputStream().read();
//		if((byte)intro == Constants.OK){
//		  int i = 0;
//		  while(i<n){
//		    qbs = send();
//		    _numberOfBuckets++;
//		    for(int j = 0; (j<qbs.length) && (i<n); j++){
//		       qb[i] = qbs[j];
//		       if(_ic != null){
//		           //sends back to the initiator player.
//			   qb[i].sendMe(_ic);
//		       }
//		       if(_bar != null){_bar.setValue(i);}
//		       i++;
//		    }
//		  }
//		  _dgc.getOutputStream().write(Constants.STOP);
//		}
//	    }
//	}catch(IOException io){
//	    throw new TimeOutException("IO/ERR send-RealQSender "+io.getMessage());
//	}finally{
//	    qbs = null;
//	}
//	return qb;
//   }
    
    
   /**
    * This method is used to send a random qubit among
    * several possibilites.
    * @param alphabet is the set of possible state to be sent.
    * @param n is the number of qubits to be sent.
    * @return the qubit that has been sent. Null if nothing has been
    * sent.
    */
//    public QuBit[] send(QuBit[] alphabet, int n)throws TimeOutException,QuBitFormatException{
//	return null;
//    }
    
    

    
    /**
    * Close the session. The public connection between the two servers
    * is closed. The connection with the DG is also close.
    */
    
    @SuppressWarnings("deprecation")
	public void close(){
	if(_tssc != null){
	    _tssc.stop();
	}
	if(_ssc != null){
	    _ssc.closeConnection();
	}
	if(_dgc != null){
	    _dgc.close();
	}
	if(_ic != null){
	    _ic.closeConnection();
	}
	_tssc = null;
    }
    
    
    
    /**
    * Returns whether or not the connection between the servers
    * is established. It also waits for the DG connection to be established.
    */
    
   public boolean isConnected(){
	return _respServerConnected;
   }
    
   /**
    * Waits for the  server-server connection to be established.
    * @return true if the server-server conection was established with success.
    */
    
    public synchronized boolean waitConnection(){
      boolean out = true;
      try{
	    while(!_respServerConnected){
		wait();
	    }
       }catch(InterruptedException ie){ out = false;}
       return out;
    }

    
   /**
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QServerSocketConnection getPubConnection(){
	return _ssc;
    }
    
   /**
    * Sets the configuration of an instance according to the ones
    * define in an hashtable. The hashtable can contain configurations
    * that apply to other cases. Two fields related to this class can be found
    * in the hashtable. 1) the port number for communication with the
    * DG and 2) the bucket size for the quantum transmission.
    * If the server DG connection was not already working then a thread 
    * listening for
    * connection with the DG is launched. No exception is thrown whenever 
    * the server of the dg connection couldn't be launched. 
    * If the bucket size is defined then the new value is set.
    * @param h is an hashtable containing some configuration keys and values.
    * @preturn a byte indicating the status: Constants.OK, Constants.ERROR,
    */
    
    @SuppressWarnings("rawtypes")
	public byte appliesConfig(Hashtable h){
	byte output = Constants.OK;
	if(h != null){
	    Object o = h.get(DG_PORT_NUMBER_KEY);
	    if(o != null){
		_portDG = ((Integer)o).intValue();
	    }
	    o = h.get(DG_IP_ADDRESS_KEY);
	    if(o != null){
		_DGIP  = (String)o;
	    }
	    o = h.get(BUCKET_SIZE_KEY);
	    if(o != null){
		_bucketSize = ((Integer)o).intValue();
	    }
	    try{
	       _dgc = new DGConnection(InetAddress.getByName(_DGIP), _portDG);
	    }catch(IOException io){
	       output = Constants.ERROR;
	       QryptoWarning.warning("Couldn't connect to the DG server...\n"+
	                             "at "+_DGIP+" port "+_portDG,"RealQSender",null);
	    }	    
	}else{
	    output = Constants.ERROR;
	    QryptoWarning.warning("Received an empty config table","RealQSender",null);
	}
	return output;
    }
    
    /**
    * This method allows the user to select the port by which it waits
    * for the DG connection. In addition, the user can select the bucket
    * size for quantum transmission.
    * @param h is the configuration settings to start with. The new 
    * configurations are added to it.
    * @param owner is the parent of this configuration window.
    * @return an hash table containing the old plus the new configurations.
    */
    
    
     @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable configure(Hashtable h, Component owner){
       String stringIPDG=Constants.DEF_INIT_DG_IP;
       String stringDG = new Integer(Constants.DEF_PORT_SEND_DG).toString();
       String stringBucket = new Integer(Constants.DEF_BUCKET_SIZE).toString();
       Object o = h.get(DG_PORT_NUMBER_KEY);
       if(o != null){
	    stringDG = ((Integer)o).toString();
       }
       o = h.get(DG_IP_ADDRESS_KEY);
       if(o != null){
	    stringIPDG = (String)o;
       }
       o  = h.get(BUCKET_SIZE_KEY);
       if(o != null){
	    stringBucket = ((Integer)o).toString();
       }
       Object[] message = new Object[6];
       message[0] = "Select the DG IP address:";
       message[1] = new JTextField(stringIPDG);
       message[2] = "Select the DG port number:";
       message[3] = new JTextField(stringDG);
       message[4] = "Select the bucket size:";
       message[5] = new JTextField(stringBucket);
       String[] options = {"OK","CANCEL"};
       try{
	 int res = JOptionPane.showOptionDialog(owner,message,
				    "Initiator real Q-transmission settings",
				     JOptionPane.DEFAULT_OPTION,
				     JOptionPane.INFORMATION_MESSAGE, null,
				     options, options[1]);
	 
	 if(res == 0){
	    String newDGIP = ((JTextField)message[1]).getText();
	    try{
		InetAddress.getByName(newDGIP);
		h.put(DG_IP_ADDRESS_KEY, newDGIP);
	    }catch(UnknownHostException uh){
		throw new NumberFormatException("DG IP address unknown");
	    }
	    Integer newdgport = Integer.valueOf(((JTextField)message[3]).getText());
	    if((newdgport.intValue() >= Constants.MIN_PORT_VALUE) && (newdgport.intValue() <= Constants.MAX_PORT_VALUE)){
		h.put(DG_PORT_NUMBER_KEY, newdgport);
	    }else{
	       throw new NumberFormatException("DG port value out of bounds");
	    }
	    Integer bucketsize = Integer.valueOf(((JTextField)message[5]).getText());
	    if((bucketsize.intValue() >0) && (bucketsize.intValue() < Constants.MAX_BUCKET_SIZE)){
		h.put(BUCKET_SIZE_KEY, bucketsize);
	    }else{
		throw new NumberFormatException("Bucket size out of bound");
	    }
	 }
       }catch(NumberFormatException nfe){
	    QryptoWarning.warning("The selected value was not set properly.\n"+
	                          nfe.getMessage()+"\n"+
				  "Previous values remains.",
				  "ReaQSender Config",owner);
       }//finally{
	  //  owner.repaint();
	//}
	return h;
    }
    
    
    /**
    * This method maps a byte returns by the DG into a qubit.
    * @param code is the byte code to map into qubit.
    * @return the qubit associated to the code. Returns null if code
    * does  not encode a correct qubit.
    */
    
//    public static BB84QuBit mapToQuBit(byte code){
//	BB84QuBit ans = null;
//	if(code == Constants.BB840R){
//	    ans = new BB84QuBit(BB84QuBit.B0,BB84QuBit.RECTILINEAR);
//	}
//	if(code == Constants.BB841R){
//	    ans = new BB84QuBit(BB84QuBit.B1,BB84QuBit.RECTILINEAR);
//	}
//	if(code == Constants.BB840D){
//	    ans = new BB84QuBit(BB84QuBit.B0, BB84QuBit.DIAGONAL);
//	}
//	if(code == Constants.BB841D){
//	    ans = new BB84QuBit(BB84QuBit.B1, BB84QuBit.DIAGONAL);
//	}
//	return ans;
 //   }
    
    
    /**
    * This method maps a byte returned by the DG into a BB84 qubit.
    * @param code is the array of bytes to be converted into an array 
    *        of qubits.
    * @return the correesponding array of BB84 qubits.
    */
    
//    public static BB84QuBit[] mapToQuBit(byte[] code){
//	BB84QuBit[] qb = null;
//	if(code.length > 0){
//	    qb = new BB84QuBit[code.length];
//	    for(int i = 0; i< code.length;i++){
//		qb[i] = mapToQuBit(code[i]);
//	    }
//	}
//	return qb;
 //   }
    
   
}
