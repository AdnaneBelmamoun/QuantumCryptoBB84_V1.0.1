package executionSimulataion;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import cryptoQuantique.Menu;

import qrypto.Simulation.Execution.Demarage_des_Visionneuses;
import qrypto.Simulation.Execution.Demarage_des_serveurs;

public class Menu_Simulateur_Cryptographie_Quantique extends JFrame implements ActionListener {
public static final long serialVersionUID = 1L;
public static JButton buttonserveurs = new JButton("Demarrer les serveurs");
public static JButton buttonvisionneuses = new JButton("Demarrer les visionneuses");
public static JButton buttonSimuTransmiQ =new JButton("Simulation de Transmission quantique");

public Menu_Simulateur_Cryptographie_Quantique(){
	setTitle("Simulateur Cryptographie Quantique avec protocol BB84 ------->" +
			"Belmamoun Adnane Master I.T -- 2010-2011");
	Image titreim = (new javax.swing.ImageIcon(getClass().getResource("labelApplication.png"))).getImage();
    setIconImage(titreim);
	setSize(700, 100);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = getContentPane();
    content.setBackground(Color.BLUE);
    content.setLayout(new FlowLayout()); 
    
    
    content.add(buttonserveurs);
    content.add(buttonvisionneuses);
    content.add(buttonSimuTransmiQ);
    
    buttonserveurs.addActionListener(this);
    buttonvisionneuses.addActionListener(this);
    buttonSimuTransmiQ.addActionListener(this);
    
    addWindowListener(new ExitListener());
    setVisible(true);
    
    
} 
public class ExitListener extends WindowAdapter {
	  public void windowClosing(WindowEvent event) {
	    System.exit(0);
	  }
	}
		@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand()=="Demarrer les serveurs"){
			new Demarage_des_serveurs();
		}
		if(e.getActionCommand()=="Demarrer les visionneuses"){
			new Demarage_des_Visionneuses();
		}
		if(e.getActionCommand()=="Simulation de Transmission quantique"){
			 java.awt.EventQueue.invokeLater(new Runnable() {
		            public void run() {
		                new Menu().setVisible(true);
		            }
		        });
		}
		
	}

		
		@SuppressWarnings("deprecation")
		public static void main(String[] args) {
			(new Menu_Simulateur_Cryptographie_Quantique()).show();

		}

}
