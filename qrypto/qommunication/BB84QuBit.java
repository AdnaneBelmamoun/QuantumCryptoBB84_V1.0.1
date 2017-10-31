package qrypto.qommunication;

import qrypto.exception.TimeOutException;




public class BB84QuBit extends QuBit {


  /** BB84 basis */



public static final boolean B0 = false;
public static final boolean B1 = true; 

public static final boolean RECTILINEAR = false;
public static final boolean DIAGONAL = true;
public static final BB84QuBit B0R = new BB84QuBit(B0,RECTILINEAR);
public static final BB84QuBit B1R = new BB84QuBit(B1,RECTILINEAR);
public static final BB84QuBit B0D = new BB84QuBit(B0,DIAGONAL);
public static final BB84QuBit B1D = new BB84QuBit(B1,DIAGONAL);
public static final BB84QuBit[] RECT = {B0R,B1R};
public static final BB84QuBit[] DIAG = {B0D,B1D};


protected boolean _bit = true;
protected boolean _basis = true;
   

/**
* Constructor for a BB84 qubit in state |0>.
*/

    public BB84QuBit(){
	//super();
	setState(B0,RECTILINEAR);
    }



  /** 
   * Constructor for a BB84 qubit.
   * @param bit is the bit
   * @param basis is one of the BB84 basis.
   *
   */

  public BB84QuBit(boolean bit, boolean basis){
      //super();
      setState(bit,basis);
  } 
  
  
  /**
  * Set this qubit to a new BB84 state.
  */
  
  public void setState(boolean bit, boolean basis){
     if(basis == RECTILINEAR){
	if(bit == B0){
	      _a = new Complex(1.0d,0.0d);
	      _b = new Complex(0.0d,0.0d);
	}else{
	      _a = new Complex(0.0d,0.0d);
	      _b = new Complex(1.0d,0.0d);
	}
      }else{
	  double ampl = Math.sqrt(1.0d / 2.0d);
	  if(bit==B0){
	      _a = new Complex(ampl,0.0d);
	      _b = new Complex(ampl,0.0d);
	  }else{
	      _a = new Complex(-ampl, 0.0d);
	      _b = new Complex(ampl,0.0d);
	  }
      }
      _bit  = bit;
      _basis = basis;
  }
  
  /**
  * Complements the bit only.
  * @return the state orgthogonal to this BB84qubit.
  */
  
  public void compl(){
    //super.compl();
    setState(!whichBB84bit(),whichBB84basis());
    //_bit = !_bit;
  }
  
  
  public QuBit copyMe(){
    return new BB84QuBit(whichBB84bit(),whichBB84basis());
  }
  
  /**
  * converts a byte code into a bb84 qubit.
  * @param bytecode is the bytecode for the BB84 qubit.
  * @return the bb84 qubit corresponding to the byte code or
  * null if it does not exist.
  */
  
  public static BB84QuBit convert(byte bytecode){
    BB84QuBit bb84 = null;
    switch(bytecode){
	case Constants.BB840R :
		bb84 = new BB84QuBit(B0,RECTILINEAR);
		break;
	case Constants.BB841R :
		bb84 = new BB84QuBit(B1,RECTILINEAR);
		break;
	case Constants.BB840D :
		bb84 = new BB84QuBit(B0, DIAGONAL);
		break;
	case Constants.BB841D :
		bb84 = new BB84QuBit(B1, DIAGONAL);
		break;
	default:
    }
    return bb84;
  }
  
  /**
  * Converts an array of bytecodes into an array of bb84 qubits.
  * @param bytecodes is the array to convert
  * @return the resulting arrau of qubits.
  */
  
  public static BB84QuBit[] convert(byte[] bytecodes){
    BB84QuBit[] out = new BB84QuBit[bytecodes.length];
    for(int i = 0; i<bytecodes.length; i++){
	out[i] = convert(bytecodes[i]);
    }
    return out;
  }
  
  
  
    /**
    * This static method converts a qubit into a bb84 qubit.
    * It returns the bb84 state that is the closest to the given state.
    * @param qb is the state to convert
    * @return a bb84 state or null if the initial qubit was null
    */
    
    public static BB84QuBit convert(QuBit qb){
	BB84QuBit out = null;
	int bitindexD = 0;
	int bitindexR = 0;
	if(qb != null){
	    boolean bitD = (B0D.inner(qb).norm() < B1D.inner(qb).norm());   
	    boolean bitR = (B0R.inner(qb).norm() < B1R.inner(qb).norm());
	    if(bitD){bitindexD=1;}
	    if(bitR){bitindexR=1;}
	    boolean basis = (DIAG[bitindexD].inner(qb).norm() > RECT[bitindexR].inner(qb).norm());
	    //boolean basis =(new BB84QuBit(bitD,DIAGONAL).inner(qb).norm() > new BB84QuBit(bitR,RECTILINEAR).inner(qb).norm());
	    if(basis == DIAGONAL){
		out = new BB84QuBit(bitD,DIAGONAL);
	    }else{
		out = new BB84QuBit(bitR,RECTILINEAR);
	    }
	}
	return out;
    }
    
    
    
    
    /**
    * This static method converts an array of qubit into an array of BB84Qubit.
    * @param qbs is the array of qubit to convert
    * @return an array of the same dimension of bb84 qubits. If some qubits in the
    * input array are null then the corresponding entry in the output array will also
    * be null.
    */
    
    public static BB84QuBit[] convert(QuBit[] qbs){
	BB84QuBit[] bb84array = new BB84QuBit[qbs.length];
	for(int i = 0; i < qbs.length; i++){
	    bb84array[i] = convert(qbs[i]);
	}
	return bb84array;
    }
    
    /**
    * sends this bb84 qubit.
    * @param pc is the connection. 
    */
    
    public void sendMe(PubConnection pc){
	pc.sendBit(_bit);
	pc.sendBit(_basis);
    }
    
    
    /**
    * Sends a BB84 qubit defined by its byte code.
    */
    
    public static void sendIt(byte bb84code, PubConnection pc){
	boolean bit = B0;
	boolean basis = RECTILINEAR;
	switch(bb84code){
	    case Constants.BB840R:
		bit = B0;
		basis = RECTILINEAR;
		break;
	    case Constants.BB841R:
		bit = B1;
		basis = RECTILINEAR;
		break;
	    case Constants.BB840D:
		bit = B0;
		basis = DIAGONAL;
		break;
	    case Constants.BB841D:
		bit = B1;
		basis = DIAGONAL;
		break;
	    default:     
	}
	pc.sendBit(bit);
	pc.sendBit(basis);
    }
    
    /**
    * Sends a bb84 qubit.
    * @param qb is the bb84 qubit.
    * @param c is the connection.
    */
    
    public static void sendBB84QuBit(BB84QuBit qb, PubConnection c){
	c.sendBit(qb.whichBB84bit());
	c.sendBit(qb.whichBB84basis());
    }
    
    /**
    * Receives a bb84 qubit. That form is used for the
    * qreceiver which knows the alphabet and not the classname.
    * That way by getting an arbitrary element of the aphabet
    * and calling this method the qubit is received correctly.
    * @param c is the connection.
    * @return a the bb84 qubit.
    * 
    */
    
    public QuBit receiveLikeMe(PubConnection c)throws TimeOutException{
	return receiveBB84QuBit(c);
    }
    
    /**
    * Receive  a bb84 qubit.
    * @param is the pub connection from which the qubit is received.
    * @return the new bb84 qubit.
    */
    
    public static BB84QuBit receiveBB84QuBit(PubConnection c)throws TimeOutException{
	boolean bit = c.receiveBit();
	boolean basis = c.receiveBit();
	return new BB84QuBit(bit,basis);
    }

    /**
     * Returns the basis of a BB84 qubit. If the
     * qubit is not of BB84 type then null is returned.
     * @return the basis or null of this qubit is not BB84.
     */

    public boolean whichBB84basis(){
	return _basis;
    }

    /**
     * Returns the bit encoded by a BB84 qubit. If the
     * qubit is not BB84 type then null is returned.
     * @return the bit or null if it is not a bb48 qubit.
     */

    public boolean whichBB84bit(){
	return _bit;
    }


    /**
     * Print on the standard output this bb84 qubit.No linefeed.
     */

    public void print(){
	System.out.println(toString());
    }


    /**
     * Prints the state the long way:a|0>+b|1>.No linefeed.
     */

    public void printLong(){
	super.print();
    }

    /**
    * Returns a string representing this qubit.
    */
    
    public String toString(){
	String s = null;
	if(_basis == RECTILINEAR){
	    if(_bit == B0){
		s = "(0,+)";
	    }else{
		s = "(1,+)";
	    }
	}else{
	    if(_bit == B0){
		s= "(0,X)";
	    }else{
		s= "(1,X)";
	    }
	}
	return s;
    }
    
    /**
    * Returns an html representation of this BB84 qubit.
    * @return the string for the encoding.
    */
    
    public String toHtml(){
	String s = null;
	if(_basis == RECTILINEAR){
	    if(_bit == B0){
		s = "<FONT COLOR=blue>0</FONT>";
	    }else{
		s = "<FONT COLOR=blue>1</FONT>";
	    }
	}else{
	    if(_bit == B0){
		s= "<FONT COLOR=brown>0</FONT>";
	    }else{
		s= "<FONT COLOR=brown>1</FONT>";
	    }
	}
	return s;
    }
    
    
    /**
    * Returns the bytecode for this bb84 state.
    */
    
    public byte byteValue(){
	byte res = -1;
	if(_basis == RECTILINEAR){
	    if(_bit == B0){
		res = Constants.BB840R;
	    }else{
		res = Constants.BB841R;
	    }
	}else{
	    if(_bit == B0){
		res = Constants.BB840D;
	    }else{
		res = Constants.BB841D;
	    }
	}
	return res;
    }
    
    /**
    * Returns a random BB84 qubits.
    */
    public static BB84QuBit random(){
	return new BB84QuBit(Math.random()<0.5001d,Math.random()<0.5001d); 
    }
    
}
