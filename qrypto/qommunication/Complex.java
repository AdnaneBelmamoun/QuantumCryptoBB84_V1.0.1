package qrypto.qommunication;

import java.lang.Math;


/**
 * Quick implementation of complex numbers. 
 */

public class Complex extends Object{

    /**
     * ZERO is the minimum value for an amplitude before being considered as 0.
     */
public final double ZERO = 0.0001;


private double _a = 0.0;
private double _b = 0.0;

  /**
   * Constructor for complex number c=a+bi.
   * @param a is the real part
   * @param b is the imaginary part
   */

  public Complex(double a, double b){
    _a = a;
    _b = b;
  }

  /**
   * Returns the imaginary part.
   * @return the imaginary part.
   */

  public double im(){
    return _b;
  }

  /**
   * Return the real part.
   * @return the real part.
   */

  public double real(){
    return _a;
  }

  /**
   * Returns the norm (the square of the length) of  this complex number.
   * @return a^2+b^2.
   */

  public double norm(){
    return (_a*_a + _b*_b);
  }

  /**
   * Returns the length of this complex number.
   * @param the sqrt if the norm.
   */

  public double length(){
    return Math.sqrt(this.norm());
  }


  /**
   * Returns a complex number that is the normalization
   * of this one.
   * @return the complex number of length one.
   */

  public Complex normalize(){
    double nrm = this.norm();
    return new Complex(_a/Math.sqrt(nrm),_b/Math.sqrt(nrm));
  }


  /**
   * Returns then omplex conjugate of this complex number.
   * @return the complex conjugate of this number.
   */

  public Complex conj(){
    return new Complex(_a,-_b);
  }

  /**
   * Adds a complex number to this one.
   * @param z is the other operand.
   */

  public Complex add(Complex z){
    return new Complex(this.real()+z.real(), this.im()+z.im());
  }

  /**
   * Multiplies this by an integer.
   * @param l is the integer
   * @return the result.
   */


  public Complex mul(int l){
    return new Complex(l*this.real(),l*this.im());
  }

  /**
   * Multiplies this by a real number.
   * @param l is the real number.
   * @return the complex number representing the outcome.
   */

  public Complex mul(double l){
    return new Complex(l*this.real(),l*this.im());
  }


  /**
   * Multiplies a complex number by this one.
   * @param z is the other operand
   * @return the product.
   */
  public Complex mul(Complex z){
    return new Complex(this.real() * z.real()-this.im() * z.im(),
		       this.real()* z.im() + this.im()*z.real());
  } 

    /**
     * Prints to the standard output this complex number. No linefeed..
     */

    public void print(){
       double d = Math.abs(im());
       if(d>ZERO){
	System.out.print(real()+"+ i"+im());
       }else{
	System.out.print(real());
       }
    }


}
