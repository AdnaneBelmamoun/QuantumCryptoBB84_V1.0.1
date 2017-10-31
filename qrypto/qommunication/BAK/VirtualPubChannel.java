package qrypto.qommunication.BAK;




 

public class VirtualPubChannel extends Object{


private static int _instances = 0;

private ComVector _buff1=null;
private ComVector _buff2=null;

private VirtualPubConnection c1=null;
private VirtualPubConnection c2=null;

  /**
   * Constructor for a new virtual public channel.
   * 
   */

  public VirtualPubChannel(){
    _buff1 = new ComVector();
    _buff2 = new ComVector();
    c1 = new VirtualPubConnection(_buff1,_buff2);
    c2 = new VirtualPubConnection(_buff2,_buff1);
    _instances++;
  }


  /**
   * Returns the number of virtual channels in used.
   * @return the number of channels in used.
   */

  public static int InUsed(){
    return _instances;
  }


  /**
   * Kill a channel.
   * @param c is the virtual pubic channel to kill.
   */

  public static void KillChannel(VirtualPubChannel c){
    if (c != null){
    c._buff1 = null;
    c._buff2 = null;
    c.c1 = null;
    c.c2 = null;
    _instances = _instances - 1;
    }
  }


  /**
   * Returns the first connection to distribute to one party.
   */

  public VirtualPubConnection getFirstConnection(){
    return c1;
  }

  /**
   * Returns the second connection to distribute to the second party.
   */

  public VirtualPubConnection getSecondConnection(){
    return c2;
  }


 


}

