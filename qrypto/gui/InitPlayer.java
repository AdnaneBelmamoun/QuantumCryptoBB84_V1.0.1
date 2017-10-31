package qrypto.gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import qrypto.protocols.ProtocolManager;
import qrypto.protocols.QProtocol;
import qrypto.qommunication.Constants;
import qrypto.qommunication.RemoteQConnection;
import qrypto.qommunication.ServerSocketConnection;
import de.netcomputing.runtime.SmallMemTable;
import de.netcomputing.runtime.SwingInstantiator;



public class InitPlayer extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String _DEFAULT_PROG_LABEL = "Progression";
	
	private boolean _init_in_progress = true; //horrible patch related to itemevent
	
	private boolean _started = false;
	private String _myidentity = null;
	private int _clientport = 0;
	private int _serverport = 0;
	@SuppressWarnings("unused")
	private boolean _peerConnected = false;
	private InetAddress _ips = null;
	private RemoteQConnection _rqc = null;
	private ServerSocketConnection _ssc = null;
	private InitPlayerRunnable _T = null;
	
	protected QProtocol _prot = null;
	protected File _outputFile = null;
	protected PrintWriter _outputStream = null;
	//outputGeneration og = null;
	//JFrame ogFrame = null;
	//JDialog ogFrame = null;
	
	
	
	//JTextArea textarea;
	
	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JScrollPane scrollpane;
	JTextField serverIP;
	JTextField serverPort;
	JTextField myIdentity;
	JTextField clientPort;
	JButton cleanup;
	JButton outputFileButton;
	JButton inspect;
	JButton configButton;
	JComboBox protSelection;
	JProgressBar progressBar;
	JRadioButton startButton;
	JRadioButton stopButton;
	JLabel progressLabel;
	JTextArea textarea;
	//$$$endVars    -----------------------------------------------------------
	
	/**
	* Constructor.
	*/
	public InitPlayer(){
	    super();
       setBackground(Color.yellow);
	    _myidentity = "";
	    _clientport = 0;
	    _serverport = 0;
	    _prot = null;
	    try{
		_ips = InetAddress.getLocalHost();
	    }catch(UnknownHostException uh){
	    }
	}
	
	/**
	* Initiatlisation de la gui.
	*/
	
	@SuppressWarnings("deprecation")
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/InitPlayer.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		scrollpane = (JScrollPane) instantiator.getObject( "scrollpane");
		serverIP = (JTextField) instantiator.getObject( "serverIP");
		serverPort = (JTextField) instantiator.getObject( "serverPort");
		myIdentity = (JTextField) instantiator.getObject( "myIdentity");
		clientPort = (JTextField) instantiator.getObject( "clientPort");
		cleanup = (JButton) instantiator.getObject( "cleanup");
		outputFileButton = (JButton) instantiator.getObject( "outputFileButton");
		inspect = (JButton) instantiator.getObject( "inspect");
		configButton = (JButton) instantiator.getObject( "configButton");
		protSelection = (JComboBox) instantiator.getObject( "protSelection");
		progressBar = (JProgressBar) instantiator.getObject( "progressBar");
		startButton = (JRadioButton) instantiator.getObject( "startButton");
		stopButton = (JRadioButton) instantiator.getObject( "stopButton");
		progressLabel = (JLabel) instantiator.getObject( "progressLabel");
		textarea = (JTextArea) instantiator.getObject( "textarea");
		//$$$endVarInit -----------------------------------------------------------
		//textarea = new JTextArea();
		//textarea.setEditable(false);
		//this.remove(textarea);
		//this.validate();
		scrollpane.setViewportView(textarea);
		//scrollpane.setAutoscrolls(true);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		
		//scrollpane.revalidate();
		addToLogin("Arret�.");
		_started = false;
		String[] allProt = ProtocolManager.getAvailableProtID();
		for(int i = 0; i<allProt.length; i++){
		 protSelection.addItem(allProt[i]);
		}
		_prot = ProtocolManager.getProt((String)protSelection.getSelectedItem());
		_init_in_progress = false;
		ButtonGroup group = new ButtonGroup();
		group.add(stopButton);
		group.add(startButton);
		stopButton.setSelected(true);
		
		//extra settings of label and bar
		
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setForeground(Color.black);
		progressLabel.setText(_DEFAULT_PROG_LABEL);
		
		//others
		
		serverIP.setText(Constants.INIT_SERVER_DEF_IP);
		serverPort.setText(Integer.toString(Constants.INIT_SERVER_CLIENT_DEF_PORT));
		clientPort.setText(Integer.toString(Constants.INIT_PLAYER_DEF_PORT));
this.setBackground(Color.yellow);		
		this.revalidate();
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
	     JFrame f = new JFrame("Nettoyage de l'index");
	     f.setResizable(true);
	     f.setBackground(Color.yellow);
	     f.getContentPane().add("Center",o);
	     f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	     f.pack();
	     f.show();
	   }catch(IOException io){
	    QryptoWarning.warning("ouverture du fichier indexe impossible\n"+
	                          "Assurez vous d'avoir selectionner le bon r�p�rtoir de sortie!",
				  "Init Player",this);
	   }finally{
		this.repaint();
	  } 
	}
	
public void outputFileButton_actionPerformed( ActionEvent var0 )
	{
	    outputFileButton.setEnabled(false);
	    File htmlDir = new File(Constants.TEMPLATES_DIR.toString()+File.separator+Constants.INITDIRNAME);
	    JFileChooser chooser = new JFileChooser(Constants.TEMPLATES_DIR);
	    if(htmlDir.exists() && (_outputFile == null)){
		chooser.setSelectedFile(htmlDir);
	    }else{
		if(_outputFile != null){chooser.setSelectedFile(_outputFile);}
	    }
	    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    chooser.setDialogTitle("Switch ON/OFF pour Generer la sortie");
	    chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
	    int returnVal = chooser.showDialog(this,"Generates output");
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
		outputFileButton.setText("aucune sortie");
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
		      JOptionPane.showMessageDialog(this, "installation du flux de sortie impossible",
							    "R�p�ter encore",JOptionPane.ERROR_MESSAGE);
		      _outputStream = null;
		      //this.repaint();
		    }
	       }else{
		    _outputStream = null;
	       }
	  }catch(IOException io){
		JOptionPane.showMessageDialog(this, "Je n'arrive pas a ouvrir le fichier s�l�ctionn�",
						    "Selectionner un autre fichier!",
						    JOptionPane.ERROR_MESSAGE);
		_outputStream = null;
		//this.repaint();
	  }
	  return _outputStream;
	}


	
	public void protSelection_itemStateChanged( ItemEvent var0 )
	{
	  if(!_init_in_progress && (var0.getID() == ItemEvent.ITEM_STATE_CHANGED)){
	    String selected_prot = (String)protSelection.getSelectedItem();
	    _prot = ProtocolManager.getProt(selected_prot);
	    extractID();
	    _prot.setID(_myidentity, _prot.peerID(),true);
	  }
	}
	
	public void inspect_actionPerformed( ActionEvent var0 )
	{
	    extractID();
	    _prot.setID(_myidentity,_prot.peerID(),true);
	    _prot.showSettings(this);
	    this.repaint();
	}


	
	public void configButton_actionPerformed( ActionEvent var0 )
	{
	     extractID();
	     _prot.configure(this);
	     this.repaint();
	}

	public void stopButton_actionPerformed( ActionEvent var0 )
	{
	    if(_started){
		int answ = JOptionPane.showConfirmDialog(this,
				"Vous voulez Vraiment Arreter l'�xecution du protocol ?",
				"Arret du Protocol",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);
			    //this.repaint();					
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
		outputFileButton.setEnabled(false);
		cleanup.setEnabled(false);
		progressLabel.setForeground(Color.black);
		progressLabel.setText(_DEFAULT_PROG_LABEL);
		boolean ok = extractSettings();
		if(ok){
		  try{
		      configButton.setEnabled(false);
		      protSelection.setEnabled(false);
		      _started = true;
		      _rqc = new RemoteQConnection(_ips, _serverport);
		      _ssc = new ServerSocketConnection(_clientport);
		      _prot.setMyID(_myidentity, true);
		      _prot.setPubConnection(_ssc);
		      _prot.setQuantConnection(_rqc);
		      _T = new InitPlayerRunnable(this);
		      _T.start();
		      addToLogin("D�marr�.");
		  }catch(IOException io){
		      stop("Connection au serveur initiateur impossible , \n V�rifiez le n� de ports et/ou" +
		      		"la configuration IP.",false);
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
	    _serverport = verifyPortString(serverPort.getText(), 1,"Port du Serveur");
	    _clientport = verifyPortString(clientPort.getText(), _serverport,"Port du client");
	    _ips = verifyIPString(serverIP.getText(),_clientport);
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
				"Le "+field_desc+" n'est pas un integer dans["+Constants.MIN_PORT_VALUE+
				"..."+Constants.MAX_PORT_VALUE+"]",
				"Mauvaise configuration",
			    JOptionPane.ERROR_MESSAGE);
		//this.repaint();
	    }
	}
	return ans;
       }
       
       
            
       private InetAddress verifyIPString(String ipstring, int go){
	InetAddress ina = null; 
	if(go>-1){
	    try{
		ina = InetAddress.getByName(ipstring);
	    }catch(UnknownHostException uh){
		ina = null;
		JOptionPane.showMessageDialog(this,
				"l'adresse IP du serveur n'as pas �t� Trouv�." ,
				"Mauvaise configuration",
			    JOptionPane.ERROR_MESSAGE);
		 //this.repaint();
	    }
	}
	return ina;
       }
	
	
      public synchronized void stop(String message, boolean success){
	String ww = "";
	int mtype = JOptionPane.INFORMATION_MESSAGE;
	if(!success){
	    mtype = JOptionPane.ERROR_MESSAGE;
	    ww = "(WOOPS)";
	}
	JOptionPane.showMessageDialog(this,
				message,
				"Les Initiateur on T�rminer "+ww,
			    mtype);
	//this.repaint();
	stop();
      }
      
      
      
      public void stop(){
	if(_started){
	    _started = false;
	    if(_rqc != null){_rqc.closeConnection();}
	    if(_ssc != null){_ssc.closeConnection();}
	    if(_outputStream != null){_outputStream.close();}
	    stopButton.setSelected(true);
	    configButton.setEnabled(true);
	    protSelection.setEnabled(true);
	    outputFileButton.setEnabled(true);
	    cleanup.setEnabled(true);
	    progressLabel.setForeground(Color.darkGray);
	    progressLabel.setText("("+progressLabel.getText()+")");
	    progressBar.setValue(0);
	    addToLogin("Arret�.");
	}
      }

            
	
	@SuppressWarnings("deprecation")
	static public void main(String[] args){
	    JFrame frame = new JFrame();
	    InitPlayer player = new InitPlayer();
	    player.initGui();
	    frame.getContentPane().add("Center", player);
	    frame.pack();
	    frame.show();
	}
}
