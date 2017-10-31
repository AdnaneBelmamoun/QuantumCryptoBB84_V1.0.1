package qrypto.server.BAK;

import qrypto.exception.*;
import qrypto.qommunication.*;

import java.util.Vector;
import java.util.Enumeration;
import qrypto.protocols.Cascade;
import qrypto.qommunication.BAK.VirtualPubChannel;
import qrypto.qommunication.BAK.VirtualPubConnection;
import qrypto.qommunication.BAK.VirtualQuantChannel;
import qrypto.qommunication.BAK.VirtualQuantConnection;



public class QryptoManager extends Object{

/** the maximum number of parties in the schedule. */
public static int CAPACITY = 6; 

/** used to store the schedules player. */
@SuppressWarnings("rawtypes")
private static Vector _schedule;


  /**
   * This method initialized the manager. Must be called initially and 
   * before any call to its functionality. Any further call will return
   * the manager to its initial state so its current state will be lost.
   */

  @SuppressWarnings("rawtypes")
static public void init(){
    if (_schedule != null){_schedule = null;} 
    _schedule = new Vector();

  }


  /**
   * Find a match if there is one between a party and the scheduled ones.
   * @param p is the party for who we search a a match.
   * @return the matching player if there is one and null if not.
   */

  @SuppressWarnings("rawtypes")
static private Party findMatchWith(Party p){
    Party p2;
    Party onematch=null;
    Party answer=null;
    boolean notfound = true;
    if(_schedule != null){
      for( Enumeration e = _schedule.elements();e.hasMoreElements() && notfound;){
	p2 = (Party)e.nextElement();
	if (p.match(p2) && p2.match(p)){onematch = p2; notfound = false;}
      }
      if (!notfound){answer = onematch;}
    }
    return answer;
  }


  /**
   * This allows to add a new party to the schedule list.
   * If a matching pair is found then the protcol is launched between them.
   * Otherwise, the new party is added until a matching party is found. 
   * @param newparty is the party to be added.
   * @return true iff a matching party has been found.
   */

@SuppressWarnings("unchecked")
static synchronized public boolean scheduleNewParty(Party newparty){
  boolean answer = false;
  Party match = null;
  if(newparty != null){
    match = findMatchWith(newparty);
    if(match != null){
      answer = true;
    }
    else{
      _schedule.addElement(newparty);
    }
  }
  if (answer){
      newparty.execute();
      match.execute();
  }
  return answer; 
}



  public static void main(String[] args)throws TimeOutException{
    /**
     * General settings for testing cascade. 
     */

    System.out.println("**TESTING CASCADE**");
    boolean[] bs1 = 
         {true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    boolean[] bs2 = 
         {true,false,true,true,true,true,false,true,true,false,true,true,true,true};
    Party p1 = new Party("ALICE","BOB");
    p1.setInitiator();
    Party p2 = new Party("BOB","ALICE");
    p2.setResponder();
    VirtualPubChannel pc = new VirtualPubChannel();
    VirtualQuantChannel qc = new VirtualQuantChannel();
    VirtualPubConnection c1 = pc.getFirstConnection();
    VirtualPubConnection c2 = pc.getSecondConnection();
    VirtualQuantConnection qc1 = qc.getFirstConnection();
    VirtualQuantConnection qc2 = qc.getSecondConnection();
    p1.setPubConnection((PubConnection) c1);
    p2.setPubConnection((PubConnection) c2);
    p1.setQuantConnection((QConnection) qc1);
    p2.setQuantConnection((QConnection) qc2);
    Cascade  prot1 = new Cascade(bs1,2,4);
    Cascade  prot2 = new Cascade(bs2,2,4);
    p1.setProt(prot1);
    p2.setProt(prot2);
    System.out.println("configuration Términé...");
 
    QryptoManager.init();
    QryptoManager.scheduleNewParty(p1);
    boolean ok = QryptoManager.scheduleNewParty(p2);
    if (ok){
      System.out.println("Protocol launched");
    }
    try{
      p1.waitCompletion();
      p2.waitCompletion();
      p1.output();
      p2.output();
    }catch(QryptoException qe){System.out.println("%%%erreur:"+qe.getMessage());}
  }

}









