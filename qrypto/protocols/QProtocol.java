package qrypto.protocols;

import qrypto.qommunication.*;
import qrypto.gui.*;

import java.awt.*;


public abstract class QProtocol extends CProtocol implements Runnable{



protected RemoteQConnection _qc=null;

    /**
     * Empty constructor.
     */

    public QProtocol(){
	super();
	PROTID = "Empty QProtocol";
    }
    


  /**
   * This set the quantum connection used for the execution 
   * of the protocol.
   * @param qc is the new quantum connection
   */

  public void setQuantConnection(RemoteQConnection qc){
    _qc = qc;
  }




  /**
   * Executes the initiator or responder algorithms depending of the identity
   * of the owner. 
   */

  public abstract void run();


   /**
    * Allows the user to configure the settings of the protocol.
    * Examples are the transmission length the error-rate tolerated etc...
    * @param parent is the frame hosting the menu-driven configuration
    */
    
    public abstract void configure(Component parent);
    
    


    /**
    * Sends the initiator configuration to the responder and waits
    * for acceptance. The connection must be active before calling this method.
    * @param ssc is the public connection on server side.
    * @return true iff the configuration has been accepted.
    *
    */
    
    public abstract boolean sendConfig(ServerSocketConnection ssc, InitPlayer owner);


    /**
    * Receives the configuration for a protocol and asks acknowledgement.
    * @param pc is the public connection from which to receive the cofiguration.
    * @return true if the configuration has been accepted.
    */

    protected abstract boolean receiveConfig(SocketPubConnection pc);


   /**
    * Allows the user to confirm the actual settings and potentially to enter
    * its own part of the configuration. 
    * @param owner is the parent component.
    * @param pc is the pub connection used to confirm the settings to the initiator.
    * @return true iff the settings are accepted.
    */
    
    public abstract boolean confirmSettings(Component owner, SocketPubConnection pc);



}





