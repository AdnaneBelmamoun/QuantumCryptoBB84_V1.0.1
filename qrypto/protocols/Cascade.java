package qrypto.protocols;

import qrypto.exception.*;
import qrypto.qommunication.*;



import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;
import java.util.Vector;



public class Cascade extends CProtocol{

public static int MAX_FRAC_FOR_BLOCKSIZE = 4; //no blocksize of more than n/4
private  static final String PROT_ID = "Cascade";
private  static String _TITLE = "Cascade";
private  static final String _RECONCILIATION = "Cascade Configuration:";
private  static final String _NUMBER_OF_PASSES = "Number of passes: ";
private  static final String _INIT_BLOCKSIZE   = "Initial block size: ";


//these constants are non-sense but ok for the time being.
 
public static int DEF_NUMBER_PASSES = 4;
public static int DEF_BLOCK_SIZE = 10;


private int _n = 0;
private int _passes = 0;
private int _basicBlockSize = 0;
private int[][] _permutations;
private int[] _blocksize;
  /** is the cureent array of bits */
private boolean[] _current;
@SuppressWarnings("unused")
private boolean[] _original;
private int _disclosed = 0;
private int[] _passDisclosed;
private int[] _errors;
private int _allErrors = 0;

@SuppressWarnings("rawtypes")
private Vector _fixedPositions; 


private JTextField _tfPasses = null;
private JTextField _tfBlockSize = null;





/**
* Constructor prior to user-configuration.
* 
*/

@SuppressWarnings("rawtypes")
public Cascade(){
    newProtID(PROT_ID);
    _disclosed = 0;
    _allErrors = 0;
    _passes = DEF_NUMBER_PASSES;
    _basicBlockSize = DEF_BLOCK_SIZE;
    //setParam(_passes,_basicBlockSize);
    _fixedPositions = new Vector();
}


 

  public Cascade(boolean[] bits, int passes, int blocksize){
    this();
    setParam(bits,passes,blocksize);
  }
  
  
  
  public void setParam(boolean[] bits, int passes, int blocksize){
    //setParam(passes,blocksize);
    setParam(bits);
    _passes = passes;
    _errors = new int[_passes];
    for(int i = 0; i<passes; i++){
      _errors[i] = 0;
    }
    _basicBlockSize = blocksize;
    _blocksize = new int[_passes];
    _blocksize[0] = blocksize;
    for(int i=1; i<_passes;i++){
      if(_blocksize[i-1]< (bits.length/MAX_FRAC_FOR_BLOCKSIZE)){ 
	 _blocksize[i] = 2*_blocksize[i-1];
      }else{
	 _blocksize[i] = _blocksize[i-1];
      }
    }   
  }
  
  /**
  * Sets the bit array to be reconcilied.
  * @param bits are the bits to be corrected using Cascade.
  */
  
  private void setParam(boolean[] bits){
    _n = bits.length;
    _original = bits;
    _current = new boolean[_n];
    for(int i=0;i<_n;i++){
      _current[i] = bits[i];
    }
  }
 
  /**
  * Sets the number of passes and the initial block size.
  * @param passes is the number of passes.
  * @param blocksize is the initial blocksize.
  */
  
  private void setParam(int passes, int blocksize){
    _passes = passes;
    _errors = new int[_passes];
    for(int i = 0; i<passes; i++){
      _errors[i] = 0;
    }
    _basicBlockSize = blocksize;
    _blocksize = new int[_passes];
    _blocksize[0] = blocksize;
    for(int i=1; i<_passes;i++){
      _blocksize[i] = 2*_blocksize[i-1];
    }
  }
  
  
  /**
  * Allows to reset this subprotocol for a new execution.
  * This is used whenever the same cascade object is used for
  * several executions. The internal data will forget the previous
  * executions if this method is called. If the number of passes and/or
  * the initial blocksize are not set then this method does nothing.
  */
  
  public void reset(){
    if((_errors != null) && (_passes >0)){
	for(int i = 0; i< _passes; i++){
	    _errors[i] = 0;
	}
	_allErrors = 0;
	_disclosed = 0;
	_fixedPositions.removeAllElements();
    }
  }

  /**
  * Allows to redefine the default initial block size. This does not
  * change the initial block size if an object of that class. The default
  * value is used for the user configuration.
  * @param size is the new default initial block size.
  */

  public static void setDefBlockSize(int size){
    DEF_BLOCK_SIZE = size;
  }
  
  /**
  * Returns the number of bits to correct.
  * @return the number of bits in input
  */
  
  public int size(){
    return _n;
  }
  

  /**
  * Allows to redefine the default value for the number of passes.This does not change the
  * number of passes if an object of that class. The default value is
  * used for the user configuration.
  * @param passes is the new default number of passes 
  */
  
  
  public static void setDefNumberOfPasses(int passes){
    DEF_NUMBER_PASSES = passes;
  }
  
  
  /**
  * Allows to set the default initial block size given an estimate on the
  * probability of errors for each bit.
  * @param p is the probility of error for each bit that will be corrected.
  * For that method to work p must be asuch that 0<p<1/2.
  * @return the computed default initial block size. The value -1 is returned
  * if the value of p was not appropriate.
  */
  
  public static int setDefBlockSize(float p){
    int s = -1;
    if ((p<0.5f) && (p>0.0)){
	s = Math.round(1.5f/p);
	setDefBlockSize(s);
    }
    return s;
  }
  
  
  /**
  * Returns the observed error-rate.
  * @return the number of errors found divided by the plain key length
  */
  
  public float obsErrorRate(){
    return new Integer(errorsFound()).floatValue()/new Integer(_n).floatValue();
  }
  
 

  /**
   * Implements cascade on initiator side.
   * @exception TimeOutException is thrown as usual.
   */


  public synchronized void initiator()throws QryptoException{
    int prev_disclosed = 0;
    _allErrors = 0;
    resetProgressTools(totalNumberOfBlocks(_passes,_blocksize,_n),_TITLE);
    _passDisclosed = new int[_passes];//record the disclosed for each pass
    _permutations = new int[_passes][_n];
    for(int i=0; i<_passes;i++){
      _permutations[i] = newPermutation(_n);
      sendPerm(_permutations[i]);
    }
    try{
      for(int i = 0; i<_passes;i++){
	 _pc.sendInt(i);
	 int numberOfBlock = numberOfBlocks(_n,_blocksize[i]);
	 for(int h = 0; h<numberOfBlock; h++){
	   //_pc.sendInt(h);
	   cascade(h,i, true);
	   addProgress();
	  }
	  _passDisclosed[i] = _disclosed-prev_disclosed;
	  prev_disclosed = _disclosed;
       }
   }catch(RuntimeException rt){
      throw new QryptoException("Cascade ran into trouble "+rt.getMessage()+"####"+rt.toString());
   }
  }
  
  
  
  
  /**
   * Implements cascade on the responder side.
   * @exception TimeOutException is thrown as usual.
   */

  public synchronized void responder() throws QryptoException{
    int prev_disclosed = 0;
    _allErrors = 0;
    resetProgressTools(totalNumberOfBlocks(_passes,_blocksize,_n),"Cascade");
    _permutations = new int[_passes][_n];
    _passDisclosed = new int[_passes];//record the disclosed bits for each pass.
    for(int i=0; i<_passes;i++){
      _permutations[i] = receivePerm();
    }
    try{
      for(int i = 0; i<_passes;i++){
	@SuppressWarnings("unused")
	int ii = _pc.receiveInt();
	int numberOfBlock = numberOfBlocks(_n,_blocksize[i]);
	for(int h = 0; h<numberOfBlock; h++){  
	  //int hh = _pc.receiveInt();
	  //if(h != hh){throw new RuntimeException("problem at block "+h+" pass "+i);}
	  cascade(h,i, false);
	  addProgress();
	}
	_passDisclosed[i] = _disclosed-prev_disclosed;
	prev_disclosed = _disclosed;
      }
    }catch(RuntimeException rt){
      throw new QryptoException("Cascade ran into troubles "+rt.getMessage()+"##"+rt.toString());
    }
  }

  
  
  /**
  * Returns the total numbers blocks generated in the given
  * number of passes and blocksize.
  * @param passes is the number of passes.
  * @param blocksize is the blocksize for each pass.
  * @param n is the total number of bits 
  * @return the total number of blocks generated in "passes" number of
  * passes and "blocksize" for each pass.
  */
  
  public static int totalNumberOfBlocks(int passes, int[] blocksize, int n){
    int res = 0;
    for(int i = 0; i<passes; i++){
	res = res + numberOfBlocks(n,blocksize[i]);
    }
    return res;
  }
  
  /**
  * Returns the number of blocks in a pass involving n bits using
  * blocks of given size.
  * @param n is the number of bits.
  * @param blockSize is the blocksize.
  * @return the number of blocks for that pass.
  */
  
  public static int numberOfBlocks(int n, int blockSize){
    int res = n/ blockSize;
    if((n % blockSize) != 0){
	res++;
    }
    return res;
  }
  
  /**
  * Returns the final bit string one reconcilied.
  * @return the reconcilied bit string.
  */
  
  public boolean[] getReconciliedKey(){
    return _current;
  }
  
  
  /**
  * Returns the initial block size.
  * @return the initial block size.
  */
  
  public int getInitialBlockSize(){
    return _basicBlockSize;
  }


  /**
  * Returns the nummber of passes.
  * @return the number of passes.
  */
  
  public int getNumberOfPasses(){
    return _passes;
  }
  
  /**
  * Returns the shannon bound on the number of parity bits
  * for correcing a string of length n with error-rate e.
  * @param e is the probability of error.
  * @param n is the length of the string.
  * @return the Shannon bound nh(e).
  */
  
  public static float shannonBound(float e, int n){
    double ee = new Float(e).doubleValue();
    double h = -ee*(Math.log(ee)/Math.log(2))-(1-ee)*(Math.log(1-ee)/Math.log(2));
    return new Double(n*h).floatValue();
  }
  
  
  /**
  * Returns an html string giving the results of the execution.
  * @return the html string
  */
  
  public String result2HTML(){
    StringBuffer s = new StringBuffer();
    s.append("<em>Cascade</em> fixed a total of <b>"+Integer.toString(errorsFound())+
	     "</b> errors revealing <b>"+Integer.toString(disclosed())+"</b> parity bits, where:\n");
    s.append("<ul>\n");
    for(int i=0; i<getNumberOfPasses();i++){
      s.append("<li><b>"+Integer.toString(errorsFound(i))+
       "</b> errors were corrected in pass <b>"+Integer.toString(i)+"</b>"+
       " using block of size <b>"+Integer.toString(_blocksize[i])+"</b> revealing <b>"+Integer.toString(disclosed(i))+"</b> parity bits.");
    }
    s.append("</ul>\n");
    return s.toString();
  }
  
  /**
  * Returns the resulting bits in HTML format with indication
  * on which positions were corrected.
  * @return the html string.
  */
  
  public String resultBits2HTML(){
  
    boolean[] key = getReconciliedKey();
    byte[] mark = new byte[key.length];
    for(int i = 0; i<key.length;i++){
	if(fixedPositions().contains(new Integer(i))){
	    mark[i] = 1;
	}else{
	    mark[i] = 0;
	}
    }
    return Constants.arrayBool2HTML(key,false,10,5,mark,"darkgreen","darkred");
  }


  /**
   * Produces an output for debugging purposes. The statistics
   * are also indicated.
   */

  public void output(){
    System.out.println("---Cascade Statistics---");
    System.out.println("There were "+errorsFound()+" errors found and corrected:");
    for(int i=0; i<_passes;i++){
      System.out.println("       * "+errorsFound(i)+" in pass "+i);
    }
    System.out.println("The total number of disclosed bits is "+disclosed());
    System.out.println("The Resulting string is:");
    for(int i = 0; i<_n; i++){
      System.out.println("bit #"+i+" is "+_current[i]);
    }
  }



 /**
   * Produces an output that is appended to a stream.
   * @param stream is the stream on which the output is written
   */

  public void output(PrintWriter stream){
    stream.println("---Cascade Statistics---");
    stream.println("There were "+errorsFound()+" errors found and corrected:");
    for(int i=0; i<_passes;i++){
      stream.println("       * "+errorsFound(i)+" in pass "+i);
    }
    stream.println("The total number of disclosed bits is "+disclosed());
  }

  /**
   * Returns the number of parity bits exchanged during  the last
   * execution of the protocol. If this protocol is executed as a thread,
   * make sure that this method is called once the execution is resumed.
   * Otherwise, the value returned will be the current one in the execution.
   * @return the number of parity bits about the initial bitstring transmitted 
   * through the public channel.  
   */

  public int disclosed(){
    return _disclosed;
  }
  
  /**
  * Returns the number of patiry bits revealed at a given pass.
  * @param i is the pass number [0..maxpass-1]
  * @return the number of revealed parity bits for pass i. 
  */
  
  public int disclosed(int i){
    return _passDisclosed[i];
  }
  
  /**
  * Returns the vector containing all fixed positions.
  * @return the fixed positions.
  */
  
  @SuppressWarnings("rawtypes")
public Vector fixedPositions(){
    return _fixedPositions;
  }



  public int errorsFound(int pass){
    int ans = 0;
    if(( pass >= 0) && (pass < _passes)){
      ans = _errors[pass];
    }
    return ans;
  }


  /**
   * Returns the total number of errors found during the execution
   * of the protocol.
   * @return the total number of errors found and corrected.
   */

  public int errorsFound(){
    int ans = 0;
    for(int i=0;i<_passes; i++){
      ans = ans + errorsFound(i);
    }
    return ans;
  }


  /**
   * Generates a new random permutation of the integers between [0..n-1].
   * @param n is such that a random permutation between the numbers 
   * in [0..n-1] will be generated.
   * @return an array p[] such that p[0],p[1],...,p[n-1] is the new permutation.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
private int[] newPermutation(int n){
    Vector v = new Vector(n);
    int[] perm  = new int[n];
    for(int i = 0; i<n; i++){
      v.addElement(new Integer(i));
    }
    Random r = new Random();
    int rp = 0;
    Integer rim = null;
    for(int i = 0; i<n; i++){
      rp = Math.abs(r.nextInt()) % v.size();
      rim = (Integer)v.elementAt(rp);
      perm[i] = rim.intValue();
      v.removeElementAt(rp);
    } 
    return perm;
  }


  /**
   * Sends a permutation through the public connection.
   * @param p is the permutation
   */

  private void sendPerm(int[] p){
    for(int i = 0; i<_n; i++){
      _pc.sendInt(p[i]);
    }
  }

  /**
   * Receives a permutation and returns it. The exepected
   * permutation is [0.._n-1] into [0.._n-1].
   * @return the permutation.
   */

  private int[] receivePerm()throws TimeOutException{
    int[] ans = new int[_n];
    for(int i = 0; i<_n; i++){
      ans[i] = _pc.receiveInt();
    }
    return ans;
  }


  /**
   * Prints a permutation for testing.
   */

  @SuppressWarnings("unused")
private void printPerm(int[] p){
    for(int i =0; i<_n; i++){
      System.out.println("Receive perm:p["+i+"]="+p[i]);
    }
  }




  /**
   * Performs a cascade from a new block. All blocks prior to that one in the current pass
   * and all blocks in the previous passes have error parity 0.
   * @param blkindex is the block index where the new error was found (staring at 0)
   * @param pass is the current pass number (starting at 0)
   * @param bits is the array of bits for the current pass. 
   * @param perms is the permutations applied for each pass
   *
   */

  @SuppressWarnings("unchecked")
private synchronized void cascade(int blkindex ,int pass, boolean initiator)
       throws QryptoException{
 
    boolean[] block = getBlock(blkindex,pass);
    Dichot d = new Dichot(block, _pc);
    if(d.test(initiator)){
      //we correct the new error found.
      int errorpos = d.search(initiator);
      int realcurrentpos = errorpos + (blkindex * _blocksize[pass]);
      int basicPos = invert(realcurrentpos, _permutations[pass]);
      if(initiator){
	 _current[basicPos] = !_current[basicPos];
      }
      _fixedPositions.addElement(new Integer(basicPos));
      _errors[pass] = _errors[pass]+1;
      _allErrors++;
      setProgressLabel(_TITLE+" fixed "+Integer.toString(_allErrors));
      BlockStack stack = new BlockStack(pass,_permutations,_blocksize);
      stack.addBlocks(basicPos, pass, -1);
      // We now empty the stack by finding new errors in the smallest
      // blocks in the stack.
      while(!stack.empty()){
	  int[] smallerBlock = stack.getSmallestBlock();
	  block = getBlock(smallerBlock[0], smallerBlock[1]);
	  Dichot newBadBlock = new Dichot(block, _pc);
	  errorpos = newBadBlock.search(initiator);
	  _errors[pass] = _errors[pass]+1;
	  _allErrors++;
	  setProgressLabel(_TITLE+" fixed "+Integer.toString(_allErrors));
	  if(errorpos >= 0){
	    realcurrentpos = errorpos + (smallerBlock[0]*_blocksize[smallerBlock[1]]);
	    basicPos = invert(realcurrentpos, _permutations[smallerBlock[1]]);
	  }else{
	    throw new RuntimeException("Cascade is running into troubles.");
	  }
	  if(initiator){_current[basicPos] = !_current[basicPos];}
	  _fixedPositions.addElement(new Integer(basicPos));
	  stack.addBlocks(basicPos,pass,blkindex);
	  _disclosed = _disclosed + newBadBlock.disclosed();
      }
    }
    _disclosed = _disclosed + d.disclosed();
  }
  
  
  /**
  * For debugging.
  * @param s the string to print
  * @param initiator coming from initiator or responder
  */
  
  public static void trace(String s, boolean initiator){
    System.out.print(s+":");
    if(initiator){
	System.out.println("INIT");
    }else{
	System.out.println("RESP");
    }
  }


   /**
    * Allows to add the configuration GUI objects into a JOPtionPane dialog.
    * It is used by superprotocols in order to allow the user to configurate 
    * the subprotocols.
    */
    
    public Object[] addMyConf(){
      Object[] o = new Object[3];
      o[0] = new JLabel(_RECONCILIATION, SwingConstants.LEFT);
      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      BoxLayout bl1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
      BoxLayout bl2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
      panel1.setLayout(bl1);
      panel2.setLayout(bl2);
      JLabel label2 = new JLabel(_NUMBER_OF_PASSES);
      JLabel label3 = new JLabel(_INIT_BLOCKSIZE);
      _tfPasses = new JTextField("4"); 
      _tfBlockSize = new JTextField("10");
      panel1.add(label2);
      panel1.add(_tfPasses);
      panel2.add(label3);
      panel2.add(_tfBlockSize);
      o[1] = panel1;
      o[2] = panel2;
      return o;
    }
    
    /**
    * Applies the configuration selected by the user.
    * @param parent is the parent component.
    * @return true iff the settings wre all correctly set by the user.
    * Otherwise the default settings are used.
    */
    
    public boolean appliesConf(Component parent){
       int passes = DEF_NUMBER_PASSES;
       int blocksize = DEF_BLOCK_SIZE;
       boolean ok = true;
       try{
	    Integer iv = Integer.valueOf(_tfPasses.getText());
	    passes = iv.intValue();
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad number of passes, default value "+Integer.toString(passes)+" remains.",
				"Config Error",
			    JOptionPane.ERROR_MESSAGE);
	     ok = false;
	     //parent.repaint();
	}
	try{
	    Integer iv = Integer.valueOf(_tfBlockSize.getText());
	    blocksize = iv.intValue();
	    if(blocksize < 1){//devrait etre un exception different...
		throw new NumberFormatException("(blocksize must be >0)");
	    }
	}catch(NumberFormatException nfe){
	     JOptionPane.showMessageDialog(parent,
				"Bad initial block size, default value "+Integer.toString(blocksize)+" remains.",
				"Config Error",
			    JOptionPane.ERROR_MESSAGE);
	     ok = false;
	     //parent.repaint();
	}
	setParam(passes,blocksize);
	return ok;
    }
    
   /**
    * Displays  the settings to the user.
    * @param owner is the parent pane of this option pane.
    * 
    */
    
    public void showSettings(Component owner){
    
    }



  /**
   * for testing purpose.
   */

  private static synchronized void printBlock(boolean[] bl){
    System.out.print(">>");
    for(int i = 0; i<bl.length; i++){
      if(bl[i]){
	System.out.print("1");
      }else{
	System.out.print("0");
      }
    }
    System.out.println("**");
  }
  
  
  /**
  * For testing purpose.
  */
  
  @SuppressWarnings("unused")
private static synchronized void printBlock(boolean[] bl, boolean initiator){
    if(initiator){
	System.out.print("INIT:");
    }else{
	System.out.print("RESP:");
    }
    printBlock(bl);
  }


  /**
   * prints the data permuted for testing purposes.
   */

  @SuppressWarnings("unused")
private synchronized void printData(int pass){
    int k = _blocksize[pass];
    System.out.println("<<");
    for(int i = 0; i<_n; i++){
      if(_current[_permutations[pass][i]]){
	System.out.print("1");
      }else{
	System.out.print("0");
      }
      if(((i+1)% k)==0){System.out.println();}
    }
    System.out.println(">>");
  } 

  /**
   * Returns the original position of a position in the image 
   * of  a given permutation.
   * @param i is the position after permutation
   * @param p is the permutation to invert
   * @return j such that p(j)=i or -1 if the inputs were faulty.
   */

  private int invert(int i, int[] p){
    int found = -1;
    int j = 0;
    while((found<0) && (j<p.length)){
      if(p[j] == i){
	found = j;
      }
      j++;
    }
    return found;
  }
 


  /**
   * Returns the block index containing the given bit position.
   * All indices start at position 0.
   * @param i is the position 
   * @param k is the blocksize
   * @return the index of the block of length k containing position i.
   */

  private static int whichBlock(int i, int k){
   return (i / k);
  }

  /**
   * Extracts and returns a block of consecutive bits in the current
   * bit string for a given pass. The block indices start at 0. The behaviour is not
   * guaranteed when the index given exceeds the upper bound of the bit
   * string. 
   * @param i is the index of the block starting at 0.
   * @param pass is the pass for which the block is computed
   * @return the i-th block of length k based on the bits 
   *         in _current at his point.
   */

  private boolean[] getBlock(int i, int pass){
    int blknumber = numberOfBlocks(_n,_blocksize[pass]);
    int size = _blocksize[pass];
    boolean[] bk;
    if((i < blknumber-1) || (_n % _blocksize[pass] == 0)){ bk  = new boolean[size];}
    else{bk = new boolean[_n % size];}
    for(int j = 0; j<_n; j++){
      if(whichBlock(_permutations[pass][j],size)==i){
	bk[_permutations[pass][j] % size] = _current[j];
      }
    }
    return bk;
  }   



// This class implements a stack of blocks where an
// odd numbers of errors has been detected.

  private class BlockStack extends Object{
    
    @SuppressWarnings("rawtypes")
	private Vector[]  _stack;
    @SuppressWarnings("unused")
	private int[][] _perms;
    @SuppressWarnings("unused")
	private int[] _blksize;

    @SuppressWarnings("rawtypes")
	public BlockStack(int npass, int[][] perms, int[] blksize){
      _perms = perms;
      _blksize = blksize;
      _stack = new Vector[npass+1];
      for(int i=0;i<_stack.length;i++){
	_stack[i] = new Vector();
      }
    }

    /**
     * This method adds in the stack the new blocks with odd parity resulting
     * in a new error found in some position relative to the unpermuted
     * bit string. It also removes all blocks already in the stack
     * stack which contains the new position and therefore return to an even parity.
     * @param pos is the position of the new error relative to the unpermuted array 
     * @param pass is the last pass to search for odd parity blocks
     * @param maxblk is the max index of the last block in the last pass to look for
     * odd parity blocks.  
     */ 

    @SuppressWarnings("unchecked")
	public void addBlocks(int pos, int pass, int maxblk){
    int locerror = 0;
    try{
      int posi=0;
      int blocki = 0;
      locerror=1;;
      for(int i = 0; i <= pass; i++){
	  locerror = 2;
	  posi = _permutations[i][pos];
	  locerror = 3;
	  blocki = Cascade.whichBlock(posi,_blocksize[i]);
	  locerror = 4;
	  if((blocki <= maxblk) || (i < pass) ){
	    int whereitis = alreadyThere(blocki,i);
	    if(whereitis >= 0){
	      locerror = 5;
	      _stack[i].removeElementAt(whereitis);
	    }else{
	      locerror = 6;
	      _stack[i].addElement(new Integer(blocki)); 
	    }
	  }
      }
      locerror = 7;
     }catch(RuntimeException rt){
	throw new RuntimeException("addBlocks("+locerror+")--\n"+rt.toString()+"--\n"+rt.getMessage());
     }
    }

    /**
     * Returns true if the given block index appears in the
     * stack for a certain  pass.
     * @param blkindex is the index of the block 
     * @param pass is the pass we are interested in
     * @return the position in the stack where the block index
     * is. The value -1 is returned if the block index does not
     * appear in the stack. 
     */
    public int alreadyThere(int blkindex, int pass){
     int locerror = -1;
     int where = -1;
     boolean found = false;
     try{
	 for(int i = 0; (i<_stack[pass].size()) && !found; i++){
	    locerror = i;
	    Integer bindex = (Integer)_stack[pass].elementAt(i);
	    found = (bindex.intValue() == blkindex);
	    if(found){where = i;}
	 }
     }catch(RuntimeException rt){
	throw new RuntimeException("--alreadyThere("+locerror+","+pass+","+_stack.length+")--");
     }
     return where;
    }

    /**
     * Gets the block of in the earlier pass that appears
     * in the stack. These are the smallest blocks since the blocksize
     * increases with the pass number. The element is not removed from the
     * stack since it is going to be removed in the next call to addBlocks.
     * @return an array r[][] such that r[0] is the block index
     * and r[1] is the pass where the block has been found. Returns
     * {-1,-1} if no block are left in the stack.
     */

    public int[] getSmallestBlock(){
      @SuppressWarnings("unused")
	int  ans = -1;
      @SuppressWarnings("unused")
	int target = -1;
      int[] out = {-1,-1};
      if(!empty()){
	boolean emptypass = true;
	for(int i = 0; (i< _stack.length) && emptypass; i++){
	  emptypass = (_stack[i].size()==0);
	  if(!emptypass){
	    out[0] = ((Integer)_stack[i].elementAt(0)).intValue();
	    out[1] = i;
	  }
	}
      }
      return out;
    } 



    /**
     * Returns whether or not the stack is empty.
     */

    public boolean  empty(){
      boolean ans = true;
      for(int i = 0;(i<_stack.length) && ans;i++){
	if(_stack[i].size()>0){ans = false;}
      }
      return ans;
    }

  }


}
