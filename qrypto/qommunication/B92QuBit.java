package qrypto.qommunication;


import java.lang.Boolean;
import java.lang.Math;


public class B92QuBit extends BB84QuBit
{ 

//public static final boolean BIT0 = false;
//public static final boolean BIT1 = true; 

boolean _B92bit = B0;
boolean _undefined = true;
   

/**
* Constructor for an undefined B92 qubit.
*/

public B92QuBit(){
    super();
    _undefined = true;
}



  /** 
   * Constructor for a B92 qubit.
   * @param bit is the bit
   * @param basis is one of the BB84 basis.
   *
   */

  public B92QuBit(boolean bit){
      super(B0, bit);
      _B92bit  = bit;
      _undefined = false;
  } 
  
  /**
  * Clones this B92 qubit.
  * @return a qubit in the same state than this one. If the original
  * was undefined that one is undefined as well.
  */
  public QuBit copyMe(){
    B92QuBit q = null;
    if(isUndefined()){q = new B92QuBit();}
    else{
	q = new B92QuBit(whichB92bit().booleanValue());
    }
    return q;
  }
  
  /**
  * Returns whether or not this qubit is defined. 
  * @return true if it is undefined.
  */
  
  
  public boolean isUndefined(){
    return _undefined;
  }
  
  
  /**
  * Sets the bit value for that qubit. If the qubit was undefined
  * then it becomes defined.
  * @param b is the bit value. If b is null then the value is unchanged.
  */
  
  public void setBit(boolean b){
     setState(B0,b);
     _undefined = false;
     _B92bit = b;
     
  }
  
  
  /**
  * Converts a bytecode into a B92 qubit.
  * @param the bytecode to be converted.
  * @return the corresponding B92 qubit or null if it does not exist.
  */
  
  public static BB84QuBit convert(byte bytecode){
    B92QuBit b92 = null;
    switch(bytecode){
	case Constants.B92U:
		b92 = new B92QuBit();
		break;
	case Constants.BB840R:
		b92 = new B92QuBit(B0);
		break;
	case Constants.BB840D:
		b92 = new B92QuBit(B1);
		break;
	default:
    }
    return b92;
  }
  
  
  
    /**
    * This static method converts a qubit into a b92 qubit.
    * It returns the b92 state that is the closest to the given state.
    * @param qb is the state to convert
    * @return a bb84 qubit encoding a b92 state or null if the initial qubit was null
    */
    
    public static BB84QuBit convert(QuBit qb){
	B92QuBit out = new B92QuBit();
	@SuppressWarnings("unused")
	boolean b = B0;
	if(qb != null){
	   if(new B92QuBit(B0).inner(qb).norm() < new B92QuBit(B1).inner(qb).norm()){
	    out.setBit(B1);
	   }
	}
	return out;
    }
    
    /**
    * This static method returns the B92 qubit resulting from
    * a VN measurement in one of the BB84 basis. If the result
    * is bit 0 then the B92 qubit will be undefined. If the result
    * is the classical bit 1 then the resulting B92 qubit is the
    * one corresponding to the complementary basis to the one used
    * in the BB82 VN measurement.
    * @param q is the BB84 qubit representing the outcome of the VN measurement.
    * @return the b92 qubit corresponding to that outcome. The B92 qubit will
    * be undefined if the result of the BB84 measruement was classical bit 0.
    */
    
    public static B92QuBit convertFromMeasurement(BB84QuBit q){
	B92QuBit out = new B92QuBit();
	out.setState(q.whichBB84bit(),q.whichBB84basis());
	if(q.whichBB84bit() == B1){
	    boolean b = B1;
	    if(q.whichBB84basis() == DIAGONAL){
		b = B0;
	    }
	    out.setBit(b);
	}
	return out;
    }
    
    /**
    * This method converts a BB84 qubit that encodes a B92 qubit into
    * a genuine B92 qubit. Attention not to confuse this method with
    * convertFromMeasurement since their behaviour is completely
    * different (the opposite). Here, |1>_+ and |1>_X are converted into undefined
    * B92 qubits. States |0>_+ and |0>_X are converted into B92 0 and
    * 1 respectively.
    * @param q is the BB84 qubit to convert.
    * @return the related B92 qubit.
    */
    
    public static B92QuBit convertFromBB84(BB84QuBit q){
	B92QuBit out = new B92QuBit();
	out.setState(q.whichBB84bit(),q.whichBB84basis());
	if(q.whichBB84bit() == B0){
	    if(q.whichBB84basis() == RECTILINEAR){
		out.setBit(B0);
	    }else{
		out.setBit(B1);
	    }
	}
	return out;
    }
    
    
    /**
    * This static method converts an array of qubit into an array of BB84Qubit.
    * @param qbs is the array of qubit to convert
    * @return an array of the same dimension of bb84 qubits encoding B92 qubits. 
    * If some qubits in the
    * input array are null then the corresponding entry in the output array will also
    * be null.
    */
    
    public static BB84QuBit[] convert(QuBit[] qbs){
	B92QuBit[] b92array = new B92QuBit[qbs.length];
	for(int i = 0; i < qbs.length; i++){
	    b92array[i] = (B92QuBit)convert(qbs[i]);
	}
	return b92array;
    }
    
    
    /**
    * Converts into B92 qubits an array of measurement in the BB84 bases.
    * @param q is the array of BB84 state representing the outcome of the 
    *   measurements.
    * @return the converted B92 array of qubits.
    */
    
    public static B92QuBit[] convertFromMeasurement(BB84QuBit[] q){
	B92QuBit[] b92array = new B92QuBit[q.length];
	for(int i = 0; i < q.length; i++){
	    b92array[i] = convertFromMeasurement(q[i]);
	}
	return b92array;
    }
    
    /**
    * This convert an array of BB82 qubits into B92 qubits.
    * @param q is the BB82 array to convert.
    * @return the corresponding array of B92 qubits.
    */
    
    public static B92QuBit[] convertFromBB84(BB84QuBit[] q){
	B92QuBit[] b92array = new B92QuBit[q.length];
	for(int i= 0; i<q.length; i++){
	    b92array[i] = convertFromBB84(q[i]);
	}
	return b92array;
    }
    

  
    /**
     * Returns the bit encoded by a B92 qubit. 
     * @return the bit or null if this qubit has undefined bitvalue.
     */

    public Boolean whichB92bit(){
	Boolean r = null;
	if(!isUndefined()){
	    r = new Boolean(_B92bit);
	}
	return r;
    }


    /**
     * Print on the standard output this bb84 qubit.No linefeed.
     */

    public void print(){
	System.out.print(toString());
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
	if(isUndefined()){
	    s="?";
	}else if(_B92bit == B0){
		s= "0";
	      }else{
		s= "1";
	      }
	return s;
    }
    
    /**
    * Returns the bytecode for this B92 state. This is one of the
    * following: Constants.B92U, Constants.BB840R, and Constants.BB840D.
    */
    
    public byte byteValue(){
	byte res = -1;
	if(isUndefined()){
	    res = Constants.B92U;
	}else if(whichB92bit().booleanValue() == B0){
		res = Constants.BB840D;
	      }else{
		res = Constants.BB840R;
	}
	return res;
    }
    
    /**
    * Returns an html representation of this B92 qubit.
    * @return the string for the encoding.
    */
    
    public String toHtml(){
	String s = null;
	if(!isUndefined()){
	    if(whichB92bit().booleanValue()){
		s = "<FONT COLOR=darkgreen>1</FONT>";
	    }else{
		s = "<FONT COLOR=darkgreen>0</FONT>";
	    }
	}else{
	    if(whichBB84basis()==RECTILINEAR){
		s= "<FONT COLOR=blue><tt>?</tt></FONT>";
	    }else{
		s= "<FONT COLOR=darkred><tt>?</tt></FONT>";
	    }
	}
	return s;
    }    
    
    
    
    /**
    * Returns a random BB84 qubits.
    */
    public static BB84QuBit random(){
	return new B92QuBit(Math.random()<0.5001d); 
    }
    


}
