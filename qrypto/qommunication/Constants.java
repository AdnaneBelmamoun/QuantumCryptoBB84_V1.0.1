package qrypto.qommunication;


import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFileChooser;

import qrypto.exception.QryptoWarning;

public class Constants { 

public static final String SOFT_NAME = "Simulateur Cryptographie Quantique -----> Par :" +
		"Belmamoun Adnane---> Master I.T 2010--2011";


    /** This is the code for acknowledging a new connection. */
public static final byte ACKNOWLEDGE = (byte)127; 
    /** This is the code for accepting a request. */
public static final byte OK = (byte)0;
    /** This is the code to indicate that a time slot cannot be kept. */
public static final byte BAD = (byte)64;
    /** This is the code for refusing a request. */
public static final byte ERROR = (byte)-128;
    /** This is the code to signal that a qubit has not been detected */
public static final byte UNDETECTED = (byte)-1;
public static final byte UNDEFINED = (byte)0;
    /** This is the code for a requested BB84 quantum transmission */
public static final byte BB84  = (byte)1;
public static final byte BB840R = (byte)0;
public static final byte BB840D = (byte)1;
public static final byte BB841R = (byte)2;
public static final byte BB841D = (byte)3; 
    /** This is the code for a requested B92 quantum transmission */  
public static final byte B92   = (byte)2;
//public static final byte B920  = (byte)0;
//public static final byte B921  = (byte)1;
public static final byte B92U  = (byte)-128;
    /** The code to signal then end of a request. Used primary for communication
	between GB and real quantum connection. */
public static final byte STOP  =(byte)-1;
    /**Byte Codes for the quantum transmission mode below */
public static final byte REAL = (byte)32;
public static final byte VIRTUAL = (byte)64; 


    /** This is the maximum raw key size the server accepts to carry out */
public static final int  MAXBB84_TRANSMISSION_LENGTH = 10000;

    /** Some default port numbers. */
public static final int DEFAULT_PORT = 1025;
public static final int MIN_PORT_VALUE = 1025;
public static final int MAX_PORT_VALUE = 9999;
public static final int DEF_PORT_INIT_FAKE_DG = 1577;
public static final int DEF_PORT_RESP_FAKE_DG = 1578;
//should be configurable..

    /**Some other defaults value related to real q-transmission*/

//the maximum bucket size for real quantum transmission.
public static final int MAX_BUCKET_SIZE = 50000;



  /**Some logfilenames **/ 
public static String INITSERVERLOGFILE = "initserv.txt";
public static String RESPSERVERLOGFILE = "respserv.txt";
public static String INITPLAYERLOGFILE = "initplay.txt";
public static String RESPPLAYERLOGFILE = "respplay.txt";
public static String VIRTUALQSENDERLOGFILE = "vqsend.txt";
public static String VIRTUALQRECEIVERLOGFILE = "vqrec.txt";
public static String RESPONDERSERVERCONNECTIONLOGFILE = "respscon.txt";
public static String RAWBB84RECEIVER = "rbb84rec.txt";
public static String RAWBB84SENDER   = "rbb84sen.txt";
public static String RAWB92RECEIVER  = "rb92rec.txt";
public static String RAWB92SENDER    = "rb92sen.txt";
public static String PLAINBB84RECEIVER = "pbb84rec.txt";
public static String PLAINBB84SENDER   = "pbb84sen.txt";
public static String PLAINB92RECEIVER  = "pb92rec.txt";
public static String PLAINB92SENDER    = "pb92sen.txt";
public static String RECBB84SENDER     = "rbb84sen.txt";
public static String RECBB84RECEIVER   = "rbb84rec.txt";
public static String RECB92SENDER      = "rb92sen.txt";
public static String RECB92RECEIVER    = "rb92rec.txt";



public static File HOME_DIR = null;
public static File CONF_FILE = null;
public static String CONFIG_FILENAME = "qrypto.conf";

public static String INITDIRNAME = "initiator";
public static String RESPDIRNAME = "responder";


/**
* Next some configurable constants through the configuration method.
*/

    /** directory where to get the templates for HTML generation. */
public static File TEMPLATES_DIR = null;//where the templates for html
					//generation are located. This is
					//the main directory under which
					//the template for initiator and responder
					//are. It contains the index.html file.

//some IP addressses					
public static String THISIP = "undefined";
public static String PEERIP = "undefined";
public static String DEF_INIT_DG_IP = "undefined";
public static String DEF_RESP_DG_IP = "undefined"; 
public static String INIT_SERVER_DEF_IP = "undefined";
public static String RESP_SERVER_DEF_IP = "undefined";
public static String INIT_PLAYER_DEF_IP = "undefined"; 


//some default ports.
public static int INIT_SERVER_CLIENT_DEF_PORT = 1630;
public static int INIT_SERVER_SERVER_DEF_PORT = 1730;
public static int RESP_SERVER_CLIENT_DEF_PORT = 1631;
public static int INIT_PLAYER_DEF_PORT = 1530;
public static int DEF_PORT_SEND_DG = 1531;//The port where the initsever connects to the initDG
public static int DEF_PORT_REC_DG = 1532;//The port where the respserver connects to the respDG
public static int DEF_PORT_FAKE_QC = 1533;//The port where the respDG connect to the initDG

//other stuffs.

public static int DEF_BUCKET_SIZE = 500;
//this is the default probability that a qubit sent by the fake DG
//will reach the responder fake DG.
public static double DEF_PROB_FAKE_SUCCESS_TRANSMISSION = 0.03d;

//probability of errors of fake or simulated detectors.
public static double DEF_DETECTOR_PROB_ERROR = 0.05d;


//Launching settings.
//defined the initial values for the checkboxes of the launch panel.
public static boolean INIT_SERVER_LAUNCH = false;
public static boolean INIT_PLAYER_LAUNCH = false;
public static boolean RESP_SERVER_LAUNCH = false;
public static boolean RESP_PLAYER_LAUNCH = false;
public static boolean FAKE_DG_LAUNCH = false;



   /**--------END OF THE CONFIGURABLE CONSTANTS-----------**/



    /** encoding of booleans into bytes */
public static final byte BYTE0 = (byte)0;
public static final byte BYTE1 = (byte)1;


    /** Timeout settings */

public static final long SERVER_CONNECTION_TIMEOUT = 10000;
public static final long CLIENT_CONNECTION_TIMEOUT = 10000;
 
public static final long NO_TIMEOUT = 0;

//Some special characters

public static final String NEWLINE = "\n";



/**
* This will be used for user configuration at the very
* beginning. If the configuration file cannot be found then
* the user is asked to input some directories ...
*/
static{
    String thisip = "undefined";
    try{
	thisip = InetAddress.getLocalHost().getHostName();
    }catch(UnknownHostException uh){
	QryptoWarning.warning("impossible de determiner sette adresse IP!\n"+
			      "cela causera des problemes...","Configuration",null);
    }
    THISIP = thisip;
    PEERIP = thisip;
    DEF_INIT_DG_IP = thisip;
    DEF_RESP_DG_IP = thisip; 
    INIT_SERVER_DEF_IP = thisip;
    RESP_SERVER_DEF_IP = thisip;
    INIT_PLAYER_DEF_IP = thisip;
    HOME_DIR = new JFileChooser().getCurrentDirectory();
    TEMPLATES_DIR = HOME_DIR;
    CONF_FILE = new File(HOME_DIR,CONFIG_FILENAME);
}
    public static int DEF_PORT_INIT_CLIENT;
    public static int DEF_PORT_SERVERS_COM;
    public static int DEF_PORT_VIRT_QCHANEL;
    public static final int DEF_PORT_RESP_CLIENT=8080;
    

/**
* This initialize the class. It loads the the configuration
* file if it is defined. Otherwise, it tells the user that
* no configuration file could be found but some default values
* will be used instead.
* @param parent is the parent frame for the warning message. Null
* means that no parent frame exists.
*/

public static void init(Component parent){
    if(CONF_FILE.exists()){
	load();
    }else{
	QryptoWarning.warning("aucun fichier configuration trouvé!\n"+
			      "Selectionner 'Editer Configuration' pour creer un nouveau",
			      "fichier Configuration ",parent);
	if(parent != null){parent.repaint();}
    }
}

/**
* Returns an hashtable with the fields and their values read
* from the CONF_FILE.
* @param br is the buffer containing the CONF_FILE
* @return the hashtable. 
*/

@SuppressWarnings({ "rawtypes", "unchecked" })
private static Hashtable load(BufferedReader br)throws IOException{
    Hashtable h = new Hashtable();
    String line = br.readLine();
    String key;
    String val;
    while(line != null){
	int i=line.indexOf("=");
	if(i > 0){
	    key = line.substring(0,i);
	    val = line.substring(i+1);
	    h.put(key,val);
	}
	line = br.readLine();
    }
    return h;
}

/**
* This method loads the configurations settings from the configuration
* file. Does nothing if the configuration file couldn't be found or
* read. 
*/

@SuppressWarnings("rawtypes")
public static void load(){
  String key;
  try {
	   BufferedReader cr = new BufferedReader(new FileReader(CONF_FILE));
	   Hashtable h= load(cr);
	   cr.close();
	   for(Enumeration e=h.keys();e.hasMoreElements();){
		key = (String)e.nextElement();
		if(key.equalsIgnoreCase("INIT_PLAYER_DEF_PORT")){
		    INIT_PLAYER_DEF_PORT = Integer.decode((String)h.get(key)).intValue();   
		}
		if(key.equalsIgnoreCase("INIT_PLAYER_DEF_IP")){
		    INIT_PLAYER_DEF_IP = (String)h.get(key);
		}
		if(key.equalsIgnoreCase("RESP_SERVER_CLIENT_DEF_PORT")){
		    RESP_SERVER_CLIENT_DEF_PORT = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("RESP_SERVER_DEF_IP")){
		    RESP_SERVER_DEF_IP = (String)h.get(key);
		}
		if(key.equalsIgnoreCase("INIT_SERVER_CLIENT_DEF_PORT")){
		    INIT_SERVER_CLIENT_DEF_PORT = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("INIT_SERVER_SERVER_DEF_PORT")){
		    INIT_SERVER_SERVER_DEF_PORT = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("INIT_SERVER_DEF_IP")){
		    INIT_SERVER_DEF_IP = (String)h.get(key);
		}
		if(key.equalsIgnoreCase("TEMPLATES_DIR")){
		     TEMPLATES_DIR = new File((String)h.get(key));
		}
		if(key.equalsIgnoreCase("DEF_INIT_DG_IP")){
		    DEF_INIT_DG_IP = (String)h.get(key);
		}
		if(key.equalsIgnoreCase("DEF_RESP_DG_IP")){
		    DEF_RESP_DG_IP = (String)h.get(key);
		}
		if(key.equalsIgnoreCase("DEF_PORT_SEND_DG")){
		    DEF_PORT_SEND_DG = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("DEF_PORT_REC_DG")){
		    DEF_PORT_REC_DG = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("DEF_BUCKET_SIZE")){
		    DEF_BUCKET_SIZE = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("DEF_PROB_FAKE_SUCCESS_TRANSMISSION")){
		    DEF_PROB_FAKE_SUCCESS_TRANSMISSION = Double.valueOf((String)h.get(key)).doubleValue();
		}
		if(key.equalsIgnoreCase("DEF_PORT_FAKE_QC")){
		    DEF_PORT_FAKE_QC = Integer.decode((String)h.get(key)).intValue();
		}
		if(key.equalsIgnoreCase("DEF_DETECTOR_PROB_ERROR")){
		    DEF_DETECTOR_PROB_ERROR = Double.valueOf((String)h.get(key)).doubleValue();
		}
		if(key.equalsIgnoreCase("INIT_SERVER_LAUNCH")){
		    INIT_SERVER_LAUNCH = Boolean.valueOf((String)h.get(key)).booleanValue();
		}
		if(key.equalsIgnoreCase("INIT_PLAYER_LAUNCH")){
		    INIT_PLAYER_LAUNCH = Boolean.valueOf((String)h.get(key)).booleanValue();
		}
		if(key.equalsIgnoreCase("RESP_SERVER_LAUNCH")){
		    RESP_SERVER_LAUNCH = Boolean.valueOf((String)h.get(key)).booleanValue();
		}		
		if(key.equalsIgnoreCase("RESP_PLAYER_LAUNCH")){
		    RESP_PLAYER_LAUNCH = Boolean.valueOf((String)h.get(key)).booleanValue();
		}
		if(key.equalsIgnoreCase("FAKE_DG_LAUNCH")){
		    FAKE_DG_LAUNCH = Boolean.valueOf((String)h.get(key)).booleanValue();
		}
	   }
  }catch (NullPointerException np){
	    throw new RuntimeException("Couldn't find TEMPLATES_DIR in CONFIG_FILE");
  }catch (FileNotFoundException exc) {
	   throw new RuntimeException("configuration file "+CONF_FILE.toString()+" not found.");
  }catch (IOException io){
	   throw new RuntimeException("configuration file "+CONF_FILE.toString()+" cannot be read."); 
  }catch (NumberFormatException nfe){
	   throw new RuntimeException("Problem with the configuration file..not formatted correctly");
  }
}


/**
* The settings are saved to the config file. Usually called by ConfConstants
* after the user has set new defaults values.
* @param h is an hashtable containing the new values.
*/

@SuppressWarnings("rawtypes")
public static void saveAndDismiss(Hashtable h)throws IOException{
    Object key;
    String val;
    PrintWriter cw = new PrintWriter(new FileOutputStream(CONF_FILE));
    cw.println("%THIS CONFIGURATION FILE SHOULDN'T BE EDITED");
    cw.println("%USE THE CONFIGURATION TOOL INSTEAD.");
    for(Enumeration e=h.keys();e.hasMoreElements();){
	key = e.nextElement();
	val = (String)h.get(key);
	cw.println((String)key + "=" + val);
    }
    cw.close();
    //the window should be closed now....
}


/**
* Prints on a stream an array of bits. 
* @param a is the array to be printed
* @param stream is where to print
* @param b0 is the boolean that corresponds to the bit 0
*/
public static void printBoolArray(boolean[] a, PrintWriter stream, boolean b0){
    for(int i = 0; i< a.length; i++){
	if(a[i] == b0){
	    stream.print("0"+"  ");
	}else{
	    stream.print("1"+"  ");
	}
	//the following abs() are there because of uncompatibility 
	//with Windows Math.IEEEremainder().
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    stream.println();
	}
	if(Math.abs(Math.IEEEremainder(i+1,25.0d))<0.5d){
	    stream.println();
	}
    }
    stream.println();
}


/**
* Returns an HTML string for an array of boolean.
* @param a is the array to convert in html.
* @param b0 is the boolean corresponding to the bit 0
* @param width is the number of groups of 5 bits per row.
* @param skip is the number of rows before skipping one.
* @return the html string.
*/

public static String arrayBool2HTML(boolean[] a, boolean b0, int width, int skip){
    int LARGER_SPACE_EVERY_X_BLOCK = Math.min(5,width);
    int blockbeforeCR = 0;
    StringBuffer s = new StringBuffer("<BR><BR><B>");
    for(int i = 0; i<a.length;i++){
	if(a[i] == b0){
	    s.append("0");
	}else{
	    s.append("1");
	}
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    s.append("&nbsp;");
	    blockbeforeCR++;
	    if(blockbeforeCR == LARGER_SPACE_EVERY_X_BLOCK){
	       s.append("&nbsp;");
	       blockbeforeCR = 0;
	    }
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width))<0.5d){
	    s.append("<BR>\n");
	    blockbeforeCR = 0;
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width*skip))<0.5d){
	    s.append("<BR>");
	}
    }
    s.append("</B><BR>");
    return s.toString();
}

/**
* Returns an HTML string for an array of boolean with specified color.
* @param a is the array to convert in html.
* @param b0 is the boolean corresponding to the bit 0
* @param width is the number of groups of 5 bits per row.
* @param skip is the number of rows before skipping one.
* @return the html string.
*/

public static String arrayBool2HTML(boolean[] a, boolean b0, int width, int skip, String color){
    StringBuffer sb = new StringBuffer(arrayBool2HTML(a,b0,width,skip));
    sb.insert(0,"<FONT COLOR="+color+">");
    sb.append("</FONT>");
    return sb.toString();
}


/**
* Returns an HTML string for an array of boolean where some
* positions can be marked with a different color.
* @param a is the array to convert in html.
* @param b0 is the boolean corresponding to the bit 0
* @param width is the number of groups of 5 bits per row.
* @param skip is the number of rows false,before skipping one.
* @param mark[i] = 1 appears in markcolork, mark[i]=2 appears marked and
* in markcolor
* @param markcolor is the color for marked positions.
* @return the html string.
*/

public static String arrayBool2HTML(boolean[] a, boolean b0, int width,
	       int skip,byte[] mark, String color, String markcolor){
    int LARGER_SPACE_EVERY_X_BLOCK = Math.min(5,width);
    int blockbeforeCR = 0;
    StringBuffer s = new StringBuffer("<FONT COLOR="+color+">"+"<BR><BR><B>");
    for(int i = 0; i<a.length;i++){
	if(mark[i] == 1){
	    s.append("<FONT COLOR="+markcolor+">");
	}else{
	    if(mark[i] == 2){
		s.append("<FONT COLOR="+markcolor+"><U>");
	    }
	}
	if(a[i] == b0){
	    s.append("0");
	}else{
	    s.append("1");
	}
	if(mark[i] == 1){
	    s.append("</FONT>");
	}else{
	    if(mark[i]==2){
		s.append("</U></FONT>");
	    }
	}
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    s.append("&nbsp;");
	    blockbeforeCR++;
	    if(blockbeforeCR == LARGER_SPACE_EVERY_X_BLOCK){
	       s.append("&nbsp;");
	       blockbeforeCR = 0;
	    }
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width))<0.5d){
	    s.append("<BR>\n");
	    blockbeforeCR = 0;
	}
	if(Math.abs(Math.IEEEremainder(i+1,5*width*skip))<0.5d){
	    s.append("<BR>");
	}
    }
    s.append("</FONT></B><BR>");
    return s.toString();
}




/**
* Prints on a std output an array of bits. 
* @param a is the array to be printed
* @param stream is where to print
* @param b0 is the boolean that corresponds to the bit 0
*/
public static void printBoolArray(boolean[] a, boolean b0){
    for(int i = 0; i< a.length; i++){
	if(a[i] == b0){
	    System.out.print("0"+"  ");
	}else{
	    System.out.print("1"+"  ");
	}
	//the following abs() are there because of uncompatibility 
	//with Windows Math.IEEEremainder().
	if(Math.abs(Math.IEEEremainder(i+1,5.0d))<0.5d){
	    System.out.println();
	}
	if(Math.abs(Math.IEEEremainder(i+1,25.0d))<0.5d){
	    System.out.println();
	}
    }
    System.out.println();
}

/**
* Writes to an output stream a small integer in a special form.
* i=256*hb+lb where hb and lb are bytes to be sent.
* @param i is the integer to be sent
* @param s is the stream where to write.
* @exception IOException when a problem occurs with the stream.
*/
public static void sendSpecialInt(int i, OutputStream s)throws IOException{
    int hb = i / 256;
    int lb = i - (hb*256);
    s.write(hb);
    s.write(lb);
}

/**
* Reads a small integer sent in a special form from a input stream.
* i = 256*hb + lb where hb and lb are the bytes to be read from the stream.
* @param s is the stream where to read
* @return the received integer. 
* @exception IOException when a problem occurs with the stream.
*/
public static int readSpecialInt(InputStream s)throws IOException{
    int hi = s.read();
    int li = s.read();
    return (hi*256 + li);
}


/**
* Test
*/

public static void main(String[] argv){
   System.out.println("No testing for that class.");
}

}
