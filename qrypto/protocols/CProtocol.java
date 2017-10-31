package qrypto.protocols;

import java.awt.Component;
import java.util.Hashtable;

import qrypto.log.Log;


public abstract class CProtocol extends SubCProtocol
{


protected Log _logfile = null;   

protected Component _parent = null;
   
protected String PROTID;
protected boolean _initiator = false;
protected String  _me = null;
protected String _peer = null;

protected String[] _options = {"Ok","Cancel"};
protected int _OK_OPTION = 0;



   
     /**
     * Empty constructor.
     */

    public CProtocol(){
	PROTID = "Empty CProtocol";
    }
    
 /**    
  * Sets the parent component when messages are shown to the user.
  * This allow to put the dialog over the player window when called
  * before the execution of a protocol.
  * @param p is the parent component.
  **/
  
  public void setParent(Component p){
    _parent = p;
  }
    
    
 /**
 * This returns the filename for this protocol. This is also the directory
 * name for the templates associated to this protocol.
 * @return the string corresponding to the filename of this protocol.
 */
 
  public String protFileName(){
    return "CProtocol";
  }


  /**
  * Returns the identity of the owner.
  * @return the identity.
  */
  
  public String myID(){
    return _me;
  }
  
  /**
  * Returns the identity of the peer.
  * @return the peer's identity
  */
  
  public String peerID(){
    return _peer;
  }



  /**
   * Returns the name of the protocol for human interpretation.
   * @return the protocol name.
   */

  public String protID(){
    return PROTID;
  }
  
  
  /**
  * Sets the peer's identity.
  * @param peer is the peer's identity.
  * @param isinit is true iff the peer is initiator.
  */
  
  public void setPeerID(String peer, boolean isinit){
    _peer = peer;
    _initiator = !isinit;
  }
  
 /**
  * Sets the peer's identity.
  * @param peer is the peer's identity.
  */
  
  public void setPeerID(String peer){
    _peer = peer;
  }
  
  /**
  * Sets the owner identity.
  * @param me is my identity.
  * @param isinit is true iff me is the initiator.  
  */
  
  public void setMyID(String me, boolean isinit){
    _me = me;
    _initiator = isinit;
  }
  
 /**
  * Sets the owner identity.
  * @param me is my identity.
  * @param isinit is true iff me is the initiator.  
  */
  
  public void setMyID(String me){
    _me = me;
  }


  /**
   * Allows to change the name of this protocol.
   * @param name is the new name.
   */

  protected void newProtID(String name){
    PROTID = name;
  }


  /**
  * Sets the identities
  */
  
  public void setID(String me, String peer, boolean initiator){
    _me = me;
    _peer = peer;
    _initiator = initiator;
 }
 

    /**
    * Allows to add the data to fill the holes of the HTML template
    * associates with this protocol. An hashtable is given as input
    * so the instance of this protocol can add its own data which 
    * consist of a new argElm entry. In this abstract class, the method
    * is implemented as a dummy method that does not do anything to the
    * hashtable. Each protocol with a template should overwrite this method.
    * @param ht is the hashtable where to add the new argElm
    * @param instaceNumber indicates what is the instance number
    * of this subprotocol. 
    * @see qrypto.htmlgenerator.argElm
    */

    @SuppressWarnings("rawtypes")
	public void addMyHoles(Hashtable ht,int instanceNumber){
    }
     

    
    /**
    * Allows to add the configuration GUI objects into a JOPtionPane dialog.
    * It is used by superprotocols in order to allow the user to configurate 
    * the subprotocols.
    */
    
    public abstract Object[] addMyConf();
    
    /**
    * Applies the configuration selected by the user.
    * @param parent is the parent component.
    * @return true iff the settings are accepted.
    */
    
    public abstract boolean appliesConf(Component parent);
    
   /**
    * Displays  the settings to the user.
    * @param owner is the parent pane of this option pane.
    * 
    */
    
    public abstract void showSettings(Component owner);



}
