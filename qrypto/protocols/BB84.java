package qrypto.protocols;

import java.awt.Color;
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



public class BB84 extends RecBB84
{

    private static int[] pow2={1,2,4,8,16,32,64,128};
    public  static int BYTE_2_STRING_L = 7;
    public  static int BYTE_2_STRING_OFFSET = 16; 


    //private String INITLOGFILENAME = Constants.BB84SENDER;
    //private String RESPLOGFILENAME = Constants.BB84RECEIVER;
    
    //public static final int _DEF_CONF_SIZE = 10;
    
    public  static final String PROT_ID = "protocol BB84 ";
    private static final String _CLASSNAME = "BB84";
    private static final String _SEC_PARAM = "Proportion pour PA:";
    private JTextField _tfsecParam = null; //the textfield for the security parameter
    private JCheckBox _sendSecretMessage = null;
    //private JTextField _secretMessage = null;
    //private JLabel _secretMessageLabel = null;
    private boolean[] _recBits = null;
    private PrivacyAmplification _pa = null;
    private int _totalDisclosedBits = 0;
    private int _finalLength = 0;
    //The security parameter is an extra shrinking parameter
    //c between 0 and 1 that the size of the final key is
    // f = (1-c)*(n-total_disclosed)
    private double _securityParameter = 0.05;
    private String _cleartext = "non défini";
    private String _ciphertext= "non défini";
    private String _keytext="non défini";
    
    
   public BB84(){
	super();
	newProtID(PROT_ID);
	_tfsecParam = new JTextField(7);
	_sendSecretMessage = new JCheckBox("Terminer la simulation avec une Transmission Secréte");
	_sendSecretMessage.setSelected(true);
   }
   
   /**
    * Constructor.
    * @param rqc is the remote quantum connection
    * @param pc is the public connection between the parties.
    */
    
    public BB84(RemoteQConnection rqc, PubConnection pc){
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
   _finalLength = 0;
   _totalDisclosedBits = 0;
   try{
    super.initiator();
    if(!_confirm.isOK()){throw new QryptoException("Confirmation de présence d'erreur:\n"+
						   "Reconciliation n'as pas réparer toutes les érreurs!");} 
    _totalDisclosedBits = _cascade.disclosed()+_confirm.getSampleSize();
    _recBits = _cascade.getReconciliedKey(); 
    if(_recBits != null){
	_finalLength = finalLength(_recBits.length,_totalDisclosedBits,_securityParameter);
    }
    if(_finalLength < 1){throw new QryptoException("Réconciliation des bits non suffisante pour l'amplification de la sécurité");}
    _pa = new PrivacyAmplification(_cascade.getReconciliedKey(),_finalLength);
    _pa.setPubConnection(_pc);
    _pa.setProgressTools(_bar,_progressLabel);
    _pa.initiator();
    if(_sendSecretMessage.isSelected() && (_finalLength/BYTE_2_STRING_L > 0) ){
      String[] sr = sendSecretMessage(getFinalKey(),_pc, _parent);
      _cleartext = sr[0];
      _keytext = sr[1];
      _ciphertext = sr[2];
    }
   }catch(QryptoException qe){
      QryptoWarning.warning("la clef finale n'as pas pû être générée \n"+
			    qe.getMessage(),"BB84 Arrété (initiateur)",_parent);
      //throw new QryptoException("BB84 execution stopped!");
   }catch(RuntimeException rt){
      throw new RuntimeException("Crash du BB84 du côté de l'initiateur...");
   }
  }
  
  
  /**
  * This is the responder part for RecBB84. First, PlainBB84 is executed,
  * then the reconciliation protocol Cascade+confirmation, and finally
  * privacy amplification.
  * @throw QryptoException whenever a problem occurs.
  */
  
  public void responder()  throws QryptoException{
   _finalLength = 0;
   _totalDisclosedBits = 0;
   try{
    super.responder();
    if(!_confirm.isOK()){throw new QryptoException("Confirmation de présence d'erreurs:\n"+
						   "Reconciliation n'as pas réparer toutes les erreurs");}
    _totalDisclosedBits = _cascade.disclosed()+_confirm.getSampleSize();
    _recBits = _cascade.getReconciliedKey();
    if(_recBits != null){
       _finalLength = finalLength(_recBits.length,_totalDisclosedBits,_securityParameter);
    }
    if(_finalLength < 1){throw new QryptoException("Réconciliation des bits insuffisante pour l'amplification de la sécurité");}
    _pa = new PrivacyAmplification(_recBits,_finalLength);
    _pa.setPubConnection(_pc);
    _pa.setProgressTools(_bar,_progressLabel);
    _pa.responder();
    if(_sendSecretMessage.isSelected()&& (_finalLength/BYTE_2_STRING_L > 0) ){
      String[] sr = receiveSecretMessage(getFinalKey(), _pc, _parent);
      _cleartext = sr[0];
      _keytext = sr[1];
      _ciphertext = sr[2];
    }
   }catch(QryptoException qe){
      QryptoWarning.warning("la clef finale n'as pa pû être génèrée \n"+
			    qe.getMessage(),"BB84 arreté (côté répondeur)",_parent);
      //throw new QryptoException("BB84 execution stopped!");   
   }catch(RuntimeException rt){
      throw new RuntimeException("Crash du BB84 du côté du répondeur...");
   }
  }
  
  /**
  * Asks the user to send a secret message to be encrypted using 
  * the final key.
  * @param sk is the secret-key.
  * @return the secret message,ciper and keys in text format.
  */
  public static String[] sendSecretMessage(boolean[] sk,PubConnection pc, Component parent){
  	JTextField _secretMessage = new JTextField(40);
	JLabel _secretMessageLabel = new JLabel();
	Object[] oo = new Object[2];
	_secretMessageLabel.setText("Veuillez tapez votre Message SECRET (max "+sk.length/BYTE_2_STRING_L+" chars):");
	oo[0] = _secretMessageLabel;
	oo[1] = _secretMessage;
	String[] opt = {"OK"};
	String cleartext = "";
	while(cleartext.length() < 1){
	 JOptionPane.showOptionDialog(parent,oo,"Selectionnez un message secret", 
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE,
		null, opt,
		opt[0]);
	 cleartext = ((JTextField)oo[1]).getText();
	 if(cleartext == null){cleartext = "";}
	 if(cleartext.length() < 1){
	    QryptoWarning.warning("le messsage été vide,\n"+
	                          "Recommencer!","Attention--Mauvais paramétre",parent);
	 }
	}
	byte[] bytekey = sk2byte(sk,cleartext.length());
	if(bytekey.length < cleartext.length()){
	    cleartext = cleartext.substring(0,bytekey.length);
	}
	Object[] ou = new Object[3];
	JPanel panel1 = new JPanel();
	BoxLayout bl = new BoxLayout(panel1, BoxLayout.X_AXIS);
	panel1.setLayout(bl);
	JLabel lab = new JLabel("Le message reçu est  :");
	JLabel valf = new JLabel("["+cleartext+"]");
	valf.setForeground(Color.blue);
	panel1.add(lab);
	panel1.add(valf);
	ou[0] = panel1;
	
	String stringkey = byte2String(bytekey);
	JPanel panel2 = new JPanel();
	bl = new BoxLayout(panel2, BoxLayout.X_AXIS);
	panel2.setLayout(bl);
	lab = new JLabel("The key is           :");
	valf = new JLabel("["+stringkey+"]");
	valf.setForeground(Color.red);
	panel2.add(lab);
	panel2.add(valf);
	ou[1] = panel2;
	
	byte[] cipherbyte = cipher(bytekey,cleartext,80);
	String ciphertext = byte2String(cipherbyte);
	JPanel panel3 = new JPanel();
	bl = new BoxLayout(panel3, BoxLayout.X_AXIS);
	panel3.setLayout(bl);
	lab = new JLabel("The ciphertext is:");
	valf = new JLabel("["+ciphertext+"]");
	valf.setForeground(Color.black);
	panel3.add(lab);
	panel3.add(valf);
	ou[2] = panel3;
	opt[0] = "Send";
	JOptionPane.showOptionDialog(parent,ou,"Envoi un Message secret", 
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE,
		null, opt,
		opt[0]);
	pc.sendBytes(cipherbyte);
	String[] outs = new String[3];
	outs[0] = cleartext;
	outs[1] = stringkey;
	outs[2] = ciphertext;
	return outs;
  }
  
  /**
  * Receives a secret message.
  */
  
  public static String[] receiveSecretMessage(boolean[] sk, PubConnection pc, Component parent)throws TimeOutException{
      byte[] cipherbyte = pc.receiveBytes();
      String ciphertext = byte2String(cipherbyte);
      byte[] bytekey = sk2byte(sk, cipherbyte.length);
      String stringkey = byte2String(bytekey);
      String m = decipher(bytekey, cipherbyte);
      Object[] ou = new Object[3];
      
      JPanel panel1 = new JPanel();
      BoxLayout bl = new BoxLayout(panel1, BoxLayout.X_AXIS);
      panel1.setLayout(bl);
      JLabel lab = new JLabel("The ciphertext is:");
      JLabel valf = new JLabel("["+ciphertext+"]");
      valf.setForeground(Color.black);
      panel1.add(lab);
      panel1.add(valf);
      ou[0] = panel1;      
      
      JPanel panel2 = new JPanel();
      bl = new BoxLayout(panel2, BoxLayout.X_AXIS);
      panel2.setLayout(bl);
      lab = new JLabel("la clef est           :");
      valf = new JLabel("["+stringkey+"]");
      valf.setForeground(Color.red);
      panel2.add(lab);
      panel2.add(valf);
      ou[1] = panel2;
	      
      JPanel panel3 = new JPanel();
      bl = new BoxLayout(panel3, BoxLayout.X_AXIS);
      panel3.setLayout(bl);
      lab = new JLabel("Le message reçu est  :");
      valf = new JLabel("["+m+"]");
      valf.setForeground(Color.blue);
      panel3.add(lab);
      panel3.add(valf);
      ou[2] = panel3;
      
      String[] opt = {"OK"};
      JOptionPane.showOptionDialog(parent,ou,"Reception d'un message secret", 
		JOptionPane.DEFAULT_OPTION,
		JOptionPane.INFORMATION_MESSAGE,
		null, opt,
		opt[0]);
      String[] outs = new String[3];
      outs[0] = m;
      outs[1] = stringkey;
      outs[2] = ciphertext;
      return outs;
  }
  
  /**
  * Returns an array of char corresponding to a secret-key.
  * @param sk is the boolean secret key. 
  */
  
  @SuppressWarnings("unused")
public static byte[] sk2byte(boolean[] sk){
    int l =  sk.length/BYTE_2_STRING_L;
    byte b1 = 1;
    byte[] out = new byte[l];
    for(int i = 0;i<l;i++){
	byte w = 0;
	for(int j = 0; j<BYTE_2_STRING_L;j++){
	    if(sk[BYTE_2_STRING_L*i+j]){
		w = new Integer(w+pow2[j]).byteValue();
	    }
	}
	out[i] = w;
    }
    return out;
  }
  
  /**
  * Returns an array of char corresponding to a secret-key of a given size.
  * @param sk is the secret key.
  * @param l is the size wanted. 
  * @return an array of byte of no more than l chars.
  */
  
  public static byte[] sk2byte(boolean[] sk, int l){
    byte[] out = sk2byte(sk);
    byte[] outshort;
    if(l>out.length){l=out.length;}
    outshort = new byte[l];
    for(int i = 0;i<l;i++){
	outshort[i] = out[i];
    }
    return outshort;
  }
  
  /**
  * Produces the ciphertext using one-time pad.
  * @param sk is the key in bytes.
  * @param m is the message to encrypt.
  * @param l is the maximum length of the message to encrypt. If the key is shorter
  * than the message then the message is set to the length of the key.
  */
  
  public static byte[] cipher(byte[] sk, String m, int l){
    byte[] mb = m.getBytes();
    if(l > m.length()){l=m.length();}
    if(l > sk.length){l=sk.length;}
    byte[] out  = new byte[l];
    byte a;
    for(int i = 0; i<l;i++){
	a = sk[i];
	out[i] = (mb[i]^=a);
    }
    return out;
  }
  
  
  /**
  * Decipher using the byte key.
  * @param sk is the secret key
  * @param c is the cipher.
  * @return the original message
  */
  
  public static String decipher(byte[] sk,byte[] c ){
    int l = sk.length;
    if(l>c.length){l=c.length;}
    byte[] out = new byte[l];
    byte a1;
    byte a2;
    for(int i = 0; i<l; i++){
     a1 = c[i];
     a2 = sk[i];
     out[i] = (a1^=a2);
    }
    return new String(out);
  }
  
  /**
  * Transforms an array of bytes in a string visible to the user.
  * Shouldn't be considered as a real one-to-one mapping. It is
  * an approximation. 
  * The byte values must be less than 127.
  * @param b is the array of bytes to transform.
  */
  public static String byte2String(byte[] b){
   @SuppressWarnings("unused")
byte OS0 = (byte)-161;
   //byte OS1 = 96;
   byte[] xb = new byte[b.length];
   for(int i =0 ; i<b.length; i++){
    xb[i] = (byte)((b[i] % 95) + 32);
    //if(b[i]<32){ //32
	//xb[i] = (byte)(OS0+xb[i]);
    //}
    if(xb[i]==60){xb[i] = -3;}
    if(xb[i]==62){xb[i] = -4;}
    //if(b[i]==127){xb[i] = -1;}
    //if(b[i]>96){//96
	//xb[i] = (byte)(OS1+xb[i]);  
    //}
   }
   return new String(xb);
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
	rate = new Double(_bb84.getBucketSize()).doubleValue()*new Double(_bb84.getNumberOfBuckets()).doubleValue()/new Double(_finalLength).doubleValue();
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
    o[11] = new toHtmlString("finalKey","<br><CENTER><FONT COLOR=red><BLINK>Undefined</BLINK>:</FONT>Reconciliation did not succeed!</CENTER><BR>");
    o[12] = new toHtmlString("bkperqb","<FONT COLOR=red><BLINK>0</BLINK></FONT>" );
    o[13] = new toHtmlString("totDisclosed","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[14]= new toHtmlString("rate","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[15]= new toHtmlString("leakinfo","<FONT COLOR=red><BLINK>0</BLINK></FONT>");
    o[16]= new toHtmlString("secretmessage",secretMessage2HTML(_cleartext,_keytext,_ciphertext,_initiator,false));
    if(_bb84 != null){
      o[2] = new toHtmlInt("rawkeysize",getRawKeySize());     
      o[4]= new toHtmlInt("pulses",_bb84.getBucketSize()*_bb84.getNumberOfBuckets());
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
	  o[16]= new toHtmlString("secretmessage",secretMessage2HTML(_cleartext,_keytext,_ciphertext,_initiator,_sendSecretMessage.isSelected()));
	}
      }
    }
    a = new argElm(BB84._CLASSNAME+"+"+instanceNumber,instanceNumber,null, o);
    ht.put(BB84._CLASSNAME+"+"+instanceNumber,a);
  }
  
  /**
  * Generates the HTML code for showing the result of a secret toy transmission.
  * If no transmission was performed then a default HTML notice is generated.
  * @param m is the message.
  * @param k is the key used.
  * @param c is the cipher.
  * @param init indicates whether or not this player is the initiator.
  * @param executed is true iff a secret message has been sent and received.
  */
  
  @SuppressWarnings("unused")
public static String secretMessage2HTML(String m, String k, String c,boolean init,boolean executed){
    StringBuffer s = new StringBuffer("<BR><BR><BIG><b>Secret Transmission</b></BIG>:\n");
    s.append("<UL>\n");
    String t;
    if(executed && (m!= null) && (k!= null) && (c!= null)){
      if(init){
	s.append("<LI><PRE><tt>Secret message :<B><font color=blue>"+m+"</font></B></tt></PRE>\n");
	s.append("<LI><PRE><tt>Encryption key :<B><font color=green>"+k+"</font></B></tt></PRE>\n");
	s.append("<LI><PRE><tt>Ciphertext sent:<B><font color=red>"+c+"</font></B></tt></PRE>\n");
      }else{
	s.append("<LI><PRE><tt>Ciphertext sent :<B><font color=red>"+c+"</font></B></tt></PRE>\n");
	s.append("<LI><PRE><tt>Decryption key  :<B><font color=green>"+k+"</font></B></tt></PRE>\n");
	s.append("<LI><PRE><tt>Received message:<B><font color=blue>"+m+"</font></B></tt></PRE>\n");
      }
    }else{
       if(init){
	s.append("<LI>aucun message secret envoyé.\n");
       }else{
	s.append("<LI>aucun message secret reçu.\n");
       }
    }
    s.append("</UL><BR><BR>\n");
    return s.toString();
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
	int r = JOptionPane.showOptionDialog(parent,o,"Configuration BB84 ", 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null, _options,
				_options[_OK_OPTION]);
	//parent.repaint();
	if(r==0){
	    appliesConf(parent);
	}else{
	    JOptionPane.showMessageDialog(parent,
				"Anciénnes valeurs réstantes:\n"+
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
		throw new NumberFormatException("Valeur en dehors de l'intervalle [0..0.5]");
	    }
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Mauvaise valeur pour PA shrinking proportion,\n"+
				" valeur "+Double.toString(_toleratedError)+" restantes.",
				"Config Erreur",
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
				"Paramétrage du BB84 ",
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
	        owner.addToLogin("paramétres refusés par le peer(corespondant,egal)...");
	    }
	}catch(TimeOutException to){
	       owner.addToLogin("Stooooooone 3iyane");
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
				"Acceptez vous "+settingString(true),
				"Paramétres de confirmation",
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
    * @param question indicates whether or not a question mark is appended.
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
	 message = message + "Taille du Vecteur clef: "+Integer.toString(_n)+"\n";
	 message = message + "Proportion d'erreur: "+Float.toString(_sampleRatio)+"\n";
	 message = message + "Taux d'erreur toléré:"+Float.toString(_toleratedError)+"\n";
	 //message = message + "Number of passes:"+Integer.toString(_cascade.getNumberOfPasses())+"\n";
	 //message = message + "Initial blocksize:"+Integer.toString(_cascade.getInitialBlockSize())+"\n";
	 message = message + "Taille Confirmation :"+Integer.toString(getConfirmSize())+"\n";
	 message = message + "Extra shrinking proportion pour PA: "+Double.toString(_securityParameter)+"\n";
	 if(_sendSecretMessage.isSelected()){
	    message = message + "Términer avec une transmission secréte.\n";
	 }
	 return message;
    } 

}
