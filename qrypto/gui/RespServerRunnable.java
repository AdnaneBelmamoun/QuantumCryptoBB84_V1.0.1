package qrypto.gui;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import qrypto.exception.RuntimeQryptoException;
import qrypto.gui.BAK.RespServerPane;
import qrypto.qommunication.Constants;
import qrypto.qommunication.QReceiver;
import qrypto.qommunication.RealQReceiver;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.VirtualQReceiver;
import qrypto.server.QSocketPubConnection;
import qrypto.server.VirtualResponderServer;


public class RespServerRunnable extends Thread{


public static final String REAL_MODE_TXT = "Canal Quantique Réel";
public static final String VIRTUAL_MODE_TXT = "Canal Quantique virtuel";


private RespServer _owner = null;
private int _portc;
private int _ports;
private InetAddress _ia = null;
private QReceiver _qr = null;
private VirtualResponderServer _vrs = null;
private ServerSocketConnection _conWithClient = null;
private QSocketPubConnection _conWithServer = null;
//private FakeRespDG _TFakeDG = null;


//private boolean _ok = false;

    public RespServerRunnable(RespServer owner){
      super();
     _owner = owner;
     _portc = _owner._portc;
     _ports = _owner._ports;
     _ia    = _owner._ia;
    }

    public RespServerRunnable(RespServerPane aThis) {
        
    }
    
    public void run(){
	int errorLocation = 0;
	try{
	     _owner.addToLogin("Connéction au serveur initiateur et attente de la visionneuse");
	     _conWithServer = new QSocketPubConnection(_ia,_ports);
	     _conWithClient = new ServerSocketConnection(_portc);
	     if(_conWithServer.getTransmissionMode() == Constants.REAL){
		   _owner.addToLogin("Canal Quantique Reel sur  IP="+_ia.toString()+
				     " port #"+_ports+" Démarré.");
		   _qr = new RealQReceiver(_conWithServer,_conWithClient);
		   _owner.qType.setText(REAL_MODE_TXT);
	     }
	     if(_conWithServer.getTransmissionMode() == Constants.VIRTUAL){
		   _owner.addToLogin("Configuration du Canal quantique sur l'IP="+_ia.toString()+
		         	     "et le port #"+_ports);
		   _qr = new VirtualQReceiver(_conWithServer,_conWithClient);
		   _owner.qType.setText(VIRTUAL_MODE_TXT);
	     }
	     _qr.setProgressBar(_owner.progressBar);
	     byte result = _qr.appliesConfig(_owner._configTable);
	     if(result != Constants.OK){
	        _owner.addToLogin("Impossible de configurer l'expéditeur quantique.");
		throw new IOException("Impossible de configurer l'expéditeur quantique Reel.");
	     }
	     errorLocation++;
	     _vrs = new VirtualResponderServer(_qr);
	     _owner.addToLogin("Attente de Requette.");
	     _vrs.acceptInitiatorServerRequest();
	     _vrs.acceptResponderClient();
	     _owner.addToLogin("Execution de la Requette.");
	     _vrs.run();
	     // System.out.println("RESP SERVER BUCKET SIZE:"+_qr.getBucketSize());
	     _owner.addToLogin("Requette Accomplie.");
	     _owner.stop();
	}catch(UnknownHostException uhe){
	     _owner.addToLogin("Resp Fake DG n'arrive pa a trouver le serveur initiateur de la machine.");
	     _owner.stop("demarage du Resp. fake DG impossible...");
	}catch(IOException io){
	    if(errorLocation == 0){
		 _owner.stop("Je n'arrive pas a établire la communication  server-server .\n"
			      +io.getMessage());
	    }else{
		 _owner.stop("Je n'arrive pas a installer le serveur répondeur.\n"+
			      io.getMessage());
	    }
	}catch(RuntimeQryptoException rt){
		 _owner.addToLogin("Erreur dans le protocol:"+rt.getMessage());
		 _owner.stop("Je n'arrive pas à compléter l'éxecution.");
	}
    }
    
    


public void closeAll(){
    if(_vrs != null){_vrs.close();_vrs = null;}
    else{
	if(_qr != null){
	    _qr.close();
	    _qr = null;
	}
    }
 }


}
