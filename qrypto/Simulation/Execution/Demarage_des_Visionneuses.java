package qrypto.Simulation.Execution;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import qrypto.gui.InitPlayer;
import qrypto.gui.RespPlayer;

public class Demarage_des_Visionneuses {

	@SuppressWarnings({ "deprecation", "static-access" })
	public Demarage_des_Visionneuses(){
		JFrame frame = new JFrame("visionneuses D'Initateur et du Répondeur------>Belmamoun Adnane");
		Image titreim = (new javax.swing.ImageIcon(getClass().getResource("labelApplication.png"))).getImage();
	    frame.setIconImage(titreim);
		frame.setSize(700, 1200);
		frame.setLayout(new GridLayout(1,2));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		//initiateur					
		InitPlayer playerinit = new InitPlayer();
	    playerinit.initGui();

	    // Repondeur
	    RespPlayer playerresp = new RespPlayer();
	    playerresp.initGui();
	    
	    playerinit.setBounds(4, 4, playerinit.WIDTH, playerinit.HEIGHT);
	    playerresp.setBounds(playerinit.WIDTH +8, 4, playerresp.WIDTH, playerresp.HEIGHT);
	    
		//frame.getContentPane().add(is);
		//frame.getContentPane().add(RS);
	  frame.getContentPane().add(playerinit);
	  frame.getContentPane().add(playerresp);
		
		
		
		
		//  sortie de l'application
		frame.addWindowListener(new WindowAdapter(){
			    public void windowClosing(WindowEvent e){
				System.exit(0);
			    }
			});
		
	    		
	    frame.pack();
	    frame.show();

	
		
	}
	public static void main(String[] args) {
		new Demarage_des_Visionneuses();
		}

}
