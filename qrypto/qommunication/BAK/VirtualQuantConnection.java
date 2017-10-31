package qrypto.qommunication.BAK;

import qrypto.exception.*;
import qrypto.qommunication.BB84QuBit;
import qrypto.qommunication.QConnection;
import qrypto.qommunication.QuBit;





public class VirtualQuantConnection implements QConnection{



public long _DELAY = 1000;

private ComVector _in=null;
private ComVector _out=null;



  /**
   * A new virtual quantum connection. 
   * @param in is the vector used to receive qubits
   * @param out is the vector used to send qubits
   */

  public VirtualQuantConnection(ComVector in, ComVector out){
    _in = in;
    _out = out;
  }



  /**
   * This method is used for sending a qubit in some
   * quantum state.
   * @param s in the quantum state to be sent.
   */

  public synchronized void send(QuBit s){
   _out.setNextElement(s);
  }


  /**
   * This method read a single qubit with the Von Neumann measurement
   * having the input state as an observable. 
   * @param b is the state tested by the Von Neumann measurement.
   * @return true if the outcome of the measurement is 
   *         in state b. Returns false otherwise.
   * @exception TimeOutException whenever a timeout occurs.
   */
  public boolean read(QuBit b) throws TimeOutException{
    QuBit  qb=null;
    Object o=null;
    boolean answer = false;
    try{
      o = _in.getNextElement();
      if(o != null){
	qb = (QuBit)o;
	answer = qb.measureVN(b);
      }
    }catch(TimeOutException te){
      throw new TimeOutException("Timeout while expecting a bit; "+te.getMessage());
    }
    return answer;
  }

    /**
     * The quantum transmission of n bb84 random qubits. Will
     * accept other coding in the future.
     * @param n is the number of random bb84 qubits to be transmitted.
     * @return the n qubits sent.
     * @exception qrypto.TimeOutException is thrown whenever a timeout
     * occured before the completion.
     */  
    public BB84QuBit[] send(int n) throws TimeOutException{
	BB84QuBit[] qb = new BB84QuBit[n];
	for(int i=0;i<n ; i++){
	    qb[i] = new BB84QuBit(randBit(),randBit());
	    send(qb[i]);
	}
	return qb;
    }


    /**
     * The reception in random bb84 bases of n random qubits assumed to be
     * random bb84 qubits. Will accept other coding as well in the future.
     * @param n is the number of expected qubits.
     * @return the qubits received.
     * @exception qrypto.TimeOutException is thrown whenever a timeout occured
     * before completion.
     */
    public BB84QuBit[] read(int n) throws TimeOutException{
	BB84QuBit[] qb = new BB84QuBit[n];
	BB84QuBit measure;
	boolean basis = false;
	boolean res = false;
	for(int i=0; i<n ; i++){
	    basis = randBit();
	    measure = new BB84QuBit(BB84QuBit.B0, basis);
	    res = read(measure);
	    qb[i] = new BB84QuBit(!res,basis);
	}
	return qb;
    }

    /**
     * Generates a random boolean.
     * @return the bit picked.
     */
    private boolean randBit(){
	double r = Math.random();
	boolean ans = false; 
	if(r > 0.50d){
	    ans = true;
	}
	return ans;
    }

    public QuBit[] send(String classname) throws QryptoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public QuBit[] read(String classname) throws QryptoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}











