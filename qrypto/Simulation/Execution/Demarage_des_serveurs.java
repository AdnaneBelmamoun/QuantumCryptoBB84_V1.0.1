package qrypto.Simulation.Execution;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import qrypto.gui.InitPlayer;
import qrypto.gui.InitServer;
import qrypto.gui.RespPlayer;
import qrypto.gui.RespServer;

public class Demarage_des_serveurs {
	
	
	@SuppressWarnings({ "deprecation", "static-access" })
public Demarage_des_serveurs(){
	JFrame frame = new JFrame("Serveurs Initateur et Répondeur ------>Belmamoun Adnane");
	Image titreim = (new javax.swing.ImageIcon(getClass().getResource("labelApplication.png"))).getImage();
    frame.setIconImage(titreim);
	frame.setSize(700, 1200);
	frame.setLayout(new GridLayout(1,2));
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	
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
		
		new Demarage_des_serveurs();
			}

}
