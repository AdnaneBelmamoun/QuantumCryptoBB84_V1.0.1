package qrypto.gui;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;



public class SelfDisposalFrame extends JFrame implements WindowListener
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SelfDisposalFrame(String title){
     super(title);
     addWindowListener(this);
    }
    
    
   public void windowClosing(WindowEvent e){
	this.removeWindowListener(this);
	this.dispose();
    }
    
    public void windowActivated(WindowEvent e){
    }
    
    public void windowClosed(WindowEvent e){
    }
    
    public void windowDeactivated(WindowEvent e){
    }
    
    public void windowDeiconified(WindowEvent e){
    }
    
    public void windowIconified(WindowEvent e){
    }
    
    public void windowOpened(WindowEvent e){
    }

}
