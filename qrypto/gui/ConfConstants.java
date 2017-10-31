package qrypto.gui;


import javax.swing.*;
import java.awt.event.*;
import de.netcomputing.runtime.*;
import java.util.Hashtable;
import java.io.IOException;
import qrypto.qommunication.Constants;

public class ConfConstants extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	String tempDir;


	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JCheckBox fakeDGCB;
	JCheckBox respPlayerCB;
	JCheckBox initPlayerCB;
	JCheckBox respServerCB;
	JCheckBox initServerCB;
	JTextField dg2dgport;
	JLabel tempDirLabel;
	JButton templateDir;
	JButton cancel;
	JButton save;
	JTextField initpPort;
	JTextField initpIP;
	JTextField respDGport;
	JTextField respscport;
	JTextField rdgIP;
	JTextField respserverIP;
	JTextField initscport;
	JTextField initDGport;
	JTextField initssport;
	JTextField idgIP;
	JTextField initserverIP;
	JTextField errorprob;
	JTextField deftransrate;
	JTextField bucksize;
	//$$$endVars    -----------------------------------------------------------
	
	
	
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		@SuppressWarnings("deprecation")
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/ConfConstants.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		fakeDGCB = (JCheckBox) instantiator.getObject( "fakeDGCB");
		respPlayerCB = (JCheckBox) instantiator.getObject( "respPlayerCB");
		initPlayerCB = (JCheckBox) instantiator.getObject( "initPlayerCB");
		respServerCB = (JCheckBox) instantiator.getObject( "respServerCB");
		initServerCB = (JCheckBox) instantiator.getObject( "initServerCB");
		dg2dgport = (JTextField) instantiator.getObject( "dg2dgport");
		tempDirLabel = (JLabel) instantiator.getObject( "tempDirLabel");
		templateDir = (JButton) instantiator.getObject( "templateDir");
		cancel = (JButton) instantiator.getObject( "cancel");
		save = (JButton) instantiator.getObject( "save");
		initpPort = (JTextField) instantiator.getObject( "initpPort");
		initpIP = (JTextField) instantiator.getObject( "initpIP");
		respDGport = (JTextField) instantiator.getObject( "respDGport");
		respscport = (JTextField) instantiator.getObject( "respscport");
		rdgIP = (JTextField) instantiator.getObject( "rdgIP");
		respserverIP = (JTextField) instantiator.getObject( "respserverIP");
		initscport = (JTextField) instantiator.getObject( "initscport");
		initDGport = (JTextField) instantiator.getObject( "initDGport");
		initssport = (JTextField) instantiator.getObject( "initssport");
		idgIP = (JTextField) instantiator.getObject( "idgIP");
		initserverIP = (JTextField) instantiator.getObject( "initserverIP");
		errorprob = (JTextField) instantiator.getObject( "errorprob");
		deftransrate = (JTextField) instantiator.getObject( "deftransrate");
		bucksize = (JTextField) instantiator.getObject( "bucksize");
		//$$$endVarInit -----------------------------------------------------------	
		
		errorprob.setText(Double.toString(Constants.DEF_DETECTOR_PROB_ERROR));
		bucksize.setText(Integer.toString(Constants.DEF_BUCKET_SIZE));
		deftransrate.setText(Double.toString(Constants.DEF_PROB_FAKE_SUCCESS_TRANSMISSION));
		initDGport.setText(Integer.toString(Constants.DEF_PORT_SEND_DG));
		respDGport.setText(Integer.toString(Constants.DEF_PORT_REC_DG));
		tempDir = Constants.TEMPLATES_DIR.toString();
		tempDirLabel.setText(tempDir);
		dg2dgport.setText(Integer.toString(Constants.DEF_PORT_FAKE_QC));
		rdgIP.setText(Constants.DEF_RESP_DG_IP);
		idgIP.setText(Constants.DEF_INIT_DG_IP);
		initpPort.setText(Integer.toString(Constants.INIT_PLAYER_DEF_PORT));
		initpIP.setText(Constants.INIT_PLAYER_DEF_IP);
		respscport.setText(Integer.toString(Constants.RESP_SERVER_CLIENT_DEF_PORT));
		respserverIP.setText(Constants.RESP_SERVER_DEF_IP);
		initscport.setText(Integer.toString(Constants.INIT_SERVER_CLIENT_DEF_PORT));
		initssport.setText(Integer.toString(Constants.INIT_SERVER_SERVER_DEF_PORT));
		initserverIP.setText(Constants.INIT_SERVER_DEF_IP);
		fakeDGCB.setSelected(Constants.FAKE_DG_LAUNCH);
		respPlayerCB.setSelected(Constants.RESP_PLAYER_LAUNCH); 
		initPlayerCB.setSelected(Constants.INIT_PLAYER_LAUNCH);
		respServerCB.setSelected(Constants.RESP_SERVER_LAUNCH);
		initServerCB.setSelected(Constants.INIT_SERVER_LAUNCH);
	
	}

	//$$$actions

	public void templateDir_actionPerformed( ActionEvent var0 )
	{
	    JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fc.setDialogTitle("Templates & HTML-Output directory");
	    int returnVal = fc.showDialog(null,"Set template dir");
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	      if(fc.getSelectedFile() != null){
		tempDir = fc.getSelectedFile().getPath();
		tempDirLabel.setText(tempDir);
	      }
	    }
	}
	
	/**
	* Exits completely. 
	*/

	public void cancel_actionPerformed( ActionEvent var0 )
	{
	    System.exit(0);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void save_actionPerformed( ActionEvent var0 )
	{
	    Hashtable h = new Hashtable();
	    h.put("INIT_PLAYER_DEF_PORT",initpPort.getText());
	    h.put("INIT_PLAYER_DEF_IP",initpIP.getText());
	    h.put("RESP_SERVER_CLIENT_DEF_PORT",respscport.getText());
	    h.put("RESP_SERVER_DEF_IP",respserverIP.getText());
	    h.put("INIT_SERVER_CLIENT_DEF_PORT",initscport.getText());
	    h.put("INIT_SERVER_SERVER_DEF_PORT",initssport.getText());
	    h.put("INIT_SERVER_DEF_IP",initserverIP.getText());
	    h.put("DEF_INIT_DG_IP",idgIP.getText());
	    h.put("DEF_RESP_DG_IP",rdgIP.getText());
	    h.put("DEF_PORT_SEND_DG",initDGport.getText());
	    h.put("DEF_PORT_REC_DG",respDGport.getText());
	    h.put("DEF_BUCKET_SIZE",bucksize.getText());
	    h.put("DEF_PROB_FAKE_SUCCESS_TRANSMISSION",deftransrate.getText());
	    h.put("DEF_PORT_FAKE_QC",dg2dgport.getText());
	    h.put("TEMPLATES_DIR",tempDir);
	    h.put("DEF_DETECTOR_PROB_ERROR",errorprob.getText());
	    h.put("INIT_SERVER_LAUNCH",new Boolean(initServerCB.isSelected()).toString());
	    h.put("INIT_PLAYER_LAUNCH",new Boolean(initPlayerCB.isSelected()).toString());
	    h.put("RESP_SERVER_LAUNCH",new Boolean(respServerCB.isSelected()).toString());
	    h.put("RESP_PLAYER_LAUNCH",new Boolean(respPlayerCB.isSelected()).toString());
	    h.put("FAKE_DG_LAUNCH",new Boolean(fakeDGCB.isSelected()).toString());
	    try{
		 Constants.saveAndDismiss(h);
	    }catch(IOException io){
		JOptionPane.showMessageDialog(this,
				"je n'arrive pas a enregistrer la configuration! \n"+io.getMessage(),
				"Erreur lors de la configuration",
			        JOptionPane.ERROR_MESSAGE);
		this.repaint();
	    }finally{
		System.exit(0);
	    }
	}
}
