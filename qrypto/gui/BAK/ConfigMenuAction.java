package qrypto.gui.BAK;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;





public class ConfigMenuAction implements ActionListener{

public double _pe = 0.0;
private Component _parent = null;

public static final String CONFIG_VIRT_QC = "Canal Quantique Virtuel";
public static final String CONFIG_REAL_QC = "Canal Quantique R�el"; 


 public ConfigMenuAction(Component parent){
    _parent = parent;
    _pe = 0.0;
 }
 
 
 public void actionPerformed(ActionEvent e){
    if(e.getActionCommand().equals(CONFIG_VIRT_QC)){
	boolean error = false;
	String out = (String)JOptionPane.showInputDialog(_parent, 
				    "Probabilit� d'erreur du canal quantique Virtuel QC :",
				    "configuration du canal quantique Virtuel QC",
				    JOptionPane.QUESTION_MESSAGE,
				    null,null,
				    new Double(_pe).toString());
	try{
	    if(out != null){
		double val = Double.valueOf(out).doubleValue();
		if((val <1.0) && (val >0.0)){
		    _pe = val;
		}else{
		   error = true;
		}
	    }
	}catch(NumberFormatException nfe){
		error = true;
	}
	if(error){
	    JOptionPane.showMessageDialog(_parent,
				"la valeur S�l�ctionner n'a pas �t� enregistrer .\n La valeure pr�c�dente "+
				   new Double(_pe).toString()+
				   " restants.",
				"Mauvaise configuration",
				JOptionPane.ERROR_MESSAGE);
	}
    }
 }
 
 
 
 public double getOneSideErrorProb(){
    return _pe;
 }

}
