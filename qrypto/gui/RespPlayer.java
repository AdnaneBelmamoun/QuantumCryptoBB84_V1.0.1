package qrypto.gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import qrypto.exception.QryptoWarning;
import qrypto.protocols.QProtocol;
import qrypto.qommunication.Constants;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.SocketPubConnection;
import de.netcomputing.runtime.SmallMemTable;
import de.netcomputing.runtime.SwingInstantiator;


public class RespPlayer extends JPanel //implements PlayerInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String _DEFAULT_PROG_LABEL = "Progression";
	private static final String _UNDEFINED_PROT ="Protocol non Implémnté";
	private boolean _started = false;
	protected String _myidentity = null;
	protected String _peeridentity = null;
	protected int _clientport = 0;
	protected int _serverport = 0;
	protected InetAddress _ips = null;
	protected InetAddress _peerip = null;
	protected RemoteQConnection _rqc = null;
	protected SocketPubConnection _pc = null;
	private RespPlayerRunnable _T = null;
	
	protected File _outputFile = null;
	protected PrintWriter _outputStream = null;
	protected QProtocol _prot = null;
	//outputGeneration og = null;
	//JFrame ogFrame = null;
	
	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JScrollPane scrollpane;
	JLabel progressLabel;
	JProgressBar progressBar;
	JRadioButton startButton;
	JRadioButton stopButton;
	JTextField serverPort;
	JTextField serverIP;
	JTextField peerIP;
	JTextField myIdentity;
	JTextField clientPort;
	JLabel protLabel;
	JButton cleanup;
	JButton outputFileButton;
	JButton inspect;
	JTextArea textarea;
	//$$$endVars    -----------------------------------------------------------
	
	/**
	* Constructor.
	*/
	public RespPlayer(){
	    super();
	    //og = new outputGeneration();
	    //og.initGui();
	    //ogFrame = new JFrame();
	    //ogFrame.getContentPane().add("Center",og);
	    //ogFrame.setResizable(false);
	    //ogFrame.setTitle("Responder");
	    //ogFrame.pack();
	    _myidentity = "";
	    _peeridentity = "";
	    _clientport = 0;
	    _serverport = 0;
	    _prot = null;
	    try{
		_ips = InetAddress.getLocalHost();
	    }catch(UnknownHostException uh){
	    }
	}
	
	/**
	* Initiatlises the gui.
	*/
	
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		@SuppressWarnings("deprecation")
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/RespPlayer.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		scrollpane = (JScrollPane) instantiator.getObject( "scrollpane");
		progressLabel = (JLabel) instantiator.getObject( "progressLabel");
		progressBar = (JProgressBar) instantiator.getObject( "progressBar");
		startButton = (JRadioButton) instantiator.getObject( "startButton");
		stopButton = (JRadioButton) instantiator.getObject( "stopButton");
		serverPort = (JTextField) instantiator.getObject( "serverPort");
		serverIP = (JTextField) instantiator.getObject( "serverIP");
		peerIP = (JTextField) instantiator.getObject( "peerIP");
		myIdentity = (JTextField) instantiator.getObject( "myIdentity");
		clientPort = (JTextField) instantiator.getObject( "clientPort");
		protLabel = (JLabel) instantiator.getObject( "protLabel");
		cleanup = (JButton) instantiator.getObject( "cleanup");
		outputFileButton = (JButton) instantiator.getObject( "outputFileButton");
		inspect = (JButton) instantiator.getObject( "inspect");
		textarea = (JTextArea) instantiator.getObject( "textarea");
		//$$$endVarInit -----------------------------------------------------------
		addToLogin("Arret.");
		_started = false;
		//this.remove(textarea);
		textarea.setEditable(false);
		scrollpane.setViewportView(textarea);
		//scrollpane.setAutoscrolls(true);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inspect.setEnabled(false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(stopButton);
		group.add(startButton);
		stopButton.setSelected(true);
		
		//Extra settings of labels and progress bar.
		
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setText(_DEFAULT_PROG_LABEL);
		protLabel.setHorizontalAlignment(SwingConstants.CENTER);
		protLabel.setText(_UNDEFINED_PROT);
		peerIP.setText(Constants.INIT_PLAYER_DEF_IP);
		serverIP.setText(Constants.RESP_SERVER_DEF_IP);
		clientPort.setText(Integer.toString(Constants.INIT_PLAYER_DEF_PORT));
		serverPort.setText(Integer.toString(Constants.RESP_SERVER_CLIENT_DEF_PORT));
		
		this.revalidate();//why's that???
	}
	
	
	public void addToLogin(String s){
	    textarea.append(s+Constants.NEWLINE);
	}

	//$$$actions

	@SuppressWarnings("deprecation")
	public void cleanup_actionPerformed( ActionEvent var0 )
	{
	   try{
	     if(_outputFile == null){throw new IOException();}
	     File indexFile = new File(_outputFile,"index.html");
	     OutputManagerPane o = new OutputManagerPane(indexFile);
	     o.initGui();
	     JFrame f = new JFrame("Nettoyage de l'Index");
	     f.setResizable(true);
	     f.getContentPane().add("Center",o);
	     f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	     f.pack();
	     f.show();
	   }catch(IOException io){
	    QryptoWarning.warning("Impossible d'ouvrir le fichier index\n"+
	        "Assurer vous d'avoir choisi le bon répertoire" +
	        "!",
	         "Visionneuse d'Initiateur",this);
	   }finally{
	    this.repaint();
	   }
	
	}

	public void outputFileButton_actionPerformed( ActionEvent var0 )
	{
	    outputFileButton.setEnabled(false);
	    File htmlDir = new File(Constants.TEMPLATES_DIR.toString()+File.separator+Constants.RESPDIRNAME);
	    JFileChooser chooser = new JFileChooser(Constants.TEMPLATES_DIR);
	    if(htmlDir.exists() && (_outputFile == null)){
		chooser.setSelectedFile(htmlDir);
	    }else{
		if(_outputFile != null){chooser.setSelectedFile(_outputFile);}
	    }
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    chooser.setDialogTitle("Switch ON/OFF output generation");
	    chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
	    int returnVal = chooser.showDialog(this,"Generation de sortie");
	    if(returnVal == JFileChooser.APPROVE_OPTION){
		_outputFile = chooser.getSelectedFile();
		//String info;
		if(_outputFile.isDirectory()){
		    //info="HTML output will be generated in \n"+_outputFile.getPath();
		    outputFileButton.setText("**HTML**");
		}else{
		    //info="TEXT output will be generated in \n"+_outputFile.getPath();
		    outputFileButton.setText("**TEXT**");
		}
		//JOptionPane.showMessageDialog(this,info);
	    }else{
		outputFileButton.setText("acune sortie");
		_outputFile = null;
	    }
	    outputFileButton.setEnabled(true);
	    this.repaint();	
       }
	    
      
	public PrintWriter openOutputStream(){
	  try{
	     if(_outputFile != null){
		if((_outputFile.canWrite() || !_outputFile.exists())){
		  FileOutputStream fout = new FileOutputStream(_outputFile);
		  PrintWriter newStream = new PrintWriter(fout, true); 
		  if(_outputStream != null){_outputStream.close();}
		  _outputStream = newStream;
		}else{
		  JOptionPane.showMessageDialog(this, "impossible de configurer le Flux de sortie",
						      "réperté encore",JOptionPane.ERROR_MESSAGE);
		  _outputStream = null;
		  //this.repaint();
		}
	      }else{
		_outputStream = null;
	      }
	   }catch(IOException io){
	      JOptionPane.showMessageDialog(this, "ouverture du fichier séléctioné impossible",
						  "Selectionner un autre fichier!",
						  JOptionPane.ERROR_MESSAGE);
	      _outputStream = null;
	      //this.repaint();
	   }
	   return _outputStream;
	}


	

	public void inspect_actionPerformed( ActionEvent var0 )
	{
	    _prot.showSettings(this);
	    this.repaint();
	}


	public void stopButton_actionPerformed( ActionEvent var0 )
	{
	     if(_started){
		int answ = JOptionPane.showConfirmDialog(this,
				"Vous voulez vraiment arréter l'execution du protocole?",
				"Arret du Protocol",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);
		this.repaint();					
		if(answ == 0){
		 stop();
		}else{
		    if(_started){startButton.setSelected(true);}
		}
	     }
	     this.repaint();
	}
	
	

	public void startButton_actionPerformed( ActionEvent var0 )
	{
	    if(!_started){
		protLabel.setText(_UNDEFINED_PROT);
		progressLabel.setText(_DEFAULT_PROG_LABEL);
		progressLabel.setForeground(Color.black);
		outputFileButton.setEnabled(false);
		cleanup.setEnabled(false);
		boolean ok = extractSettings();
		if(ok){
		  String message = "";
		  try{
		       _started = true;
		      try{
		        _rqc = new RemoteQConnection(_ips, _serverport);
		      }catch(IOException io){
			int ans=JOptionPane.showConfirmDialog(this,"Le serveur Répondeur ne Répond pas \n"+
						    "Réessayer?",
						    "aucune Connexion possible",
						    JOptionPane.YES_NO_OPTION);
			if(ans==JOptionPane.YES_OPTION){
			    _rqc = new RemoteQConnection(_ips,_serverport);
			}else{
			 throw new IOException("Connexion au serveur répondeur impossible");
			}
		      }
		      try{
		        message = "arréter le serveur Répondeur avant de redémarrer";
			_pc = new SocketPubConnection(_peerip,_clientport);
		      }catch(IOException io){
			int ans=JOptionPane.showConfirmDialog(this,"Le client Initiateur ne répond pas\n"+
						    "Réessayez?",
						    "aucune connection possible",
						    JOptionPane.YES_NO_OPTION);
			if(ans==JOptionPane.YES_OPTION){
			    _pc = new SocketPubConnection(_peerip,_clientport);
			}else{
			 throw new IOException("Connexion a la visionneuse d'initiation impossible");
			}		      
		      }
		      _pc.sendString(_myidentity);
		      _T = new RespPlayerRunnable(_myidentity,_pc,_rqc,this);
		      _T.start();
		      addToLogin("démarre.");
		   }catch(IOException io){
		      stop("Toujours impossible de se connecter,.... J'abondonne!\n"
		           +message,false);
		   }
		}else{
		     stopButton.setSelected(true);
		}
	    }
	    this.repaint();
	}
	
	
	private void extractID(){
	    _myidentity = myIdentity.getText().trim();
	}
	
	
	
	private boolean extractSettings(){
	    extractID();
	    _ips = null;
	    _peerip   = verifyIPString(peerIP.getText(),1,"address IP du tiérce correspondant");
	    if(_peerip != null){
		_serverport = verifyPortString(serverPort.getText(), 1,"Port du Serveur"); 
		_clientport = verifyPortString(clientPort.getText(), _serverport,"Port du Client");
		_ips = verifyIPString(serverIP.getText(),_clientport,"Adresse IP du Serveur");
	    }
	    return (_ips != null);
	}
	

             private int verifyPortString(String vals, int go, String field_desc){
	int ans = -1;
	if(go>-1){
	    try{
		Integer iv = Integer.valueOf(vals);
		ans = iv.intValue();
	    }catch(NumberFormatException nfe){
		ans = -1;
	    }
	    if((ans <Constants.MIN_PORT_VALUE)||(ans >Constants.MAX_PORT_VALUE)){
		ans = -1;
	    }
	    if(ans <0){
		JOptionPane.showMessageDialog(this,
				"Le "+field_desc+" n'est pas un integer dans ["+Constants.MIN_PORT_VALUE+
				"..."+Constants.MAX_PORT_VALUE+"]",
				"Mauvais paramétre",
			    JOptionPane.ERROR_MESSAGE);
		//this.repaint();
	    }
	}
	return ans;
       }
       
      
       
       private InetAddress verifyIPString(String ipstring, int go, String message){
	InetAddress ina = null; 
	if(go>-1){
	    try{
		ina = InetAddress.getByName(ipstring);
	    }catch(UnknownHostException uh){
		ina = null;
		JOptionPane.showMessageDialog(this,
				"Le "+message+ " n'as pas été trouvé." ,
				"Mauvaise configuration",
			    JOptionPane.ERROR_MESSAGE);
		 //this.repaint();
	    }
	}
	return ina;
       }
	
	
           
      public synchronized void stop(String message, boolean success){
       String ww = "";
       if(_started){
	    int mtype = JOptionPane.INFORMATION_MESSAGE;
	    if(!success){
	       mtype = JOptionPane.ERROR_MESSAGE;
	       ww = "(WOOPS)";
	    }
	    JOptionPane.showMessageDialog(this,
				    message,
				    "Le Répondeure a términer "+ww,
				mtype);
	    stop();
	}
      }
      
      
      
      
      public void stop(){
	if(_started){
	    _started = false;
	    //if(_rqc != null){_rqc.closeConnection();}
	    //if(_rqc != null){_pc.closeConnection();}
	    if(_rqc != null){_rqc.closeConnection();}
	    if(_pc != null){_pc.closeConnection();}
	    if(_outputStream != null){_outputStream.close();}
	    stopButton.setSelected(true);
	    outputFileButton.setEnabled(true);
	    cleanup.setEnabled(true);
	    if(!progressLabel.getText().equals(_DEFAULT_PROG_LABEL)){
	      progressLabel.setForeground(Color.darkGray);
	      progressLabel.setText("("+progressLabel.getText()+")");  
	    }
	    progressBar.setValue(0);
	    addToLogin("Arrété.");
	}
      }
      
      
     
      protected void setNewProt(QProtocol prot){
	_prot = prot;
	inspect.setEnabled(true);
	protLabel.setText(_prot.protID());
      }
      
      
	
	@SuppressWarnings("deprecation")
	static public void main(String[] args){
	    JFrame frame = new JFrame();
	    RespPlayer player = new RespPlayer();
	    player.initGui();
	    frame.getContentPane().add("Center", player);
	    frame.pack();
	    frame.show();
	}
}
