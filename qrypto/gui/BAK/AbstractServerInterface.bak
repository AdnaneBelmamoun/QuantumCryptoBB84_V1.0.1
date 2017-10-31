package qrypto.gui.BAK;

import qrypto.qommunication.*;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;





public abstract class AbstractServerInterface implements ActionListener
{
public Font boldFont = new Font("Dialog", Font.BOLD, 12);



public final static int _REAL_CST = 1;
public final static int _VIRTUAL_CST = 0;
public final static int _START_CST = 1;
public final static int _STOP_CST = 0;
public final static String _START="Démarrer";
public final static String _STOP ="Arreter";
public final static String _VIRTUAL = "Virtuel";
public final static String _REAL = "Réel";
public final static String _QPORTLABEL = "Port:";
public final static String imdir = "src/qrypto/gui/images/";

public final static Dimension hpad5 = new Dimension(5,1);
public final static Dimension hpad10 = new Dimension(10,1);
public final static Dimension hpad20 = new Dimension(20,1);
public final static Dimension hpad25 = new Dimension(25,1);
public final static Dimension hpad30 = new Dimension(30,1);
public final static Dimension hpad40 = new Dimension(40,1);
public final static Dimension hpad80 = new Dimension(80,1);

public final static Dimension vpad5 = new Dimension(1,5);
public final static Dimension vpad10 = new Dimension(1,10);
public final static Dimension vpad20 = new Dimension(1,20);
public final static Dimension vpad25 = new Dimension(1,25);
public final static Dimension vpad30 = new Dimension(1,30);
public final static Dimension vpad40 = new Dimension(1,40);
public final static Dimension vpad80 = new Dimension(1,80);

public final static Insets insets0 = new Insets(0,0,0,0);
public final static Insets insets5 = new Insets(5,5,5,5);
public final static Insets insets10 = new Insets(10,10,10,10);
public final static Insets insets15 = new Insets(15,15,15,15);
public final static Insets insets20 = new Insets(20,20,20,20);


protected boolean _started = false;
protected static Border _emptyBorder=null;
protected JPanel superpanel = null;
protected Component _components = null;
protected JFrame _frame = null;
protected JTextArea _textarea = null;
protected TextField _portsfield = null;
protected TextField _portcfield = null;
protected TextField _qport = null;
protected int _portc = 0;
protected int _ports = 0;
protected int _portq = 0; 
protected JRadioButton _action[] = new JRadioButton[2];
protected JCheckBox _chkQC[] = new JCheckBox[2];
public SPBar progressBar = null;
private ConfigMenuAction _cma = null; 
 
protected final static String _PORTS  = "Port du Serveur : ";
protected final static String _PORTC  = "Port du Client : ";
protected final static String _BSTART = "Démarage d'écoute du canal quantique";
protected final static String _BSTOP  = "Arrêt d'écoute du canal quantique";
protected final static String _BCLOSE = "Fermeture du serveur";
protected final static String _LOGWIN = "Fenetre d'historique:";
protected final static String NEWLINE = "\n"; 

protected int DEF_CLIENT_PORT_NUMBER = Constants.DEF_PORT_INIT_CLIENT;
protected int DEF_SERVER_PORT_NUMBER = Constants.DEF_PORT_SERVERS_COM;
protected int DEF_QC_PORT_NUMBER = Constants.DEF_PORT_VIRT_QCHANEL;


public static ImageIcon radio;
public static ImageIcon radioPressed;
public static ImageIcon radioSelected;    
      
static{     
    _emptyBorder = new EmptyBorder(5,5,5,5); 
    radio =  new ImageIcon(imdir+"radio.gif","with blue triangle inside");
    radioSelected = new ImageIcon(imdir+"radioSelected.gif","green inside");
    radioPressed = new ImageIcon(imdir+"radioPressed.gif","purple inside");
}      
      

  /**
    * Show the window and waits for the user to select.
    */


    public void show(){
	_frame.pack();
	_frame.setVisible(true);
    }



   /**
    * Creates the menubar for configuring the behaviour. Only the error
    * of the virtual channel is actually supported.
    */
    
    public void  createConfigMenu(GridBagConstraints c, GridBagLayout g){
	_cma = new ConfigMenuAction(superpanel);
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("Config");
	JMenuItem it1 = new JMenuItem(ConfigMenuAction.CONFIG_VIRT_QC);
	it1.addActionListener(_cma);
	//JMenuItem it2 = new JMenuItem(ConfigMenuAction.CONFIG_REAL_QC); 
	menu.add(it1);
	//menu.add(it2);
	bar.add(menu);
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(bar, c);
	superpanel.add(bar);
    }
    
    
    
    
    public ConfigMenuAction getConfigSetting(){
	return _cma;
    }
 


  /**
    * Creates the log area of the server frame.
    */
    
    protected void createLogArea(GridBagConstraints c, GridBagLayout g){
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	JLabel l = new JLabel(_LOGWIN);
	g.setConstraints(l, c);
	superpanel.add(l);
	c.weightx = 1.0;
	_textarea = new JTextArea("Arreté." + NEWLINE,7,35);
	_textarea.setEditable(false);
	g.setConstraints(_textarea, c);
	JScrollPane topScrollPane = new JScrollPane(_textarea);
	topScrollPane.setAutoscrolls(true);
	g.setConstraints(topScrollPane, c);
	superpanel.add(topScrollPane);
    }
    
    
    
    /**
    * Creates the buttons at the buttom of server frame.
    */
    

    protected void createButtons(GridBagConstraints c, GridBagLayout g){
     

    // text&image buttons
	ButtonGroup group = new ButtonGroup();

	JPanel tiButtons = createHorizontalPanel();
	//tiButtons.setAlignmentX(LEFT_ALIGNMENT);
	Border buttonBorder = new TitledBorder(null, "Actions", 
					       TitledBorder.LEFT, TitledBorder.TOP,
					       boldFont);
	CompoundBorder compoundBorder = new CompoundBorder(buttonBorder, _emptyBorder);
	tiButtons.setBorder(compoundBorder);

	JRadioButton button1 = new JRadioButton(_STOP, radio);
	group.add(button1);
	button1.setToolTipText("Arret du serveur");
	button1.setSelected(true);
	button1.setSelectedIcon(radioPressed);
	button1.setPressedIcon(radioPressed);
	button1.addActionListener(this);
	_action[0] = button1;
	tiButtons.add(button1);
	tiButtons.add(Box.createRigidArea(hpad10));

	JRadioButton button2 = new JRadioButton(_START, radio);
	
	group.add(button2);
	_action[_START_CST] = button2;
	button2.setToolTipText("Démarage du serveur");
	button2.setSelectedIcon(radioSelected);
	button2.setPressedIcon(radioSelected);
	button2.addActionListener(this);
	tiButtons.add(button2);
	tiButtons.add(Box.createRigidArea(hpad10));

	//Add the progress Bar.
	JPanel vpanel = createVerticalPanel();
	progressBar = new SPBar();
	vpanel.add(progressBar.getTitle());
	vpanel.add(progressBar);
	tiButtons.add(vpanel);
	
	//Finalize by putting all things in the superpanel.

	c.weighty  = 0.0;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(tiButtons,c);
	superpanel.add(tiButtons);
  }
    
    
    protected void createPortArea(GridBagConstraints c, GridBagLayout g){
	JPanel ppanel = createHorizontalPanel();
	Border ppBorder = new TitledBorder(null, "Configuration de la Communication Quantique", 
					       TitledBorder.LEFT, TitledBorder.TOP,
					       boldFont);			   
	Border compoundBorder = new CompoundBorder(ppBorder, _emptyBorder);
	ppanel.setBorder(compoundBorder);
	JPanel portspanel = createHorizontalPanel();
	JPanel portcpanel = createHorizontalPanel();
	JLabel labelports = new JLabel(_PORTS);
	JLabel labelportc = new JLabel(_PORTC);
	_portsfield = new TextField((new Integer(DEF_SERVER_PORT_NUMBER)).toString(),4);
	_portsfield.setColumns(4);
	_portcfield = new TextField((new Integer(DEF_CLIENT_PORT_NUMBER)).toString(),4);
	_portcfield.setColumns(4);
	portspanel.add(labelports);
	portspanel.add(_portsfield);
	portcpanel.add(labelportc);
	portcpanel.add(_portcfield);
	ppanel.add(portspanel);
	ppanel.add(portcpanel);
	c.weighty = 0.0;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(ppanel,c);
	superpanel.add(ppanel);
    }
       
    
    
	
    protected void createQCArea(GridBagConstraints c, GridBagLayout g){
	ButtonGroup grp = new ButtonGroup();
	// First checkbox       
	JPanel imageButtons = createHorizontalPanel();
	//imageButtons.setAlignmentX(LEFT_ALIGNMENT);
	Border buttonBorder = new TitledBorder(null, "Configuration du Canal Quantique", 
					       TitledBorder.LEFT, TitledBorder.TOP,
					       boldFont);					   
	Border compoundBorder = new CompoundBorder(buttonBorder, _emptyBorder);
	imageButtons.setBorder(compoundBorder);
	//Modification to standard checkbox
	JCheckBox button1 = new JCheckBox(_VIRTUAL, true);
	button1.setToolTipText("Canal Quantique Virtuel");
	button1.addActionListener(this);
	_chkQC[_VIRTUAL_CST] = button1;
	grp.add(button1);
	imageButtons.add(button1);
	imageButtons.add(Box.createRigidArea(hpad10));
	//Second checkbox.
	JCheckBox button2 = new JCheckBox(_REAL,false);
	button2.setToolTipText("Canal Quantique Reel");
	button2.addActionListener(this);
	_chkQC[_REAL_CST]= button2;
	grp.add(button2);
	imageButtons.add(button2);
	imageButtons.add(Box.createRigidArea(hpad10));
	// Port text area.
	JPanel portpanel = createHorizontalPanel();
	_qport = new TextField((new Integer(DEF_QC_PORT_NUMBER)).toString(),5);
	_qport.setColumns(5);
	JLabel qportlabel = new JLabel(_QPORTLABEL);
	portpanel.add(qportlabel);
	portpanel.add(_qport);
	imageButtons.add(portpanel);
	// Add button panels to superpanel
	c.weighty = 1.0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(imageButtons, c);
	superpanel.add(imageButtons);    
    }
    
    
  /**
    * Creation of a horizontal panel.
    * @return the panel.
    */
    
    protected JPanel createHorizontalPanel(){
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
	return p;
    }
    
    
    /**
    * Creation of a vertical panel.
    * @return the panel.
    */
    
    protected JPanel createVerticalPanel(){
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
	return p;
    }
	
      
    /**
    * Verifies the port number and the qauntum channel type settings.
    * As a side effect, the port numbers are stored in the global variables
    * _portc, _ports, _portq.
    * @return true iff the values make sense. 
    */
    
    protected boolean verifyPortSetting(){
	int ports=0;
	int portc=0;
	int portq=0;
	boolean ok = true;
	ports = extractPort(_portsfield,"Port du Serveur",0);
	portc = extractPort(_portcfield,"Port du Client",ports);
	portq = extractPort(_qport,"port quantique",portc);
	ok = (portq>0); 
	if(ok){
	    ok = verifyQConnection();
	}
	if(ok){
		_portc = portc;
		_portq = portq;
		_ports = ports;
	}else{
	 JOptionPane.showMessageDialog(_frame,
			    "Le serveur n'as pas démarer correctement;Vérifiezle numéro de port ou  " +
			    "le type du canal quantique QC.",
			    "Mauvaise configuration",
			JOptionPane.ERROR_MESSAGE);
	 _frame.repaint();
	}
	return ok;
    }
    
     
    
    protected int extractPort(TextField tf, String field_desc, int go){
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
		JOptionPane.showMessageDialog(_frame,
				"Le "+field_desc+" n'est pas un entier dans ["+Constants.MIN_PORT_VALUE+
				"..."+Constants.MAX_PORT_VALUE+"]",
				"Mauvaise configuration",
			    JOptionPane.ERROR_MESSAGE);
		_frame.repaint();
	    }
	}
	return ans;
    }
    
 
      
    
    private boolean verifyQConnection(){
	String message = "Seul le canal quantique Virtuel est supporté.";
	boolean result = true;
	if(!_chkQC[_VIRTUAL_CST].isSelected()){
	    result = false;
	    JOptionPane.showMessageDialog(_frame,
			    message,
			    "mauvaise configuration",
			JOptionPane.ERROR_MESSAGE);
	    _frame.repaint();
	}
	return result;
    }
    
    
    /**
    * What to do upon the action buttons are presse.
    */
    
    public abstract void actionPerformed(ActionEvent e);
    
    
    
    
   /**
    * Add further text in the login text area. 
    * @param s is the text to be added.
    */
    
    
    public void addToLogin(String s){
	_textarea.append(s+NEWLINE);
    }
    
    
   /**
    * Stop the current execution by putting the action buttonn to stop. 
    * Does nothing if the server is not executing. Remarks: This method
    * does not close the thread that is running and probably that has called
    * this method. 
    * @param message is the error message to show before stopping everything.
    */
    
    protected synchronized void stop(String message){
	if(_started){
	    _started = false;
	    addToLogin("Arreté.");
	    JOptionPane.showMessageDialog(_frame,
				message,
				"Arret",
			    JOptionPane.WARNING_MESSAGE);
	    _action[_STOP_CST].setSelected(true);
	    _frame.repaint();
	}
    }
     
    /**
    * Stops the execution gently without poping information.
    * This is called by the running thread to indicate the end of the
    * quantum transmission.
    */
    
    protected synchronized void stop(){
	if(_started){
	    _started = false;
	    addToLogin("Arreter.");
	    _action[_STOP_CST].setSelected(true);
	    _frame.repaint();
	}
    }

}
