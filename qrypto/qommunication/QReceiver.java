package qrypto.qommunication;

import qrypto.server.QSocketPubConnection;

import java.util.Hashtable;
import javax.swing.JProgressBar;
import qrypto.exception.*;


public interface QReceiver
{


   
   /**
   * Gets the bucket size.
   * @return the bucket size.
   */
   
   public int getBucketSize();
   
   /**
   * Sets the bucket size. This has no effect when called
   * from virtual receiver.
   * @param bs is the new bucket size.
   */
   
   public void setBucketSize(int bs);
   
   

   /**
   * Sends the number of buckets used back to the
   * client. 
   * @return the number of bucket.
   */
   
  public void sendNumberOfBucketsToClient();
  
  
  /**
   * Returns the number of buckets needed.
   * @return the number of buckets.
   */
   
   public int getNumberOfBuckets();
 


  /**
   * Sets the progress for showing progress. If the progress bar
   * is not set then nothing is shown.
   * @param bar is the progress bar.
   */
   
   public void setProgressBar(JProgressBar bar);

 
   /**
   * Returns the type of this quantum receiver. Constants.VIRTUAL
   * or Constants.REAL.
   * @return the type.
   */
   
   public byte qType();

   /**
    * This method is used to read qubits.
    * @param n is the number of qubit to be read.
    * @return the qubit that has been received. Null if nothing has been
    * received or a problem occured.
    */
    //public QuBit[] read(int n) throws TimeOutException, QuBitFormatException;
    
    
   /**
    * This method allows to receive a qubit from an alphabet.
    * The returned qubit will be in the same class than
    * the qubit in the alphabet.
    * @param alphabet is the alphabet of possible states.
    * @param obs is the observable for each possible measurement.
    * obs[i] is an observable for he i-th possible measurement.
    * the measurement is choosen at random among the possible.
    * If obs[i] is the outcome obs[i] is returned otherwise
    * obs[i].negMe() is returned. For bb84 obs[0]=|0> and 
    * obs[1]=|0>+|1> do the job.
    * @param n is the number of qubits expected.
    * @return the received qubits which are all of the same class
    * than the observable.
    */
    
   // public QuBit[] read(QuBit[] alphabet,QuBit[] obs, int n)throws TimeOutException;

    
    /**
    * Receives qubits according to some coding.
    * @param bytecode is the code coresponding to the coding.
    * @param n is the number of qubits expected.
    * @return the array of qubits received.
    */
    
    public QuBit[] read(byte bytecode, int n)throws TimeOutException;
    
    
    /**
    * Receives qubits and automatically forward them to the
    * responder client. The connection with the client must be up and 
    * ready. The result is not stored. If the connection with the client
    * does not work then everything is lost.
    * @param bytecode is the code corresponding to the coding to be used.
    * @param n is the number of qubits to be received.
    */
    
    public void readAndForward(byte bytecode, int n)throws TimeOutException;
    
    
    /**
    * This method close all connections. If it is a real connection then
    * the DG connection is closed. 
    */
    
    public void close();
    
    
    
    
    /**
    * This method indicates whether or not this receiver is connected
    * to the DG (if it is a real receiver). The output  is interpreted
    * as whether or not the connections are ready to proceed.
    * @return true iff the connection has been established.
    */
    
    public boolean isConnected();
   
    
   /**
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QSocketPubConnection getPubConnection();
    
    
   /**
    * Sets the client connection.
    * @param ic is the new client connection.
    */
    
    public void setClientConnection(ServerSocketConnection ic);
    
    /**
    * Returns the client connection associated to this receiver.
    * @return the client connection or null is it has not been set.
    */
    
    public ServerSocketConnection getClientConnection();
    
   /**
    * Sets the configuration of an instance according to the ones
    * define in an hashtable. The hashtable can contain configurations
    * that apply to other cases.
    * @param h is an hastable containing some configuration keys and values.
    * @return Constants.OK iff everything went well.
    */
    
    @SuppressWarnings("rawtypes")
	public byte appliesConfig(Hashtable h);
    

}
