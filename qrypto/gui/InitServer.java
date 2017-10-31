package qrypto.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import de.netcomputing.runtime.*;
import java.util.*;
import java.io.*;

import qrypto.server.*;
import qrypto.qommunication.*;
import qrypto.exception.*;



public class InitServer extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String _DEF_INFO = "Inactive";
    @SuppressWarnings("unused")
	private final static String _PORTS  = "Port du Serveur: ";
    @SuppressWarnings("unused")
    private final static String _PORTC  = "Port du Client: ";
    @SuppressWarnings("unused")
    private final static String _BSTART = "Démarage D'écoute";
    @SuppressWarnings("unused")
    private final static String _BSTOP  = "Arrét D'écoute";
    @SuppressWarnings("unused")
    private final static String _BCLOSE = "Férmeture du Serveur";
    @SuppressWarnings("unused")
    private final static String _LOGWIN = "Fenetre d'historique:";
    private final static String NEWLINE = "\n"; 

    //private int DEF_CLIENT_PORT_NUMBER = Constants.DEF_PORT_INIT_CLIENT;
    //private int DEF_SERVER_PORT_NUMBER = Constants.DEF_PORT_SERVERS_COM;



	private final static int _REAL_CST = 1;
	private final static int _VIRTUAL_CST = 0;
	private final static int _START_CST = 1;
	private final static int _STOP_CST = 0;
	
	
	private JRadioButton _action[];
	private JCheckBox _chkQC[];
	
	protected int _portc = 0;
	protected int _ports = 0;
	protected int _portq = 0;
	protected boolean _fakeDG = false;
	private Thread _fakeDGThread = null;
	
	
	private boolean _started = false;  
	private InitServerRunnable _T = null;
	@SuppressWarnings("rawtypes")
	protected Hashtable _configTable = null;
	
	
	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JTextArea _textarea;
	JScrollPane scrollpane;
	JRadioButton stopButton;
	JRadioButton startButton;
	JProgressBar progressBar;
	JTextField _portcfield;
	JTextField _portsfield;
	JButton configButton;
	JLabel infoLabel;
	JCheckBox _chkrqc;
	JCheckBox _chckvqc;
	//$$$endVars    -----------------------------------------------------------

	
	
	@SuppressWarnings("rawtypes")
	public InitServer(){
		super();
		_action = new JRadioButton[2];
		_chkQC = new JCheckBox[2];
		_started = false;
		_configTable = new Hashtable();
	}
	
	@SuppressWarnings("deprecation")
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/InitServer.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		_textarea = (JTextArea) instantiator.getObject( "_textarea");
		scrollpane = (JScrollPane) instantiator.getObject( "scrollpane");
		stopButton = (JRadioButton) instantiator.getObject( "stopButton");
		startButton = (JRadioButton) instantiator.getObject( "startButton");
		progressBar = (JProgressBar) instantiator.getObject( "progressBar");
		_portcfield = (JTextField) instantiator.getObject( "_portcfield");
		_portsfield = (JTextField) instantiator.getObject( "_portsfield");
		configButton = (JButton) instantiator.getObject( "configButton");
		infoLabel = (JLabel) instantiator.getObject( "infoLabel");
		_chkrqc = (JCheckBox) instantiator.getObject( "_chkrqc");
		_chckvqc = (JCheckBox) instantiator.getObject( "_chckvqc");
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
		
		
		//Mettre les checkboxes dans _chkQC[0] et _chkQC[1]. 
		ButtonGroup chkgrp = new ButtonGroup();
		_chkQC[_VIRTUAL_CST] = _chckvqc;
		_chkQC[_REAL_CST] = _chkrqc;
		_chkQC[_VIRTUAL_CST].setSelected(true);
		//former un groupe mutuellement exclusif de checkboxes
		chkgrp.add(_chckvqc);
		chkgrp.add(_chkrqc);
		
		//Sets ports to the defaut values.
		_portcfield.setText(Integer.toString(Constants.INIT_SERVER_CLIENT_DEF_PORT));
		_portsfield.setText(Integer.toString(Constants.INIT_SERVER_SERVER_DEF_PORT));
		
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setForeground(Color.black);
		infoLabel.setText(_DEF_INFO);
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
	    _fakeDGThread=new Thread(new FakeInitDG(Constants.DEF_PORT_FAKE_QC));
	    _fakeDGThread.start();
	    addToLogin("Fake DG est lancé.");
	 }catch(IOException io){
	    setFakeDG(false);
	    QryptoWarning.warning("je n'arrive pas a lancer le Fake DG\n"+
	                          io.toString(),
	                          "InitServer",this);
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

	public void configButton_actionPerformed( ActionEvent var0 )
	{
	    if(isQCReal()){
		_configTable = RealQSender.configure(_configTable,this);
	    }else{
		_configTable = VirtualQSender.configure(_configTable,this);
	    }
	    this.repaint();
	}

	public void startButton_actionPerformed( ActionEvent var0 )
	{
	   boolean ok = false;
	   if(!_started){
		configButton.setEnabled(false);
		infoLabel.setForeground(Color.black);
		infoLabel.setText(_DEF_INFO);
		ok = verifyPortSetting();
	        if(ok){
		    _T = new InitServerRunnable(this);
		    _T.start();
		    _started = true;
		    addToLogin("Démaré.");
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
			    "Vous voulez vraiment arretez le serveur initiateur?",
			    "Arret du serveur",
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
	
    public boolean isQCReal(){
	return _chkQC[_REAL_CST].isSelected();
    }
	
    public Object getConfigSetting(String configKey){
	return _configTable.get(configKey);
    }
 	
	
    protected void stop(String message){
	if(_started){
	    JOptionPane.showMessageDialog(this,
				message,
				"Arret",
			    JOptionPane.WARNING_MESSAGE);
	    _action[_STOP_CST].setSelected(true);
	    //this.repaint();
	    stop();
	}
    }
     
    protected void stop(){
	if(_started){
	    _started = false;
	    if(_T != null){
		_T.closeAll();
	    }
	    progressBar.setValue(0);
	    configButton.setEnabled(true);
	    _action[_STOP_CST].setSelected(true);
	    addToLogin("Arret.");
	}
    }
    
    
    
    protected boolean verifyPortSetting(){
	int ports=0;
	int portc=0;
	boolean ok = true;
	ports = extractPort(_portsfield,"Port du Serveur",0);
	portc = extractPort(_portcfield,"Port du client",ports);
	if(ok){
		_portc = portc;
		_ports = ports;
	}else{
	 JOptionPane.showMessageDialog(this,
			    "N'as pas pu demarrer le serveur correctement;vérifiez le n° de port ou" +
			    " le type du Canal Quantique QC.",
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
				"Le "+field_desc+" n'est pas un integer dans ["+Constants.MIN_PORT_VALUE+
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
	String message = "Seul le Canal Quantique est supporté.";
	boolean result = true;
	if(!_chkQC[_VIRTUAL_CST].isSelected()){
	    result = false;
	    JOptionPane.showMessageDialog(this,
			    message,
			    "Mauvaise Configuration",
			JOptionPane.ERROR_MESSAGE);
	    //this.repaint();
	}
	return result;
    }
    
    
    public void addToLogin(String s){
	_textarea.append(s+NEWLINE);
    }
    
     
     
      @SuppressWarnings("deprecation")
	public static void main(String argv[]){
	JFrame frame = new JFrame("Serveur Initiateur");
	InitServer ii = new InitServer();
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
