package qrypto.gui;

/*****************************************************
 * AnyJ Templatefile 
 */


import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import qrypto.qommunication.Constants;
import de.netcomputing.runtime.SmallMemTable;
import de.netcomputing.runtime.SwingInstantiator;



public class Launch extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private JFrame _frame = null;


	//$$$vars       ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JCheckBox fakeDG;
	JCheckBox initplay;
	JCheckBox respplay;
	JCheckBox initserver;
	JCheckBox respserver;
	JButton cancel;
	JButton config;
	JButton start;
	//$$$endVars    -----------------------------------------------------------
	
	public Launch(JFrame f){
	    super();
	    _frame = f;
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/Launch.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		fakeDG = (JCheckBox) instantiator.getObject( "fakeDG");
		initplay = (JCheckBox) instantiator.getObject( "initplay");
		respplay = (JCheckBox) instantiator.getObject( "respplay");
		initserver = (JCheckBox) instantiator.getObject( "initserver");
		respserver = (JCheckBox) instantiator.getObject( "respserver");
		cancel = (JButton) instantiator.getObject( "cancel");
		config = (JButton) instantiator.getObject( "config");
		start = (JButton) instantiator.getObject( "start");
		//$$$endVarInit -----------------------------------------------------------
		
		fakeDG.setSelected(Constants.FAKE_DG_LAUNCH);
		initplay.setSelected(Constants.INIT_PLAYER_LAUNCH);
		respplay.setSelected(Constants.RESP_PLAYER_LAUNCH);
		initserver.setSelected(Constants.INIT_SERVER_LAUNCH);
		respserver.setSelected(Constants.RESP_SERVER_LAUNCH);
	}

	//$$$actions

	@SuppressWarnings("deprecation")
	public void config_actionPerformed( ActionEvent var0 )
	{
	    _frame.setVisible(false);
	    JFrame fc = new JFrame("Configuration Panel");
	    ConfConstants cc = new ConfConstants();
	    cc.initGui();
	    fc.getContentPane().add("Center",cc);
	    fc.setResizable(false);
	    fc.pack();
	    fc.show();
	    _frame.dispose();
	}
	
	/**
	* Cancel the execution.
	*/

	public void cancel_actionPerformed( ActionEvent var0 )
	{
	    _frame.dispose();
	    System.exit(0);
	}
	
       /**
	* Starts the selected entities.
	*/

	public void start_actionPerformed( ActionEvent var0 )
	{
	    String[] argA = {Alice.NO_OPT, Alice.NO_OPT};
	    String[] argB = {Alice.NO_OPT, Alice.NO_OPT};
	    _frame.setVisible(false);
	    if(initplay.isSelected() || initserver.isSelected()){
	        if(!initplay.isSelected()){
		    argA[0] = Alice.ONLY_SERVER_OPT;
		}else{
		    if(initserver.isSelected()){
			argA[0] = Alice.WITH_SERVER_OPT;
		    }
		}
	        if(fakeDG.isSelected()){
		    argA[1] = Alice.FAKE_DG_OPT; 
		}
		Alice.main(argA);
	    }
	    if(respplay.isSelected() || respserver.isSelected()){
	        if(!respplay.isSelected()){
		    argB[0] = Alice.ONLY_SERVER_OPT;
		}else{
		    if(respserver.isSelected()){
		      argB[0] = Alice.WITH_SERVER_OPT;
		    }
		}
		if(fakeDG.isSelected()){
		    argB[1] = Alice.FAKE_DG_OPT;
		}
	        Bob.main(argB);
	    }
	    _frame.dispose();
	}
	
	@SuppressWarnings("deprecation")
	static public void main(String[] argv){
	    JFrame frame = new JFrame(Constants.SOFT_NAME+"'s Lanceur");
	    Launch l = new Launch(frame);
	    Constants.init(frame);
	    l.initGui();
	    frame.getContentPane().add("Center",l);
	    frame.setResizable(false);
	    frame.pack();
	    frame.show();
	}
}
