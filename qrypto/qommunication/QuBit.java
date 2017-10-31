package qrypto.qommunication;


import qrypto.exception.*;

public class QuBit extends Object{

  /** The real and imaginary parts */
 
protected Complex _a=null;
protected Complex _b=null;


/**
* Constructor for a new qubit in the standard state |0>.
*/


public QuBit(){
 _a = new Complex(1.0d, 0.0d);
 _b = new Complex(0.0d, 0.0d);
}




  /**
   * Constructor of a qubit in state a|0>+b|1>.
   * If the input numbers do not defined a normalized vector
   * then it is normalised.
   * @param a is the amplitude associated to |0>
   * @param b is the amplitude associated to |1>
   */

  public QuBit( Complex a, Complex b){
    double l = a.norm()+b.norm();
    _a = new Complex(a.real()/l,a.im()/l);
    _b = new Complex(b.real()/l,b.im()/l);
  }

  /**
   * Returns the amplitude associated to |0> in this state.
   * @return the amplitude associated to |0>. 
   */

  public Complex part0(){
    return _a;
  }

  /**
   * Returns the amplitude associated to |1> in this state.
   * @return the amplitude associated to |1>.
   */

  public Complex part1(){
    return _b;
  }

  /**
   * Returns the state orthogonal to that one.
   * @return the state 
   */

  public QuBit neg(){
    return new QuBit((this.part1()).mul(-1),this.part0());    
  }
  
  /**
  * Rotate by 90deg this state. It does tha same as neg() but modifies
  * this qubit state.
  */
  
  public void compl(){
      double tr = _a.real();
      double ti = _a.im();
      //Complex t  = new Complex(_a.real(),_a.im());
      _a = _b.mul(-1);
      _b = new Complex(tr,ti);
  }
  
  /**
  * Copy this qubit into a new one.
  * @return a new qubit in the same state.
  */
  
  public QuBit copyMe(){
    return new QuBit(part0(),part1());
  }
  
  
 
  
  /**
   * Returns the inner product or overlap between
   * two qubit. If this = a|0>+b|1> and q=c|0>+d|1> then
   * the inner product is a*c.conj()+ b*d.conj().
   * @see Complex
   * @param q is the second operand.
   * @return the complex numbere representing the
   *         inner product between this and q.
   */

  public Complex inner(QuBit q){
    Complex p=null;
    Complex r=null;
    Complex qa = q.part0();
    Complex qb = q.part1();
    p = (this.part0()).mul(qa.conj());
    r = (this.part1()).mul(qb.conj());
   return p.add(r);
  }


  /**
   * Returns the (simulated) outcome of measuring this
   * qubit with a Von Neumann measurement.
   * @param obs is the state of one observable of a VN measurement
   * in the two-dimensional hilbert space.
   * @return true if the outcome corresponds to the given
   * observable.
   */

  public boolean measureVN(QuBit obs){
    boolean r = false;
    Complex inprod = this.inner(obs);
    double prob = inprod.norm();
    if(Math.random()<prob){
      r=true;
    }
    return r;
  }
  
  /**
  * sends this qubit.
  * @param pc is the connection.
  */
  
  public void sendMe(PubConnection pc){
    sendQuBit(this,pc);
  }
  
  
  /**
  * Allows to send the description if a qubit through a public connection.
  * The connection must be established before the call.
  * @param qb is the qubit to be sent
  * @param pc is the connection by which the qubit is sent.
  * @return true iff the transmission is successfull.
  */
  
  public static void sendQuBit(QuBit qb,PubConnection pc ){
    pc.sendString(Double.toString(qb.part0().real()));
    pc.sendString(Double.toString(qb.part0().im()));
    pc.sendString(Double.toString(qb.part1().real()));
    pc.sendString(Double.toString(qb.part1().im()));
  }
  
  /**
  * Allows to receive a qubit from a public connection. The connection
  * must be established before the call.
  * @param pc is the pub connection from which the qubit is received.
  * @return the received qubit. Null if no well-formed qubit has been received.
  * @exception TimeOutException when a timeout occurs before receiving the qubit.
  * @exception QuBitFormatException when what has been received is not a well-formed qubit. 
  */
  public static QuBit receiveQuBit(PubConnection pc)throws TimeOutException, QuBitFormatException{
    QuBit qb = null;
    try{
	double c0r = Double.valueOf(pc.receiveString()).doubleValue();
	double c0i = Double.valueOf(pc.receiveString()).doubleValue();
	double c1r = Double.valueOf(pc.receiveString()).doubleValue();
	double c1i = Double.valueOf(pc.receiveString()).doubleValue();
	qb = new QuBit(new Complex(c0r,c0i), new Complex(c1r,c1i));
    }catch(NumberFormatException nfe){
	throw new QuBitFormatException(nfe.getMessage());
    }
    return qb;
  }
  
  
   /**
    * Receives a qubit. That form is used for the
    * qreceiver which knows only the alphabet and not the classname.
    * That way by getting an arbitrary element of the aphabet
    * and calling this method the qubit is received correctly.
    * @param c is the connection.
    * @return a the bb84 qubit.
    * 
    */
    
    public QuBit receiveLikeMe(PubConnection c)throws TimeOutException{
	return receiveQuBit(c);
    }

    /**
     * Print to the standard output the state if this qubit. No linefeed added.
     */

    public void print(){
	Complex c0 = part0();
	Complex c1 = part1();
	c0.print();
	System.out.print("|0>+");
	c1.print();
	System.out.print("|1>");
    }
    
    /**
    * Returns a string representing this qubit.
    * @return the string for this qubit.
    */
    
    public String toString(){
	String s ="("+Double.toString(part0().real())+"+ i"
	            + Double.toString(part0().im())+")|0>" +
		  "+("+Double.toString(part1().real())+ "+ i"
		    + Double.toString(part1().im())+")|1>";
	return s;
    }
    
    /**
    * Produces an html output for this qubit.
    * @return the string encoding the html output. 
    */
    
    public String toHtml(){
	return toString();
    }
    
  /**
   * Translate an array of qubit in HTML.
   * @param qb is the array of qubit. 
   * @param width is the number of groups of 5 bits per row.
   * @param skip is the number ofo rows before skipping one.
   * @return the html string for that array.
   */
    
  public static String array2Html(QuBit[] qb,int width, int skip){
    int LARGER_SPACE_EVERY_X_BLOCK = Math.min(5,width);
    int blockbeforeCR = 0;
    StringBuffer s = new StringBuffer("<BR><BR><B>");
    for(int i = 0; i<qb.length;i++){
	s.append(qb[i].toHtml());
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    s.append("&nbsp;");
	    blockbeforeCR++;
	    if(blockbeforeCR == LARGER_SPACE_EVERY_X_BLOCK){
	       s.append("&nbsp;&nbsp;&nbsp;");
	       blockbeforeCR = 0;
	    }
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width))<0.5d){
	    s.append("<BR>\n");
	    blockbeforeCR = 0;
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width*skip))<0.5d){
	    s.append("<BR>\n");
	}
    }
    s.append("</B><BR>");
    return s.toString();
  }
  
  
  /**
   * Translate an array of qubit in HTML with striked positions.
   * @param qb is the array of qubit. 
   * @param width is the number of groups of 5 bits per row.
   * @param skip is the number ofo rows before skipping one.
   * @param isOK is sucht that if isOK[i]=true then position i is not
   * striked.
   * @return the html string for that array.
   */
    
  public static String array2Html(QuBit[] qb,int width, int skip,boolean[] isOK ){
    int LARGER_SPACE_EVERY_X_BLOCK = Math.min(5,width);
    int blockbeforeCR = 0;
    StringBuffer s = new StringBuffer("<BR><BR><B>");
    for(int i = 0; i<qb.length;i++){
	if(!isOK[i]){s.append("<S>");}
	s.append(qb[i].toHtml());
	if(!isOK[i]){s.append("</S>");}
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    s.append("&nbsp;");
	    blockbeforeCR++;
	    if(blockbeforeCR == LARGER_SPACE_EVERY_X_BLOCK){
	       s.append("&nbsp;&nbsp;&nbsp;");
	       blockbeforeCR = 0;
	    }
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width))<0.5d){
	    s.append("<BR>\n");
	    blockbeforeCR = 0;
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width*skip))<0.5d){
	    s.append("<BR>");
	}
    }
    s.append("</B><BR>");
    return s.toString();
  }

}













