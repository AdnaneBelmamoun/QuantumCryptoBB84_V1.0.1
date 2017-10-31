package qrypto.qommunication;


import qrypto.exception.*;
import qrypto.server.QServerSocketConnection;
import javax.swing.JProgressBar; 
import java.util.Hashtable;


public interface QSender
{

   
   /**
   * Gets the bucket size.
   * @return the bucket size.
   */
   
   public int getBucketSize();
   
   /**
   * Sends the number of buckets used back to the
   * client. 
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
    * This method is used to send a random qubit among
    * several possibilites.
    * @param alphabet is the set of possible state to be sent.
    * @param n is the number of qubits to be sent.
    * @return the qubits that have been sent. Null if nothing has been
    * sent and/or problem occurs.
    */
    //public QuBit[] send(QuBit[] alphabet, int n)throws TimeOutException, QuBitFormatException;
   
    
   /**
    * This method is used to send a random qubit among
    * several possibilites.
    * @param bytecode is the code for the coding scheme to be used.
    * @param n is the number of qubits to be sent.
    * @return the qubits that have been sent. Null if nothing has been
    * sent and/or problem occurs.
    */
    
    public QuBit[] send(byte bytecode, int n)throws TimeOutException, QuBitFormatException;
   
    
    /**
    * Sends qubits and automatically forward them to the
    * responder client. The connection with the client must be up and 
    * ready. The result is not stored. If the connection with the client
    * does not work then everything is lost.
    * @param bytecode is the code corresponding to the coding to be used.
    * @param n is the number of qubits to be received.
    */
    
    public void sendAndForward(byte bytecode, int n)throws TimeOutException;
    
    

    
   /**  
    * Returns the type of the Qsender.
    * @return Constants.REAL or Constants.VIRTUAL
    */
    public byte qType();
    
    
   /**
    * Close the session.
    */
    
    public void close();
    
    /**
    * Returns whether or not the connection between servers is established.
    * @return true iff the connection is established.
    */
    
    public boolean isConnected();
    
   /**
    * Waits for the two servers to establish a connection. 
    */
    
    public boolean waitConnection();

    /**
    * Returns the public connection used by the servers.
    * @return the public connection.
    */
    
    public QServerSocketConnection getPubConnection();
    
    /**
    * Sets the public connection.
    * @param ic is connection to be added.
    */
    
    public void setClientConnection(ServerSocketConnection ic);
    
    /**
    * Returns the client connection.
    * @return the client connection or null if it has not been set.
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
