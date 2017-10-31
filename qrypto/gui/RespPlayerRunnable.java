package qrypto.gui;

import java.io.PrintWriter;
import java.util.Hashtable;

import qrypto.htmlgenerator.htmlGenerator;
import qrypto.protocols.ProtocolManager;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.SocketPubConnection;


public class RespPlayerRunnable extends Thread
{

    private RespPlayer  _owner = null;
    
    
    public RespPlayerRunnable(String me, SocketPubConnection pc, 
			      RemoteQConnection rqc, RespPlayer owner){
	super();
	_owner = owner;
    }
    
    
   
    public void run(){
	boolean ok = false;
	try{
	    _owner.addToLogin("Attente de configuration.");
	    _owner._prot = ProtocolManager.receiveProt(_owner._pc, _owner);
	    _owner.addToLogin("Got  "+_owner._prot.peerID());
	    if(_owner._prot != null){
		_owner._prot.setMyID(_owner._myidentity, false);
		ok = _owner._prot.confirmSettings(_owner,_owner._pc);
	    }else{
		_owner.addToLogin("les configurations n'ont pas été correctement spécifiés par l'initiateur.");
		throw new RuntimeException("Spécification des configurations des protocols incorrecte !");
	    }
	    if(!ok){throw new RuntimeException("configuration refusée.");}
	    _owner._prot.setPubConnection(_owner._pc);
	    _owner._prot.setQuantConnection(_owner._rqc);
	    _owner._prot.setParent(_owner);
	    _owner.setNewProt(_owner._prot);
	    _owner.addToLogin(_owner._prot.protID()+" est en éxecution.");
	    _owner._prot.setProgressTools(_owner.progressBar, _owner.progressLabel);
	    _owner._prot.run();
	    _owner.addToLogin("Fin de l'execution de "+_owner._prot.protID()+" .");
	    //output generation next
	    if(_owner._outputFile != null){
	      if(!_owner._outputFile.isDirectory()){
		PrintWriter pw = _owner.openOutputStream();
		_owner.addToLogin("Enregistrement de la sortie.");
		_owner._prot.output(pw);
	      }else{//patch for HTML generation..
		_owner.addToLogin("Generation de sortie HTML dans "+_owner._outputFile.toString());
		//_owner.showOutputGenerationWindow();
		@SuppressWarnings("rawtypes")
		Hashtable ht = new Hashtable();
		_owner._prot.addMyHoles(ht,1);
		htmlGenerator hg = new htmlGenerator(ht,_owner._outputFile);
		if(hg.isOK()){
		    _owner.addToLogin("Make output.");
		    String filename = hg.makeHTMLOutput(_owner._prot.protFileName(),"index.html",null);
		    _owner.addToLogin("Le fichier HTML:"+filename+" a été généré avec succés.");
		    hg.makeHTMLindex(filename,_owner._prot.protID(),0);
		}else{
		    _owner.addToLogin("génération de Sortie HTML impossible.");
		}
		
	      }//end of patch.
	    }
	    _owner.addToLogin("Términé avec succé.");
	    _owner.stop("Execution Términé avec succé.",true);
	}catch(RuntimeException rt){
		_owner.stop("Execution du répondeur abondonée:"+rt.getMessage(),false);
	}
    }

}
