package qrypto.qommunication;

import qrypto.exception.*;
import javax.swing.JProgressBar;

public interface QodingType {


   /**
    * Sets the bucket size externally. Usually called by the
    * initiator and responder servers when the QodingType is
    * created. The default value of the bucket size is 1.
    * This is the value for virtual quantum transmission.
    * @param bs is the new bucket size. Must be smaller
    * than 256*255 + 255 and greater than 0.
    */
    
    public void setBucketSize(int bs);
    
    
    /**
    * Returns the bucket size. This is usually called by the
    * players in ordert to recover the bucket size used for the
    * last quantum transmission. Can also be called by the
    * servers but seems rather useless in this case since they
    * already have that information.
    * @return the bucket size.
    */
    
    public int getBucketSize();
   
    
    /**
    * Returns the number of buckets needed for last quantum transmission.
    * It is intended to be used by the clients.
    * @return the number of buckets.
    */
    
    public int getNumberOfBuckets();


    /**
    * This method returns the type of quantum transmission
    * that is either real or virtual (or other if there are any).
    * @return the type of the quantum transmission:Constants.REAL or Constants.VIRTUAL
    */
    
    public byte getQType();


    /**
    * This sets the type of quantum transmission. This is called
    * by the server to tell what kind of q-trans was performed.
    * @param t is the bytecode for the type of q-trans.
    * This is Constants.VIRTUAL or Constants.REAL.
    */
    
    public void setQType(byte b);
    
    /**
    * Sets the progress bar that is modified whenever the player
    * is get the output of a quantum transmission. If this object is not
    * a player this has not to be called. If a player has this field null then
    * no progress is shown.
    * @param bar is the progress bar.
    */
    
    public void setPlayerProgressBar(JProgressBar bar);
    




   /**
    * Sets the players identities.
    * @param me is my identity
    * @param peer is the peer identity
    * @param iminitiator is true iff i'm the initiator
    */

   public void setID(String me, String peer, boolean iminitiator);


   /**
   * This returns the name of this coding scheme in a better
   * form than the classname.
   * @return the name of this coding scheme.
   */
   
   public String getCodingName();



    /**
    * This method returns the name of the method used for quantum transmission.
    * @return the name of the method (it is a classname).
    */

    public String getMethod();



    /**
     * This method gets the parameter required for BB84 transmission but on the
     * server side. This is called by the initiator server.
     * @param ssc is the server connection with the client.
     * @exception qrypto.exception.AcknowledgeException when the acknowlegement
     * could not be processed.
     */ 

    public void acceptRequest(ServerSocketConnection ssc) throws AcknowledgeException;


    /**
     * This sends  a request for a BB84 quant transmission to the intiator server.
     * This is called by the initiator client.
     * @param spc is the connection with the initiator server.
     * @exception qrypto.exception.AcknowledgeException when the request couldn't be made.
     */

    public void makeInitiatorRequest(SocketPubConnection spc) throws AcknowledgeException;




    /**
     * This method is called by the responder server in order to transmit to the responder
     * client the transmission request received from the initiator server.
     * @param ssc is the server pub connection between the responder server to the responder client.
     * @exception qrypto.exception.AcknowledgeException when the two servers couldn't agree.
     */

    public void retransmitRequest(ServerSocketConnection ssc) throws AcknowledgeException;




    /**
     * This method is called by the responder client for receiving the setting of
     * the next quantum transmission for which the request between the initiator and responder
     * servers has been agreed upon.
     * @param spc is the pub connection on the responder client side with the responder server.
     * @exception qrypto.exception.AcknowledgeException whener the two couldn't agree.
     */

    public void waitForResponderServer(SocketPubConnection spc)throws AcknowledgeException;



    /**
     * This method sends the request back to the responder server through
     * a public connection between the two servers.
     * This is called by the initiator server which is the server of the pub connection.
     * @param ssc is the pub connection between the two servers.
     * @exception qrypto.exception.AcknowledgeException whenever the responder
     * server did not acknowledge.
     */ 


    public void setTransmissionRequest(ServerSocketConnection ssc) throws AcknowledgeException;




    /**
     * This method is called by the responder server for receiving the setup
     * of the next quantum transmission. This is the receiving part associated to
     * </tt>setTransmission</tt>.
     * @param spc is the pub connection listener side between the two servers.
     * @exception qrypto.exception.AcknowledgeException whenever no agreement was met.
     */

    public void getTransmissionRequest(SocketPubConnection spc) throws AcknowledgeException;





    /**
     * Transmission over the quantum channel according to the agreed parameters.
     * @param qc is the quantum sender to be used for this transmission. 
     * Is called by the initiator server only.
     * The data are simultaneously sent back to the client whenever qc contains
     * an active client connection.
     * @exception qrypto.exception.QryptoException when a problem occurs before completion.
     */

    public void sendQTransmission(QSender qc) throws QryptoException;
    
    
   

    /**
     * Reception of BB84 random qubits. Is called by the responder server only.
     * It sends the data back to the client whenever qc contains an active
     * client connection.
     * @param qc is the quantum receiver to be used for the transmission.
     * @exception qrypto.exception.QryptoException when a problem occured before completion.
     */

    public void readQTransmission(QReceiver qc)throws QryptoException;



    /**
     * Receives the last transmsision from the server.
     * This is called by both the initiator and receiver clients.
     * @param sc is the connection with the server.
     * @exception qrypto.exception.QryptoException when a problem occured.
     */

    public QuBit[] getResult(SocketPubConnection sc) throws QryptoException;
    
    
    
   /**
    * This method returns the qubits resulting from an execution of this
    * coding. 
    * @return the latest execution or null if it has not happened yet.
    */
    
    public BB84QuBit[] getQuBits();
  

}
