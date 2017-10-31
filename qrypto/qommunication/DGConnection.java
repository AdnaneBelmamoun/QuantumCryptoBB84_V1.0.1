package  qrypto.qommunication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;




public class DGConnection extends Socket{


    /**
     * This is a constructor for a server socket connection by
     * listening to a given port.
     * @param  ip is the ip address of the DG server.
     * @param port is the port number where to wait for connection, should be above 1024.
     * @exception IOException if an I/O error occurs while creating the socket.
     */

    public DGConnection(InetAddress ip, int port)throws IOException{
	    super(ip,port);
    }

    public void close(){
      try{
	super.close();
      }catch(IOException io){
	System.out.println("Couldn't close the DGConnection properly.");
      }
    }




}
