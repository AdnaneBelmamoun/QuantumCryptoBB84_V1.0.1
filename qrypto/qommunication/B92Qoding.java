package qrypto.qommunication;

import qrypto.exception.QryptoException;
import qrypto.log.Log;



public class B92Qoding extends BB84Qoding
{

    public static final QuBit[] B92ALPHABET = {new B92QuBit(B92QuBit.B0),
					       new B92QuBit(B92QuBit.B1)};

    //private BB84QuBit[] _qb = null;


    /**
     * Empty constructor.
     */
   public B92Qoding() {
	super();
   }


    /**
     * Empty constructor with logfile.
     * @param logfile is the logfile. No header added. If logfile=null then no logfile
     *  is produced. 
     */

    public B92Qoding(Log logfile){
      this();
      _logfile = logfile;
    }


    /**
     * Constructor where n is kept as default.
     * @param me is my identity
     * @param peer is the peer identity
     * @param iminitiator is true iff i'm the initiator
     */


    public B92Qoding(String me, String peer, boolean iminitiator){
	this(DEFAULT_N, me, peer, iminitiator,null);
    }


    /**
     * Constructor for BB84 transmission of a certain length and two parties.
     * @param n is the length.
     * @param me is my identity.
     * @param peer is the identity of the peer.
     * @param iminitiator is true if I am initiator client. Otherwise I'm a responder client.
     */

    public B92Qoding(int n, String me, String peer, boolean iminitiator){
	this(n,me,peer,iminitiator,null);
    }




    /**
     * Constructor for BB84 transmission of a certain length and two parties with logfile.
     * @param n is the length.
     * @param me is my identity.
     * @param peer is the identity of the peer.
     * @param iminitiator is true if I am initiator client. Otherwise I'm a responder client.
     * @param logfile is the logfile. No header added and if null then no logfile is produced.
     */

    public B92Qoding(int n, String me, String peer, boolean iminitiator, Log logfile){
	this();
	if(logfile != null){
	    _logfile = logfile;
	}
	_n = n;
	setID(me,peer,iminitiator);
	_qb = null;
    }
    
    /**
    * Returns the name of this coding.
    * @return "B92"
    */
    
    public String getCodingName(){
	return "B92";
    }
    
    


    /**
     * Transmission over a virtual quantum channel according to the agreed parameters.
     * This is the initiator server part of the algorithm.
     * @param qc is the quantum sender used for this transmission.
     * @exception qrypto.exception.QryptoException when the parameters were not set properly.
     */

    public synchronized void sendQTransmission(QSender qc) throws QryptoException{
	if(_n>0){
	   //QuBit[] tqb = qc.send(Constants.B92,_n);
	  // _qb = new BB84QuBit[_n];
	  // for(int i = 0; i<tqb.length;i++){
	   //   _qb[i] = (BB84QuBit)tqb[i];
	  // }
	   qc.sendAndForward(Constants.B92,_n);
	   qc.sendNumberOfBucketsToClient();
	}else{
	    throw new QryptoException("Serveur initiateur n'arrive pas a transmettre sur le canale quantique.");
	}
    }
    
    
    /**
     * Reception of BB84 random qubits over a virtual quantum channel.
     * This is the responder part of the algorithm.
     * @param qc is the quantum receiver used for this transmission.
     * @exception qrypto.exception.QryptoException when a problem occured.
     */

    public synchronized void readQTransmission(QReceiver qc) throws QryptoException{
	if(_n>0){
	    //QuBit[] tqb = qc.read(Constants.B92,_n);
	    //_qb = new BB84QuBit[_n];
	    //for(int i = 0; i<tqb.length;i++){
		//_qb[i] = (BB84QuBit)tqb[i];
	    //}
	    qc.readAndForward(Constants.B92,_n);
	    qc.sendNumberOfBucketsToClient();
	}else{
	    throw new QryptoException("serveur Respondeur  posséde une taille de Transmission Négative.");
	}
    }
    
    /**
    * Returns the B92 states sent by the sender. 
    */
    
    public B92QuBit[] getSenderResult(){
	return B92QuBit.convertFromBB84(_qb);
    }
    
    /**
    * Returns the B92 qubits the receiver received. When the classical
    * bit 0 has been obtained from the measurement then the B92 state
    * is undefined.
    */
    
    public B92QuBit[] getReceiverResult(){
	return B92QuBit.convertFromMeasurement(_qb);
    }
    


    /** 
     * This method allows both clients to get the result of the last quantum
     * transmission. The result is represented as a BB84 qubit array. On the
     * receiver side those qubits will in fact be B92 qubits whereas on the
     * receiver side those qubits are BB84. The receiver should call 
     * convertFromMeasurement() in order to obtain the final B92 qubits.
     * The sender should call convertFromBB84() in order to get the final B92
     * qubits.
     * @param spc is the socket pub connection from the client to the server.
     * @return the sequence of qubits received or sent during last quantum transmission.
     * @exception qrypto.exception.AcknowledgeException whenever the result was not
     * available.
     */

//    public synchronized QuBit[] getResult(SocketPubConnection spc) throws AcknowledgeException{


   /**
    * This method returns the B92 qubits resulting from an execution of this
    * coding. 
    * @return the latest execution or null if it has not happened yet.
    */
    
 //   public BB84QuBit[] getB92Result(){
//	return _qb;
//    }



}
