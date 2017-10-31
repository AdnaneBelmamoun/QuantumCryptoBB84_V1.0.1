package qrypto.protocols;

import qrypto.exception.TimeOutException;
import qrypto.gui.RespPlayer;
import qrypto.qommunication.SocketPubConnection;


@SuppressWarnings("rawtypes")
public class ProtocolManager
{

public static final String prefix = "qrypto.protocols.";

//Here whenever a new protocol is added it should be added in this array.
//Just add the name of the class in the following array.
public static final String[] classname = {"BB84","B92", "RecB92", "RecBB84","RawB92","RawBB84"};

public static String[] _protID = null;
public static Class[]  _class  = null;


    static{
	_protID = new String[classname.length];
	_class  = new Class[classname.length];
	try{
	    for(int i = 0; i<classname.length; i++){
	     Class c = Class.forName(prefix+classname[i]);
	     QProtocol proc = (QProtocol)c.newInstance();
	     _protID[i] = proc.protID();
	     _class[i]  = proc.getClass();
	    }
	}catch(ClassNotFoundException cnf){
	    throw new RuntimeException("Erreur de paramétrage du gestionnaire de protocol:"+cnf.getMessage());
	}catch(IllegalAccessException ia){
	    throw new RuntimeException("Erreur de paramétrage du gestionnaire de protocol:"+ia.getMessage());
	}catch(InstantiationException ie){
	    throw new RuntimeException("Erreur de paramétrage du gestionnaire de protocol:"+ie.getMessage());
	}
    }
    
    /**
    * Returns the classname related to a protocol identification.
    * @returns the classname of the protocol with id protid. Returns null
    * if no protocol the given id exists.
    */
    
    public static String getClassName(String protid){
	String out = null;
	Class c = getClass(protid);
	if(c != null){
		out = c.getName();
	}
	return out;
    }
    
    
    /**
    * Returns the class of the given protid.
    * @param protid is the id for the protocol.
    * @return the class for the protocol with id protid.
    */
    
    public static Class getClass(String protid){
	Class c = null;
	int i = getIndex(protid);
	if(i  >= 0){
	    c = _class[i];
	}
	return c;
    }
    
    
    /**
    * Returns an empty protocol object corresponding to an id.
    * @return the protocol object or null if it does not exist. 
    */
    
    public static QProtocol getProt(String protid){
	QProtocol qprot  = null;
	Class c = getClass(protid);
	if(c != null){
	    try{
	       qprot = (QProtocol)c.newInstance();
	    }catch(IllegalAccessException ia){
	       System.out.println("%%%Erreur de paramétrage du gestionnaire de protocol IllegalAccessException:"+ia.getMessage());
	       qprot = null;
	    }catch(InstantiationException ie){
	       System.out.println("%%%Erreur de paramétrage du gestionnaire de protocol InstanciationException of "+protid+":"+ie.getMessage());
	       qprot = null;
	    }
	}else{
	    System.out.println("%%%Erreur de paramétrage du gestionnaire de protocol:couldn't get classname for "+protid);
	}
	return qprot;
    }
    
    /**
    * Returns an array of strings containing the available protocol.
    * @return the array of strings containing all protocol id's 
    * available in the qrypto.protocols package.
    */
    
    public static String[] getAvailableProtID(){
	return _protID;
    }
    
    
    /**
    * This method returns a QProtocol object with the received configuration.
    * @param pc is the public connection between the two parties.
    * @return an intance of the received protocol and configuration. Null is returned
    * if a problem occured or the responder party does not agree with the settings.
    */
    
    public static QProtocol receiveProt(SocketPubConnection pc, RespPlayer owner){
	String id = null;
	QProtocol qp = null;
	try{
	    id = pc.receiveString();
	    owner.addToLogin("Le Répondeur a recu une requette pour le protocol:"+id);
	    qp = getProt(id);
	    if(qp != null){
		owner.addToLogin("Le Répondeur attend la configuration.");
		boolean ok = qp.receiveConfig(pc);
		owner.addToLogin("le Répondeur est configuré.");
		if(!ok){qp = null;}
	    }
	}catch(TimeOutException to){
	    owner.addToLogin("BLOODY HELL--Don't know what happeed!.");
	}
	return qp;
    }
    
    /**
    * Returns the index of the protocol with given ID.
    * @param protid is the ID to look for.
    * @return the index of the protocol or -1 if it does not exist.
    */
    private static int getIndex(String protid){
	int r  = -1;
	for(int i = 0; (i< _protID.length) && (r<0); i++){
	    if(protid.equals(_protID[i])){
		r = i;
	    }
	}
	return r;
    }
    

}
