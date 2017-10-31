package qrypto.gui.BAK;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import qrypto.gui.InitServerRunnable;
import qrypto.log.Log;
import qrypto.qommunication.VirtualQSender;
import qrypto.server.VirtualInitiatorServer;



public class InitServerPane extends AbstractServerInterface 
			    implements ActionListener{


    
private Log _logfile = null;
public static final String log_filename = "src/qrypto/gui/initserver.txt";



@SuppressWarnings("unused")
private VirtualInitiatorServer  _vis = null;    
@SuppressWarnings("unused")
private VirtualQSender _vqs = null;   
private InitServerRunnable _T = null;

  
    
     /**
     * Constructor of a new initiator server interface.
     */
    public InitServerPane(){
      try{
	_logfile = new Log(log_filename, true, "Serveur Initiateur");
	//_logfile.header();
      }catch(IOException io){
	System.out.println("Probleme d'ouvérture du fichier LOGFILE."); 
      }
      _started = false;
      _frame = new JFrame("Serveur Initiateur");
      _components = createComponents();
      _frame.getContentPane().add(_components, BorderLayout.CENTER);
      _frame.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
			_logfile.closeAnyway();
			System.exit(0);
		    }
		});
    }
    
  
    /**
    * Sets the components of the window.
    */
    
    @SuppressWarnings("unused")
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
	createConfigMenu(c,gridbag);
	createLogArea(c,gridbag);
	createPortArea(c,gridbag);
	createQCArea(c,gridbag);
	createButtons(c,gridbag);
	return superpanel;
    }
    
    
       
    
       
    @SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e){
       boolean ok = true;
       if(e.getActionCommand().equals(_STOP) && _started){
	     int answ = JOptionPane.showConfirmDialog(_frame,
			    "Vous voulez vraiment arréter le Serveur Initiateur?",
			    "Arret du Serveur Initiateur",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);
			_frame.repaint();					
	     if(answ == _STOP_CST){
	      if(_started){
		    _T.stop();
		    _T.closeAll();
		    _started = false;
		    addToLogin("Arret.");
	      }
	     }else{
		if(_started){_action[_START_CST].setSelected(true);}
	    }
	}
	if(e.getActionCommand().equals(_START) && !_started){
		progressBar.setValue(0);
		ok = verifyPortSetting();
	        if(ok){
		   // try{
			_T = new InitServerRunnable(this);
			_T.start();
			_started = true;
			addToLogin("Démarré.");
		   	_frame.repaint();
			if(_T != null){_T.stop();_T.closeAll();}
			ok  = false;
		    
		 }
		 if(!ok){
		    _action[_STOP_CST].setSelected(true);
		    _started = false;
		}
	}
    }
     
    
    public static void main(String argv[]){
	InitServerPane ii = new InitServerPane();
	ii.show();
    }






}
