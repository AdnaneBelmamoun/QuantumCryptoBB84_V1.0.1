package qrypto.protocols;

import java.io.PrintWriter;
import java.util.Random;

import qrypto.exception.QryptoException;
import qrypto.exception.TimeOutException;
import qrypto.qommunication.Constants;




public class Confirm extends SubCProtocol
{


private static String _CONFIRM_ID = "Confirmation";
private static String _ERROR_FOUND = "(Echec)";

public static int DEF_SAMPLE_SIZE = 10;

private int _sample_size;
private boolean[] _bits = null;
private boolean _ok = true;
private boolean[] _badSample = null;
private boolean _done = false;


  /**
  * Constructors.
  */
  
  public Confirm(){
    _done = false;
    _ok = true;
    _badSample = null;
    _sample_size = DEF_SAMPLE_SIZE;
  }
  
  /**
  * @param bits are the bits to confirm
  */
  
  public Confirm(boolean[] bits){
    this();
    _bits = bits;
  }
  
  /**
  * @param bits are the bits to confirm
  * @param samplesize is the size of the check. If k is chosen
  * then the confirmation will be right except with probability 2^{-k}.
  */
  
  public Confirm(boolean[] bits, int samplesize){
    this(bits);
    _sample_size =  samplesize;
  }
  
  
  /**
  * Sets the bits to confirm.
  * @param bits are the bits to confirm
  */
  
  public void setBits(boolean[] bits){
    _bits = bits;
  }
  
  /**
  * Sets the sample size.
  * @samplesize is the new sample size.
  */
  
  public void setSampleSize(int samplesize){
    _sample_size = samplesize;
  }
  
  /**
  * Returns the sample size which is the number of parity exchanged.
  * @return the number of parity bits that are compared.
  */
  
  public int getSampleSize(){
    return _sample_size;
  }


  /**
   * This implements the initiator's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void initiator() throws QryptoException{
    _ok = true;
    _badSample = null;
    boolean[] sample;
    boolean res;
    boolean respres;
    resetProgressTools(_sample_size, _CONFIRM_ID);
    for(int i = 0; (i<_sample_size) && _ok; i++){
	addProgress();
	sample = newSample();
	_pc.sendBits(sample);
	res = parity(sample);
	_pc.sendBit(res);
	respres = _pc.receiveBit();
	if(res != respres){
	    _ok = false;
	    _badSample = sample;
	    setProgressLabel(_CONFIRM_ID+" "+_ERROR_FOUND);
	}else{
	    setProgressLabel(_CONFIRM_ID+" ("+i+")");
	}
    }
    _done = true;
  }

  /**
   * This implements the responder's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void responder() throws QryptoException{
    _ok = true;
    _badSample = null;
    boolean[] sample;
    boolean res;
    boolean respres;
    resetProgressTools(_sample_size, _CONFIRM_ID);
    for(int i = 0; (i<_sample_size) && _ok; i++){
	addProgress();
	sample = _pc.receiveBits();
	respres = _pc.receiveBit();
	res = parity(sample);
	_pc.sendBit(res);
	if(res != respres){
	    _ok = false;
	    _badSample = sample;
	     setProgressLabel(_CONFIRM_ID+" "+_ERROR_FOUND);
	}else{
	     setProgressLabel(_CONFIRM_ID+" ("+i+")");
	}
    }
    _done = true;
  }
  
    /**
    * Returns whether or not the confirmation was successful.
    * If not then one can get the sample that shows an error by
    * calling getErrorSet. This method should be called after 
    * the execution of either initiator() or responder() is 
    * completed.
    * @return true if the confirmation was successful and false otherwise.
    * If the execution has not been completed then true is returned.
    * @see getErrorSample
    */
    
    public boolean isOK(){
	return _ok;
    }
    
    /**
    * Returns the random set of positions where a parity error occurs.
    * The set is encoded by an array of boolean a where a[i]=true if the
    * position was part of the set.
    * @return the subset of positions or null if the confirmation was successful.
    */
    
    public boolean[] getErrorSet(){
	return _badSample;
    }
    
   

  /**
   * Print the output of the protocol for human reading.
   */

  public void output(){
  
  }
  
  /**
  * Just returns 2^{-l} where l is the confirm size.
  * @return the error prob.
  */
  public float errorProb(){
    return new Double(Math.pow(2d,-getSampleSize())).floatValue();
  }
  
  /**
  * Returns the result of an execution in HTML format.
  * @param myid is the identification of the excecuting party
  * @param peerid is the id of the peer.
  * @return the HTML string.
  */
  
  public String result2HTML(String myid, String peerid){
    int ss = getSampleSize();
    StringBuffer s= new StringBuffer("<UL>");
    s=s.append("<LI>Confirmation par comparaison publique <b>"+Integer.toString(ss)+"</b> parity bits was performed.\n");
    if(isOK()){
	s.append("<LI>Confirmation  <b><u>réussi avec succés</u></b>.\n");
	s.append("<LI> les clefs "+myid+" et "+peerid+"sont identiques avec une probabilité <b>2<SUP>-"+ss+"</SUP></b>,");
	s.append("qui est environ <b>"+Float.toString(errorProb())+"</b>.\n");
    }else{
	s.append("<LI>Confirmation <FONT COLOR=red><b><blink>echoué</blink></b></FONT>, <b>l'erreur est a gauche!</b>\n");
    	s.append("<LI>les clefs "+myid+" et "+peerid+" <b>ne sont pas identiques </b>");
    }
    s.append("</UL>");
    return s.toString();
  }
  
 /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream is the output stream.
  */
  
  public void output(PrintWriter stream){
    if(_done){
	stream.println("Echange de "+_sample_size+" bits de parité pour confirmation.");
	if(isOK()){
	    stream.println("La Confirmation des bits identiques partagés a réussie.");
	    stream.println("*ces Bits ne sont identiques que par la probabilté:"+ 
			    Double.toString(Math.pow(2.0d,(double)-_sample_size)));
	}else{
	    stream.println("La Confirmation des bits identiques partagés a échoué:");
	    stream.println("Erreur trouvé dans le sous ensemble suivant:");
	    Constants.printBoolArray(_badSample,stream,false);
	}
    }else{
	stream.println("aucune Sortie de confirmation n'est valable...non executé."); 
    }
    stream.println("****************************************");
  }
  
  /**
  * Selects a new random set of positions. Each position are selected
  * with probability 1/2. 
  * @return a random set of positions encoded in an array of boolean. It
  * returns null if the bits have not been set in the constructor or with
  * setBits.
  */
  
  private boolean[] newSample(){
    boolean[] out = null;
    if(_bits != null){
	out = new boolean[_bits.length];
	Random r = new Random();
	for(int i = 0; i<_bits.length; i++){
	    out[i] = ((r.nextInt() % 2) == 1);
	}
	
    }
    return out;
  
  }
  
  
  /**
  * Returns the parity of a random subset of positions.
  * @param sample is the selected set of positions
  * @return the parity.
  */
  
  private boolean parity(boolean[] sample){
    boolean res = true;
    for(int i = 0; i<_bits.length;i++){
	res = (sample[i] ? _bits[i]^res : res); 
    }
    return res;
  }



}
