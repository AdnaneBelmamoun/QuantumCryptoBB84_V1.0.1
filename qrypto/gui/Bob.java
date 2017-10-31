package qrypto.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Bob{

 
public static RespServer rs = null;
public static RespPlayer rp = null;
public static JFrame rsframe = null;
public static JFrame rpframe = null;
public static boolean fakeDG = false;


/**
* Is called to stop the execution.
*/

public static void exit(){
    if(fakeDG && (rs != null)){
	rs.stopFakeDG();
    }
}




@SuppressWarnings("deprecation")
public static void main(String[] args) {
    boolean launch_player = true;
    boolean launch_server = false;
    fakeDG = false;
    if(args != null){
	if((args.length > 0)&& args[0].equalsIgnoreCase(Alice.WITH_SERVER_OPT)){
	    launch_server = true;
	}
	if((args.length > 0)&& args[0].equalsIgnoreCase(Alice.ONLY_SERVER_OPT)){
	    launch_player = false;
	    launch_server = true;
	}
	if((args.length > 1)&& args[1].equalsIgnoreCase(Alice.FAKE_DG_OPT)){
	    fakeDG = true;
	}
    }
    if(launch_server){
	rs = new RespServer();
	rs.initGui();
	if(fakeDG){
	    rs.setAndLaunchFakeDG();
	}
	rsframe = new JFrame("Serveur de Réponse Quantique");
	rsframe.setResizable(false);
	rsframe.getContentPane().add("Center",rs);
	rsframe.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		      int s = JOptionPane.showConfirmDialog(null,
				    "Voulez bous vraiment arreter tous les processus?",
				    "Arréte d'execution",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE);
		      if(s == JOptionPane.YES_OPTION){
			Bob.exit();
			Alice.exit();
			System.exit(0);
		      }
		    }
		});
	rsframe.pack();
	rsframe.show();
    }
    if(launch_player){
	rp = new RespPlayer();
	rp.initGui();
	rpframe = new JFrame("Visionneuse de Réponse Quantique");
	rpframe.setResizable(false);
	rpframe.getContentPane().add("Center", rp);
	rpframe.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		      int s = JOptionPane.showConfirmDialog(null,
				    "Voulez bous vraiment arreter tous les processus?",
				    "Arrét d'execution",
				    JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE);
		      if(s == JOptionPane.YES_OPTION){
			Bob.exit();
			Alice.exit();
			System.exit(0);
		      }
		    }
		});
	rpframe.pack();
	rpframe.show();
    }
}




}
