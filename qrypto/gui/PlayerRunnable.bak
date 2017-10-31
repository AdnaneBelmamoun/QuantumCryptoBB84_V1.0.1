package qrypto.gui;

import java.io.PrintWriter;
import java.util.Hashtable;

import qrypto.exception.TimeOutException;
import qrypto.htmlgenerator.htmlGenerator;
import qrypto.qommunication.ConnectionNotify;
import qrypto.qommunication.ServerSocketConnection;




public class InitPlayerRunnable extends Thread implements ConnectionNotify
{

    private InitPlayer _owner = null;
    private ServerSocketConnection _ssc = null;
    private boolean _peerConnected = false;
    private Thread _TPubConn = null;
    
    
    
    public InitPlayerRunnable(InitPlayer owner ){
	super(); 
	_owner = owner;
	_ssc = (ServerSocketConnection)_owner._prot.getPubConnection();
	if(!_ssc.isConnected()){
	    _ssc.setNotification(this);
	    _TPubConn = new Thread(_ssc);
	    _TPubConn.start();
	    _peerConnected = false;
	}
	else{
	    _peerConnected = true;
	}
    }
    
    
    
    @SuppressWarnings("rawtypes")
	public void run(){
	try{
	    boolean ok = ackWithPeer();
	    if(ok){
		_owner.addToLogin(_owner._prot.protID()+" est en execution.");
		_owner._prot.setProgressTools(_owner.progressBar, _owner.progressLabel);
		_owner._prot.setParent(_owner);
		_owner._prot.run();
		_owner.addToLogin("End of "+_owner._prot.protID()+" execution.");
		if(_owner._outputFile != null){
		    if(!_owner._outputFile.isDirectory()){
		         //printing the output in a text file.
			 PrintWriter pw = _owner.openOutputStream();
			 _owner.addToLogin("Saving the output.");
			 _owner._prot.output(pw);
		    }else{
			//patch for HTML generation 
			_owner.addToLogin("Generatinon de la sortie HTML dans "+_owner._outputFile.toString());
			//_owner.showOutputGenerationWindow();
			Hashtable ht = new Hashtable();
			_owner._prot.addMyHoles(ht,1);
			htmlGenerator hg = new htmlGenerator(ht,_owner._outputFile);
			if(hg.isOK()){
			    _owner.addToLogin("Fabriquation de la sortie.");
			    String filename = hg.makeHTMLOutput(_owner._prot.protFileName(),"index.html",null);
			    _owner.addToLogin("Le Fichier HTML:"+filename+" a été correctement généré.");
			    hg.makeHTMLindex(filename,_owner._prot.protID(),0);
			}else{
			    _owner.addToLogin("Génération fu fichier HTML impossible.");
			}
			//_owner.closeOutputGenerationWindow();
			//end of the patch.
		    }
		}
		_owner.addToLogin("Términé.");
		_owner.stop("Execution Términé avec Succes.",true);
	    }else{
		_owner.stop("Impossible to agree with peer, \n execution aborted!",false);
	    }
	}catch(RuntimeException rt){
	    _owner.stop("l'éxecution de l'Initiateur à été abondonnée:"+rt.getMessage(),false);
	}
    }
    
    
     
      
      public synchronized void notifyNewConnection(){
	_peerConnected = true;
	notifyAll();
      }
      
      
      private synchronized  boolean ackWithPeer(){
	boolean ok;
	_owner.addToLogin("Attente du Tierce corréspondant pour la Connéxion.");
	while(!_peerConnected){
	    try{
		wait();
	    }catch(InterruptedException ie){}
	}
	try{
	    String peer = _ssc.receiveString();
	    _owner.addToLogin(peer+" connecté, Envoi de la configuration.");
	    _owner._prot.setPeerID(peer);
	    ok = _owner._prot.sendConfig(_ssc,_owner);
	    if(ok){
		    _owner.addToLogin("Configuration acceptée par "+_owner._prot.peerID()+".");
	    }else{
		    _owner.addToLogin("Configuration refusée  par "+_owner._prot.peerID()+".");
	    }
	}catch(TimeOutException to){
	    ok = false;
	    _owner.addToLogin("Je n'est pas du tout Reçue l'identité du tiérce corréspondant(Peer).");
	}
	return ok;
      }
      

}
