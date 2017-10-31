package qrypto.server;




import java.io.IOException;
import java.net.InetAddress;

import qrypto.exception.TimeOutException;
import qrypto.qommunication.B92QuBit;
import qrypto.qommunication.BB84QuBit;
import qrypto.qommunication.ClientDGConnection;
import qrypto.qommunication.Constants;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.SocketPubConnection;




public class FakeDG extends Thread
{


    private ClientDGConnection _spc = null;
    private int bucketsize = 0;
    private byte qcode = 0;
    private double _success = 1.0;
    private boolean _initiator = true;
    private String _whichDG = "Initiator ";
    private SocketPubConnection _fakeQC = null;
    private ServerSocketConnection _fakeQS = null;
    @SuppressWarnings("unused")
	private Thread _t = null;

    /**
    * Creates a fake dg with no connection with the other FakeDG.
    * @param port is the port number where to connect with the server.
    * @param ip is the IP address of the server.
    * @param success is the success probability of a time slot.
    * It means that with probability success a photon is detected
    * in each time slot. A quantum transmission is usually the concatenation
    * of several buckets out of which only the detected time slot
    * are kept. After receiving a new bucket, the server discuss with
    * the other server so they can find out which time slots will define
    * the qubits that are going to be used. The bucket size should be
    * more or less the expected number of time slots needed in order to keep
    * one qubit out of it.
    * @param initiator tells whether or not the DG will interact with the
    * initiator server or the responder server.
    * @exception IOException when the server couldn't be contacted.
    */
    public FakeDG(InetAddress ip,int port,  double success, boolean initiator)throws IOException{
	super();
	_success = success;
	_spc = new ClientDGConnection(ip,port);
	_initiator = initiator;
	if(!_initiator){
	   _whichDG = "Responder ";
	}
    }
    
    /**
    * Same as before except that it is for the client FakeDG which connects
    * to the server FakeDG.
    */
    
    public FakeDG(InetAddress ip, int port,double success, InetAddress ipF, int portF)throws IOException{
	this(ip,port, success, false);
	_fakeQC = new SocketPubConnection(ipF,portF);
    }
    
    
    /**
    * Creates a server FakeDG. 
    */
    
    public FakeDG(InetAddress ip, int port, int portF)throws IOException{
	this(ip,port,1.0d,true);
	_fakeQS = new ServerSocketConnection(portF);
	Thread _t = new Thread(_fakeQS);
	_t.start();
	
    }
    
    
    /**
    * Runs a fake data grabber session. Expects <OK><Bucket Size> or <STOP>.
    * Upon <OK><Bucket Size=b>, this return b random slot of random BB84 qubits.
    * Each time slot contains a detected photon with probability success.
    */
    
    public void run(){
      try{
	if(_initiator){
	    _fakeQS.waitConnection(0);
	}
	//receive the bytecode for the coding used in next transmission.
	//only used when the initiator server is used.
	qcode = (byte)(_spc.getInputStream().read());
	//receive the bucket size for the transmission.
	//two unsigned bytes encoding an integer.
	int highbs = _spc.getInputStream().read();
	int lowbs  = _spc.getInputStream().read();
	bucketsize = highbs*256 + lowbs;
	//send OK to acknowledge the received parameter.
	_spc.getOutputStream().write((int)Constants.OK);
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
		byte[] trans = new byte[bucketsize];
		for(int i = 0; i<bucketsize; i++){
		  if(_initiator){
		    trans[i] = (qcode == Constants.BB84)?
			      BB84QuBit.random().byteValue():B92QuBit.random().byteValue();
		    _fakeQS.sendByte(trans[i]);
		  }else{
		    byte nextqb = _fakeQC.receiveByte();
		    if(Math.random() <_success){
		      trans[i] = Constants.UNDETECTED;
		    }else{//simulates the measurement in random BB84 basis.
		      BB84QuBit obs = BB84QuBit.random();
		      if(BB84QuBit.convert(nextqb).measureVN(obs)){
			trans[i] = obs.byteValue();
		      }else{
			trans[i] = BB84QuBit.convert(obs.neg()).byteValue();
		      }
		    }
		  }
		}
		_spc.getOutputStream().write(trans);
		_spc.getOutputStream().flush();
	    }
	}
      }catch(IOException io){
	    throw new RuntimeException(_whichDG+" connection with DG broken.");
      }catch(TimeOutException to){
	    throw new RuntimeException(_whichDG+" connection with DG broken.");
      }
    }
    
    
    public void close(){
	if(_spc != null){
	    _spc.closeConnection();
	}
	if(_fakeQS != null){
	    _fakeQS.closeConnection();
	}
	if(_fakeQC != null){
	    _fakeQC.closeConnection();
	}
    }

}
