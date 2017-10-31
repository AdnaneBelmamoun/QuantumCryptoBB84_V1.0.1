package qrypto.gui;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Simulation_Crypto_Quantique_Belmamoun_Adnane {

	/**
	 * @param args
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(700, 1200);
		frame.setLayout(new GridLayout(1,2));
		
		
		InitServer is = new InitServer();
		is.initGui();
						
		InitPlayer playerinit = new InitPlayer();
	    playerinit.initGui();
	    
	    // la reponse
	    RespServer RS = new RespServer();
		RS.initGui();
		RespPlayer playerresp = new RespPlayer();
	    playerresp.initGui();
	    
	    is.setBounds(4, 4, is.WIDTH, is.HEIGHT);
	    RS.setBounds(is.WIDTH +8, 4, RS.WIDTH, RS.HEIGHT);
	    
		frame.getContentPane().add(is);
		frame.getContentPane().add(RS);
	  //frame.getContentPane().add(playerinit);
	  //frame.getContentPane().add(playerresp);
		
		
		
		
		//  sortie de l'application
		frame.addWindowListener(new WindowAdapter(){
			    public void windowClosing(WindowEvent e){
				System.exit(0);
			    }
			});
		
	    
	    
	    
	    
		
		
		
		
	    frame.pack();
	    frame.show();

	}

}
