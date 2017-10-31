package qrypto.gui.BAK;

import java.awt.Component;
import javax.swing.*;




public class SPBar extends JProgressBar{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static String _TITLE = "Progression du Transfert ";
private static int _LENGTH = 10;

    /**
    * Constructor of a new progress bar.
    */
    
    public SPBar(){
	super(JProgressBar.HORIZONTAL, 0, _LENGTH);
	this.getAccessibleContext().setAccessibleName("status du transfert Quantique");
    }
    
    /**
    * Resets the progress bar to 0.
    */
    
    public void reset(){
	setValue(0);
    }
    
    
    /**
    * Returns the standard title for this bar.
    * @return the label associates to this bartitle.
    */
    
    public JLabel getTitle(){
	JLabel bartitle = new JLabel(_TITLE);
	bartitle.setAlignmentX(Component.CENTER_ALIGNMENT);
	return bartitle;
    }
    

}
