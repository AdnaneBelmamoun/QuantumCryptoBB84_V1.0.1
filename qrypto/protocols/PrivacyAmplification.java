package qrypto.protocols;

import java.awt.Component;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import qrypto.exception.QryptoException;
import qrypto.exception.TimeOutException;
import qrypto.qommunication.Constants;


public class PrivacyAmplification extends CProtocol {

//  private String INITLOGFILENAME = Constants.PRIVAMPSENDER;
//  private String RESPLOGFILENAME = Constants.PRIVAMPRECEIVER;
    
  private static final String _CONF_LABEL = "Taille Finale: ";
  public  static final String PROT_ID = "Amplification de la sécurité";

  private BigIntHash  theHash;   // hash function used to reduce the number of bits
  private boolean[]     inBits;  // Bitstring delivered to this class for hashing
  private boolean[]     outBits; // PA'd Bitstring computed by this class
  private int         inBitsN;	 // Number of bits in the inBits array.
  private int         outBitsN;  // Number of bits in the outBits array. 
  private JTextField  _tfFinalKeyLength = null;
  
  public PrivacyAmplification()
  {
    newProtID(PROT_ID); 
  }

  public PrivacyAmplification(boolean[] bits, int desired_size)
  {
    this();
    inBits   = bits;
    inBitsN  = bits.length;
    outBits  = new boolean[desired_size];
    outBitsN = desired_size;
  }
  
  /**
  * Returns the final key. Should only be called after 
  * either the initiator or the responder has been executed.
  * @return the final bitkey.
  */

 public boolean[] getFinalKey(){
    return outBits;
 }


    /**
    * Allows to add the configuration GUI objects into a JOPtionPane dialog.
    * It is used by superprotocols in order to allow the user to configurate 
    * the subprotocols.
    */
    
  public Object[] addMyConf()
  {
      Object[] o = new Object[2];
      o[0] = new JLabel(_CONF_LABEL, SwingConstants.LEFT);
      JPanel panel1 = new JPanel();
      BoxLayout bl1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
      panel1.setLayout(bl1);
      JLabel label2 = new JLabel("Reduce key to:");
      _tfFinalKeyLength = new JTextField(Integer.toString(inBitsN)); 
      panel1.add(label2);
      panel1.add(_tfFinalKeyLength);
      o[1] = panel1;
      return o;
  };
    
    
    
    /**
    * Applies the configuration selected by the user.
    * @param parent is the parent component.
    * @return true iff the settings are accepted.
    */
    
  @SuppressWarnings("unused")
public boolean appliesConf(Component parent)
  {
    boolean ok = true;
    try
    {
      Integer temp_int = Integer.valueOf(_tfFinalKeyLength.getText());
      outBitsN = temp_int.intValue();
      outBits = new boolean[outBitsN];
      return true;
    }
    catch(NumberFormatException nfe)
    {
      JOptionPane.showMessageDialog(parent,
				    _tfFinalKeyLength.getText()+" is not a wellformed number. "+Integer.toString(outBitsN)+" remains.",
				    "Config Error",
			            JOptionPane.ERROR_MESSAGE);
      ok = false;
      parent.repaint();
      return false;
    }
  };
    
    

  /**
   * This implements the initiator's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   * Classspecific:
   * Privacy Amplification chooses a random Universel2 hashfunction and transmits
   * a description of the hashfunction.
   */

  public void initiator() throws QryptoException 
  {
    resetProgressTools(5,"Amplification de la sécurité");
    try{
      // Send the hashfunction over the public channel
      theHash  = new BigIntHash(outBitsN, inBitsN);
      _pc.sendInt(theHash.getSeedSize());
      //System.out.println("PAINIT:the hash seed sent");
      addProgress();
      _pc.sendInt(theHash.getShiftSize());
      //System.out.println("PAINIT:the shift size sent");
      addProgress();
      _pc.sendBytes(theHash.getSeedVector().toByteArray());
      addProgress();
      //System.out.println("PAINIT:the seed was sent");
      _pc.sendBytes(theHash.getShiftVector().toByteArray());
      addProgress();
      //System.out.println("PAINIT:shift vector sent");
      if (!_pc.receiveBit())
	throw new RuntimeException("Responder disagrees on size of reconciled bitvector\n");

      // Do the hash of the inBits boolean array
      _pc.receiveBit();
      //System.out.println("INITIATOR:");
      outBits = theHash.doHash(inBits);
      //System.out.println("INBITS SIZE = "+inBits.length);
      //System.out.println("OUTBITS SIZE = "+outBits.length);
      //System.out.println("INITIATOR SEED = "+theHash.getSeedVector().toString(16));
      //System.out.println("INITIATOR SVEC = "+theHash.getShiftVector().toString(16));
      //System.out.println("INITIATOR IN   = "+theHash.getLastInput().toString(16));
      //System.out.println("INITIATOR OUT  = "+theHash.getLastOutput().toString(16));
      //System.out.println("PAINIT:did hashing");
      // In the final version we don't send the hash of the bits, of course, since this is our
      // secret key. For now we exchange them to be able to check.
      //_pc.sendBits(outBits);
      addProgress();
   }catch(RuntimeException rt){
      throw new QryptoException("Privacy Amplification threw an exception: "+rt.getMessage()+"####"+rt.toString());
   }
  
  };



  /**
   * This implements the responder's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public void responder() throws QryptoException
  {
    int seed_size, shift_size;
    byte seed_bytes[], shift_bytes[];
    @SuppressWarnings("unused")
	boolean initiator_array[];
    
    resetProgressTools(5,"Privacy Amplification");
    try{
      // Receive the hashfunction over the public channel
      seed_size   = _pc.receiveInt();
      addProgress();
      shift_size  = _pc.receiveInt();
      addProgress();
      seed_bytes  = _pc.receiveBytes();
      addProgress();
      shift_bytes = _pc.receiveBytes();
      addProgress();
      
	    
      // Check that the sizes supplied matches the data we have already.
      // seed_size (from initiator) should be equal to the size of the boolean array
      // (from responder) we want to hash-shrink
      if (seed_size!=inBitsN)
      {
	_pc.sendBit(false);
	throw new RuntimeException("Initiators hash parameters does not match data.\n"+
	                           "Initiator size of in-vector: "+Integer.toString(seed_size)+"\n"+
				   "Responder size of in-vector: "+Integer.toString(inBitsN));
      }
      _pc.sendBit(true);
      addProgress();
      

      // The supplied hashfunction determines the size of the final key (shift_size)
      // If we're up-tight about it we could do a check of similarity here too.
      outBits  = new boolean[shift_size];
      outBitsN = shift_size;
      
      
      // Do the hash of the inBits boolean array
      theHash  = new BigIntHash(outBitsN, inBitsN, shift_bytes, seed_bytes);
      //System.out.println("RESPONDER:");
      outBits = theHash.doHash(inBits);
      //System.out.println("RESPONDER SEED = "+theHash.getSeedVector().toString(16));
      //System.out.println("RESPONDER SVEC = "+theHash.getShiftVector().toString(16));
      //System.out.println("RESPONDER IN   = "+theHash.getLastInput().toString(16));
      //System.out.println("RESPONDER OUT  = "+theHash.getLastOutput().toString(16));
      _pc.sendBit(true);
      //System.out.println("PARESP:did hash...");
     // this.output();
      
      // In the final version we don't exchange the hash of the bits, of course, since this is our
      // secret key. For now we exchange them to be able to check.
      // initiator_array = _pc.receiveBits();
      addProgress();
      
      //for (int i=0; i<initiator_array.length; i++)
	//  if (initiator_array[i]!=outBits[i]) 
	//    throw new RuntimeException("Initiator/Responder does not get same secret key.");
    }catch(RuntimeException rt){
      throw new QryptoException("Privacy Amplification threw an exception: "+rt.getMessage()+"####"+rt.toString());
    }
  };


  public void showSettings(Component owner){};



  /*
   * Produces an output for debugging purposes.
   */

  public void output(){
    System.out.println("---Privacy Amplification Statistics---");
    System.out.println("Input bits:  "+Integer.toString(inBitsN));
    System.out.println("Output bits: "+Integer.toString(outBitsN));
    System.out.println("*******************************************");
    Constants.printBoolArray(inBits,true);
    System.out.println("*******************************************");
    Constants.printBoolArray(outBits,true);

  }
  
  /*
   * Produces an output that is appended to a stream.
   * @param stream is the stream on which the output is written
   */

  public void output(PrintWriter stream){
    stream.println("---Privacy Amplification Statistics---");
    stream.println("Input bits:  "+Integer.toString(inBitsN));
    stream.println("Output bits: "+Integer.toString(outBitsN));
  }


}
