package qrypto.server.BAK;

import qrypto.qommunication.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * This is an abstract class for the common methods
 * of both VirtualResponderServer and VirtualInitiatorServer.
 * 
 * @author  Louis Salvail (salvail@brics.dk)
 * @version $Id: 0.0$
 */


public abstract class AbstractVirtualServer implements Runnable{
    
protected boolean _run = false;
protected int _port = 0;
protected QConnection _qc = null;
protected ServerSocketConnection  _ssc = null;


    public AbstractVirtualServer(){
	_run = false;
	_port = 0;
	_qc = null;
	_ssc = null;
    }
  
   /**
     * Returns the quantum connection.
     * @return the quantum connection.
     */

    public QConnection getQConnection(){
	return _qc;
    }


    /**
     * Sets the quantum connection needed for the simulation 
     * of a quantum channel.
     * @param qc is the quantum connection
     */

    public void setConnection(QConnection qc){
	_qc = qc;
    }




    /**
     * Returns the port where this server listen for the client.
     * @return the port number.
     */

    public int whichPort(){
	return _port;
    }


    /**
     * Returns the IP address of this server.
     * @return the IP address.
     * @excception UnknownHostException whenever no IP address 
     *             for the host could be found.
     */

    public InetAddress getAddress()throws UnknownHostException{
	return InetAddress.getLocalHost();
    }




    /**
     * Returns whether or not this server is actually running.
     * @return true if this server is running.
     */

    public synchronized boolean running(){
	return _run;
    } 



    /**
     * Returns the server socket connection used by the client to send
     * the request.
     * @return the connection.
     */

    public ServerSocketConnection getServerConnection(){
	return _ssc;
    }



    /**
     * This method runs the server as a thread.
     */

    public abstract void run();



}
