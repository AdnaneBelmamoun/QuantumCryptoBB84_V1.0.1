package qrypto.protocols;

import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import qrypto.exception.QryptoException;
import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;
import qrypto.gui.InitPlayer;
import qrypto.htmlgenerator.argElm;
import qrypto.htmlgenerator.toHtml;
import qrypto.htmlgenerator.toHtmlInt;
import qrypto.htmlgenerator.toHtmlString;
import qrypto.log.Log;
import qrypto.qommunication.B92Qoding;
import qrypto.qommunication.B92QuBit;
import qrypto.qommunication.BB84QuBit;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;
import qrypto.qommunication.QuBit;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.SocketPubConnection;




public class RawB92 extends QProtocol implements Runnable
{
    private String INITLOGFILENAME = Constants.RAWB92SENDER;
    private String RESPLOGFILENAME = Constants.RAWB92RECEIVER;
    
    private static final String _CLASSNAME = "RawB92";
    public  static final int DEFAULT_N = 500;
    private static final String _CONF_LABEL = "Transmission length: ";
    public  static final String PROT_ID = "Raw B92 transmission";


    protected B92Qoding _b92 = null;
    
    
    protected long _startTime = 0;
    protected long _endTime = 0;
    protected int _n = 0;
    private JTextField _tf = null;
    
    
    
    /**
    * Empty constructor.
    */
    
    public RawB92(){
	_n = DEFAULT_N;
	newProtID(PROT_ID);
	_tf = new JTextField(7);
    }
    
    
    /**
    * Constructor.
    * @param rqc is the remote quantum connection
    * @param pc is the public connection between the parties.
    */
    
    public RawB92(RemoteQConnection rqc, PubConnection pc){
	this();
	_qc = rqc;
	_pc = pc;
    }
    
    /**
    * Constructor of a new RawBB84 transmission on initiator side.
    * @param rqc is the remote quantum connection.
    * @param pc is the public connection between the players
    * @param me is the initiator identity
    * @param n is the number of qubits to be exchanged.
    * @param peer is the responder identity
    * @param initiator is true of me is initiator, false otherwise.
    */
    public RawB92(String me, String peer,int n,
		   RemoteQConnection rqc, PubConnection pc, boolean initiator){
	this(rqc,pc);
	setID(me,peer,initiator);
	_pc = pc;
	_qc = rqc;
	_n = n;
    }    
    
    
 /**
  * Returns the filename for that protocol and which is also the 
  * directory name of where the HTML templates can be found.
  * @return "RawB92"
  */
  
  public String protFileName(){
    return _CLASSNAME;
  }
  
  /**
  * Returns the number of raw qubits.
  * @return the number of qubits.
  */
  
  public int getRawKeySize(){
    return _n;
  }

  /**
  * This runs the initiator if me is initiator and runs the responder otherwise.
  */
  
  
  public void run() throws RuntimeQryptoException{
   try{
	String s = INITLOGFILENAME;
	if(!_initiator){s = RESPLOGFILENAME;}
	_logfile = new Log(s, true, "RawB92");
	_logfile.header();
	runningPatch(_initiator);
    }catch(QryptoException qe){
	throw new RuntimeQryptoException(qe.getMessage());
    }catch(IOException io){
	throw new RuntimeQryptoException("Couldn't open the logfile for rawB92");
    }
  }
    
    
  /**
   * This implements the initiator's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void initiator() throws QryptoException{
   runningPatch(true);
  }

  /**
   * This implements the responder's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void responder() throws QryptoException{
   runningPatch(false);
  }
  
  
  /**
  * Patch for addresing both the initiator and responder algortihms.
  * @param init is true for the initiator side.
  */
  
  private void runningPatch(boolean init)throws QryptoException{
    if(_n < 1){throw new QryptoException("Protocol aborted: parameter n < 1.");}
    if(_me == null){throw new QryptoException("Protocol aborted:my identity is unknown.");}
    if(_peer == null){throw new QryptoException("Protocol aborted:peer identity is unknown.");}
    _b92 = new B92Qoding(_n,_me,_peer,init,_logfile);
    _qc.setProc(_b92,init);
    _qc.setProgressTools(_bar,_progressLabel);
    _startTime = System.currentTimeMillis();
    _qc.run();
    _endTime = System.currentTimeMillis();
    if(!_qc.success()){
	throw new QryptoException("Quantum Transmission aborted. Weird termination...Ask for your money back!");
    }
  }
  
  
  /**
  * Returns the output of the raw bb84 quantum transmission.
  * @returns the array of qubits or null if no output has been produced yet.
  */
  
  public BB84QuBit[] getOutput(){
    return BB84QuBit.convert(_qc.getResult());
  }


  /**
   * Print the output of the protocol for reading. Not implemented.
   */

  public void output(){
  
  }
  
 /**
  * This adds the objects to be plugged in the html template for
  * this protocol. Should be called after the execution is completed.
  * The holes to be added are:
  * qType,myid,myid,length,timeSlotNumber,detected,time,rate,qubits
  * @param ht is the hashtable where to add the holee for that protocol
  * @param instanceNumber is the instance number for that subprotocol
  * with respect to the surrounding protocol.
  */
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public void addMyHoles(Hashtable ht,int instanceNumber){  
    argElm a = null;
    toHtml[] o = new toHtml[10];
    String s = null;
    switch(_b92.getQType()){
	case Constants.VIRTUAL: s="VIRTUAL"; break;
	case Constants.REAL   : s="REAL"; break;
	default               : s="UNDEFINED";
    }
    int bs = _b92.getBucketSize();
    int nb = _b92.getNumberOfBuckets();
    float rate = new Integer(bs*nb).floatValue()/new Integer(_n).floatValue();
    o[0] = new toHtmlString("qType","<b>"+s+"</b>");
    s = _initiator?"<EM> (sender)</EM>":"<EM> (receiver)</EM>";
    o[1] = new toHtmlString("myid",_me+s);
    s = _initiator?"<EM> (receiver)</EM>":"<EM> (sender)</EM>";
    o[2] = new toHtmlString("peerid",_peer+s);
    o[3] = new toHtmlString("qserverIP",_qc.qServerIPAddress().toString());
    o[4] = new toHtmlInt("qserverPort",_qc.qServerPort());
    o[5] = new toHtmlInt("length",_n);
    o[6] = new toHtmlInt("timeSlotNumber",bs*nb);
    o[7] = new toHtmlString("time",new Float(RawBB84.duration(_startTime,_endTime)).toString());
    o[8] = new toHtmlString("rate",Float.toString(rate).toString());
    if(this._initiator){
	o[9] = new toHtmlString("qubits",QuBit.array2Html(B92QuBit.convertFromBB84(getOutput()),10,5));
    }else{
	o[9] = new toHtmlString("qubits",QuBit.array2Html(B92QuBit.convertFromMeasurement(getOutput()),10,5));  
    }
    a = new argElm(RawB92._CLASSNAME+"+"+instanceNumber,instanceNumber,null, o);
    ht.put(RawB92._CLASSNAME+"+"+instanceNumber,a);
  }
  
  
  
  /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream si the output stream.
  */
  
  public void output(PrintWriter stream){
    BB84QuBit[] _bb84output = null;
    String initID = _me;
    if(!_initiator){initID  = _peer;}
    stream.println("**************"+PROTID+" Output*****************");
    stream.println(new Date().toString());
    stream.println("*****************************************************");
    stream.println("Transmission of length:"+_n);
    stream.println("My identity:"+_me);
    stream.println("Peer identity:"+_peer);
    stream.println("Initiator:"+initID);
    stream.println("Output:");
    _bb84output = getOutput();
    for(int i = 0; i< _bb84output.length; i++){
	stream.print(_bb84output[i].toString()+"  ");
	//the following abs() are there because of uncompatibility 
	//with Windows Math.IEEremainder().
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    stream.println();
	}
	if(Math.abs(Math.IEEEremainder(i+1,25.0d))<0.5d){
	    stream.println();
	}
    }
    stream.println("******************************************************");
  }
  
    /**
    * Allows the user to configure the quantum transmission.
    * This means to select the length of the quantum transmission.
    */
    
    public void configure(Component parent){
	Object[] o = addMyConf();
	int r = JOptionPane.showOptionDialog(parent,o,"Raw BB84 Config", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null, _options,
				_options[_OK_OPTION]);
	//parent.repaint();
	if(r==0){
	    appliesConf(parent);
	}else{
	    JOptionPane.showMessageDialog(parent,
				"Default value n="+Integer.toString(_n)+" remains.",
				"Ok",
			    JOptionPane.ERROR_MESSAGE);
	     //parent.repaint();
	}
    }
    
    
    /**
    * Add configuration components into a modal frame. Used by superprotocols
    * to configure this subprotocol into the same frame. 
    */
    
    public Object[] addMyConf(){
	Object[] o = new Object[1];
	JPanel panel = new JPanel();
	BoxLayout bl = new BoxLayout(panel,BoxLayout.X_AXIS);
	panel.setLayout(bl);
	JLabel lab = new JLabel(_CONF_LABEL);
	_tf.setText(Integer.toString(_n));
	panel.add(lab);
	panel.add(_tf);
	o[0] = panel;
	return o;
    }
    
    /**
    * Extracts the configuration whenever the user press OK.
    * @param parent is the parent component.
    * @return true if the new value are accepted.
    */
    public boolean appliesConf(Component parent){
	int ans = -1;
	try{
		Integer iv = Integer.valueOf(_tf.getText());
		ans = iv.intValue();
	}catch(NumberFormatException nfe){
		ans = -1;
	}
	if(ans<1){
	     JOptionPane.showMessageDialog(parent,
				"Bad value setting, n="+Integer.toString(_n)+" remains.",
				"Config Error",
			    JOptionPane.ERROR_MESSAGE);
	     //parent.repaint();
	}else{
	    _n = ans;
	}
	return (ans > 0);
    }
    
    
   /**
    * Displays  the settings to the user.
    * @param owner is the parent pane of this option pane.
    * 
    */
    
    public void showSettings(Component owner){
	JOptionPane.showMessageDialog(owner,
				settingString(),
				"Raw B92 Settings",
				JOptionPane.INFORMATION_MESSAGE);
	//owner.repaint();
    }
    
    
    /**
    * Sends to the responder the configuration of this protocol.
    * @param ssc is the public connection
    * @return true iff the configuration has been accepted.
    */
    
    public boolean sendConfig(ServerSocketConnection ssc, InitPlayer owner){
	boolean output = false;
	ssc.sendString(PROT_ID);
	ssc.sendString(myID());
	ssc.sendInt(_n);
	try{
	    byte answer = ssc.receiveByte();
	    if(answer == Constants.OK){
		output = true;
	    }else{
	        owner.addToLogin("Settings refused...");
	    }
	}catch(TimeOutException to){
	       owner.addToLogin("BAD TRIP");
	}
	return output;
    }
    
    /**
    * Receives the configuration from the initiator server.
    * @param pc is the public connection between the parties.
    * @return true if the responder accepts the settings.
    */
    
    protected boolean receiveConfig(SocketPubConnection pc){
       boolean answer = false; 
       try{
	    String peer = pc.receiveString();
	    setPeerID(peer,true);
	    _n = pc.receiveInt();
	    answer = true;
	}catch(TimeOutException to){
	}
	return answer;
    }
    
    /**
    * Allows the user to confirm the actual settings.
    * @param owner is the parent component.
    * @return true iff the settings are accepted.
    */
    
    public boolean confirmSettings(Component owner,SocketPubConnection pc){
	 int answ= JOptionPane.showConfirmDialog(owner,
				"Do you accept the following settings? \n"+settingString(),
				"Confirming settings",
				JOptionPane.YES_NO_OPTION);
	//owner.repaint();
	if(answ == 0){pc.sendByte(Constants.OK);}
	else{pc.sendByte(Constants.ERROR);}
	return (answ == 0);
    }
    
    /**
    * Returns the string showing the settings.
    * @return the settings.
    */
    
    private String settingString(){
	 String message =                         "Protocol: "+protID()+"\n";
	 if(peerID() != null){message = message + "Peer identity: "+peerID()+"\n";}
	 if(myID() != null){message = message + "Size of transmission: "+Integer.toString(_n)+".";}
	 return message;
    }
    

}
