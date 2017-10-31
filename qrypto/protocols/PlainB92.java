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
import qrypto.qommunication.B92QuBit;
import qrypto.qommunication.BB84QuBit;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.SocketPubConnection;
 
 

public class PlainB92 extends RawB92
{
    //private static final String _BASES_ANNOUNCEMENT_ID = "Bases announcement";
    public float DEF_SAMPLESIZE_RATIO = 0.1f;

    private String INITLOGFILENAME = Constants.PLAINB92SENDER;
    private String RESPLOGFILENAME = Constants.PLAINB92RECEIVER;
    private static final String _CLASSNAME = "PlainB92";
    
    
    public  static final String PROT_ID = "Plain B92 transmission";
    private static final String _CONF_LABEL = "Proportion for sample [0..1]: ";
    private JTextField _tfs = null; //the text field for the sample size.
    
    protected float _sampleRatio = DEF_SAMPLESIZE_RATIO;
    protected int _sampleSize = 0; //the size of the sample for error testing.
				 //can be set by the user through configuration.
    private NoiseEstimate _noiseEstimate = null;
    private boolean[] _keptpos = null;
   /**
    * Empty constructor.
    */
    
   public PlainB92(){
	super();
	newProtID(PROT_ID);
	_tfs = new JTextField(7);
   }
   
  /**
   * @param ratio is the ragtio of bits taken for error-estimation.
   */
   
   public PlainB92(float ratio){
     this();
     _sampleRatio = ratio;
   }
  
    
    
    /**
    * Constructor.
    * @param rqc is the remote quantum connection
    * @param pc is the public connection between the parties.
    */
    
    public PlainB92(RemoteQConnection rqc, PubConnection pc){
	this();
	_qc = rqc;
	_pc = pc;
    }
    
    
   /**
    * Constructor of a new PlainBB84 transmission on initiator side.
    * @param rqc is the remote quantum connection.
    * @param pc is the public connection between the players
    * @param me is the initiator identity
    * @param n is the number of qubits to be exchanged.
    * @param peer is the responder identity
    * @param initiator is true of me is initiator, false otherwise.
    */
    public PlainB92(String me, String peer,int n,
		   RemoteQConnection rqc, PubConnection pc, boolean initiator){
	super(me,peer,n,rqc,pc,initiator);
	newProtID(PROT_ID);
	setID(me,peer,initiator);
	if(_sampleSize == 0){_sampleSize++;}
	_tfs = new JTextField(7);
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
  * This runs the initiator if me is initiator and runs the responder otherwise.
  */
  
  
  public void run() throws RuntimeQryptoException{
    try{
      String s = INITLOGFILENAME;
      if(!_initiator){s = RESPLOGFILENAME;}
      _logfile = new Log(s, true, "PlainB92");
      _logfile.header();
      if(_initiator){
 	initiator();
      }else{
	responder();
      }
    }catch(QryptoException qe){
	throw new RuntimeQryptoException(qe.getMessage());
    }catch(IOException io){
	throw new RuntimeQryptoException("Couldn't open the logfile for PlainB92");
    }
  }
  
  /**
   * This implements the initiator's side protocol. It is the annoucement
   * of the bases used for all positions. The reception of the good positions
   * (i.e. the ones for which the bases match). Then, the announcement
   * of a subset of positions of size sampleSize selected by the user through
   * the settings configuration. If the error-rate is below the tolerated
   * error rate toleratedErrorRate that is also selected by the user then
   * in the execution is successful otherwise the abort flag is raised...
   * @exception QryptoException is thrown whenever 
   * a communication problem occurs.
   */

  public void initiator() throws QryptoException{
   super.initiator();
   B92QuBit[] qresult = B92QuBit.convertFromBB84(super.getOutput());
   boolean[] goodpos = _pc.receiveBits();//array of bits a[i]=true -> kept
   int kept = 0;
   for(int i=0; i<goodpos.length; i++){
    if(goodpos[i]){kept++;}
   }
   _keptpos = new boolean[kept];
   int j = 0;
   for(int i = 0; i<qresult.length; i++){
    if(goodpos[i]){
	_keptpos[j] = qresult[i].whichB92bit().booleanValue();
	j++;
    }
   }
   _sampleSize = Math.round(_sampleRatio*_n);
   if(_sampleSize == 0){_sampleSize++;}
   _noiseEstimate = new NoiseEstimate(_sampleSize,_keptpos,_pc);
   _noiseEstimate.setProgressTools(_bar,_progressLabel);
   _noiseEstimate.initiator();
  }

  /**
   * This implements the responder's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void responder() throws QryptoException{
   super.responder();
   B92QuBit[] qresult = B92QuBit.convertFromMeasurement(super.getOutput());
   boolean[] good_pos = new boolean[qresult.length];
   int kept = 0;
   for(int i = 0; i<qresult.length; i++){
    good_pos[i] = !qresult[i].isUndefined();
    if(good_pos[i]){
	kept++;
    }
   }
   _pc.sendBits(good_pos);
   _keptpos = new boolean[kept];
   int j = 0;
   for(int i=0;i<qresult.length;i++){
    if(good_pos[i]){
	_keptpos[j] = qresult[i].whichB92bit().booleanValue();
	j++;
    }
   }
   _sampleSize = Math.round(_sampleRatio*_n);
   if(_sampleSize == 0){_sampleSize++;}   
   _noiseEstimate = new NoiseEstimate(_sampleSize,_keptpos,_pc);
   _noiseEstimate.setProgressTools(_bar,_progressLabel);
   _noiseEstimate.responder();
  }
   
  
  /**
  * Returns the output of the raw bb84 quantum transmission. These
  * are the bits sent and received in the same basis from which the
  * sampled bits have been removed.
  * @returns the array of qubits or null if no output has been produced yet.
  */
  
  public boolean[] getOutputBits(){
    return _noiseEstimate.outputData();
  }
  
  /**
  * Returns the noise estimation object that contains the result
  * of the sampling.
  * @return the noise estimation object. 
  */
  
  public NoiseEstimate getNoiseEstimate(){
    return _noiseEstimate;
  }


  /**
   * Print the output of the protocol for reading. Not implemented.
   */

  public void output(){
  
  }
  
  
  /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream si the output stream.
  */
  
  public void output(PrintWriter stream){
    super.output(stream);
    stream.println();
    stream.println("**Raw key containing the positions with conclusive outcomes**");
    stream.println("Number of positions kept:"+_keptpos.length);
    Constants.printBoolArray(_keptpos, stream, BB84QuBit.B0);
    _noiseEstimate.output(stream);
  }
  
  
  
 /**
  * This adds the objects to be plugged in the html template for
  * this protocol. Should be called after the execution is completed.
  * The holes to be added are in addition to thos of RawBB84 are:
  * myid,peerid, initialSize,numberOfFinalBits, cbits,
  * sampleSize, numberOfErrors, errorRate, differentProbes, keptPos
  * @param ht is the hashtable where to add the holee for that protocol
  * @param instanceNumber is the instance number of this subprotocol with
  * respect to the surrounding protocol.
  */
  
  @SuppressWarnings("unchecked")
public void addMyHoles(@SuppressWarnings("rawtypes") Hashtable ht, int instanceNumber){
    super.addMyHoles(ht,1);
    int sizeAfterSample = _noiseEstimate.size()-_noiseEstimate.differentProbes();
    float rate = (new Integer(_b92.getBucketSize()*_b92.getNumberOfBuckets()).floatValue())/new Integer(sizeAfterSample).floatValue();
    String inter = Float.toString(_noiseEstimate.interval(0.95));
    argElm a = null;
    toHtml[] o = new toHtml[14];
    o[0] = new toHtmlString("myid",_me);
    o[1] = new toHtmlString("peerid",_peer);
    o[2] = new toHtmlInt("initialSize",_noiseEstimate.size());
    o[3] = new toHtmlInt("numberOfFinalBits",_keptpos.length);
    o[4] = new toHtmlString("keptpos",Constants.arrayBool2HTML(_keptpos,false,10,5,"darkblue"));
    o[5] = new toHtmlString("cbits",_noiseEstimate.probesToHtml());
    o[6] = new toHtmlInt("sampleSize",_noiseEstimate.sampleSize());
    o[7] = new toHtmlInt("numberOfErrors",_noiseEstimate.errorsFound());
    o[8] = new toHtmlString("errorRate",Float.toString(_noiseEstimate.estimate()));
    o[9] = new toHtmlInt("differentProbes",_noiseEstimate.differentProbes());
    o[10]= new toHtmlInt("keptPos",sizeAfterSample);
    o[11]= new toHtmlString("rate",new Float(rate).toString());
    o[12]= new toHtmlString("confidence", "95");
    o[13]= new toHtmlString("interval",inter);
    a = new argElm(PlainB92._CLASSNAME+"+"+instanceNumber,instanceNumber,null, o);
    ht.put(PlainB92._CLASSNAME+"+"+instanceNumber,a);
  }
  
  
  /**
  * Returns an html string representing the qubits kept
  * after the quantum transmission.
  * @return the html string.
  */
  
//  private String keptBits2HTML(){
//    StringBuffer s = new StringBuffer("<BR><BR><B>");
//    String b = "";
//    for(int i = 0; i< _keptpos.length; i++){
//      if(_keptpos[i]){	
//	    b="1";
//      }else{
//	    b="0";
//      }
//      s.append(b);
      //the following abs() are there because of uncompatibility 
      //with Windows Math.IEEremainder().
//      if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
//	s.append(" \n");
//      }
//      if(Math.abs(Math.IEEEremainder(i+1,20.0d))<0.5d){
//	s.append("<BR>");
//      }
//      if(Math.abs(Math.IEEEremainder(i+1,100.0d))<0.5d){
//	s.append("<BR>");
//      }
//    }
//    s.append("</B><BR>");
//    return s.toString();  
//  }

  
    /**
    * Allows the user to configure the quantum transmission.
    * This means to select the length of the quantum transmission,
    * the size of the sample for error-rate estimation.
    * @param parent is the component that owns the message dialog.
    */
    
    public void configure(Component parent){
	Object[] o = addMyConf();
	int r = JOptionPane.showOptionDialog(parent,o,"Plain B92 Config", 
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
				"\n Sample Size=" + Integer.toString(_sampleSize) +
				" used.",
				"Ok",
			        JOptionPane.ERROR_MESSAGE);
	     //parent.repaint();
	}
	
    }
    
    
    /**
    * Generates the configuration fields for that protocol.
    * @return the field from rawBB84 + the size of the sample.
    */
    
    public Object[] addMyConf(){
	Object[] o0 = super.addMyConf();
	Object[] o = new Object[2];
	o[0] = o0[0];
	JPanel panel = new JPanel();
	BoxLayout bl = new BoxLayout(panel, BoxLayout.X_AXIS);
	panel.setLayout(bl);
	JLabel lab = new JLabel(_CONF_LABEL);
	_tfs.setText(Float.toString(_sampleRatio));
	panel.add(lab);
	panel.add(_tfs);
	o[1] = panel;
	return o;
    }
    
    /**
    * Applies the configuration set by the user.
    * @param parent is the parent frame for the dialog.
    * @return true iff all configurations were correctly set. Otherwise
    * the default values are used.
    */
    
    public boolean appliesConf(Component parent){
       boolean ok = super.appliesConf(parent);
       try{
	    Float iv = Float.valueOf(_tfs.getText());
	    _sampleRatio = iv.floatValue();
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad value setting, sampleSize="+Integer.toString(_sampleSize)+" remains.",
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
				"Plain B92 Settings",
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
	ssc.sendString(Float.toString(_sampleRatio));
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
	    String peer = pc.receiveString();
	    setPeerID(peer,true);
	    _n = pc.receiveInt();
	    _sampleRatio = Float.valueOf(pc.receiveString()).floatValue();
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
	 String message =                         "Protocol: "+protID()+"\n";
	 if(peerID() != null){message = message + "Peer identity: "+peerID()+"\n";}
	 if(myID() != null){
			    message = message + "Size of transmission: "+Integer.toString(_n)+"\n";
			    message = message + "Ratio for error sample: "+Float.toString(_sampleRatio)+"\n";
	 }
	 return message;
    } 

}
