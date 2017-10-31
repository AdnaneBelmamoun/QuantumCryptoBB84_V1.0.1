package qrypto.server.BAK;


import qrypto.exception.*;
import qrypto.protocols.QProtocol;


/**
 * This class implements the thread a party lauchnes when the
 * execution of the protocol is started.
 * @author Louis Salvail (salvail@brics.dk) 
 */




public class PartyThread extends Object implements Runnable{
 

private Party _party = null;
private boolean _success = false;
private QryptoException  _message = null;

  /**
   * This is a constructor for a new thread associated to the execution
   * of a protocol by one party.
   * @param party is the party owning this thread.
   * @param prot is the protocol to be executed by that thread
   */

  public PartyThread(Party party){
    _party = party;
  }

  /**
   * This method runs the protocol for the owning party.
   * The initiator part of the protocol is run if this instance
   * is for initiator. Otherwise, the responder's part is executed.
   * After the execution, the party is notified. 
   * @see Party#done
   */

  public void run(){
    QProtocol prot = _party.protocol();
    _success = true;
    try{
      if(_party.type()){prot.initiator();}
      else{prot.responder();}
    }catch(QryptoException qe){
      _message = qe; 
      _success = false;
    }
    _party.done(_success,_message);
  }





}
