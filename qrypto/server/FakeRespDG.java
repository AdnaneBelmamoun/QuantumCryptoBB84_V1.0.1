package qrypto.server;




import qrypto.qommunication.*;
import qrypto.exception.*;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.Vector;
import java.io.IOException;
import java.net.InetAddress;




public class FakeRespDG implements ConnectionNotify, Runnable
{


    private ServerFakeDGConnection _spc = null;
    private double _success = Constants.DEF_PROB_FAKE_SUCCESS_TRANSMISSION;
    private SocketPubConnection _fakeQC = null;
    Thread _s = null;
    boolean _server_connected = false;
    
     
   /**
    * Constructor.
    * @param ip the ip for the initiator fake DG.
    * @param portFDG is the fake DG port where to connect
    * to the InitFakeDG.
    * @exception IOException when the server socket for the fake DG connection
    * couldn't be established.
    */
    
    
    public FakeRespDG(InetAddress ip, int portFDG)throws IOException{
      _fakeQC = new SocketPubConnection(ip,portFDG);
      initServerConnection();
    }
    
    
    /**
    * Constructor with given probability for a quantum transmission
    * to reach the responder side.
    * @param ip is the ip for the initiator fake DG.
    * @param portFDG is the fake DG port where to connect to the InitFakeDG
    * @param success is the probability that a qubit sent will be detected
    * at the responder side of the fake quantum channel. 
    */
    
    public FakeRespDG(InetAddress ip, int portFDG, double success)throws IOException{
	this(ip,portFDG);
	_success = success;
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
	_spc = new ServerFakeDGConnection(Constants.DEF_PORT_REC_DG,this);
	_s = new Thread(_spc);
	_s.start();
    }
    
    
    /**
    * Gets notification whenever the initiator client connects
    * to this fake DG.
    */
    
    public synchronized void notifyNewConnection(){
      if(!_server_connected){
	_server_connected = true;
	notifyAll();
      }
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
    
    @SuppressWarnings("rawtypes")
	public void run(){
      try{
       while(true){
	waitServerConnection();
	boolean finish = false;
	while(!finish){
	    //receives the next action:Constants.OK or Constants.STOP
	    int action = _spc.getInputStream().read();
	    if((byte)action == Constants.STOP){
		//STOP means that the session is over.
		finish = true;
	    }else{ 
		//received Constants.OK which means that a new
		//bucket is expected.
		//Upload the bucket to the server
		byte[] trans = new byte[_spc.bucketSize()];
		for(int i = 0; i<_spc.bucketSize(); i++){
		    byte nextqb = _fakeQC.receiveByte();
		    if(Math.random() >_success){
		      trans[i] = Constants.UNDETECTED;
		    }else{//simulates the measurement in random BB84 basis.
		      @SuppressWarnings("unused")
			boolean error = false;
		      BB84QuBit obs = BB84QuBit.random();
		      if(BB84QuBit.convert(nextqb).measureVN(obs)){
		        if(Math.random() > Constants.DEF_DETECTOR_PROB_ERROR){
			    trans[i] = obs.byteValue();
			}else{
			    error = true;
			    obs.compl();
			    trans[i] = obs.byteValue();
			}
		      }else{
		        obs.compl();
		        if(Math.random() > Constants.DEF_DETECTOR_PROB_ERROR){
			    trans[i] = obs.byteValue();
			}else{
			    error = true;
			    obs.compl();
			    trans[i] = obs.byteValue();
			}
		      }
		      //System.out.println("sent:"+nextqb+" received:"+trans[i]+
			//		 " error:"+error+" good pos:"+i);
		    }
		}
		Vector posV = getGoodPos(trans);
		int numberGoodPos = posV.size();
		Constants.sendSpecialInt(numberGoodPos, _spc.getOutputStream());
		for(int i =0;i<numberGoodPos;i++){
		    int vi = ((Integer)posV.elementAt(i)).intValue();
		    //sends the byte at the new position
		    _spc.getOutputStream().write(trans[vi]);
		    Constants.sendSpecialInt(vi,_spc.getOutputStream());
		}
		_spc.getOutputStream().flush();
	    }
	}//while(!finish)
	initServerConnection();//waits for a new resp server to connect
       }//while(true)
      }catch(IOException io){
	    throw new RuntimeException("Resp Fake DG got a broken connection with Resp Server.");
      }catch(TimeOutException to){
	    throw new RuntimeException("Resp Fake DG connection with InitFakeDG broken.");
      }
    }
    
    /**
    * Returns the DETECTED positions in the transmitted array.
    * @param trans is the result of the quantum transmission.
    * @return the vector of detected positions.
    */
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	static private Vector getGoodPos(byte[] trans){
	Vector v = new Vector();
	int l = trans.length;
	for(int i = 0; i<l; i++){
	    if(trans[i] != Constants.UNDETECTED){
		v.addElement(new Integer(i));
	    }
	}
	return v;
    }
    
    
    public void close(){
	if(_spc != null){
	    _spc.closeConnection();
	}
	if(_fakeQC != null){
	    _fakeQC.closeConnection();
	}
    }

}
