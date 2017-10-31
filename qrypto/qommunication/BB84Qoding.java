package qrypto.qommunication;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import qrypto.exception.AcknowledgeException;
import qrypto.exception.QryptoException;
import qrypto.exception.QuBitFormatException;
import qrypto.exception.TimeOutException;
import qrypto.log.Log;



public class BB84Qoding implements QodingType{

   // public static final QuBit[] BB84ALPHABET = {new BB84QuBit(true,true),
	//					 new BB84QuBit(true,false),
	//					 new BB84QuBit(false,true), 
	//					 new BB84QuBit(false,false)};
    public static final BB84QuBit[] BB84ALPHABET = { BB84QuBit.B1D, 
						     BB84QuBit.B1R, 
						     BB84QuBit.B0D,
						     BB84QuBit.B0R};
						   
    public static final BB84QuBit[] BB84OBS      = { BB84QuBit.B0R,
						     BB84QuBit.B0D};

    //This is the name for this qoding type.
    
    protected JProgressBar _playerBar = null;
    
    public static final int DEFAULT_N = 100;
    protected int _n; // the length of the transmission.
    protected int _bucketSize = 1;
    protected int _numberOfBuckets = 0;
    protected String _initiatoridentity = null; //is the identity of the initiator
    protected String _responderidentity = null; //is the identity of the responder.
    protected boolean _iminitiator = true;
    protected BB84QuBit[] _qb = null;

    
    protected Log _logfile = null;
    protected byte _qType = Constants.UNDEFINED;//The type of the quantum transmission

    /**
     * Empty constructor.
     */
   public BB84Qoding() {
	_logfile = null;
	_n = DEFAULT_N;
	_qb = null;
   }


    /**
     * Empty constructor with logfile.
     * @param logfile is the logfile. If null then no logfile
     *  is produced. 
     */

    public BB84Qoding(Log logfile){
      this();
      _logfile = logfile;
    }


    /**
     * Constructor where n is kept as default.
     * @param me is my identity
     * @param peer is the peer identity
     * @param iminitiator is true iff i'm the initiator
     */


    public BB84Qoding(String me, String peer, boolean iminitiator){
	this(DEFAULT_N, me, peer, iminitiator,null);
    }


    /**
     * Constructor for BB84 transmission of a certain length and two parties.
     * @param n is the length.
     * @param me is my identity.
     * @param peer is the identity of the peer.
     * @param iminitiator is true if I am initiator client. Otherwise I'm a responder client.
     */

    public BB84Qoding(int n, String me, String peer, boolean iminitiator){
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

    public BB84Qoding(int n, String me, String peer, boolean iminitiator, Log logfile){
	if(logfile != null){
	    _logfile = logfile;
	}
	_n = n;
	setID(me,peer,iminitiator);
	_qb = null;
    }
    
    /**
    * Returns the quantum transmission type. Constants.VIRTUAL or Constant.REAL
    * This is typically called by the client to learn which kind of q-trans
    * was performed.
    * @param t is the new type.
    */
    
    public byte getQType(){
	return _qType;
    }
    
    /**
    * This sets the type of quantum transmission. This is called
    * by the server to tell what kind of q-trans was performed.
    * @param t is the bytecode for the type of q-trans.
    * This is Constants.VIRTUAL or Constants.REAL.
    */
    
    public void setQType(byte b){
	_qType = b;
    }
    
    /**
    * Sets the bucket size externally. Usually called by the
    * initiator and responder servers when the QodingType is
    * created. The default value of the bucket size is 1.
    * This is the value for virtual quantum transmission.
    * @param bs is the new bucket size. Must be smaller
    * than 256*255 + 255 and greater than 0.
    */
    
    public void setBucketSize(int bs){
     if( ( bs< Constants.MAX_BUCKET_SIZE ) && (bs>0)){
	_bucketSize = bs;
     }
    }
    
    
    /**
    * Returns the bucket size. This is usually called by the
    * players in ordert to recover the bucket size used for the
    * last quantum transmission. Can also be called by the
    * servers but seems rather useless in this case since they
    * already have that information.
    * @return the bucket size.
    */
    
    public int getBucketSize(){
	return _bucketSize;
    }
    
    
    /**
    * Returns the number of buckets needed for last quantum transmission.
    * It is intended to be used by the clients.
    * @return the number of buckets.
    */
    
    public int getNumberOfBuckets(){
	return _numberOfBuckets;
    }
    
    
    
   /**
    * Sets the progress bar that is modified whenever the player
    * is get the output of a quantum transmission. If this object is not
    * a player this has not to be called. If a player has this field null then
    * no progress is shown.
    * @param bar is the progress bar.
    */
    
    public void setPlayerProgressBar(JProgressBar bar){
	_playerBar = bar;
    }
    
    
    /**
    * Sets the players identities.
    * @param me is my identity
    * @param peer is the peer identity
    * @param iminitiator is true iff i'm the initiator
    */

    public void setID(String me, String peer, boolean iminitiator){
	_iminitiator = iminitiator;
	if(iminitiator){
	     _initiatoridentity = me;
	     _responderidentity = peer;
	}else{
	     _initiatoridentity = peer;
	     _responderidentity = me;
	}
    }
    
  /**
   * This returns the name of this coding scheme in a better
   * form than the classname.
   * @return the name of this coding scheme.
   */
   
   public String getCodingName(){
    return "BB84";
   }




    /**
    * Returns the name of this quantum method of transmission;which is the name 
    * of this class fpr the time being.
    * @return the same as this.className().
    */
    
    public String getMethod(){
	return this.getClass().getName();
    }
    

    /**
     * This method gets the parameter required for BB84 transmission on the
     * initiator side. This is run by the initiator server.
     * That is the length n, the initiator identity and the responder identity
     * @param ssc is the server connection with the client.
     * @exception qrypto.exception.AcknowledgeException when the acknowlegement
     * could not be processed.
     */ 

    public void acceptRequest(ServerSocketConnection ssc) throws AcknowledgeException{
	try{
	    Log.write(_logfile,"server Initiateur  accepte une requette "+getCodingName()+ " .",true);
	    _initiatoridentity = ssc.receiveString();
	    Log.write(_logfile,"server Initiateur requis pour :"+_initiatoridentity,true);
	    _responderidentity = ssc.receiveString();
	    Log.write(_logfile,"server Initiateur requis aussi pour :"+_responderidentity,true);
	    _n = ssc.receiveInt();
	    Log.write(_logfile,"server Initiateur requis pour n=",false);
	    Log.write(_logfile,_n,false);
	    ssc.sendByte(_qType);
	    ssc.sendInt(_bucketSize);
	    Log.write(_logfile,"...Envoi..OK..precedent.",true);
	    ssc.sendByte(Constants.OK);
	}catch(TimeOutException to){
	    throw new 
	     AcknowledgeException("le serveur initiateur n'as pas recu de requette valide.");
	}
    }


    /**
     * This makes a request for a BB84 or subclass quant transmission to a server on the initiator side.
     * This is called only by the initiator client.
     * Client: ack       <--------->            Server
     *         CLASSNAME  --------->            Server
     *                   <---------- OK
     *                               or ERROR
     *     if OK then n  ----------->           Server
     * @param spc is the pub connection with the initiator server.
     * @exception qrypto.exception.AcknowledgeException whenever a problem occured.
     */

    public void makeInitiatorRequest(SocketPubConnection spc) throws AcknowledgeException{
	    Log.write(_logfile,"init: client anvoi classname au serveur",true);
	    spc.sendString(this.getMethod());
	    Log.write(_logfile,"init: Envoi son identité.",true);
	    spc.sendString(_initiatoridentity);
	    Log.write(_logfile,"init: Envoi l'identité du Répondeur",true);
	    spc.sendString(_responderidentity);
	    spc.sendInt(_n);
	    byte ok = Constants.ERROR;
	    try{
		Log.write(_logfile,"init:Reception OK du  client:",false);
		_qType = spc.receiveByte();
		_bucketSize = spc.receiveInt();
		ok = spc.receiveByte();
		Log.write(_logfile,new Boolean(ok== Constants.OK).toString(),true);
	    }catch(TimeOutException to){
		throw 
	      new AcknowledgeException("init:  OK du server n'as jamais été reçue sur une requette:CLASSNAME");
	    }
	    if(ok != Constants.OK){
	     throw new AcknowledgeException("init: client N'as pas accepter la requette:"+this.getMethod());
	    }
    }



    /**
     * This method is called by the responder client for receiving the request made by
     * the initiator from the responder server. The communication in that
     * method involved the responder client and the responder server.
     *
     * Server: protid ------------->(classname)
     *         peerid ------------->            Client
     *         myid   ------------->
     *           n    ------------->
     *                <-------------     OK or
     *                                 ERROR
     * @param spc is the connection with the responder server.
     * @exception qrypto.exception.AcknowledgeException whenever the two couldn't agree.
     */

    public void waitForResponderServer(SocketPubConnection spc) throws AcknowledgeException{
	String protid = null;
	String initidentity = null;
	String respidentity = null;
	int n1 = 0;
	Log.write(_logfile,"resp: client attent la description du protocol depuis le serveur repondeur",true);
	try{
	    protid = spc.receiveString();
	    Log.write(_logfile, "resp: identification du protocol :"+protid,true); 
	    initidentity = spc.receiveString();
	    Log.write(_logfile, "resp: identité du client initiateur  :"+initidentity,true);
	    respidentity = spc.receiveString();
	    Log.write(_logfile,"resp: identité du client repondeur:"+respidentity,true);
	    n1 = spc.receiveInt();
	    Log.write(_logfile,"resp:Reçu n=",false);
	    Log.write(_logfile,n1,true);
	    _qType = spc.receiveByte(); 
	    _bucketSize = spc.receiveInt();
	    //System.out.println("REC CLIENT GOT BUCKET SIZE:"+_bucketSize);
	}catch(TimeOutException to){
	    Log.write(_logfile,"n'arrive pa a avoir la description de procédure:"+to.getMessage(),true);
	   throw new 
	     AcknowledgeException("Respondeur n'arrive pa a avoir la description de procédure du serveur:"+to.getMessage());
	}
	if(initidentity.equals(_initiatoridentity) && 
		respidentity.equals(_responderidentity) && protid.equals(this.getMethod())){
	    Log.write(_logfile,"resp: la requette est accepté .",true);
	    _n = n1;
	    spc.sendByte(Constants.OK);
	}else{
	    Log.write(_logfile,"resp: la requette est refusé.",true);
	   spc.sendByte(Constants.ERROR);
	   throw new
	       AcknowledgeException("Respondeur n'as pas vu juste "+getCodingName()+ " paramétrage a partir du serveur.");
	}
    }


 
    /**
     * This method is called by the responder server for receiving the setup
     * of the next quantum transmission. This is the receiving part associated to
     * </tt>setTransmission</tt>. This is server-server public communication.
     * @param spc is the pub connection listener side between the two servers.
     * @exception qrypto.exception.AcknowledgeException whenever no agreement was met.
     */

    public void getTransmissionRequest(SocketPubConnection spc) throws AcknowledgeException{
	try{
	    Log.write(_logfile,"serveur Respondeur a recu la requette suivante.",true);
	    _initiatoridentity = spc.receiveString();
	    Log.write(_logfile,"serveur Respondeur a contacter l'initiator:"+_initiatoridentity,true);
	    _responderidentity = spc.receiveString();
	    Log.write(_logfile,"Serveur Respondeur posséde un répondeur :"+_responderidentity,true);
	    _bucketSize = spc.receiveInt();
	    //System.out.println("RESP SERVER GOT BUCKET SIZE:"+_bucketSize);
	    _n = spc.receiveInt();
	    Log.write(_logfile,"serveur Respondeur  a maintenant n="+_n,true);
	}catch(TimeOutException to){
	    spc.sendByte(Constants.ERROR);
	    throw new AcknowledgeException("serveur Respondeur n'as pas recu de requette du serveur initiateur.");
	}
	spc.sendByte(Constants.OK);
    }




    /**
     * This method is called by the responder server in order to transmit to the responder
     * client the transmission request received from the initiator server.
     * @param ssc is the server pub connection between the responder server to the responder client.
     * @exception qrypto.exception.AcknowledgeException when the two servers couldn't agree.
     */

    public void retransmitRequest(ServerSocketConnection ssc) throws AcknowledgeException{
	if((_initiatoridentity != null) &&(_responderidentity != null)){
	    ssc.sendString(this.getMethod());
	    ssc.sendString(_initiatoridentity);
	    ssc.sendString(_responderidentity);
	    ssc.sendInt(_n);
	    ssc.sendByte(_qType);
	    //System.out.println("BUCKET SIZE BAK TO THE RESP CLIENT:"+_bucketSize);
	    ssc.sendInt(_bucketSize);
	}else{
	    throw new AcknowledgeException("serveur Respondeur  n'as pas pu renvoyer la requete au client initiateur.");
	}
	try{
	    byte res = ssc.receiveByte();
	    if(res != Constants.OK){
	     throw new AcknowledgeException("le client répondeur n'as pas accepter la requette.");
	    }
	}catch(TimeOutException to){
	    throw new AcknowledgeException("Le client répondeur n'as pas répondu a la requette.");
	}
    }


    /**
     * This method sends the request back to the responder server through
     * a public connection between the two servers.
     * This is called by the initiator server which is the server of the pub connection.
     * @param ssc is the pub connection between the two servers.
     * @exception qrypto.exception.AcknowledgeException whenever the responder
     * server did not acknowledge.
     */


    public void setTransmissionRequest(ServerSocketConnection ssc) throws AcknowledgeException{
       ssc.sendString(this.getMethod());
       ssc.sendString(_initiatoridentity);
       ssc.sendString(_responderidentity);
       //System.out.println("SEND BUCKET SIZE TO RESP SERVER:"+_bucketSize);
       ssc.sendInt(_bucketSize);
       ssc.sendInt(_n);
       byte res = Constants.ERROR;
       try{
	   res = ssc.receiveByte();
       }catch(TimeOutException to){
	   throw new AcknowledgeException("serveur Initiateur n'as pas recu le OK du serveur respondeur.");
       }
       if(res != Constants.OK){
	   throw new AcknowledgeException("serveur Initiateur a Recu ERROR du serveur Répondeur.");
       }
    }





    /**
     * Transmission over a (virtual or real) quantum channel according to the agreed parameters.
     * This the initiator server part of the algorithm.
     * @param qc is the quantum sender used for this transmission.
     * @exception qrypto.exception.QryptoException when the parameters were not set properly.
     */

    public void sendQTransmission(QSender qc) throws QryptoException{
	if(_n>0){
	    //QuBit[] tqb = qc.send(Constants.BB84,_n);
	    //_qb = new BB84QuBit[_n];
	    //for(int i = 0; i<tqb.length;i++){
		//_qb[i] = (BB84QuBit)tqb[i];
	    //}
	   qc.sendAndForward(Constants.BB84,_n);
	   qc.sendNumberOfBucketsToClient();
	}else{
	    throw new QryptoException("serveur Initiateur n'arrive pas a transmettre sur le canal quantique.");
	}
    }
    
    
    /**
     * Reception of BB84 random qubits over a (virtual or random) quantum channel.
     * This is the responder server part of the algorithm.
     * @param qc is the quantum receiver used for this transmission.
     * @exception qrypto.exception.QryptoException when a problem occured.
     */

    public void readQTransmission(QReceiver qc) throws QryptoException{
	if(_n>0){
	    //QuBit[] tqb= qc.read(Constants.BB84, _n);
	    //_qb = new BB84QuBit[_n];
	    //for(int i = 0; i<tqb.length;i++){
		//_qb[i] = (BB84QuBit)tqb[i];
	    //}
	    qc.setBucketSize(_bucketSize);
	    qc.readAndForward(Constants.BB84,_n);
	    qc.sendNumberOfBucketsToClient();
	}else{
	    throw new QryptoException("Serveur Respondeur a une taille de transmission négatif.");
	}
    }
    


    /** 
     * This method allows both clients to get the result of the last quantum
     * transmission.
     * @param spc is the socket pub connection from the client to the server.
     * @return the sequence of qubits received or sent during last quantum transmission.
     * @exception qrypto.exception.AcknowledgeException whenever the result was not
     * available.
     */

    public QuBit[] getResult(SocketPubConnection spc) throws AcknowledgeException{
	@SuppressWarnings("unused")
	boolean bit;
	@SuppressWarnings("unused")
	boolean basis;
	if(_n>0){
	    _qb = new BB84QuBit[_n];
	    if(_playerBar !=  null){
	      _playerBar.setMaximum(_n-1);
	      _playerBar.setValue(0);
	    }
	    try{
		Log.write(_logfile,"Reception de "+_n+" Qubits.",true);
		for(int i = 0; i<_n; i++){
		    _qb[i] = BB84QuBit.receiveBB84QuBit(spc);
		    if(_playerBar != null){
			_playerBar.setValue(i);
		    }
		}
		_numberOfBuckets = spc.receiveInt();
	    }catch(TimeOutException to){
		throw new AcknowledgeException("Client n'arrive pas a recevoir tout les Qubits  QT arret avant Timeout."); 
	    }catch(QuBitFormatException qfe){
		throw new AcknowledgeException("Reception de miettes:"+qfe.getMessage());
	    }
	}else{
	    Log.write(_logfile,"Client n'arrive pas a avoir les résultat depuis _n=",false);
	    Log.write(_logfile,_n,true);
	    throw new AcknowledgeException("Client n'as pas encore acceder au resultat des Qbuits transmis QT.");
	}
	return _qb;
    }
    
    
    /**
    * Allows the user to configure the quantum transmission.
    * This means to select the length of the quantum transmission.
    */
    
    public void configure(Component parent){
	int ans = -1;
	String s = JOptionPane.showInputDialog(parent,
					"Nombre de Qbuits pour l'échangé:",
					"Vecteur config de transmission BB84",
					JOptionPane.QUESTION_MESSAGE);
	parent.repaint();
	try{
		Integer iv = Integer.valueOf(s);
		ans = iv.intValue();
	}catch(NumberFormatException nfe){
		ans = -1;
	}
	if(ans<1){
	     JOptionPane.showMessageDialog(parent,
				"Paramétrage par une mauuvaise valeur, n="+Integer.toString(_n)+" remains.",
				"Erreur Config",
			    JOptionPane.ERROR_MESSAGE);
	     parent.repaint();
	}else{
	    _n = ans;
	}
	
    }
    
    
   /**
    * Displays  the settings to the user.
    * @param owner is the parent pane of this option pane.
    * 
    */
    
    public void showSettings(Component owner){
	String me = _initiatoridentity;
	String peer  = _responderidentity;
	if(!_iminitiator){
	    me = peer;
	    peer = _initiatoridentity;
	}
	if(me == null){me = "non defini";}
	if(peer == null){peer = "non defini";}
	JOptionPane.showMessageDialog(owner,
				"la longeur ou taille de Transmission est n="+Integer.toString(_n)+"\n"+
				"Mon  identité est :"+me+"\n"+
				"Identité du correspondant:"+peer+".",
				"Paramétres du vecteur BB84 ",
				JOptionPane.WARNING_MESSAGE);
	owner.repaint();
    }
    
    
    /**
    * This method returns the qubits resulting from an execution of this
    * coding. 
    * @return the latest execution or null if it has not happened yet.
    */
    
    public BB84QuBit[] getQuBits(){
	return _qb;
    }


    @SuppressWarnings("unused")
	private boolean randBit(){
	double r = Math.random();
	boolean ans = false; 
	if(r > 0.50d){
	    ans = true;
	}
	return ans;
    }
    
    /**
    * finalize this object by releasing the array_qb.
    */
    
    public void finalize(){
	_qb = null;
    }

}
