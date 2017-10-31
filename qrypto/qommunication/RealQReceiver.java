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
import qrypto.server.QSocketPubConnection;



public class RealQReceiver implements QReceiver
{


    public static final String _LOGFILENAME = "realr.txt";
    public static final String DG_PORT_NUMBER_KEY = "DG port number";
    public static final String DG_IP_ADDRESS_KEY = "DG IP address";

    private ServerSocketConnection _ic  = null;
    private QSocketPubConnection _spc = null;
    private DGConnection _dgc = null;
    private JProgressBar _bar = null;
   
    private int _bucketSize = Constants.DEF_BUCKET_SIZE;
    private int _numberOfBucket = 0;
    private int _portDG = Constants.DEF_PORT_REC_DG;
    private String _DGIP;
    
    /**
    * Created a new instance. 
    * @param ips is the ip address of the initiator server.
    * @param ports  is the port where to connect with the initiator server.
    * @param portdg is the connection with the data grabber.
    * @param ipdg is the ip address for the DG server.
    * @param ic is the connection with the initiator client. If null
    * then qubit sent are not automatically retransmitted to the client.
    * If given, the client connection must not be active, that is the
    * connection is not actually waiting for a client or is not connected
    * to a client. This constructor launch the server connection to wait
    * for a new client.
    */

    public RealQReceiver(InetAddress ips, int ports,InetAddress ipdg, int portdg,ServerSocketConnection ic)throws IOException{
       boolean spcopen = false;
       _DGIP = ipdg.toString();
       _portDG = portdg;
       try{
	   _spc = new QSocketPubConnection(ips,ports);
	   spcopen = true;
	   _dgc = new DGConnection(ipdg,portdg);
	   _ic = ic;
       }catch(IOException io){
	    if(spcopen){_spc.closeConnection();}
	    throw new IOException(io.getMessage());
       }
    }
    


   /**
    * Created a new instance. 
    * @param ssc is the server-server already established connection
    * @param IPDG is the ip address for the DG server connection.
    * @param portdg is the connection with the data grabber.
    * @param ic is the connection with the initiator client. If null
    * then qubit sent are not automatically retransmitted to the client.
    * If given, the client connection must not be active, that is the
    * connection is not actually waiting for a client or is not connected
    * to a client. This constructor launch the server connection to wait
    * for a new client.
    */

    public RealQReceiver(QSocketPubConnection ssc, InetAddress IPDG, int portdg,ServerSocketConnection ic)throws IOException{
	this(ssc,ic);
	_dgc = new DGConnection(IPDG,portdg);
    }
    
    
    
   /**
    * Created a new instance with undefined dg connection.
    * @param ssc is the server-server already established connection
    * @param ic is the connection with the initiator client. If null
    * then qubit sent are not automatically retransmitted to the client.
    * If given, the client connection must not be active, that is the
    * connection is not actually waiting for a client or is not connected
    * to a client. This constructor launch the server connection to wait
    * for a new client. Before using this class functionalities, one must
    * call appliesConfig(h) where h containns the settings for the DG port
    * number and the bucket size.
    */

    public RealQReceiver(QSocketPubConnection ssc,ServerSocketConnection ic){
	   _numberOfBucket  =0;
	   _spc = ssc;
	   _ic = ic;
    }
    
  /**
  * Returns the type for the quantum transsmission.
  * @return Constants.REAL
  */
  
  public byte qType(){
    return Constants.REAL;
  }
  
  /**
   * Gets the bucket size.
   * @return the bucket size.
   */
   
   public int getBucketSize(){
    return _bucketSize;
   }
   
   /**
   * Sets the bucket size. The new size must be smaller
   * than Constants.MAX_BUCKET_SIZE and greater than 0.
   * @param bs is the new bucket size.
   */
   
   public void setBucketSize(int bs){
    if((bs>0) && (bs<Constants.MAX_BUCKET_SIZE)){
	_bucketSize = bs;
    }
   }
   
   
  /**
   * Sends the number of buckets used back to the
   * client. 
   * @return the number of bucket.
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
   * Sets the progress for showing progress. If the progress bar
   * is not set then nothing is shown.
   * @param bar is the progress bar.
   */
   
   public void setProgressBar(JProgressBar bar){
    _bar = bar;
   }
    
    
    /**
    * Closes all connections.
    */
    
    public void close(){
       if(_dgc != null){
	    _dgc.close();
       }
       if(_spc != null){
	    _spc.closeConnection();
       }
       if(_ic != null){
	    _ic.closeConnection();
       }
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
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QSocketPubConnection getPubConnection(){
	return _spc; 
    }
    
    /**
    * Indicates if the DG connection is established.
    * @return true iff it is established.
    */
    
    public boolean isConnected(){
	return (_dgc != null);
    }
    
   
   /**
    * This method is used to read a qubit. No updating of the progress bar nor
    * retransmission of the qubit to the responder client is performed.
    * @return the qubit that has been received. Null if nothing has been
    * received or a problem occured.
    */
//    private QuBit[] read2() throws IOException,TimeOutException, QuBitFormatException{
//       int peerGoodTimeSlot = 0;
//       byte[] qb = new byte[_bucketSize];
//       QuBit[] qbs = null;
//       Vector goodIndices = new Vector();
//       _dgc.getOutputStream().write((int)Constants.OK);
//       boolean ok = true;
//       //Gets the next bucket
//       int dgstatus = _dgc.getInputStream().read(qb);
//       if(dgstatus != _bucketSize){throw new IOException("RealQReceiver : only "+dgstatus+" bytes received in a bucket");}
//       byte initstatus = _spc.receiveByte();
//       if(initstatus != Constants.OK){
//	    throw new QuBitFormatException("RealQReceiver:Responder receives an error from the initiator server.");
//       }
//       _spc.sendByte(Constants.OK);
//       for(int i = 0; i< _bucketSize; i++){
//	 if(qb[i] != Constants.UNDETECTED){
//	    peerGoodTimeSlot++;
//	    goodIndices.addElement(new Integer(i));
//	 } 
//       }
//       _spc.sendInt(peerGoodTimeSlot);
//       int j = 0;
//       for(int i=0;i<peerGoodTimeSlot;i++){
//	  _spc.sendInt(((Integer)goodIndices.elementAt(j)).intValue());
//	  byte keepit = _spc.receiveByte();
//	  if(keepit == Constants.UNDETECTED){
//	      goodIndices.removeElementAt(j);
//	  }else{
//	    j++;
//	  }
//	}
//	int s = goodIndices.size();
//	qbs = new QuBit[s];
//	for(int i = 0; i<s; i++){
//	    qbs[i] = RealQSender.mapToQuBit(qb[((Integer)goodIndices.elementAt(i)).intValue()]);
//	}
//	return qbs;
 //   }


   /**
    * More efficient implementation of read()-send().
    * This method is used to read a qubit. No updating of the progress bar nor
    * retransmission of the qubit to the responder client is performed.
    * @return the qubit that has been received. Null if nothing has been
    * received or a problem occured.
    */
//    private QuBit[] read() throws IOException,TimeOutException, QuBitFormatException{
//       byte[] qb = null;
//       int[] pos = null;
//       QuBit[] qbs = null;
//       _dgc.getOutputStream().write((int)Constants.OK);
//       byte initstatus = _spc.receiveByte();
//       if(initstatus != Constants.OK){
//	    throw new QuBitFormatException("RealQReceiver:Responder receives an error from the initiator server.");
//       } 
//       int numberGoodSlots = Constants.readSpecialInt(_dgc.getInputStream());
//       //Gets the next bucket
//       qb = new byte[numberGoodSlots];
//       pos = new int[numberGoodSlots];
//       for(int i = 0; i<numberGoodSlots; i++){
//	qb[i] = new Integer(_dgc.getInputStream().read()).byteValue();
//	pos[i] = Constants.readSpecialInt(_dgc.getInputStream()); 
//       }
//       _spc.sendByte(Constants.OK);
//       _spc.sendInt(numberGoodSlots);
//       for(int i = 0; i< numberGoodSlots; i++){
//	 _spc.sendInt(pos[i]);
//       }
//       qbs = new QuBit[numberGoodSlots];
//       for(int i = 0; i<numberGoodSlots; i++){
//	    qbs[i] = RealQSender.mapToQuBit(qb[i]);
//       }
//       qb = null;
//       pos = null;
//       return qbs;
//    }

   /**
    * More efficient implementation of read()-send().
    * This method is used to read a qubit. No updating of the progress bar nor
    * retransmission of the qubit to the responder client is performed.
    * @return the qubit that has been received. Null if nothing has been
    * received or a problem occured.
    */
    private byte[] read() throws IOException,TimeOutException, QuBitFormatException{
       byte[] qb = null;
       int[] pos = null;
       //QuBit[] qbs = null;
       _dgc.getOutputStream().write((int)Constants.OK);
       byte initstatus = _spc.receiveByte();
       if(initstatus != Constants.OK){
	    throw new QuBitFormatException("RealQReceiver:Responder receives an error from the initiator server.");
       } 
       int numberGoodSlots = Constants.readSpecialInt(_dgc.getInputStream());
       //Gets the next bucket
       qb = new byte[numberGoodSlots];
       pos = new int[numberGoodSlots];
       for(int i = 0; i<numberGoodSlots; i++){
	qb[i] = new Integer(_dgc.getInputStream().read()).byteValue();
	pos[i] = Constants.readSpecialInt(_dgc.getInputStream()); 
       }
       _spc.sendByte(Constants.OK);
       _spc.sendInt(numberGoodSlots);
       for(int i = 0; i< numberGoodSlots; i++){
	 _spc.sendInt(pos[i]);
       }
       //qbs = new QuBit[numberGoodSlots];
       //for(int i = 0; i<numberGoodSlots; i++){
	//    qbs[i] = RealQSender.mapToQuBit(qb[i]);
       //}
       //qb = null;
       //pos = null;
       return qb;
    }



   
   /**
    * This method is used to read qubits.
    * @param n is the number of qubit to be read.
    * @param bytecode is the bytecode for this transmission.
    * This is ignored for the time being as all supported transmission
    * use the same coding for BB84 states.
    * @return the (BB84) qubits that have been received. Null if nothing has been
    * received or a problem occured.
    */
    public QuBit[] read(byte bytecode, int n) throws TimeOutException, QuBitFormatException{
	if((bytecode != Constants.BB84) && (bytecode != Constants.B92)){
	    throw new QuBitFormatException("bytecode for q-transmission is unknown");
	}
	BB84QuBit[] qb = null;
	byte[] qbs = null;
	_numberOfBucket = 0;
	if(_bar != null){_bar.setMaximum(n-1);_bar.setValue(0);}
	//_bucketSize = _spc.receiveInt();
	//if((_bucketSize>0)&&(_bucketSize < Constants.MAX_BUCKET_SIZE)){ 
	//    _spc.sendByte(Constants.OK);
	//}else{
	//    _spc.sendByte(Constants.ERROR);
	//    throw new QuBitFormatException("Couldn't agree on the bucket size.");
	//}
	try{
	    _dgc.getOutputStream().write(Constants.BB84);
	    Constants.sendSpecialInt(_bucketSize, _dgc.getOutputStream());
	    int intro = _dgc.getInputStream().read();
	    if((byte)intro == Constants.OK){
		qb = new BB84QuBit[n];	
		int i = 0;
		while(i<n){
		  qbs = read();
		  _numberOfBucket++;
		  for(int j = 0; (j<qbs.length) && (i<n); j++){
			//qb[i] = RealQSender.mapToQuBit(qbs[j]);
			qb[i] = BB84QuBit.convert(qbs[j]);
			if(_ic != null){
			  qb[i].sendMe(_ic);
			}
			if(_bar != null){
			    _bar.setValue(i);
			}
			i++;
		  }
		}
		_dgc.getOutputStream().write((int)Constants.STOP);
	    }
    	}catch(IOException io){
	    throw new TimeOutException("IO/ERR-RealQReceiver:"+io.getMessage());
	}finally{
	    qbs = null;
	}
	return qb;
    }
    
    
   /**
    * This method is used to read qubits. They are not returned to the
    * server but automatically forward to the responder client. 
    * The connection with the client must be set and ready.
    * @param n is the number of qubit to be read.
    * @param bytecode is the bytecode for this transmission.
    * This is ignored for the time being as all supported transmission
    * use the same coding for BB84 states.
    */
    public void readAndForward(byte bytecode, int n) throws TimeOutException, QuBitFormatException{
	if((bytecode != Constants.BB84) && (bytecode != Constants.B92)){
	    throw new QuBitFormatException("bytecode for q-transmission is unknown");
	}
	//BB84QuBit[] qb = null;
	byte[] qbs = null;
	_numberOfBucket = 0;
	if(_bar != null){_bar.setMaximum(n-1);_bar.setValue(0);}
	//_bucketSize = _spc.receiveInt();
	//System.out.println("The bucket size for the resp server:"+_bucketSize);
	//if((_bucketSize>0)&&(_bucketSize < Constants.MAX_BUCKET_SIZE+1)){ 
	//    _spc.sendByte(Constants.OK);
	//}else{
	//    _spc.sendByte(Constants.ERROR);
	//    throw new QuBitFormatException("Couldn't agree on the bucket size.");
	//}
	try{
	    _dgc.getOutputStream().write(Constants.BB84);
	    Constants.sendSpecialInt(_bucketSize, _dgc.getOutputStream());
	    int intro = _dgc.getInputStream().read();
	    if((byte)intro == Constants.OK){
		//qb = new BB84QuBit[n];	
		int i = 0;
		while(i<n){
		  qbs = read();
		  _numberOfBucket++;
		  for(int j = 0; (j<qbs.length) && (i<n); j++){
			//qb[i] = RealQSender.mapToQuBit(qbs[j]);
			//qb[i] = BB84QuBit.convert(qbs[j]);
			//qb[i].sendMe(_ic);
			BB84QuBit.sendIt(qbs[j],_ic);
			if(_bar != null){
			    _bar.setValue(i);
			}
			i++;
		  }
		}
		_dgc.getOutputStream().write((int)Constants.STOP);
	    }
    	}catch(IOException io){
	    throw new TimeOutException("IO/ERR-RealQReceiver:"+io.getMessage());
	}finally{
	    qbs = null;
	}
    }
	
    
    
   /**
    * Sets the configuration of an instance according to the one
    * define in an hashtable. The hashtable can contain configurations
    * that apply to other cases. Two fields related to this class can be found
    * in the hashtable. 1) the port number for communication with the
    * DG and 2) the bucket size for the quantum transmission.
    * If the server DG connection was not already working then a thread 
    * listening for
    * connection with the DG is launched. No exception is thrown whenever 
    * the server of the dg connection couldn't be launched. 
    * @param h is an hashtable containing some configuration keys and values.
    */
    
    public byte appliesConfig(@SuppressWarnings("rawtypes") Hashtable h){
	byte output = Constants.OK;
	if(h != null){
	    Object o = h.get(DG_PORT_NUMBER_KEY);
	    if(o != null){
		_portDG = ((Integer)o).intValue();
	    }
	    o = h.get(DG_IP_ADDRESS_KEY);
	    if(o != null){
		_DGIP = (String)o;
	    }
	    try{
	       _dgc = new DGConnection(InetAddress.getByName(_DGIP), _portDG);
	    }catch(IOException io){
	       if(_spc != null){_spc.closeConnection();}
	       output = Constants.ERROR;
	       QryptoWarning.warning("Couldn't connect to the DG server...\n"+
	                             "at "+_DGIP+" port "+_portDG,"RealQReceiver",null);
	    }	 
	}else{
	    output = Constants.ERROR;
	    QryptoWarning.warning("Received an empty config table","RealQReceiver",null);
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
       String stringIPDG=Constants.DEF_RESP_DG_IP;
       String stringDG = new Integer(Constants.DEF_PORT_REC_DG).toString();
       Object o = h.get(DG_PORT_NUMBER_KEY);
       if(o != null){
	    stringDG = ((Integer)o).toString();
       }
       o = h.get(DG_IP_ADDRESS_KEY);
       if(o != null){
	    stringIPDG = (String)o;
       }
       Object[] message = new Object[4];
       message[0] = "Select the DG IP address:";
       message[1] = new JTextField(stringIPDG);
       message[2] = "Select the DG port number:";
       message[3] = new JTextField(stringDG);
       String[] options = {"OK","CANCEL"};
       try{
	 int res = JOptionPane.showOptionDialog(owner,message,
				    "Responder real Q-transmission settings",
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
	       throw new NumberFormatException("DG port value out of bound");
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
    

}
