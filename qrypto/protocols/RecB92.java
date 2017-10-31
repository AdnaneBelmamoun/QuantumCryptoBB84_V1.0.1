package qrypto.protocols;

import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;
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
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.SocketPubConnection;



public class RecB92 extends PlainB92
{


    private String INITLOGFILENAME = Constants.RECB92SENDER;
    private String RESPLOGFILENAME = Constants.RECB92RECEIVER;
    
    public static final int _DEF_CONF_SIZE = 10;
    
    private static final String _CLASSNAME = "RecB92";
    public  static final String PROT_ID = "Reconciled B92";
    private static final String _TOL_ERROR_LABEL = "Tolerated error-rate: ";
    private static final String _CONF_LABEL =      "Size of confirm sample: ";
    private JTextField _tfConf  = null;
    private JTextField _tfError = null; //the text field for the tolerated error-rate.
    
    //private boolean[] _rawBits = null;
    
    protected float _toleratedError = 0.15f;
    protected int _confSize = _DEF_CONF_SIZE;
    protected Cascade _cascade = null;
    protected Confirm _confirm = null;


   public RecB92(){
	super();
	newProtID(PROT_ID);
	_tfError = new JTextField(7);
	_tfConf  = new JTextField(5);
	_cascade = new Cascade();
   }
   
   /**
    * Constructor.
    * @param rqc is the remote quantum connection
    * @param pc is the public connection between the parties.
    */
    
    public RecB92(RemoteQConnection rqc, PubConnection pc){
	this();
	_qc = rqc;
	_pc = pc;
    }
    
  /*
  * Returns name of this class which is also the name 
  * of the file containing the template for HTMl generation.
  * @return the name of this filename.
  */
  
  public String  protFileName(){
    return _CLASSNAME;
  }
   
 
  
  /**
  * Sets the tolerated error rate before executing the reconciliation procedure.
  * If the observed error-rate is larger thann the tolerated one then reconciliation
  * is not performed. 
  * @param newrate is the new tolerated error-rate. To be effective the value of rate
  * must be positive and smaller than 1/2.
  * @return true iff newrate was correctly set.
  */
  
  public boolean setToleratedErrorRate(float newrate){
    boolean r = false;
    if((newrate < 0.5f)&&(Math.abs(newrate)==newrate)){
	_toleratedError = newrate;
	r = true;
    }
    return r;
  }	
  
  
  /**
  * Sets the number of subsets of positions used by confirm to 
  * confirm the equality of Alice and Bob reconcilied strings.
  * @param size is the new number of subset.
  * @see qrypto.protocols.Confirm
  */
  
  public void setConfirmSize(int size){
    _confSize = size;
  }
  
  /**
  * Returns the number of subsets usede to confirm Alice and Bob reconciled
  * strings.
  * @return the number of subsets.
  */
  
  public int getConfirmSize(){
    return _confSize;
  }
  

    
    
 /**
  * This runs the initiator if me is initiator and runs the responder otherwise.
  */
  
  
  public void run() throws RuntimeQryptoException{
    try{
      String s = INITLOGFILENAME;
      if(!_initiator){s = RESPLOGFILENAME;}
      _logfile = new Log(s, true, "RecB92");
      _logfile.header();
      if(_initiator){
 	initiator();
      }else{
	responder();
      }
    }catch(QryptoException qe){
	throw new RuntimeQryptoException(qe.getMessage());
    }catch(IOException io){
	throw new RuntimeQryptoException("Couldn't open the logfile for RecB92");
    }
  }
  
  
  /**
  * This is the initiator part for RecB92. First, PlainB92 is executed and
  * then the reconciliation protocol Cascade.
  * @throw QryptoException whenever a problem occurs.
  */
  
  public void initiator()  throws QryptoException{
    super.initiator();
    _cascade.reset();//makes sure that all internal data are reset before a new exec. 
    float e = getNoiseEstimate().estimate();
    boolean[] plainkey = getOutputBits();
    _cascade.setParam(plainkey,RecBB84.choosePassNumber(e,plainkey.length,getNoiseEstimate()),RecBB84.chooseBlockSize(e,getNoiseEstimate().interval(0.8),plainkey.length));
    if(errorRateIsOK()){
	_cascade.setPubConnection(_pc);
	_cascade.setProgressTools(_bar, _progressLabel);
	_cascade.initiator();
	_confirm = new Confirm(_cascade.getReconciliedKey(),getConfirmSize());
	_confirm.setPubConnection(_pc);
	_confirm.setProgressTools(_bar,_progressLabel);
	_confirm.initiator();
    }else{
	throw new QryptoException("Error-rate exceeds the tolerated rate,\n"+
				  "execution aborted before reconciliation.");
    }
  }
  
  
  /**
  * This is the responder part for RecB92. First, PlainB92 is executed and
  * then the reconciliation protocol Cascade.
  * @throw QryptoException whenever a problem occurs.
  */
  
  public void responder()  throws QryptoException{
    super.responder();
    _cascade.reset();//makes sure that all internal adata are reset before a new exec
    float e = getNoiseEstimate().estimate();
    boolean[] plainkey = getOutputBits();
    _cascade.setParam(plainkey,RecBB84.choosePassNumber(e,plainkey.length,getNoiseEstimate()),RecBB84.chooseBlockSize(e,getNoiseEstimate().interval(0.8),plainkey.length));
    if(errorRateIsOK()){
	_cascade.setPubConnection(_pc);
	_cascade.setProgressTools(_bar,_progressLabel);
	_cascade.responder();
	_confirm = new Confirm(_cascade.getReconciliedKey(),getConfirmSize());
	_confirm.setPubConnection(_pc);
	_confirm.setProgressTools(_bar,_progressLabel);
	_confirm.responder();
    }else{
	throw new QryptoException("Observed error-rate exceeds the tolerated rate, "+
				  "execution aborted before reconciliation.");
    }
  }
  
  /**
   * Print the output of the protocol for reading. Not implemented.
   */

  public void output(){
  
  }
  
  
  
 /**
  * This adds the objects to be plugged in the html template for
  * this protocol. Should be called after the execution is completed.
  * The holes to be added are in addition to thos of RecBB84 are:
  * myid,peerid,errorestimate, toleratederrror, blocksize, passes,results bits.
  * @param ht is the hashtable where to add the holee for that protocol
  * @param instanceNumber is the instance number of this subprotocol with
  * respect to the surrounding protocol.
  */
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public void addMyHoles(Hashtable ht, int instanceNumber){
    super.addMyHoles(ht,1);
    argElm a = null;
    toHtml[] o = new toHtml[15];
    o[0] = new toHtmlString("myid",myID());
    o[1] = new toHtmlString("peerid",peerID());
    o[2] = new toHtmlString("errorestimate","0.0");
    o[3] = new toHtmlString("toleratederror",Float.toString(_toleratedError));
    o[4] = new toHtmlInt("blocksize",0);
    o[5] = new toHtmlInt("passes",0);
    o[6] = new toHtmlString("results","<FONT COLOR=red><BLINK><b>Undefined</b></BLINK></FONT>");
    o[7] = new toHtmlString("bits","<FONT COLOR=red><b>Cascade not executed</b></FONT>");
    o[8] = new toHtmlString("confirm","<FONT COLOR=red><b>Confirm not executed</b></FONT>" );
    o[9] = new toHtmlInt("totdisclosed",0);
    o[10]= new toHtmlInt("initialSize",0);
    o[11]= new toHtmlString("observederror","0.0");
    o[12]= new toHtmlString("rate","0.0");
    o[13]= new toHtmlString("shannon","0.0");
    o[14]= new toHtmlString("goodprob","0.0");
    if(getNoiseEstimate() != null){
	o[2] = new toHtmlString("errorestimate",Float.toString(getNoiseEstimate().estimate()));
    }
    if((_cascade != null) && (_confirm != null)){
	String observedErrorRate = Float.toString(_cascade.obsErrorRate());
	int totaldisclosed = _cascade.disclosed()+_confirm.getSampleSize();
	int nn = getOutputBits().length;
	float rate  = new Integer(nn-totaldisclosed).floatValue()/new Integer(nn).floatValue(); 
	o[4] = new toHtmlInt("blocksize",_cascade.getInitialBlockSize());
	o[5] = new toHtmlInt("passes",_cascade.getNumberOfPasses());
	o[6] = new toHtmlString("results",resultInfo2HTML());
	o[7] = new toHtmlString("bits",resultBits2HTML());
	o[8] = new toHtmlString("confirm", _confirm.result2HTML(myID(),peerID()));
	o[9] = new toHtmlInt("totdisclosed",totaldisclosed);
	o[10]= new toHtmlInt("initialSize",nn);
	o[11]= new toHtmlString("observederror",observedErrorRate);
	o[12]= new toHtmlString("rate",Float.toString(rate));
	o[13]= new toHtmlString("shannon",Float.toString((nn-Cascade.shannonBound(_cascade.obsErrorRate(),nn))/nn));
	if(_confirm.isOK()){
	     o[14]= new toHtmlString("goodprob",Float.toString(1f-_confirm.errorProb()));
	}else{
	     o[14]= new toHtmlString("goodprob","<FONT COLOR=red><BLINK>0.0</BLINK></FONT>");
	}
    }    
    a = new argElm(RecB92._CLASSNAME+"+"+instanceNumber,instanceNumber,null, o);
    ht.put(RecB92._CLASSNAME+"+"+instanceNumber,a);
  }
  
  
 /**
  * Returns whether or not the error estimate was below
  * the tolerated error-rate.
  * @return true iff the estimate < toleratedError rate .
  */
  
  public boolean errorRateIsOK(){
    return (getNoiseEstimate().estimate() < (_toleratedError+0.001f));
  }
  
  
  /**
  * Outputs an html string giving information about the current
  * execution.
  * @return the HTML encoding of information.
  */
  private String resultInfo2HTML(){
    StringBuffer s = new StringBuffer();
    if(errorRateIsOK()){
	s.append(_cascade.result2HTML());
    }else{
      s.append("<br><br><b><CENTER><LARGE>Execution aborted:noiselevel too high</LARGE></CENTER></b>");
    }
    return s.toString();
  }
  
  /**
  * Outputs the resulting bits with information about which were corrected.
  * @return the html string.
  */
  
  private String resultBits2HTML(){
    StringBuffer sb = new StringBuffer();
    if(errorRateIsOK()){
       sb.append(_cascade.resultBits2HTML());
    }else{
       sb.append("<b><CENTER><FONT color=red>No reconciled key produced</FONT>:Too much noise observed...<br>\n");
       sb.append("<em>(execution aborted)</em></b>\n");
    }
    return sb.toString();
  }  
 
  
  
 /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream si the output stream.
  */
  
  public void output(PrintWriter stream){
   // super.output(stream);
   // _cascade.output(stream);
   // _confirm.output(stream);
   // stream.println("The reconciled key prior privacy amplification is:");
   // Constants.printBoolArray(_cascade.getReconciliedKey(),stream,B92QuBit.BIT0);
   // stream.println("********************************************");
  }
  
   /**
    * Allows the user to configure the quantum transmission.
    * This means to select the length of the quantum transmission,
    * the size of the sample for error-rate estimation.
    * @param parent is the component that owns the message dialog.
    */
    
    public void configure(Component parent){
	Object[] o = addMyConf();
	int r = JOptionPane.showOptionDialog(parent,o,"Reconciled B92 Config", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null, _options,
				_options[_OK_OPTION]);
	//parent.repaint();
	if(r==0){
	    appliesConf(parent);
	}else{
	    JOptionPane.showMessageDialog(parent,
				"Values    n="+Integer.toString(_n)+
				"\n Sample size=" + Integer.toString(_sampleSize) +
				"\n Tolerated error-rate="+Float.toString(_toleratedError)+
				//"\n Number of passes="+Integer.toString(_cascade.getNumberOfPasses())+
				//"\n Initial block size="+Integer.toString(_cascade.getInitialBlockSize())+
				" used.",
				"Ok",
			        JOptionPane.ERROR_MESSAGE);
	    // parent.repaint();
	}
	
    }
    
    
   /**
    * Generates the configuration fields for that protocol.
    * @return the field from rawBB84 + the size of the sample.
    */
    
    public Object[] addMyConf(){
	Object[] o0 = super.addMyConf();
	//Object[] o1 = _cascade.addMyConf();
	int l = o0.length;//+o1.length;
	Object[] o = new Object[l + 2];
	for(int i = 0; i<o0.length; i++){
	    o[i] = o0[i];
	}
	JPanel panel = new JPanel();
	BoxLayout bl = new BoxLayout(panel, BoxLayout.X_AXIS);
	panel.setLayout(bl);
	JLabel lab = new JLabel(_TOL_ERROR_LABEL);
	_tfError.setText(Float.toString(_toleratedError));
	panel.add(lab);
	panel.add(_tfError);
	o[l] = panel;
	//for(int i = o0.length+1; i<l+1;i++){
	//    o[i] = o1[i-o0.length-1];
	//}
	JPanel panel2 = new JPanel();
	BoxLayout bl2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
	panel2.setLayout(bl2);
	JLabel lab2 = new JLabel(_CONF_LABEL);
	_tfConf.setText(Integer.toString(getConfirmSize()));
	panel2.add(lab2);
	panel2.add(_tfConf);
	o[l+1] = panel2;
	return o;
     }
    
    /**
    * Applies the configuration set by the user.
    * @param parent is the parent frame for the dialog.
    * @return true iff all configurations were corectly set. Otherwise
    * the default values are used.
    */
    
    public boolean appliesConf(Component parent){
       boolean ok = super.appliesConf(parent);
       //ok = ok && _cascade.appliesConf(parent);
       try{
	    Float iv = Float.valueOf(_tfError.getText());
	    float zozo = iv.floatValue();
	    if((zozo<0.5f)&&(Math.abs(zozo)== zozo)){
		setToleratedErrorRate(zozo);
	    }else{//Il faut Traiter cette erreur separemment....
		throw new NumberFormatException("Value out of range [0..0.5]");
	    }
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad value for tolerated error-rate, value "+Float.toString(_toleratedError)+" remains.",
				"Config Error",
			    JOptionPane.ERROR_MESSAGE);
	     ok = false;
	     //parent.repaint();
	}
	try{
	    Integer iv = Integer.valueOf(_tfConf.getText());
	    int zozo = iv.intValue();
	    if(zozo>-1){
		setConfirmSize(zozo);
	    }else{//Il faut Traiter cette erreur separemment....
		throw new NumberFormatException("Value out of range");
	    }
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad value for confirm size, value "+Integer.toString(getConfirmSize())+" remains.",
				"Config Error",
			    JOptionPane.ERROR_MESSAGE);
	     ok = false;
	     //parent.repaint();
	}
	return ok;
    }
    
    
   /**
    * Displays  the settings to the user.
    * @param owner is the parent pane of this option pane.
    * 
    */
    
    public void showSettings(Component owner){
	JOptionPane.showMessageDialog(owner,
				settingString(),
				"Reconciled B92 Settings",
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
	//ssc.sendString(myID());
	//ssc.sendInt(_n);
	//ssc.sendInt(_sampleSize);
	//ssc.sendString(Float.toString(_toleratedError));
	//ssc.sendInt(_cascade.getNumberOfPasses());
	//ssc.sendInt(_cascade.getInitialBlockSize());
	//ssc.sendInt(getConfirmSize());
	sendOnlyLocalConfig(ssc);
	try{
	    byte answer = ssc.receiveByte();
	    if(answer == Constants.OK){
		output = true;
	    }else{
	        owner.addToLogin("Settings refused by the peer...");
	    }
	}catch(TimeOutException to){
	       owner.addToLogin("BAD TRIP");
	}
	return output;
    }
    
   /**
    * Sends the configuration without waiting the acknowledgment from the
    * receiver. It does not send the protocol ID neither.
    * This is used by a subclass of this protocol to configure this superclass.
    * @param ssc is the connection.
    */
    
    public void sendOnlyLocalConfig(ServerSocketConnection ssc){
	ssc.sendString(myID());
	ssc.sendInt(_n);
	ssc.sendString(Float.toString(_sampleRatio));
	ssc.sendString(Float.toString(_toleratedError));
	//ssc.sendInt(_cascade.getNumberOfPasses());
	//ssc.sendInt(_cascade.getInitialBlockSize());
	ssc.sendInt(getConfirmSize());	
    }
    
    
    /**
    * Receives the configuration from the initiator server.
    * @param pc is the public connection between the parties.
    * @return true if the received configuration could be understood.
    * Error handling should be improved. Now, no error is signaled to the user.
    */
    
    protected boolean receiveConfig(SocketPubConnection pc){
       boolean answer = false; 
       try{
	    String peer = pc.receiveString();
	    setPeerID(peer,true);
	    _n = pc.receiveInt();
	    _sampleRatio = Float.valueOf(pc.receiveString()).floatValue();
	    _toleratedError = (Float.valueOf(pc.receiveString())).floatValue();
	 //   int passes = pc.receiveInt();
	 //   int blocksize = pc.receiveInt();
	 //   _cascade.setParam(passes, blocksize);
	    int confSize = pc.receiveInt();
	    setConfirmSize(confSize);
	    answer = true;
	}catch(TimeOutException to){
	}catch(NumberFormatException nfe){
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
	 String message = "Protocol: "+protID()+"\n";
	 if(peerID() != null){message = message + "Peer identity: "+peerID()+"\n";}
	 message = message + "Size of the raw key: "+Integer.toString(_n)+"\n";
	 message = message + "Proportion for error sample: "+Float.toString(_sampleRatio)+"\n";
	 message = message + "Tolerated error-rate:"+Float.toString(_toleratedError)+"\n";
	 //message = message + "Number of passes:"+Integer.toString(_cascade.getNumberOfPasses())+"\n";
	 //message = message + "Initial blocksize:"+Integer.toString(_cascade.getInitialBlockSize())+"\n";
	 message = message + "Confirm size:"+Integer.toString(getConfirmSize())+"\n";
	 return message;
    } 
    
  
  


}
