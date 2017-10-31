package qrypto.server;




import qrypto.qommunication.*;
import qrypto.exception.*;
import java.lang.Runnable;
import java.lang.Thread;
import java.io.IOException;
import java.net.InetAddress;



@SuppressWarnings("unused")
public class FakeInitDG  implements ConnectionNotify, Runnable
{


    private ServerFakeDGConnection _spc = null;
    private ServerSocketConnection _fakeQS = null;
    private boolean _server_connected = false;
    private Thread _t = null;
    private Thread _s = null;

   /**
    * Constructor.
    * @param portFDG is the fake DG port where to listen for a connection
    * with the respFakeDG.
    * @exception IOException when the server socket for the fake DG connection
    * couldn't be established.
    */
    
    
    public FakeInitDG(int portFDG)throws IOException{
      //a server waiting for a connection from the resp fake DG
      _fakeQS = new ServerSocketConnection(portFDG);
      _t = new Thread(_fakeQS);
      _t.start();
      initServerConnection();
    }
    
    /**
    * This method starts 1 thread waiting for the initserver connection.
    * This used to restart the fake init DG server after a session.
    */
    
    @SuppressWarnings("deprecation")
	private void initServerConnection() throws IOException{
	_server_connected = false;
	if(_s != null){
	    _s.stop();
	    _s = null;
	}
	if(_spc != null){
	    _spc.closeConnection();   
	}
	_spc = new ServerFakeDGConnection(Constants.DEF_PORT_SEND_DG,this);
	_s = new Thread(_spc);
	_s.start();
    }
    
    
    /**
    * Gets notification whenever the initiator client connects
    * to this fake DG.
    */
    
    public synchronized void notifyNewConnection(){
	_server_connected = true;
	notifyAll();
    }
    
    /**
    * Waits for a connection by the initiator server.
    * @return true if at the end the serrver is really connected.
    */
    
    public synchronized boolean waitServerConnection(){
      try{
	if(!_server_connected){
	    wait(0);
	    _server_connected = true;
	}
      }catch(InterruptedException ie){
      }
      return _server_connected;
    }
    
    
    /**
    * Runs a fake data grabber session. Expects <OK><Bucket Size> or <STOP>.
    * Upon <OK><Bucket Size=b>, this return b random slot of random BB84 qubits.
    * Each time slot contains a detected photon with probability success.
    */
    
    public void run(){
      try{
	while(true){
	    _fakeQS.waitConnection(0);
	    waitServerConnection();
	    boolean finish = false;
	    while(!finish){
	     //receives the next action:Constants.OK or Constants.STOP
	     int action = _spc.getInputStream().read();
	     if((byte)action == Constants.STOP){
		//STOP means that the current session is over.
		finish = true;
	     }else{ 
		//received Constants.OK which means that a new
		//bucket is expected.
		//Upload the bucket to the initiator server
		byte[] trans = new byte[_spc.bucketSize()];
		for(int i = 0; i<_spc.bucketSize(); i++){
		    trans[i] = (_spc.qCode() == Constants.BB84)?
			      BB84QuBit.random().byteValue():B92QuBit.random().byteValue();
		    _fakeQS.sendByte(trans[i]);
		}
		int goodpos = Constants.readSpecialInt(_spc.getInputStream());
		int[] index = new int[goodpos];
		for(int h=0; h<goodpos; h++){
		     index[h] = Constants.readSpecialInt(_spc.getInputStream());
	 	    //int index = Constants.readSpecialInt(_spc.getInputStream());
		    //_spc.getOutputStream().write(trans[index]);
		}
		for(int h=0; h<goodpos; h++){
		    //System.out.println("FAKEINITDG good pos:"+index[h]+" sent:"+trans[index[h]]);
		    _spc.getOutputStream().write(trans[index[h]]);
		}
		//_spc.getOutputStream().flush();
	     }
	   }//while(!finish)
	   initServerConnection();//waits for another init server.
	}//while(true)
      }catch(IOException io){
	    throw new RuntimeException("FakeInitDG: DG connection broken..."+io.getMessage());
      }
    }
    
    
    /**
    * Closes all connections.
    */
    
    @SuppressWarnings("deprecation")
	public void close(){
	if(_s != null){
	    _s.stop();
	}
	if(_t != null){
	    _t.stop();
	}
	if(_spc != null){
	    _spc.closeConnection();
	}
	if(_fakeQS != null){
	    _fakeQS.closeConnection();
	}
    }

}
