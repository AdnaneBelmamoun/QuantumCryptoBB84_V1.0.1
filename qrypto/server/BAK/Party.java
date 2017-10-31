package qrypto.server.BAK; 

import qrypto.exception.*;
import qrypto.protocols.Cascade;
import qrypto.qommunication.*;


import java.util.Date;
import qrypto.protocols.QProtocol;


public class Party extends Object{



private String _id=null;
private Date _creation = null;
private String _peer=null;
@SuppressWarnings("unused")
private boolean _match = false;

@SuppressWarnings("unused")
private PubConnection _pb=null;
private boolean _isInitiator=false;
private boolean _success = false;
private QryptoException _errormessage = null;



  /**The quantum connection */
private QConnection  _qc = null;
  /**The public connection */
private PubConnection _pc = null;
  /**The quantum Protocol*/
private QProtocol _qp=null;
@SuppressWarnings("unused")
private Cascade _casc=null;

private boolean _completed = false;

  
   public Party(String name){
    _id = name;
    _creation = new Date();
  }


  /** 
   * Constructor with peer name.
   * @param name is the name of the new party
   * @param peer is the name of the peer
   */

  public Party(String name, String peer){
    _id = name;
    _peer = peer;
    _creation = new Date();
  }



  /**
   * Sets the peer id.
   * @param peer is the peer id.
   */

  public void newPeer(String peer){
    _peer = peer;
  }

  /**
   * Sets this party to be initiator of the protocol.
   */

  public void setInitiator(){
    _isInitiator = true;
  }

  /**
   * Sets this party to be a responder.
   */

  public void setResponder(){
    _isInitiator = false;
  }

  /**
   * Returns the type of party.
   * @return true if this party is an initiator and return false if it is
   * a responder.
   */

  public boolean type(){
    return _isInitiator;
  }


  /**
   * Sets the public connection.
   * @param pc is the new public connection
   */

  public void setPubConnection(PubConnection pc){
    _pc = pc;
  }

  /**
   * Sets the quantum connection.
   * @param qc is the new quantum connection.
   */

  public void setQuantConnection(QConnection qc){
    _qc = qc;
  }


  /**
   * Returns the public connection.
   * @return the public connection.
   */

  public PubConnection getPubConnection(){
    return _pc;
  }


  /**
   * Returns the quantum connection.
   * @return the quantum connection.
   */

  public QConnection getQuantConnection(){
    return _qc;
  }

  /**
   * Sets the protocol to be run.
   * @param qp is the protocol.
   */

  public void setProt(QProtocol qp){
    _qp = qp;
  }
  public void setProt(Cascade pr) {
            _casc = pr;
    }

  /**
   * Returns the protocol.
   * @return the protocol or null if none has been selected.
   */

  public QProtocol protocol(){
    return _qp;
  }

  /**
   * Returns the identification of the party.
   * @return the identification of the actual party. Returns "Undefined" if
   * if the identity is not set.
   */

  public String id(){
    if (_id != null){
      return _id;
    }
    else{
      return "Undefined";
    }
  }

  /**
   * This method returns the time when this party
   * has been created.
   * @return the time at which this party has been created.
   * @see Date
   */

  public Date when(){
    return _creation;
  }

  /**
   * This method returns the identification of the peer.
   * @return the string describing the peer identity.
   */

  public String withWhom(){
    return _peer;
  }

  /**
   * This method returns true iff the party given as input is the peer.
   * @param p is the party that is verified to match with the peer of this party.
   * @return true iff the party given is an instanciation of the peer this party is
   * looking for.
   */

  public boolean match(Party p){
    boolean answer;
    answer = (withWhom() == p.id());
    return answer;
  }


  /**
   * This method starts the execution of the selected protocol.
   * Before calling this method, the protocol type, the type and
   * the connections must be defined.
   * @see #setPubConnection
   * @see #setQuantConnection
   */

  public synchronized void execute(){
    Thread t=null;
    _qp.setPubConnection(_pc);
    _qp.setQuantConnection((RemoteQConnection) _qc);
    PartyThread  pt = new PartyThread(this); 
    t = new Thread(pt);
    _completed = false;
    t.start();
  }


  /**
   * This is called by the QProtocol called through the execute method
   * if this object.
   * @param completed is a flag indicating if the protocol has
   * been successfully completed.
   */

  protected synchronized void done(boolean r, QryptoException error){
    _success = r;
    _errormessage = error;
    _completed = true;
    notifyAll();
  }



  /**
   * This method waits for the protocol execution to be done
   * and indicates the status of the last execution.
   * @exception QryptoException is thrown when the execution
   * has produced an error.
   */

  public synchronized void waitCompletion()throws QryptoException{
    while(!_completed){
      try{
	wait();
      }catch(InterruptedException ie){}
    }
    if (!_success){throw _errormessage;}
  }


    /**
     * This method prompts the user to fullfill the parameter and the
     * beginning of the next session.
     */

    public synchronized void activate(){

    }





  /**
   * Prints the output of the last execution.  If an error occured 
   * during the last execution then an error diagnose is printed.
   *If an execution of the protocol is in progress, nothing if printed. 
   * @return true iff the execution is completed. 
   */

  public synchronized boolean output(){
    if(_completed){
      System.out.println("********************************************");
      System.out.println("PROTOCOL OUTPUT ON "+id()+"'S POINT OF VIEW");
      (this.protocol()).output();
      System.out.println("********************************************");
    }
    return _completed;
  }


  
}



