package qrypto.protocols;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import qrypto.exception.TimeOutException;
import qrypto.qommunication.BB84QuBit;
import qrypto.qommunication.Constants;
import qrypto.qommunication.PubConnection;
import qrypto.server.BAK.Party;





public class NoiseEstimate extends SubCProtocol{

  /**
   *  the name of this protocol. 
   */
private static final String _PROT_ID = "Error sample";


private int    _samplesize=0;
private boolean[] _inputData;
@SuppressWarnings("rawtypes")
private Vector _thrownaway=null;
@SuppressWarnings("rawtypes")
private Vector _errorFound = null;
private int _inputsize;
private boolean[] _outputData=null;
//private float _estimate= 0.0F;
private int _error = 0;



  /**
   * This constructs an instance of the error-rate evaluation protocol.
   * @param samplesize is the number of tested positions
   * @param in is the input bit string to be tested. 
   * @param pc is the public connection used to execute the protocol with the peer.
   */

  @SuppressWarnings("rawtypes")
public NoiseEstimate(int samplesize, boolean[] in, PubConnection pc){
    _inputData = in;
    _inputsize = _inputData.length;
    _samplesize = Math.abs(samplesize);
    _pc = pc;
    _thrownaway = new Vector();
  }
  
  /**
  * Returns the sample size for the error-estimation.
  * @return the sample size.
  */
  
  public int sampleSize(){
    return _samplesize;
  }
  
  /**
  * Retuns the number of different positions that were probed.
  * This can be different from sampleSize() since a position
  * can be probed more than once.
  * @return the number of different probed positions.
  */
  
  public int differentProbes(){
    return _thrownaway.size();
  }
  
  /**
  * Returns the number of errors found.
  * @return the number.
  */

  public int errorsFound(){
    //return _errorFound.size();
    return _error;
  }
  
  /**
  * Returns the number of distinct errors found
  * @return the number of distinct positions with an error
  */
  
  public int distinctErrorsFound(){
    return _errorFound.size();
  }
  
  
  /**
  * Returns an upper bound for the distribution function of the normal
  * probability distribution.
  * @param x >= 0 is the input value.
  * @return an upper bound for the value of the distribution function for x.
  */
  private static double phi(double x){
     return (1 - (1/x)*(Math.exp(-x*x/2)/(Math.sqrt(2*Math.PI))));
  }
  
  /**
  * Finds the smallest integer x such that phi(x)>=confidence.
  * @param c is the confidance level.
  * @return x
  */
  
  private static int conf(double c){
    boolean found = false;
    int i = 1;
    for(i=1; !found; i++){
	if(phi(new Integer(i).doubleValue()) > c){ found = true;}
    }
    return i;
  }
  
  
  
  /**
  * Returns the interval of the last error sampling
  * with given confidence. 
  * @param c is the confidence wanted.
  * @return the interval.
  */
  
  public float interval(double c){
   // k = z_{alpha/2} * sqrt(tilde{p}(1-tilde{p})/s)
    double out = 1.0f;
    if(_samplesize > 0){
	double e =  new Float(estimate()).doubleValue();
	out =  conf(1-((1-c)/2))*Math.sqrt(((e+0.001d)*(1-e))/_samplesize);
    }
    return new Double(out).floatValue();
  }
  


  /**
   * This runs the protocol on the initiator side. The public connection
   * must be defined in order for this method to work.
   * Note that a position can sampled twice.
   * @exception TimeOutException is thrown whenever one connection
   * was expecting to receive something while a timeout occured.
   *
   */

  @SuppressWarnings({ "rawtypes", "unchecked" })
public synchronized void initiator() throws TimeOutException{
    boolean rbit = false;
    boolean b = true;
    int[] pos = new int[_samplesize];
    _error = 0;
    resetProgressTools(_samplesize, _PROT_ID);
    _errorFound = new Vector();
    for(int i=0;i<_samplesize;i++){
      addProgress();
      pos[i] = randomPos(_inputsize);
      _pc.sendInt(pos[i]);
      b = _inputData[pos[i]];
      _pc.sendBit(b);
     }
     for(int i =0;i<_samplesize; i++){
      rbit = _pc.receiveBit();
      if (_inputData[pos[i]]^rbit){
	    _error++;
	    _errorFound.addElement(new Integer(pos[i]));
	    setProgressLabel(_PROT_ID+" found ("+_error+")");
      }
      if(!contains(_thrownaway,pos[i])){_thrownaway.addElement(new Integer(pos[i]));}
     }
    //Integer ierror = new Integer(error);
    //Integer isize = new Integer(_samplesize);
    //_estimate = ierror.floatValue() / isize.floatValue();
  }

  /**
   * This runs the protocol on the responder side.The public connection
   * must be defined in order for this method to work (test not implemented).
   * Note that a position can be sampled twice.
   * @exception TimeOutException is thrown whenever a symbol is expected from the
   * public connection and the timeout occurs.
   */

  @SuppressWarnings({ "rawtypes", "unchecked" })
public synchronized void responder() throws TimeOutException{
    //int pos = 0;
    //boolean rb = false;
    boolean b = true;
    int[] pos = new int[_samplesize];
    boolean[] rb = new boolean[_samplesize];
    _error = 0;
    resetProgressTools(2*_samplesize, _PROT_ID);
    _errorFound = new Vector();
    for(int i = 0; i<_samplesize; i++){
      addProgress();
      pos[i] = _pc.receiveInt();
      rb[i] = _pc.receiveBit();
    }
    for(int i = 0; i<_samplesize; i++){
      b = _inputData[pos[i]];
      _pc.sendBit(b);
      if(b^rb[i]){
	_error++;
	_errorFound.addElement(new Integer(pos[i]));
	setProgressLabel(_PROT_ID+" found ("+_error+")");
      }
      if(!contains(_thrownaway,pos[i])){
	 _thrownaway.addElement(new Integer(pos[i]));
      }
    }
//    Integer ierror = new Integer(error);
//    Integer isize = new Integer(_samplesize);
//    _estimate = ierror.floatValue()/isize.floatValue();
  }

  /**
   * Returns the total number of bits.
   * @return the total number of bits in the input data.
   */

  public int size(){
    return _inputsize;
  }


  /**
   * Returns the estimation for the error-rate. Must be called
   * after the execution of the protocol. It is defined simply as
   * the total number of errors (including repetitions) divided
   * by the size of the sample.
   * @return the error-rate observed during the sampling.
   * @see Party#waitCompletion
   */

  public float estimate(){
    return new Integer(_error).floatValue()/new Integer(_samplesize).floatValue();
  } 


  /**
   * Returns the output data following the execution of the protocol.
   * This contains the bits that have not been sampled. The other
   * are thrown away.
   * @return the new data where the sampled bits have been removed.
   */

  public boolean[] outputData(){
    int kept = _inputsize - _thrownaway.size();
    _outputData = new boolean[kept];
    int j = 0;
    for(int i=0; i<_inputsize;i++){
      if ( !_thrownaway.contains(new Integer(i))){
	_outputData[j] = _inputData[i];
	j++;
      }
    }
    return _outputData;
  }
  
  /**
  * Dummy output procedure...
  */
  
  public void output(){
  
  }
  
  /**
  * Returns an html output for the exeuction of that subprotocol.
  * It shows the bits and probes. Does not include the statistics.
  * They have to be produced by the main protocol invoking that one.
  * @return the html string.
  */
  
  public String probesToHtml(){
     int[] probes = probe();
     byte[] flash = new byte[_inputData.length];
     for(int i = 0; i<flash.length; i++){
	flash[i] = 0;
     }
     for(int i = 0; i<probes.length ;i++){
       if(_errorFound.contains(new Integer(probes[i]))){
	flash[probes[i]] = 2;
       }else{
	flash[probes[i]] = 1;
       }
     }
     return Constants.arrayBool2HTML(_inputData,false,10,5,flash,"darkgreen","darkred");
  }
  
  /**
  * This print the output of the execution on a stream. It contains
  * the sampled positions, the number of errors found and the final
  * shorter array of bits.
  * @param stream is the stream where to print.
  */
  
  public void output(PrintWriter stream){
    stream.println("*************Noise Estimation Results**********");
    stream.println("Sample size:"+_samplesize);
    stream.println("Observed noise rate:"+estimate());
    _outputData = outputData();
    stream.println("Number of remaining bits:"+_outputData.length);
    stream.println("Remaining bits:");
    Constants.printBoolArray(_outputData,stream,BB84QuBit.B0);
    stream.println("**** list of distinct probes****");
    int[] sp = probe();
    for(int i = 0; i<sp.length;i++){
	stream.print(sp[i]+" ");
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	   stream.println();
	}
    }
    stream.println();
    stream.println("**************************************************");
  }

  /**
   * Returns the set of bits probed and sent during the execution of the protocol.
   * These bits will be removed from the key since they are no more
   * private.
   * @return the bits that have been sampled in the input data.
   */

  public int[] probe(){
    int ar[] = new int[_thrownaway.size()];
    for (int i = 0; i<_thrownaway.size();i++){
      ar[i] = ((Integer)_thrownaway.elementAt(i)).intValue();
    }
    return ar;
  }

  /**
   * Returns a random position in the output data.
   * @param maxn is the maximum value + 1 for the random position. That
   *        is the random value r is between 0..int-1.
   */

  private int randomPos(int maxn){
    double r = Math.random();
    Double temp = new Double(Math.floor(r*maxn));
    return temp.intValue();
  }



  /**
   * Returns true if the given integer is in the given vector.
   * @param in is the list of elements
   * @param v is the value we are looking for
   * @return true iff the integer v belongs to in.
   */

  @SuppressWarnings("rawtypes")
private boolean contains(Vector in, int v){
    boolean found = false;
    for(Enumeration e = in.elements();e.hasMoreElements() && !found;){
      Integer i = (Integer)e.nextElement();
      if(v==i.intValue()){found = true;}
    }
    return found;
  } 



}

