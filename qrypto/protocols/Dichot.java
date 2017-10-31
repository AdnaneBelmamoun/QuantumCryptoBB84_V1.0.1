package qrypto.protocols;

import qrypto.exception.TimeOutException;
import qrypto.qommunication.*;

import java.lang.Math;



/**
 * This class implements the interactive dichotomic search
 * of an error. This is the basic primitive for correcting
 * errors in Cascade. A public connection is required for
 * executing the protocol. Each instance of that class remember
 * the number of bits disclosed over the public channel. This
 * is usefull when privacy amplification takes place. For practical
 * reason, this class is not a subclass of quantum protocol since
 * it has no application by its own. It is rather a procedure
 * that can be called from quantum protocol.
 *
 * @author Louis Salvail (salvail@brics.dk)
 */

public class Dichot extends Object{

private PubConnection _pc;
private boolean[] _block;
private int _disclose;

  public Dichot(boolean[] block, PubConnection pc){
    _pc = pc;
    _block = block;
    _disclose = 0;
  }

  /**
   * Test whether or not the parity of this block is the same
   * for both parties. 
   * @param initiator is true if the protocol on the initiator side. 
   * Otherwise initiator os false.
   * @return true iff at least one error exists
   * @exception TimeOutException is thrown as usual.
   */

  public boolean test(boolean initiator)throws TimeOutException{
    boolean ans = false;
    if(initiator){ans = testI();}
    else{ans = testR();}
    return ans;
  }


  /**
   * This method search for an error in a odd-parity
   * block. 
   * @param initiator is true if the protocol is run 
   * on initiator side and is false otherwise.
   * @return the position in the block where the error
   * has been found. If the method is called on an even-parity
   * block then the outcoome is unpredictable.
   * @exception TimeOutException is thrown as usual.
   */

  public int search(boolean initiator)throws TimeOutException{
    int ans = -1;
    if(initiator){ans = searchI();}
    else{ans=searchR();}
    return ans;
  }





  /**
   * This returns the number of bits transmitted over the
   * public channel about the original bit string. All those
   * bits of information are parity bits. Knowing the number
   * of parity bits disclosed to an eavesdropper allows to
   * remove them during privacy amplification. 
   * @return the number of parity bits disclosed to a potential
   * eavesdropper so far. 
   */

  public int disclosed(){
    return _disclose;
  }

  /**
   * This method implements the test method on the initiator side.
   * @return true iff an error can be corrected.
   */
 
  private boolean testI()throws TimeOutException{
    boolean p = parity();
    _pc.sendBit(p);
    boolean q = _pc.receiveBit();
    _disclose++;
    return (p != q);
  }

  /**
   * This method implements the test method on the responder side.
   * @return true iff an error can be corrected.
   */

  private boolean testR()throws TimeOutException{
    boolean p = _pc.receiveBit();
    boolean q = parity();
    _pc.sendBit(q);
    _disclose++;
    return (p != q);
  }



  /**
   * Search an error in an odd error parity block on the initiator
   * side.
   * @return the position in the block where the error was found.
   */

  private int searchI()throws TimeOutException{
    int j = _block.length - 1;
    int i = 0;
    while(i<j){
      int mid = (int)Math.floor((double)(i+j)/(double)2);
      boolean p = parity(i,mid);
      _pc.sendBit(p);
      boolean q = _pc.receiveBit();
      if(p == q){
	i=mid+1;
      }else{
	j=mid;
      }
      _disclose++;
    }
    //    boolean bb = _pc.receiveBit();
    //    if(bb == _block[j]){System.out.println("BUG IN SEARCH, 'Dichot.java'.");}
    return j;
  }



  /**
   * Search an error in an odd error parity block on the responder
   * side.
   * @return the position in the block where the error was found.
   */


  private int searchR()throws TimeOutException{
    int j = _block.length - 1;
    int i = 0;
    while(i<j){
      int mid = (int)Math.floor((double)(i+j)/(double)2);
      boolean q = parity(i,mid);
      boolean p = _pc.receiveBit();
      _pc.sendBit(q);
      if(p == q){
	i=mid+1;
      }else{
	j=mid;
      }
      _disclose++;
    }
    // _pc.sendBit(_block[j]);
    return j;
  }


  /**
   * Returns the parity of the block between two indices.
   * @param i is the left index 
   * @param j is the right index
   * @return false iff the number of true in b_i+b_{i+1}+...+b_j is even.
   */

  private boolean parity(int i, int j){
    boolean ans = _block[i];
    for(int h=i+1; h <= j; h++){
      ans = ans^_block[h];
    }
    return ans; 
  }

  /**
   * Returns the parity of the whole block.
   * @return false iff the number of true is even 
   */
  
  private boolean parity(){
    return parity(0,_block.length-1);
  }

}




