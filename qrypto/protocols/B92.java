package qrypto.protocols;


import java.awt.Component;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import qrypto.exception.QryptoException;
import qrypto.exception.QryptoWarning;
import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;
import qrypto.gui.InitPlayer;
import qrypto.htmlgenerator.argElm;
import qrypto.htmlgenerator.toHtml;
import qrypto.htmlgenerator.toHtmlInt;
import qrypto.htmlgenerator.toHtmlString;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.SocketPubConnection;




public class B92 extends RecB92
{

    public  static final String PROT_ID = "B92 protocol";
    private static final String _CLASSNAME = "B92";
    private static final String _SEC_PARAM = "Security parameter:";
    private JTextField _tfsecParam = null; //the textfield for the security parameter
    
    private boolean[] _recBits = null;
    private PrivacyAmplification _pa = null;
    private int _totalDisclosedBits = 0;
    private int _finalLength = 0;
    private JCheckBox _sendSecretMessage = null;
    //The security parameter is an extra shrinking parameter
    //c between 0 and 1 that the size of the final key is
    // f = (1-c)*(n-total_disclosed)
    private double _securityParameter = 0.05;
    private String _cleartext = "Undefined";
    private String _ciphertext= "Undefined";
    private String _keytext="Undefined";
    
    
    
    
   /**
    * Empty constructor.
    */
    
   public B92(){
	super();
	newProtID(PROT_ID);
	_tfsecParam = new JTextField(7);
	_sendSecretMessage = new JCheckBox("Terminates with secret transmission");
	_sendSecretMessage.setSelected(false);
 
   }
   
   /**
    * Constructor.
    * @param rqc is the remote quantum connection
    * @param pc is the public connection between the parties.
    */
    
    public B92(RemoteQConnection rqc, PubConnection pc){
	this();
	_qc = rqc;
	_pc = pc;
    }
    
    
	
  /**
  * Returns name of this class which is also the name 
  * of the file containing the template for HTMl generation.
  * @return the name of this filename.
  */
  
  public String  protFileName(){
    return _CLASSNAME;
  }
  
  /**
  * Sets the security parameter. That is a value 0<=c<1 such that
  * the final key will be of length f = (1-c)*(n-total_disclosed_bit)
  * @param c is the new value for the security paramater in [0..1[.
  *        if the security parameter is not in the intervall then the old
  *        value remains.
  */
  
  public void setSecurityParameter(double c){
    if((c<1.0d)&&(Math.abs(c)== c)){
	_securityParameter = c;
    }
  }
   
  

    
 /**
  * This runs the initiator if me is initiator and runs the responder otherwise.
  */
  
  
  public void run() throws RuntimeQryptoException{
    try{
      if(_initiator){
 	initiator();
      }else{
	responder();
      }
    }catch(QryptoException qe){
	throw new RuntimeQryptoException(qe.getMessage());
    }
  }
  
  
  /**
  * This is the initiator part for RecBB84. First, PlainBB84 is executed,
  * then the reconciliation protocol Cascade + cofirmation, and finally
  * privacy amplification.
  * @throw QryptoException whenever a problem occurs.
  */
  
  public void initiator()  throws QryptoException{
   _totalDisclosedBits = 0;
   _finalLength = 0;
   try{
    super.initiator();
    if(!_confirm.isOK()){throw new QryptoException("Confirmation found errors:\n"+
						   "reconciliation failed!");} 
    _totalDisclosedBits = _cascade.disclosed()+_confirm.getSampleSize();
    _recBits = _cascade.getReconciliedKey();
    if(_recBits != null){
	_finalLength = finalLength(_recBits.length,_totalDisclosedBits,_securityParameter);
    }
    if(_finalLength < 1){throw new QryptoException("Not enough reconciled bits ("+_recBits.length+") for privacy amplification");}
    _pa = new PrivacyAmplification(_cascade.getReconciliedKey(),_finalLength);
    _pa.setPubConnection(_pc);
    _pa.setProgressTools(_bar,_progressLabel);
    _pa.initiator();
    if(_sendSecretMessage.isSelected() && (_finalLength/BB84.BYTE_2_STRING_L > 0) ){
      String[] sr = BB84.sendSecretMessage(getFinalKey(),_pc, _parent);
      _cleartext = sr[0];
      _keytext = sr[1];
      _ciphertext = sr[2];
    }
   }catch(QryptoException qe){
      QryptoWarning.warning("la clé finale n'as pas pû étre généré \n"+
			    qe.getMessage(),"B92 Arrété (initiateur)",null);
      //throw new QryptoException("BB84 execution stopped!");
   }catch(RuntimeException rt){
      throw new RuntimeException("Crash du B92 du coté de l'initiateur...");
   }
  }
  
  
  /**
  * This is the responder part for RecBB84. First, PlainBB84 is executed,
  * then the reconciliation protocol Cascade+confirmation, and finally
  * privacy amplification.
  * @throw QryptoException whenever a problem occurs.
  */
  
  public void responder()  throws QryptoException{
   _totalDisclosedBits = 0;
   _finalLength = 0;
   try{
    super.responder();
    if(!_confirm.isOK()){throw new QryptoException("Confirmation found errors:\n"+
						   "reconciliation failed!");}
    _totalDisclosedBits = _cascade.disclosed()+_confirm.getSampleSize();
    _recBits = _cascade.getReconciliedKey();
    if(_recBits != null ){
	_finalLength = finalLength(_recBits.length,_totalDisclosedBits,_securityParameter);
    }
    if(_finalLength < 1){throw new QryptoException("Not enough reconciled bits ("+_recBits.length+") for privacy amplification");}
    _pa = new PrivacyAmplification(_recBits,_finalLength);
    _pa.setPubConnection(_pc);
    _pa.setProgressTools(_bar,_progressLabel);
    _pa.responder();
    if(_sendSecretMessage.isSelected() && (_finalLength/BB84.BYTE_2_STRING_L > 0)){
      String[] sr = BB84.receiveSecretMessage(getFinalKey(), _pc, _parent);
      _cleartext = sr[0];
      _keytext = sr[1];
      _ciphertext = sr[2];
    }
   }catch(QryptoException qe){
      QryptoWarning.warning("The final key couldn't be generated \n"+
			     qe.getMessage(),"B92 Stopped (responder)",null);
      //throw new QryptoException("BB84 execution stopped!");   
   }catch(RuntimeException rt){
      throw new RuntimeException("Crash of B92 on the responder's side...");
   }
  }
  
  
  /**
  * Computes the length of the final key. 
  * @param rl is the size of the reconciled key. The size of the
  * key to be hashed in privacy amplification.
  * @param db is the number of parity bits disclosed during reconciliation and
  * confirmation. 
  * @param s is the security parameter in between 0..1. The larger
  * the value the more secure is the final key.
  * @return rl-db-s*rl (this is a somewhat arbitrary way of defining the final length).
  * Might return a negative value.
  */
  public static int finalLength(int rl,int db, double s){
    int extra = new Double(Math.ceil(rl*s)).intValue();
    if(extra<1){extra = 1;}
    return rl-db-extra;
  }
  
  /**
  * Returns the final secret-key. Should be called 
  * when available. 
  * @return the final key.
  */
  
  public boolean[] getFinalKey(){
    return _pa.getFinalKey();
  }
  
  /**
  * Returns an upper bound on the information an eavesdropper
  * limited to individual attack can get on the final key.
  * @param extra is the number of bits extracted in addition
  * to the disclosed bits.
  * @param m is the final length of the secret key.
  * @return an upper bound on the eavesdropper;s information.
  */
  
  public static double upperBoundForRestrictedAttacks(int extra,int m){
    return new Double(m*Math.pow(2,-extra/2d)).doubleValue();
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
     int secremove = 0;
    double rate = 0d;
    double efficiency = 0d;
    double infobound = _finalLength;
    if(_finalLength >0 && (_confirm != null) && (_cascade != null)){
	secremove = _recBits.length-_totalDisclosedBits-_finalLength;
	rate = new Double(_b92.getBucketSize()).doubleValue()*new Double(_b92.getNumberOfBuckets()).doubleValue()/new Double(_finalLength).doubleValue();
	double dfl = new Integer(_finalLength).doubleValue();
	double drks = new Integer(getRawKeySize()).doubleValue();
	efficiency = dfl/ drks;
	infobound = upperBoundForRestrictedAttacks(secremove,_finalLength);
    }
    super.addMyHoles(ht,1);
    argElm a = null;
    toHtml[] o = new toHtml[17];
    o[0] = new toHtmlString("peerID",peerID());
    o[1] = new toHtmlString("myID", myID());
    o[2] = new toHtmlInt("rawkeysize",getRawKeySize());
    o[3] = new toHtmlString("secparameter", Double.toString(_securityParameter));
    o[4]= new toHtmlInt("pulses", 0);
    o[5] = new toHtmlInt("size",0);
    o[6] = new toHtmlString("cascadeDisclosed","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[7] = new toHtmlString("confirmDisclosed","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[8] = new toHtmlString("secparameter", Double.toString(_securityParameter));
    o[9] = new toHtmlString("finalSize","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[10] = new toHtmlString("secremove","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[11] = new toHtmlString("finalKey","<BR><CENTER><FONT COLOR=red><BLINK>Undefined</BLINK>:</FONT>Reconciliation did not succeed!</CENTER><BR>");
    o[12] = new toHtmlString("bkperqb","<FONT COLOR=red><BLINK>0</BLINK></FONT>" );
    o[13] = new toHtmlString("totDisclosed","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[14]= new toHtmlString("rate","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[15]= new toHtmlString("leakinfo","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[16]= new toHtmlString("secretmessage",BB84.secretMessage2HTML(_cleartext,_keytext,_ciphertext,_initiator,false));
    if(_b92 != null){
      o[2] = new toHtmlInt("rawkeysize",getRawKeySize());     
      o[4]= new toHtmlInt("pulses",_b92.getBucketSize()*_b92.getNumberOfBuckets());
      if(errorRateIsOK() && (_recBits != null) && (_cascade != null) && (_confirm != null)){ 
	o[5] = new toHtmlInt("size",_recBits.length);
	o[6] = new toHtmlInt("cascadeDisclosed",_cascade.disclosed());
	o[7] = new toHtmlInt("confirmDisclosed",_confirm.getSampleSize());
	if((_finalLength > 0) && _confirm.isOK()){
	  o[9] = new toHtmlInt("finalSize",_finalLength);
	  o[10] = new toHtmlInt("secremove",secremove);
  	  o[11] = new toHtmlString("finalKey",Constants.arrayBool2HTML(getFinalKey(),true,10,5,"darkgreen"));
	  o[12] = new toHtmlString("bkperqb",Double.toString(efficiency) );
	  o[13] = new toHtmlInt("totDisclosed",_totalDisclosedBits);
	  o[14]= new toHtmlString("rate",Double.toString(rate));
	  o[15]= new toHtmlString("leakinfo",Double.toString(infobound));
	  o[16]= new toHtmlString("secretmessage",BB84.secretMessage2HTML(_cleartext,_keytext,_ciphertext,_initiator,_sendSecretMessage.isSelected()));
	}
      }
    }
    a = new argElm(B92._CLASSNAME+"+"+instanceNumber,instanceNumber,null, o);
    ht.put(B92._CLASSNAME+"+"+instanceNumber,a);
  }
  

  
 /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream si the output stream.
  */
  
  public void output(PrintWriter stream){
    super.output(stream);
  }
  
   /**
    * Allows the user to configure the quantum transmission.
    * This means to select the length of the quantum transmission,
    * the size of the sample for error-rate estimation.
    * @param parent is the component that owns the message dialog.
    */
    
    public void configure(Component parent){
	Object[] o = addMyConf();
	int r = JOptionPane.showOptionDialog(parent,o,"B92 Configuration", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null, _options,
				_options[_OK_OPTION]);
	//parent.repaint();
	if(r==0){
	    appliesConf(parent);
	}else{
	    JOptionPane.showMessageDialog(parent,
				"Old values remains:\n"+
				 settingString(),
				"Ok",
			        JOptionPane.ERROR_MESSAGE);
	     //parent.repaint();
	}
	
    }
    
    
   /**
    * Generates the configuration fields for the BB84 protocol.
    * @return the field from recBB84 + security parameter.
    */
    
    public Object[] addMyConf(){
	Object[] o0 = super.addMyConf();
	int l = o0.length;
	Object[] o = new Object[l + 2];
	for(int i = 0; i<l; i++){
	    o[i] = o0[i];
	}
	JPanel panel = new JPanel();
	BoxLayout bl = new BoxLayout(panel, BoxLayout.X_AXIS);
	panel.setLayout(bl);
	JLabel lab = new JLabel(_SEC_PARAM);
	_tfsecParam.setText(Double.toString(_securityParameter));
	panel.add(lab);
	panel.add(_tfsecParam);
	o[l] = panel;
	o[l+1] = _sendSecretMessage;
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
       try{
	    Double iv = Double.valueOf(_tfsecParam.getText());
	    double zozo = iv.doubleValue();
	    if((zozo<1.0d)&&(Math.abs(zozo)== zozo)){
		setSecurityParameter(zozo);
	    }else{//Il faut Traiter cette erreur separemment....
		throw new NumberFormatException("Value out of range [0..0.5]");
	    }
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad value for the security parameter,\n"+
				" value "+Double.toString(_toleratedError)+" remains.",
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
				"B92 Settings",
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
	this.sendOnlyLocalConfig(ssc);
	ssc.sendString(Double.toString(_securityParameter));
	ssc.sendBit(_sendSecretMessage.isSelected());
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
    * Receives the configuration from the initiator server.
    * @param pc is the public connection between the parties.
    * @return true if the received configuration could be understood.
    * Error handling should be improved. Now, no error is signaled to the user.
    */
    
    protected boolean receiveConfig(SocketPubConnection pc){
       boolean answer = false; 
       try{
	    if(super.receiveConfig(pc)){
		_securityParameter = (Double.valueOf(pc.receiveString())).doubleValue();
		_sendSecretMessage.setSelected(pc.receiveBit());
		answer = true;
	    }
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
				"Do you accept "+settingString(true),
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
	return settingString(false);
    }
    
    
    /**
    * Returns the string showing the settings.
    * @return the settings.
    */
    
    private String settingString(boolean question){
	 String message = protID()+" with "+peerID();
	 if(question){
	    message = message+"?\n";
	 }else{
	    message = message+"\n";
	 }
	 message = message + "----------------------------------\n";
	 //if(peerID() != null){message = message + "Peer identity: "+peerID()+"\n";}
	 message = message + "Size of the raw key: "+Integer.toString(_n)+"\n";
	 message = message + "Proportion for error sample: "+Float.toString(_sampleRatio)+"\n";
	 message = message + "Tolerated error-rate:"+Float.toString(_toleratedError)+"\n";
	 //message = message + "Number of passes:"+Integer.toString(_cascade.getNumberOfPasses())+"\n";
	 //message = message + "Initial blocksize:"+Integer.toString(_cascade.getInitialBlockSize())+"\n";
	 message = message + "Confirm size:"+Integer.toString(getConfirmSize())+"\n";
	 message = message + "Proportion for PA:"+Double.toString(_securityParameter)+"\n";
	 if(_sendSecretMessage.isSelected()){
	    message = message + "Ending with secret transmission.\n";
	 }
	 return message;
    } 


}
