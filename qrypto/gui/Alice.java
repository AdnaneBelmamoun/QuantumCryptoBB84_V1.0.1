package qrypto.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Alice
{


//possible options when launching the initiator entities. 
public static final String NO_OPT = "def";
public static final String FAKE_DG_OPT  = "fakeDG";
public static final String WITH_SERVER_OPT = "server";
public static final String ONLY_SERVER_OPT = "noplayer";
    
  
public static InitServer is = null;
public static InitPlayer ip = null;
public static JFrame isframe = null;
public static JFrame ipframe = null;
public static boolean fakeDG = false;



public static void exit(){
    if(fakeDG && (is != null)){
	is.stopFakeDG();
    }
}


@SuppressWarnings("deprecation")
public static void main(String[] args) {
    boolean launch_player = true;
    boolean launch_server = false;
    fakeDG = false;
    if(args != null){
	if((args.length >0) && args[0].equalsIgnoreCase(WITH_SERVER_OPT)){
	    launch_server = true;
	}
	if((args.length >0) && args[0].equalsIgnoreCase(ONLY_SERVER_OPT)){
	    launch_player = false;
	    launch_server = true;
	}
	if((args.length>1) && args[1].equalsIgnoreCase(FAKE_DG_OPT)){
	    fakeDG = true;
	}
    }
    if(launch_server){
	 is = new InitServer();
	 is.initGui();
	 if(fakeDG){
	    is.setAndLaunchFakeDG();
	 }
	 isframe = new JFrame("Server Initiation de Transmission quantique");
	 isframe.setResizable(false);
	 isframe.getContentPane().add("Center",is);
	 isframe.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		      int s = JOptionPane.showConfirmDialog(null,
				    "Voulez bous vraiment arréter tout les processus?",
				    "Arret d'Execution",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE);
		      if(s == JOptionPane.YES_OPTION){
			Alice.exit();
			Bob.exit();
			System.exit(0);
		      }
		    }
		});
	 isframe.pack();
	 isframe.show();
    }
    if(launch_player){
	ip = new InitPlayer();
	ip.initGui();
	ipframe = new JFrame("Visionneuse d'Initiation de Transmission quantique");
	ipframe.setResizable(false);
	ipframe.getContentPane().add("Center", ip);
	ipframe.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		      int s = JOptionPane.showConfirmDialog(null,
				    "Voulez bous vraiment arreter tous les processus?",
				    "Arret d'Execution",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE);
		      if(s == JOptionPane.YES_OPTION){
			Alice.exit();
			Bob.exit();
			System.exit(0);
		      }
		    }
		});	
	ipframe.pack();
	ipframe.show();
    }
}

}
