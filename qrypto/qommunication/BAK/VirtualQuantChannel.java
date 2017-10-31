package qrypto.qommunication.BAK;



public class VirtualQuantChannel extends Object{




private static int _instances = 0;

private ComVector _buff1=null;
private ComVector _buff2=null;

private VirtualQuantConnection c1=null;
private VirtualQuantConnection c2=null;

  /**
   * Constructor for a new virtual quantum channel.
   * 
   */

  public VirtualQuantChannel(){
    _buff1 = new ComVector();
    _buff2 = new ComVector();
    c1 = new VirtualQuantConnection(_buff1,_buff2);
    c2 = new VirtualQuantConnection(_buff2,_buff1);
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

  public static void KillChannel(VirtualQuantChannel c){
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

  public VirtualQuantConnection getFirstConnection(){
    return c1;
  }

  /**
   * Returns the second connection to distribute to the second party.
   */

  public VirtualQuantConnection getSecondConnection(){
    return c2;
  }


 

}

