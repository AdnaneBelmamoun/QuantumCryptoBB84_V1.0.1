package qrypto.protocols;

import qrypto.exception.*;
import qrypto.qommunication.*;

import javax.swing.*;
import java.io.*;


public abstract class SubCProtocol extends Object
{

protected JProgressBar  _bar = null;
protected JLabel _progressLabel = null;   
   
protected PubConnection _pc = null;




/**
 * Sets the progress bar and the progress label associated with
 * an execution of the protocol. 
 * @param bar is the progress bar.
 * @param label is the progress label.
 */
     
  public void setProgressTools(JProgressBar bar, JLabel label){
    _progressLabel = label;
    _bar = bar;
   }
     
     
 /**
 * Reset the progress tools by setting the progressBar to 0 and
 * the label to a new value. This has an effect only if the tools
 * are defined.
 */
 
 public void resetProgressTools(int max, String label){
     if((_bar != null) && (_progressLabel != null)){
	_bar.setMaximum(max);
	_bar.setValue(0);
	_progressLabel.setText(label);
    }
 }
 
 /**
 * Sets the progess label. It applies only if the progress tools
 * are defined. Otherwose nothing is done.
 * @param label is the new label.
 */
 
 public void setProgressLabel(String label){
    if((_bar != null) && (_progressLabel != null)){
	_progressLabel.setText(label);
    }
 }
 
 /**
 * Adds one to the progress bar. It applies if the progress tools are defined.
 */
 
 public void addProgress(){
     if((_bar != null) && (_progressLabel != null)){
	_bar.setValue(_bar.getValue()+1);
    }
 }


  /**
   * This set the public connection used for the execution
   * of the protocol.
   * @param pc is the new public connection
   */

  public void setPubConnection(PubConnection pc){
    _pc = pc;
  }
  
      
  /**
   * Gets the public connection. Null is it has not been set.
   * @return the public connection.
   */
    
  public PubConnection getPubConnection(){
	return _pc;
  }


  /**
   * This implements the initiator's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public abstract void initiator() throws QryptoException;

  /**
   * This implements the responder's side protocol.
   * @exception TimeOutException is thrown whenever 
   * a message was expected from one connection but the timeout expired.
   */

  public abstract void responder() throws QryptoException;
    
 
  /**
   * Print the output of the protocol for human reading.
   */

  public abstract void output();
  
 /**
  * Prints the output in a stream. One should wait for the
  * end of the execution before calling this method.
  * @param stream is the output stream.
  */
  
  public abstract void output(PrintWriter stream);

}
