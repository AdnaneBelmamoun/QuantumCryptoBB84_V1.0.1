package qrypto.gui.BAK;

import qrypto.qommunication.*;
import qrypto.server.*;
import qrypto.log.Log;

import java.lang.Integer;
import java.lang.Thread;
import java.util.Vector;
import java.io.IOException;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import qrypto.gui.RespServerRunnable;


@SuppressWarnings("unused")
public class RespServerPane extends AbstractServerInterface implements ActionListener{

private static final String _IP_INPUT = "Address IP du Serveur initiateur :";
private TextField _IPfield = null;
private static InetAddress THIS_IP_ADDRESS = null;
private static String THIS_IP_ADD_STRING = null;
    
private Log _logfile = null;
public static final String log_filename = "src/qrypto/gui/respserver.txt";
private VirtualResponderServer  _vrs = null;    
private VirtualQReceiver _vqr = null;
protected InetAddress _ia = null;  

private RespServerRunnable _T = null;
  
  
static{
    try{
	THIS_IP_ADDRESS = InetAddress.getLocalHost();
	//THIS_IP_ADD_STRING = cleanIPAddress(THIS_IP_ADDRESS.toString());
	THIS_IP_ADD_STRING = THIS_IP_ADDRESS.toString();
    }catch(IOException io){
	THIS_IP_ADD_STRING = "non défini";
    }
}


    private static String cleanIPAddress(String s){
	String output;
	boolean done = false;
	int i = 0;
	while(!done){
	    if(s.charAt(i) == '/'){
		done  = true;
		i++;
	    }
	}
	output = s.substring(i,s.length());
	return output;
    }
  
    
     
    public RespServerPane(){
      try{
	_logfile = new Log(log_filename, true, "Serveur Répondeur");
	//_logfile.header();
      }catch(IOException io){
	System.out.println("PROBLEME D'OUVERTURE DU LOGFILE."); 
      }
      _started = false;
      DEF_CLIENT_PORT_NUMBER = Constants.DEF_PORT_RESP_CLIENT;
      _frame = new JFrame("Serveur Repondeur");
      _components = createComponents();
      _frame.getContentPane().add(_components, BorderLayout.CENTER);
      _frame.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
			_logfile.closeAnyway();
			System.exit(0);
		    }
		});
    }
    
  
    
    
    public JPanel createComponents(){
	superpanel = new JPanel();	
	JLabel l = null;
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	superpanel.setLayout(gridbag);
	superpanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(
						1,1,2,2,Color.black),
				BorderFactory.createEmptyBorder(5,5,5,5)));
	createLogArea(c,gridbag);
	createIPArea(c,gridbag);
	createPortArea(c,gridbag);
	createQCArea(c,gridbag);
	createButtons(c,gridbag);
	return superpanel;
    }
    
    
    
    
    private void createIPArea(GridBagConstraints c, GridBagLayout g){
	JPanel ppanel = createHorizontalPanel();
	Border ppBorder = new TitledBorder(null, "Configuration de l'adresse", 
					       TitledBorder.LEFT, TitledBorder.TOP,
					       boldFont);			   
	Border compoundBorder = new CompoundBorder(ppBorder, _emptyBorder);
	ppanel.setBorder(compoundBorder);
	JPanel IPpanel = createHorizontalPanel();
	JLabel labelIP = new JLabel(_IP_INPUT);
	_IPfield = new TextField(THIS_IP_ADD_STRING,20);
	_IPfield.setColumns(20);
	IPpanel.add(labelIP);
	IPpanel.add(_IPfield);
	ppanel.add(IPpanel);
	c.weighty = 0.0;
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(ppanel,c);
	superpanel.add(ppanel);
    }
    
       
    public void actionPerformed(ActionEvent e){
       boolean ok = true;
       if(e.getActionCommand().equals(_STOP) && _started){
	     int answ = JOptionPane.showConfirmDialog(_frame,
			 "Vous voulez vraiment arretez le serveur répondeur?",
			"Arrêt du serveur répondeur",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);
			_frame.repaint();					
	     if(answ == _STOP_CST){
		_T.closeAll();
		_started = false;
		addToLogin("Arret.");
		_T = null;
	     }else{
		if(_started){_action[_START_CST].setSelected(true);}
	    }
	}
	if(e.getActionCommand().equals(_START) && !_started){
		ok = verifyIPSetting();
		if(ok){
		       ok = verifyPortSetting();
		}
		//if ok then all _portXX are set and _ia is also ok.
	        if(ok){
		    _T = new RespServerRunnable(this);
		    addToLogin("Démarré");
		    _started = true;
		    _T.start();
		 }else{
		    _action[_STOP_CST].setSelected(true);
		    _started = false;
		}
	}
    }
    
    
  private boolean verifyIPSetting(){
	boolean ok = false;
	String ipstring = _IPfield.getText();
	try{
	    _ia = InetAddress.getByName(ipstring);
	    ok  = true;
	}catch(UnknownHostException uh){
	    JOptionPane.showMessageDialog(_frame,
			    "L'adresse IP du serveur répondeur n'as pas été trouvé." ,
			    "Mauvaise configuration",
			JOptionPane.ERROR_MESSAGE);
	    _frame.repaint();
	}
	return ok;
    }
 
    
    public static void main(String argv[]){
	RespServerPane ii = new RespServerPane();
	ii.show();
    }






}
