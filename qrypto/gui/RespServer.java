package qrypto.gui;



import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

import qrypto.exception.QryptoWarning;
import qrypto.qommunication.Constants;
import qrypto.qommunication.RealQReceiver;
import qrypto.qommunication.VirtualQReceiver;
import qrypto.server.FakeRespDG;
import de.netcomputing.runtime.SmallMemTable;
import de.netcomputing.runtime.SwingInstantiator;




public class RespServer extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String _COMM_MODE_UNDEFINED = "Mode non défini";
   
    private final static String NEWLINE = "\n"; 


	//private final static int _REAL_CST = 1;
	//private final static int _VIRTUAL_CST = 0;
	private final static int _START_CST = 1;
	private final static int _STOP_CST = 0;
	
	public final static String VIRT_ERROR_RATE = "Taux d'Erreur pour la Transmission quantique Virtuelle";
	
	private JRadioButton _action[];
	private boolean _realQChanel = false;
	
	protected int _portc = 0;
	protected int _ports = 0;
	protected InetAddress _ia = null;
	protected boolean _fakeDG = false;
	private Thread _fakeDGThread = null;
	private boolean _started = false;  
	private RespServerRunnable _T = null;
	@SuppressWarnings("rawtypes")
	protected Hashtable _configTable = null;
	
	public boolean isQCReal = false;
	
	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JScrollPane scrollpane;
	JRadioButton stopButton;
	JRadioButton startButton;
	JProgressBar progressBar;
	JLabel qType;
	JButton configVirtualButton;
	JButton configRealButton;
	JTextField _IPfield;
	JTextField _portcfield;
	JTextField _portsfield;
	JTextArea _textarea;
	//$$$endVars    -----------------------------------------------------------

	
	
	@SuppressWarnings("rawtypes")
	public RespServer(){
		super();
		_action = new JRadioButton[2];
		_started = false;
		_configTable = new Hashtable();
	}
	
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		@SuppressWarnings("deprecation")
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/RespServer.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		scrollpane = (JScrollPane) instantiator.getObject( "scrollpane");
		stopButton = (JRadioButton) instantiator.getObject( "stopButton");
		startButton = (JRadioButton) instantiator.getObject( "startButton");
		progressBar = (JProgressBar) instantiator.getObject( "progressBar");
		qType = (JLabel) instantiator.getObject( "qType");
		configVirtualButton = (JButton) instantiator.getObject( "configVirtualButton");
		configRealButton = (JButton) instantiator.getObject( "configRealButton");
		_IPfield = (JTextField) instantiator.getObject( "_IPfield");
		_portcfield = (JTextField) instantiator.getObject( "_portcfield");
		_portsfield = (JTextField) instantiator.getObject( "_portsfield");
		_textarea = (JTextArea) instantiator.getObject( "_textarea");
		//$$$endVarInit -----------------------------------------------------------
		
		
		//Hack for putting the textarea in the scrollingpane.
		//this.remove(_textarea);
		_textarea.setEditable(false);
		scrollpane.setViewportView(_textarea);
		//scrollpane.setAutoscrolls(true);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		//Mettre les bouttons start et stop dans _actions[0] et _actions[1]
		//Creer un groupe exclusif pour ces bouttons.
		ButtonGroup group = new ButtonGroup();
		group.add(startButton);
		group.add(stopButton);
		_action[_START_CST] = startButton;
	        _action[_STOP_CST]  = stopButton;
		_action[_STOP_CST].setSelected(true);
		
		
		//Sets ports to the defaut values.
		_portcfield.setText(Integer.toString(Constants.RESP_SERVER_CLIENT_DEF_PORT));
		_portsfield.setText(Integer.toString(Constants.INIT_SERVER_SERVER_DEF_PORT));
		//Sets some labels and text field.
		qType.setHorizontalAlignment(SwingConstants.CENTER);
		qType.setText(_COMM_MODE_UNDEFINED);
		_IPfield.setText(Constants.INIT_SERVER_DEF_IP);
		addToLogin("Arreté.");
		 
		this.revalidate();
	}
	
       
	
	public void setFakeDG(boolean val){
	    _fakeDG = val;
	    if(!val){
		_fakeDGThread = null;
	    }
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void setAndLaunchFakeDG(){
	 setFakeDG(true);
	 try{
	    if(_fakeDGThread != null){
		_fakeDGThread.stop();
		_fakeDGThread = null;
	    }
	    _fakeDGThread=new Thread(new FakeRespDG(
	                              InetAddress.getByName(Constants.DEF_INIT_DG_IP),
				      Constants.DEF_PORT_FAKE_QC,
				      Constants.DEF_PROB_FAKE_SUCCESS_TRANSMISSION));
	    _fakeDGThread.start();
	    addToLogin("Fake DG est Démarré.");
	 }catch(IOException io){
	    setFakeDG(false);
	    QryptoWarning.warning("Je n'arrive pas à lancer le fake DG\n"+
	                           io.getMessage(),
	                          "RespServer",this);
	 }   
	}
	
		
	@SuppressWarnings("deprecation")
	public void stopFakeDG(){
	  if(_fakeDGThread != null){
	    _fakeDGThread.stop();
	  }
	  setFakeDG(false);
	}
	


	//$$$actions

	public void configRealButton_actionPerformed( ActionEvent var0 )
	{
	    _configTable = RealQReceiver.configure(_configTable,this);
	    this.repaint();
	}

	public void configVirtualButton_actionPerformed( ActionEvent var0 )
	{
	    _configTable = VirtualQReceiver.configure(_configTable,this);
	    this.repaint();
	}


	public void startButton_actionPerformed( ActionEvent var0 )
	{
	  boolean ok = false;
	  if(!_started){
		ok = verifyIPSetting();
		if(ok){
		       ok = verifyPortSetting();
		}
	        if(ok){
		    _T = new RespServerRunnable(this);
		    addToLogin("Démarré");
		    _T.start();
		    configVirtualButton.setEnabled(false);
		    configRealButton.setEnabled(false);
		    _started = true;
		 }else{
		    _action[_STOP_CST].setSelected(true);
		    _started = false;
		}
	  }
	  this.repaint();
	}
	
	
	

	public void stopButton_actionPerformed( ActionEvent var0 )
	{
	  if(_started){
	     int answ = JOptionPane.showConfirmDialog(this,
			 "Vous voulez vraiment arréter le serveur répondeur?",
			"Arret d'execution du serveur répondeur ",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);
			//this.repaint();					
	     if(answ == _STOP_CST){
		stop();
	     }else{
		if(_started){_action[_START_CST].setSelected(true);}
	     }
	  }
	  this.repaint();
	}
	
	
   
    
    public Object getConfigSetting(String configKey){
	return _configTable.get(configKey);
    }
 	
	
	
    
    protected void stop(String message){
	if(_started){
	    JOptionPane.showMessageDialog(this,
				message,
				"Arret d'execution",
			    JOptionPane.WARNING_MESSAGE);
	    stop();
	}
    }
     
    
    protected void stop(){
	if(_started){
	    _started = false;
	    addToLogin("Arrété.");
	    _action[_STOP_CST].setSelected(true);
	    //this.repaint();
	    qType.setText(_COMM_MODE_UNDEFINED);
	    configRealButton.setEnabled(true);
	    configVirtualButton.setEnabled(true);
	    progressBar.setValue(0);
	    if(_T != null){
		_T.closeAll();
	    }
	}
    }
    
    
    protected boolean verifyPortSetting(){
	int ports=0;
	int portc=0;
	boolean ok = true;
	ports = extractPort(_portsfield,"server port",0);
	portc = extractPort(_portcfield,"client port",ports);
	if(ok){
		_portc = portc;
		_ports = ports;
	}else{
	 JOptionPane.showMessageDialog(this,
			    "Je n'arrive pas a démarrer le serveur correctement;Vérifiez le n° de port ou" +
			    "le type de la communication quantique QC .",
			    "Mauvaise configuration",
			JOptionPane.ERROR_MESSAGE);
	 //this.repaint();
	}
	return ok;
    }
    
     
    
    protected int extractPort(JTextField tf, String field_desc, int go){
	String vals = tf.getText();
	int ans = -1;
	if(go>=0){
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
				"le "+field_desc+" in'est pas un integer dans ["+Constants.MIN_PORT_VALUE+
				"..."+Constants.MAX_PORT_VALUE+"]",
				"Mauvaise Configuration",
			    JOptionPane.ERROR_MESSAGE);
		//this.repaint();
	    }
	}
	return ans;
    }
    
 
      
   
    
    @SuppressWarnings("unused")
	private boolean verifyQConnection(){
	String message = "Seul le Canal quantique virtuel est supporté.";
	boolean result = true;
	if(_realQChanel){
	    result = false;
	    JOptionPane.showMessageDialog(this,
			    message,
			    "Mauvaise config",
			JOptionPane.ERROR_MESSAGE);
	    //this.repaint();
	}
	return result;
    }
    
    
      
    private boolean verifyIPSetting(){
	boolean ok = false;
	String ipstring = _IPfield.getText();
	try{
	    _ia = InetAddress.getByName(ipstring);
	    ok  = true;
	}catch(UnknownHostException uh){
	    JOptionPane.showMessageDialog(this,
			    "The server IP address cannot be found." ,
			    "Bad Setting",
			JOptionPane.ERROR_MESSAGE);
	    //this.repaint();
	}
	return ok;
    }
 
    
    
    
  public void addToLogin(String s){
	_textarea.append(s+NEWLINE);
    }
    
      @SuppressWarnings("deprecation")
	public static void main(String argv[]){
	JFrame frame = new JFrame("Serveur Répondeur");
	RespServer ii = new RespServer();
	ii.initGui();
	
	frame.getContentPane().add(BorderLayout.CENTER,ii);
	frame.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
			System.exit(0);
		    }
		});
	frame.pack();
	frame.show();
    }
    
    
    
    
}
