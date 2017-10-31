package qrypto.gui;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;

import qrypto.exception.QryptoWarning;
import qrypto.htmlgenerator.htmlManager;
import de.netcomputing.runtime.SmallMemTable;
import de.netcomputing.runtime.SwingInstantiator;




public class OutputManagerPane extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	File _indexFile = null;
	String _indexPath = null;
	String _indexDirectory = null;
	int removed = 0;


	//$$$vars 		------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
	JLabel indexsizeLabel;
	JLabel filesremovedLabel;
	JEditorPane htmlpane;
	//$$$endVars    -----------------------------------------------------------
	
	
	
	public OutputManagerPane(File indexFile)throws IOException{
	    super();
	    removed = 0;
	    _indexFile = indexFile;
	    _indexPath = indexFile.getPath();
	    _indexDirectory = _indexPath;
	    int a = _indexDirectory.indexOf("index.html");
	    if(a != -1){
		_indexDirectory = _indexDirectory.substring(0,a);
	    }
	}
	
	
	public void initGui()
	{
	 	SmallMemTable nameMap = new SmallMemTable( 19 );
		nameMap.put( "TARGET", this );
		nameMap.put( "CONTROLLER", this );
		@SuppressWarnings("deprecation")
		SwingInstantiator instantiator = SwingInstantiator.New( "src/qrypto/gui/forms/OutputManagerPane.gml", nameMap );
	 	//$$$varInit    ------- GENERATED CODE, DO NOT EDIT THIS SECTION ----------
		indexsizeLabel = (JLabel) instantiator.getObject( "indexsizeLabel");
		filesremovedLabel = (JLabel) instantiator.getObject( "filesremovedLabel");
		htmlpane = (JEditorPane) instantiator.getObject( "htmlpane");
		//$$$endVarInit -----------------------------------------------------------		
		htmlpane.setEditable(false);
		try{
		 htmlpane.setPage("fichier:///"+_indexPath.replace(File.separatorChar,'/'));
		 int w = htmlManager.numberOfEntries(_indexFile);
		 indexsizeLabel.setText(new Integer(w).toString());
	        }catch(IOException io){
		    htmlpane.setText("Je n'arrive pas a trouver le fichier indexe:"+
		                     "("+io.getMessage()+")"+_indexPath);
		}
	}

	//$$$actions

	
	public void htmlpane_hyperlinkUpdate( HyperlinkEvent var0 )
	{
	    if(var0.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
		//System.out.println("*****GETURL:"+var0.getURL().toString());
		String filename = var0.getURL().getFile();
		if((filename.indexOf("output") != -1) && (filename.indexOf("html")!= -1)){
		  try{
		    @SuppressWarnings("rawtypes")
			Vector r = htmlManager.cleanFrom(filename,_indexDirectory);
		    String trimFileName = filename.substring(filename.substring(0,filename.lastIndexOf("/")).lastIndexOf("/")+1);
		    int a = JOptionPane.showConfirmDialog(this,
		                            "êtes vous sûre de vouloir supprimer"+
					    "Tout les fichiers ("+r.size()+")\n"+
					    "reachable à partir de "+
					    trimFileName+"?",
					    "Suppréssion de la Sortie",
					    JOptionPane.YES_NO_OPTION);
		    if(a == JOptionPane.YES_OPTION){
		      htmlManager.removeAll(r);
		      htmlManager.removeFromIndex(_indexFile,trimFileName);
		      htmlpane.read(new FileInputStream(_indexFile),null);
		      //htmlpane.setPage("file://+");
		      htmlpane.setPage("fichier:///"+_indexPath.replace(File.separatorChar,'/'));
		      removed = removed + r.size();
		      int w = htmlManager.numberOfEntries(_indexFile);
		      indexsizeLabel.setText(new Integer(w).toString());
		      filesremovedLabel.setText(new Integer(removed).toString());
		    }
		  }catch(IOException io){
		     QryptoWarning.warning("Néttoyage du répertoire Sortie impossible: \n"+
					  io.getMessage(),"Output Manager",null);
		  }  
		}else{    
		    QryptoWarning.warning("le fichier:"+var0.getURL().getFile()+"\n"+
					  "ne correspond pas a un fichier de sortie Valide!",
					  "Output Manager",
					  this);
	        }
	    }
	}
	
	/**
	* Testing.
	*/
	
	@SuppressWarnings("deprecation")
	public static void main(String[] argv){
	 try{
	   File indexFile = new File("/templates/initiator/index.html");
	   OutputManagerPane o = new OutputManagerPane(indexFile);
	   o.initGui();
	   JFrame f = new JFrame("TEST");
	   f.setResizable(true);
	   f.getContentPane().add("Center",o);
	   f.addWindowListener(new WindowAdapter(){
		     public void windowClosing(WindowEvent e){
			System.exit(0);
		     }
				});
				   
	   //f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	   f.pack();
	   f.show();
	 }catch(IOException io){
	    System.out.println("ERREUR:"+io.getMessage());
	 }
	}
}
