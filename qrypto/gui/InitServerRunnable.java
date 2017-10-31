package qrypto.gui;


import java.io.IOException;
import java.net.UnknownHostException;

import qrypto.exception.RuntimeQryptoException;
import qrypto.exception.TimeOutException;
import qrypto.gui.BAK.InitServerPane;
import qrypto.qommunication.Constants;
import qrypto.qommunication.QSender;
import qrypto.qommunication.RealQSender;
import qrypto.qommunication.ServerSocketConnection;
import qrypto.qommunication.VirtualQSender;
import qrypto.server.VirtualInitiatorServer;



public class InitServerRunnable extends Thread{


private InitServer _owner = null;
private int _portc;
private int _ports;
@SuppressWarnings("unused")
private int _portq;
private QSender _qs = null;
private VirtualInitiatorServer _vis = null;
private ServerSocketConnection _conWithClient = null;





public InitServerRunnable(InitServer owner){
    super();
    _owner = owner;
    _portc = _owner._portc;
    _ports = _owner._ports;
    _portq = _owner._portq;
    _owner = owner;
}

    public InitServerRunnable(InitServerPane aThis) {
        throw new UnsupportedOperationException("Not yet implemented");
    }




public void run(){
    int errorLocation = 0;
    try{
	_owner.addToLogin("Confiuguration de la connection du client.");
	_conWithClient = new ServerSocketConnection(_portc);
	if(_owner.isQCReal()){
	     _owner.addToLogin("configuration Reel Canal Quantique au port #"+_ports+".");
	     _qs = new RealQSender(_ports,_conWithClient);
	}else{
	   _owner.addToLogin("configuration Virtuel Canal Quantique au port #"+_ports+".");
	   _qs = new VirtualQSender(_ports,_conWithClient);
	}
	byte result = _qs.appliesConfig(_owner._configTable);
	if(result != Constants.OK){
	    _owner.addToLogin("Configuration de l'envoyeur quantique impossible.");
	    throw new IOException("Configuration de l'envoyeur quantique impossible");
	}
	_qs.setProgressBar(_owner.progressBar);
	errorLocation++;
	_vis = new VirtualInitiatorServer(_qs);
	_vis.setInfoLabel(_owner.infoLabel);
	_vis.setClientTimeout(0);
	if(_vis.getQSender().waitConnection()){
	    _owner.addToLogin("ATTente de Requette.");
	    _vis.run();
	    _owner.addToLogin("Requette Accomplie.");
	    _owner.stop();
	}else{
	  throw new TimeOutException("Le Temps autorisé s'est écrouler avant l'établissementde la connection quantiique.");
	}
    }catch(UnknownHostException uhe){
	_owner.addToLogin("L'initialisation du Fake DG n'as pas pu trouver le serveur initiateur de la machine.");
	_owner.stop("Démarage de L'initiateur du fake DG impossible...");
    }catch(IOException io){
      if(errorLocation == 0){
	    _owner.stop("Installation de la Connexion Quantiquue impossible.\n"+io.getMessage());
      }else{
	    _owner.stop("Installation du Serveur initiateur impossible.\n"+io.getMessage());
      }
    }catch(NumberFormatException nf){
	_owner.stop("La Configuration  de la transmission quantique Virtuelle n'as pas été installé corréctement.");
    }catch(RuntimeQryptoException rt){
	_owner.addToLogin(rt.getMessage());
	_owner.stop(rt.getMessage());
    }catch(TimeOutException to){
	_owner.stop("Temps écoulé.");
	_owner.stop(to.getMessage());
    }catch(Exception rte){
	_owner.addToLogin("Erreur:"+rte.getMessage());
	_owner.stop("Erreur inconnu?");
    }
}



/**
* Close the opened connections. Is called when an execution
* is stopped.
* 
*/

public void closeAll(){
    if(_vis != null){
	_vis.close();
	_vis = null;
    }else{
      if(_qs != null){
	 _qs.close();
	 _qs = null;
      }
    }
}


}
