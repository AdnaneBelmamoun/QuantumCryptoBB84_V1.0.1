package qrypto.htmlgenerator;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import qrypto.exception.QryptoWarning;

/**
* This is the main class for generating the HTML output.
* @author Neils Damgaard (damgaard@brics.dk)
*/

public class htmlGenerator extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//variable for the frame owning the warning windows.
    static JFrame frame;
    JTextField titleName;
    JTextField dirName;
    JTextField protName;
    JTextField sizeName;
    myAlert errorWindow;
    int detailLevels;
    @SuppressWarnings("rawtypes")
	Hashtable instanceNoTable;
    @SuppressWarnings("rawtypes")
	Hashtable protocolStep2Name;    
    @SuppressWarnings("rawtypes")
	Hashtable protocolName2Step;
    @SuppressWarnings("rawtypes")
	Hashtable experimentNumberHT;
    //added by Louis.
    @SuppressWarnings("rawtypes")
	private Hashtable _ht;
    private boolean _OK = true;
    private String _dirName = null;
    
    /**
    * @param ht is the hashtable containing the material to be filled.
    * @param dirName is the directory where to find the templates.        
    */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public htmlGenerator(Hashtable ht, File dirName){
	String s;
	myAlert warning = null;
	_OK = true;
	_ht = ht;
	try{
	    //verifies that a directory has been given.
	    if(!dirName.isDirectory()){
	        s = "The directory for templates is not set properly";
	        warning = new myAlert(s);
		throw new IOException(s);
	    }
	    //verifies that each protocols in ht has a template directory.
	    String[] list = dirName.list();
	    boolean found = true;
	    String t="";
	    instanceNoTable = new Hashtable();
	    for(Enumeration e = ht.keys(); e.hasMoreElements();){
		    //ceci est une plogue qui ne marche pas s'il y a plus
		    //d'une instance d'un sous-protocole...
		    //Il faudrait compter le nombre d'instances et mettre
		    //ce nombre au lieu de Integer(1)...
		    String cName = (String)e.nextElement();
		    int last = cName.indexOf("+");
		    if(last != -1){
			cName = cName.substring(0,last);
		    }
		    instanceNoTable.put(cName,new Integer(1));
	    }
	    for(Enumeration e = instanceNoTable.keys(); e.hasMoreElements() && found;){
		boolean isThere = false;
		t  = (String)e.nextElement();
		for(int i = 0; (i<list.length) && !isThere;i++){
		    isThere = list[i].equals(t);
		}
		found = isThere;
	    }
	    if(!found){
		s = "Templates for "+t+" not found in "+dirName.toString()+"!";
	        warning = new myAlert(s);
		throw new IOException(s);
	    }
	    // Initialize Hashtables
	    protocolStep2Name = new Hashtable();
	    protocolName2Step = new Hashtable();
	    experimentNumberHT = new Hashtable();
	    _dirName = dirName.getPath();
	}catch(IOException io){
	    _OK = false;
	    warning.show();
	}
    }
    
    
    
    /////////////////
    // Constructor //
    /////////////////
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public htmlGenerator() {
	// Initialize Hashtables
	protocolStep2Name = new Hashtable();
	protocolName2Step = new Hashtable();
	instanceNoTable   = new Hashtable();
	experimentNumberHT = new Hashtable();

	// Make the instance number hashtable
	instanceNoTable.put("Number1", new Integer(1)); // Hardwired
	instanceNoTable.put("Number2", new Integer(1));
	instanceNoTable.put("Number3", new Integer(1));
	instanceNoTable.put("Number4", new Integer(1));
	instanceNoTable.put("Composite", new Integer(1));

	// Title
	JLabel titleLabel = new JLabel("Title");
	titleName = new JTextField("");
	
	// Protocol Directory
	JLabel dirLabel = new JLabel("Protocol Directory");
	dirName = new JTextField("Protocols");
	dirName.setColumns(50);

	// Protocol Name
	JLabel protLabel = new JLabel("Protocol Name");
	protName = new JTextField("Composite");
	protName.setColumns(50);

	// Size
	JLabel sizeLabel = new JLabel("Size");
	sizeName = new JTextField("500");

	// Run button
	JButton runButton = new JButton("Run");
	runButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    myAlert errorWindow;

		    // Check that the fields are filled out
		    if (titleName.getText().compareTo("")==0) {
			errorWindow = new myAlert("Please type in a title");
			errorWindow.show();
		    }
		    else if (dirName.getText().compareTo("")==0) {
			errorWindow = new myAlert("Please type in a directory name");
			errorWindow.show();
		    }
		    else if (protName.getText().compareTo("")==0) {
			errorWindow = new myAlert("Please type in a protocol name");
			errorWindow.show();
		    }
		    else if (sizeName.getText().compareTo("")==0) {
			errorWindow = new myAlert("Please type in a size");
			errorWindow.show();
		    }
		    // Initialize the hashtable and call HTML constructing functions
		    else {
			Hashtable ht;
			ht = new Hashtable();
			Random rg;
			rg = new Random();
			int size = Integer.parseInt(sizeName.getText());
			int[] dv = new int[size];
			toHtml[] objects = new toHtml[10];   // Example
			argElm arg;

			// Make objects
			objects[0] = new toHtmlInt("intObj1", -10);
			objects[1] = new toHtmlInt("intObj2", 0);
			objects[2] = new toHtmlInt("intObj3", 10);
			objects[3] = new toHtmlInt("intObj4", 100);
			objects[4] = new toHtmlInt("intObj5", -250);
			objects[5] = new toHtmlString("strObj1", "Test1");
			objects[6] = new toHtmlString("strObj2", "Test test");
			objects[7] = new toHtmlString("strObj3", 
						     "click <a href=\"http://www.brics.dk\">here</a> please");
			objects[8] = new toHtmlString("strObj4", "Hello there");
			objects[9] = new toHtmlString("strObj5", "Test2");

			// Make entry for Number1 instance 1         // Example
			for (int i=0 ; i<size ; i++) {
			    dv[i] = rg.nextInt() % 4;
			}
			arg = new argElm(protName.getText(), 1, dv, objects);
			ht.put("Number1+" + 1, arg);
						
			// Make entry for Number2 instance 1
			dv = new int[size];
			for (int i=0 ; i<size ; i++) {
			    dv[i] = rg.nextInt() % 4;
			}
			arg = new argElm(protName.getText(), 1, dv, objects);
			ht.put("Number2+" + 1, arg);
			
			// Make entry for Number3 instance 1
			dv = new int[size];
			for (int i=0 ; i<size ; i++) {
			    dv[i] = rg.nextInt() % 4;
			}
			arg = new argElm(protName.getText(), 1, dv, objects);
			ht.put("Number3+" + 1, arg);
			
			// Make entry for Number4 instance 1
			dv = new int[size];
			for (int i=0 ; i<size ; i++) {
			    dv[i] = rg.nextInt() % 4;
			}
			arg = new argElm(protName.getText(), 1, dv, objects);
			ht.put("Number4+" + 1, arg);
			
			// Make entry for Composite instance 1
			dv = new int[size];
			for (int i=0 ; i<size ; i++) {
			    dv[i] = rg.nextInt() % 4;
			}
			arg = new argElm(protName.getText(), 1, dv, objects);
			ht.put("Composite+" + 1, arg);
			
			String fileName = makeHTMLOutput(ht,dirName.getText(),protName.getText(),
							 "index.html", null);
			if (fileName.compareTo("")!=0)
			    makeHTMLindex(fileName,dirName.getText(),titleName.getText(),size);
		    }
		}
	    }
	);

	// Exit button
	JButton exitButton = new JButton("Exit");
	exitButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		}
	    }
	);

	// Put objects on panel
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();	
	setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;

	gridbag.setConstraints(titleLabel, c);
	add(titleLabel);
	gridbag.setConstraints(titleName, c);
	add(titleName);

	gridbag.setConstraints(dirLabel, c);
	add(dirLabel);
	gridbag.setConstraints(dirName, c);
	add(dirName);

	gridbag.setConstraints(protLabel, c);
	add(protLabel);
	gridbag.setConstraints(protName, c);
	add(protName);

	gridbag.setConstraints(sizeLabel, c);
	add(sizeLabel);
	gridbag.setConstraints(sizeName, c);
	add(sizeName);

	c.gridwidth = GridBagConstraints.RELATIVE;
	gridbag.setConstraints(exitButton, c);
	add(exitButton);

	c.gridwidth = GridBagConstraints.REMAINDER;
	c.anchor = GridBagConstraints.WEST;
	gridbag.setConstraints(runButton, c);
	add(runButton);
    }
    
    /**
    * Returns whether or not the constructor has been called with success.
    * @return true if the constructor has been executed with success.
    */
    public boolean isOK(){
	return _OK;
    }
    
    /**
    * Makes the HTML output upon the hashtable given to the constructor.
    * @param protName is the name of the protocol to start with. This is the
    *        name one should find associated to the objects to fill the template
    *        (the argElm element in the hashtbale).
    *        This is also the name of the directory where to find the template.
    *        ex. dirName/protName is where to find the template.
    * @param indexPage ...
    * @param compositeName...
    * @return the produced filename located in... 
    */

    public String makeHTMLOutput(String protName,
				 String indexPage, String compositeName) {
	return makeHTMLOutput(_ht, _dirName, protName, indexPage,compositeName);
    }
    
    ////////////////////
    // makeHTMLOutput //
    ////////////////////
    
    /**
    * Generates the html output file from an hashtable.
    * @param ht is the hashtable...
    * @param dirName is the protocols main directory where all protocol
    *        directories containing the templates are to be found.
    * @param protName is the name of the protocol to start with. This is the
    *        name one should find associated to the objects to fill the template
    *        (the argElm element in the hashtbale).
    *        This is also the name of the directory where to find the template.
    *        ex. dirName/protName is where to find the template.
    * @param indexPage ...
    * @param compositeName...
    * @return the produced filename located in... 
    */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public String makeHTMLOutput(Hashtable ht, String dirName, String protName,
				 String indexPage, String compositeName) {
	File dir;
	argElm arg;
	int[] dv;
	toHtml[] objects;
	String[] dirlist;
	String protocolsMainDir, protocolDir, fileName, tempStr;
	Hashtable protocolLinkNames;
	@SuppressWarnings("unused")
	Hashtable protocolSteps;
	int gifCounter = 0;
	int count;
	int first;
	int experimentNumber;
	
	// Initialize
	protocolLinkNames = new Hashtable();
	first = 1;

	// Extract the arguments from the hashtable
	//The if and the else statements has been added by Louis
	if(instanceNoTable.get(protName) != null){
	  arg = (argElm) ht.get(protName + "+" + instanceNoTable.get(protName));
	}else{
	  arg = (argElm) ht.get(protName);
	}
	dv = arg.qubits;
	objects = arg.objects;

	// Find new filename
	protocolsMainDir = dirName;
	//ICICICICICICICICICICI
	if (!protocolsMainDir.endsWith(File.separator))
	    protocolsMainDir += File.separator;
	protocolDir = protocolsMainDir + protName;
	if (!protocolDir.endsWith(File.separator))
	    protocolDir += File.separator;
	if (!protName.endsWith(File.separator))
	    protName += File.separator;
	//****ICICICICICICICICCI***
	dir = new File(protocolDir);
	dirlist = dir.list();
	count=0;
	int aa = 0;
	int bb =0;
	boolean[] used = new boolean[dirlist.length];
	//System.out.println("DIRLISTLENGTH:"+dirlist.length);
	for(int i = 0;i<used.length;i++){used[i]=false;}
	for (int i=0 ; i<dirlist.length ; i++){
	    aa = dirlist[i].indexOf("output");
	    bb = dirlist[i].indexOf("_1.html");
	    if ((aa != -1) && (bb != -1) && (!dirlist[i].endsWith("~"))
	                                 &&((bb-aa-7)>= 0)){
		//count++;
		try{
		 //System.out.println(dirlist[i].substring(aa+6,bb));
		 used[Integer.valueOf(dirlist[i].substring(aa+6,bb)).intValue()]=true;
		}catch(NumberFormatException nf){
		    QryptoWarning.warning("There exists a badly \n" +
		                            "formatted output filename in:\n"+
					    protocolDir.toString(),
					    "HTML generation",
					    null);
		}
	    }
	}
	boolean found = false;
	for(int i=0;(i<used.length) && !found;i++){
	    found = !used[i];
	    count = i;
	}
	if(!found){experimentNumber = count+1;}
	else{experimentNumber = count;}
	//experimentNumber = count+1;

	// Find number of templates
	//dir = new File(protocolDir);
	//dirlist = dir.list();
	count = 0;
	for (int i=0 ; i<dirlist.length ; i++)
	    if (dirlist[i].indexOf("template") != -1 && 
	      dirlist[i].indexOf(".html") != -1 && 
	      !dirlist[i].endsWith("~"))
		count++;
	detailLevels = count;

	// Run through the templates
	BufferedReader template;
	PrintWriter newfile;
	for (int n=1 ; n<=detailLevels ; n++) {
	    // Open new output file
	    fileName = protocolDir + "output" + experimentNumber + "_" + n + ".html";
	    try {
		newfile = new PrintWriter(new FileOutputStream(fileName)); }
	    catch (IOException exc) {
		myAlert errorWindow;
		errorWindow = new myAlert("Can't write to the file " + fileName);
		errorWindow.show();
		return "";
	    }
	    // Open template file
	    fileName = protocolDir + "template" + n + ".html";
	    try {
		template = new BufferedReader(new FileReader(fileName)); }
	    catch (FileNotFoundException exc) {
		errorWindow = new myAlert("Can't read the file " + fileName);
		errorWindow.show();
		return "";
	    }

	    // 'Parse' template file, send to new file, but plug holes
	    String line;
	    try {
		line = template.readLine(); }
	    catch (IOException exc) {
		errorWindow = new myAlert("Can't read from a template file");
		errorWindow.show();
		return "";
	    }
	    while (line != null) {
		// Plug hole : Previous
		if (line.indexOf("<!-- HOLE:previous ") != -1) {
		    String name;
		    String img = new String();
		    int start, stop;

		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "Previous";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }
		    
		    if (experimentNumber==1)
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"../index.html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"../index.html\"> <img border=0 src=\"" + img + "\"></a>");
		    else
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"output" + (experimentNumber-1) + "_" + n +
					    ".html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"output" + (experimentNumber-1) + "_" + n +
					    ".html\"> <img border=0 src=\"" + img + "\"></a>");

		}
		
		// Plug hole : Next
		else if (line.indexOf("<!-- HOLE:next ") != -1) {
		    String name;
		    String img = new String();
		    int start, stop;
		    
		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "Next";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }

		    if (img.compareTo("")==0)
			newfile.println("<a href=\"output" + (experimentNumber+1) + "_" + n +
					".html\">" + name + "</a>");
		    else
			newfile.println("<a href=\"output" + (experimentNumber+1) + "_" + n +
					".html\"> <img border=0 src=\"" + img + "\"></a>");
		}
		
		// Plug hole : Less Details
		else if (line.indexOf("<!-- HOLE:lessDetails ") != -1) {
		    String name;
		    String img = new String();
		    int start, stop;

		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "Less Details";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }
		    
		    if (n==1)
			if (indexPage.indexOf("index.html") != -1)
			    if (img.compareTo("")==0)
				newfile.println("<a href=\"../index.html\">" + name + "</a>");
			    else
				newfile.println("<a href=\"../index.html\"> <img border=0 src=\"" + img + "\"></a>");
			else
			    if (img.compareTo("")==0)
				newfile.println("<a href=\"../"+indexPage+"_1.html\">" + name + "</a>");
			    else
				newfile.println("<a href=\"../"+indexPage+"_1.html\"> <img border=0 src=\"" + img + "\"></a>");
		    else
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"output" + experimentNumber +  "_" + (n-1) +
					    ".html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"output" + experimentNumber +  "_" + (n-1) +
					    ".html\"> <img border=0 src=\"" + img + "\"></a>");
		}
		
		// Plug hole : More Details
		else if (line.indexOf("<!-- HOLE:moreDetails ") != -1) {
		    String name;
		    String img = new String();
		    int start, stop;
		    
		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "More Details";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }
		    
		    if (n==detailLevels)
			if (indexPage.indexOf("index.html") != -1)
			    if (img.compareTo("")==0)
				newfile.println("<a href=\"../index.html\">" + name + "</a>");
			    else
				newfile.println("<a href=\"../index.html\"> <img border=0 src=\"" + img + "\"></a>");

			else
			    if (img.compareTo("")==0)
				newfile.println("<a href=\"../"+indexPage+"_1.html\">" + name + "</a>");
			    else
				newfile.println("<a href=\"../"+indexPage+"_1.html\"> <img border=0 src=\"" + img + "\"></a>");

		    else
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"output" + experimentNumber +  "_" + (n+1) +
					    ".html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"output" + experimentNumber +  "_" + (n+1) +
					    ".html\"> <img border=0 src=\"" + img + "\"></a>");
		}
		
		// Plug hole : Previous Subprotocol
		else if (line.indexOf("<!-- HOLE:protocolPrevious ") != -1) {
		    String name;
		    String prev;
		    String img = new String();
		    int start, stop;

		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "Previous Protocol";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }

		    try {
			prev = (String) protocolStep2Name.get(compositeName+"+"+
			       (((Integer) protocolName2Step.get(compositeName+protName)).intValue()-1));
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"../" + prev + "/output" + ((Integer) experimentNumberHT.get(compositeName+"+"+prev+"/")).intValue() + "_" + n + ".html\">" + name + "</a>");
			else 
			    newfile.println("<a href=\"../" + prev + "/output" + ((Integer) experimentNumberHT.get(compositeName+"+"+prev+"/")).intValue() + "_" + n + ".html\"> <img border=0 src=\"" + img + "\"></a>");
		    }
		    catch (NullPointerException exc) {
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"../"+indexPage+"_1.html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"../"+indexPage+"_1.html\"> <img border=0 src=\"" + img + "\"></a>");
		    }
		}
		
		// Plug hole : Next Subprotocol
		else if (line.indexOf("<!-- HOLE:protocolNext ") != -1) {
		    String name;
		    String img = new String();
		    String next;
		    int start, stop;
		    
		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1){
			name = "Next Protocol";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }

		    try {
			next = (String) protocolStep2Name.get(compositeName+"+"+
			       (((Integer) protocolName2Step.get(compositeName+protName)).intValue()+1));
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"../" + next + "/output" + ((Integer) experimentNumberHT.get(compositeName+"+"+next+"/")).intValue() + "_" + n + ".html\">" + name + "</a>");
			else 
			    newfile.println("<a href=\"../" + next + "/output" + ((Integer) experimentNumberHT.get(compositeName+"+"+next+"/")).intValue() + "_" + n + ".html\"> <img border=0 src=\"" + img + "\"></a>");
		    }
		    catch (NullPointerException exc) {
			if (img.compareTo("")==0)
			    newfile.println("<a href=\"../"+indexPage+"_1.html\">" + name + "</a>");
			else
			    newfile.println("<a href=\"../"+indexPage+"_1.html\"> <img border=0 src=\"" + img + "\"></a>");
		    }
		}
		
		// Plug hole : Goto surrounding composite protocol
		else if (line.indexOf("<!-- HOLE:surroundingProtocol ") != -1) {
		    String name;
		    String img = new String();
		    int start, stop;
		    
		    // Processing parameter
		    start = line.indexOf("name=");
		    if (start == -1) {
			name = "Surrounding Protocol";
			start = line.indexOf("img=");
			stop = line.substring(start).indexOf("//-->") + start;
			img = line.substring(start,stop);
		    }
		    else {
			start += 5;
			if (line.substring(start).indexOf("img=")!=-1) {
			    stop = line.substring(start).indexOf("img=") + start;
			    name = line.substring(start,stop);
			    start = stop+4;
			    stop = line.substring(start).indexOf("//-->") + start;
			    img = line.substring(start,stop);
			}
			else {
			    stop = line.substring(start).indexOf("//-->") + start;
			    name = line.substring(start,stop);
			}
		    }

		    if (img.compareTo("")==0)
			newfile.println("<a href=\"../"+indexPage+"_1.html\">" + name + "</a>");
		    else
			newfile.println("<a href=\"../"+indexPage+"_1.html\"> <img border=0 src=\"" + img + "\"></a>");
		}

		// Plug hole : Object
		else if (line.indexOf("<!-- HOLE:object ") != -1) {
		    String name;
		    int start, stop;
		    int detail;
		    String detailStr;
		    toHtml obj;
		    
		    // Processing parameter name
		    start = line.indexOf("name=");
		    if (start == -1) {
			errorWindow = new myAlert("The object should have a name");
			errorWindow.show();
			return "";
		    }
		    else {
			start += 5;
			stop = line.substring(start).indexOf("detail=");
			if (stop == -1)
			    stop = line.substring(start).indexOf("//-->");
			
			stop += start;
			name = line.substring(start,stop);
		    }
		    stop = name.indexOf(" ");
		    if (stop != -1)
			name = name.substring(0,stop);

		    // Processing parameter detail
		    start = line.indexOf("detail=");
		    if (start == -1)
			detail = 0;
		    else {
			start += 7;
			stop = line.substring(start).indexOf("//-->") + start;
			detailStr = line.substring(start,stop);

			stop = detailStr.indexOf(" ");
			if (stop != -1)
			    detailStr = detailStr.substring(0,stop);
			detail = Integer.parseInt(detailStr);
		    }
		    // Find the object with the right name
		    obj = null;
		    for (int i=0 ; i<objects.length ; i++){
			if (objects[i].getName().equals(name))
			    obj = objects[i];
		    }
		    if (obj == null) {
			errorWindow = new myAlert("An object with the name " + name + " does not exist");
			errorWindow.show();
			return "";
		    }
		    newfile.println(obj.tohtml(detail));
	        }

		// Plug hole : The long text representation
		else if (line.indexOf("<!-- HOLE:longTextList //-->") != -1) {
		    newfile.println("<table border=1>");
		    newfile.println("<tr><td>");
		    for (int i=0 ; i<dv.length ; i++)
			switch (dv[i]) {
			case 0:
			    newfile.print("0x ");
			    break;
			case 1:
			    newfile.print("1x ");
			    break;
			case 2:
			    newfile.print("0+ ");
			    break;
			case 3:
			    newfile.print("1+ ");
			    break;
			}
		    newfile.println();
		    newfile.println("</td></tr>");
		    newfile.println("</table>");
		}
		
		// Plug hole : The color text representation
		else if (line.indexOf("<!-- HOLE:colorTextList //-->") != -1) {
		    newfile.println("<table border=1>");
		    newfile.println("<tr><td>");
		    for (int i=0 ; i<dv.length ; i++)
			switch (dv[i]) {
			case 0:
			    newfile.print("<font color=#FF0000>0</font> ");
			    break;
			case 1:
			    newfile.print("<font color=#FF0000>1</font> ");
			    break;
			case 2:
			    newfile.print("<font color=#0000FF>0</font> ");
			    break;
			case 3:
			    newfile.print("<font color=#0000FF>1</font> ");
			    break;
			}
		    newfile.println();
		    newfile.println("</td></tr>");
		    newfile.println("</table>");
		}
		
		// Plug hole : Protocol
		else if (line.indexOf("<!-- HOLE:protocol ") != -1) {
		    String name;
		    int start, stop;
		    
		    // Processing parameter name
		    start = line.indexOf("name=");
		    if (start == -1) {
			errorWindow = new myAlert("The protocol should have a name");
			errorWindow.show();
			return "";
		    }
		    else {
			start += 5;
			stop = line.substring(start).indexOf("step") + start;
			name = line.substring(start,stop);
		    }
		    
		    stop = name.indexOf(" ");
		    if (stop != -1)
			name = name.substring(0,stop);

		    if (n==1) {
			if (first==1) {
			    first = 0;
			    // Open template file
			    BufferedReader template2;
			    fileName = protocolDir + "template" + n + ".html";
			    try {
				template2 = new BufferedReader(new FileReader(fileName)); }
			    catch (FileNotFoundException exc) {
				errorWindow = new myAlert("Can't read the file " + fileName);
				errorWindow.show();
				return "";
			    }
			
			    // Build hashtable with steps
			    String line2;
			    try {
				line2 = template2.readLine(); }
			    catch (IOException exc) {
				errorWindow = new myAlert("Can't read from a template file");
				errorWindow.show();
				return "";
			    }
			    while (line2 != null) {
				if (line2.indexOf("<!-- HOLE:protocol ") != -1) {
				    String name2;
				    String step2;
				
				    // Processing parameter name
				    start = line2.indexOf("name=");
				    if (start == -1) {
					errorWindow = new myAlert("The protocol should have a name");
					errorWindow.show();
					return "";
				    }
				    else {
					start += 5;
					stop = line2.substring(start).indexOf("step") + start;
					name2 = line2.substring(start,stop);
				    }
				
				    stop = name2.indexOf(" ");
				    if (stop != -1)
					name2 = name2.substring(0,stop);
				
				    // Processing parameter step
				    start = line2.indexOf("step=");
				    if (start == -1) {
					errorWindow = new myAlert("The protocol should have a step value");
					errorWindow.show();
					return "";
				    }
				    else {
					start += 5;
					stop = line2.substring(start).indexOf("//-->") + start;
					step2 = line2.substring(start,stop);
				    }
				
				    stop = step2.indexOf(" ");
				    if (stop != -1)
					step2 = step2.substring(0,stop);

				    protocolStep2Name.put(protName+"+"+step2, name2);
				    //ICICICICICICICICICICICICICICICICI
				    protocolName2Step.put(protName+name2+"/", 
							  new Integer(Integer.parseInt(step2)));

				    // Calculate experimentNumber for the subprotocols
	                            fileName = protocolsMainDir + name2+File.separator;
				    dir = new File(fileName);
				    dirlist = dir.list();
				    count=0;
				    for (int i=0 ; i<dirlist.length ; i++)
					if (dirlist[i].indexOf("output") != -1 && 
					    dirlist[i].indexOf("_1.html") != -1 && 
					    !dirlist[i].endsWith("~"))
					    count++;
				    experimentNumberHT.put(protName+"+"+name2+"/", new Integer(count+1));
				    //*****ICICICICICICICICICICI
				}
			    
				try {
				    line2 = template2.readLine(); }
				catch (IOException exc) {
				    errorWindow = new myAlert("Can't read from a template file");
				    errorWindow.show();
				    return "";
				}
			    }

			    // Close template file
			    try {
				template2.close(); }
			    catch (IOException exc) {
				errorWindow = new myAlert("Can't close a template file");
				errorWindow.show();  }
			}

			// Call subprotocol
			tempStr = makeHTMLOutput(ht, protocolsMainDir, name, 
						 protName+"output"+experimentNumber, protName);
			protocolLinkNames.put(name, tempStr);
		    }

		    newfile.println("<a href=\"../" + protocolLinkNames.get(name) + 
				    "_" + n + ".html\">" + name + "</a>");
		}

		// Plug hole : The graphical representation
		else if (line.indexOf("<!-- HOLE:graphicList ") != -1) {
		    // Parse the parameters
		    //// width
		    int start = line.indexOf("width=");
		    if (start == -1) {
			errorWindow = new myAlert("The graphicList has to set width");
			errorWindow.show();
			return "";
		    }
		    start += 6;
		    int stop = line.substring(start).indexOf(" ") + start;
		    int width = Integer.parseInt(line.substring(start,stop));
		    
		    //// pixelWidth
		    start = line.indexOf("pixel=");
		    if (start == -1) {
			errorWindow = new myAlert("The graphicList has to set pixel dimensions");
			errorWindow.show();
			return "";
		    }
		    start += 6;
		    stop = line.substring(start).indexOf(",") + start;
		    int pixelWidth = Integer.parseInt(line.substring(start,stop));
		    
		    //// pixelHeight
		    start = line.indexOf(",");
		    if (start == -1) {
			errorWindow = new myAlert("The graphicList has to set pixel dimensions");
			errorWindow.show();
			return "";
		    }
		    start++;
		    stop = line.substring(start).indexOf(" ") + start;		
		    int pixelHeight = Integer.parseInt(line.substring(start,stop));
		    
		    //// Crop width
		    width -= width%pixelWidth;
		    
		    
		    // Build image
		    int xBegin, yBegin;
		    int height = (dv.length*pixelWidth*pixelHeight)/width;
		    if ((dv.length*pixelWidth*pixelHeight)%width != 0)
			height++;
		    if (height%pixelHeight != 0)
			height += pixelHeight - height%pixelHeight;
		    byte r[][] = new byte[width][height];
		    byte g[][] = new byte[width][height];
		    byte b[][] = new byte[width][height];
		    
		    //// Initialize all the pixels to white
		    for (int i=0 ; i<width ; i++)
			for (int j=height-pixelHeight ; j<height ; j++) {
			    r[i][j] = (byte) 255;
			    g[i][j] = (byte) 255;
			    b[i][j] = (byte) 255;
			}
		    
		    //// Write the rgb arrays
		    int k = 0;
		    for (int i=0 ; i<dv.length ; i++) {
			xBegin = (k%(width/pixelWidth))*pixelWidth;
			yBegin = (k/(width/pixelWidth))*pixelHeight;
			switch (dv[i]) {
			case 0:
			    for (int x = xBegin  ;  x < xBegin+pixelWidth  ;  x++)
				for (int y = yBegin  ;  y < yBegin+pixelHeight  ;  y++) {
				    r[x][y] = (byte) 0; 
				    g[x][y] = (byte) 0; 
				    b[x][y] = (byte) 255; 
				} 
			    k++;
			    break;
			case 1:
			    for (int x = xBegin  ;  x < xBegin+pixelWidth  ;  x++)
				for (int y = yBegin  ;  y < yBegin+pixelHeight  ;  y++) {
				    r[x][y] = (byte) 255; 
				    g[x][y] = (byte) 0; 
				    b[x][y] = (byte) 0; 
				} 
			    k++;
			    break;
			case 2:
			    for (int x = xBegin  ;  x < xBegin+pixelWidth  ;  x++)
				for (int y = yBegin  ;  y < yBegin+pixelHeight  ;  y++) {
				    r[x][y] = (byte) 0; 
				    g[x][y] = (byte) 255; 
				    b[x][y] = (byte) 0; 
				} 
			    k++;
			    break;
			case 3:
			    for (int x = xBegin  ;  x < xBegin+pixelWidth  ;  x++)
				for (int y = yBegin  ;  y < yBegin+pixelHeight  ;  y++) {
				    r[x][y] = (byte) 0;
				    g[x][y] = (byte) 0;
				    b[x][y] = (byte) 0;
				}
			    k++;
			    break;
			}
		    }
		    
		    
		    // Save as GIF
		    String gifName = protocolDir + "output" + experimentNumber + "_" + 
			             n + "_" + gifCounter + ".gif";
		    try {
			BufferedOutputStream gifFile= new BufferedOutputStream(new FileOutputStream(gifName));
			GIFEncoder gifEnc = new GIFEncoder(r, g, b);
			gifEnc.Write(gifFile); 
			gifFile.close();
		    }
		    catch (FileNotFoundException exc) {
			errorWindow = new myAlert("Can't open the GIF file");
			errorWindow.show();
			return "";
		    }
		    catch (AWTException exc) {
			errorWindow = new myAlert("Can't convert to GIF");
			errorWindow.show();
			return "";
		    }
		    catch (IOException exc) {
			errorWindow = new myAlert("Can't save GIF file");
			errorWindow.show();
			return "";
		    }

		    
		    // Link to the image
		    newfile.print("<img src=\"output" + experimentNumber + "_" + 
				  n + "_" + gifCounter + ".gif" + "\">");
		}
		else
		    newfile.println(line);
		
		try {
		    line = template.readLine(); }
		catch (IOException exc) {
		    errorWindow = new myAlert("Can't read from a template file");
		    errorWindow.show();
		    return "";
		}
	    }
	    
	    // Close the files
	    try {
		template.close(); }
	    catch (IOException exc) {
		errorWindow = new myAlert("Can't close a template file");
		errorWindow.show(); }
	    newfile.close();
	}

	return protName + "output" + experimentNumber;
    }
    
    
    /**
    * Generates the html index lacated in the directory indicated in
    * the call to the constructor.
    * @param filename is filename of the index
    * @title is the title to be given for the new entry in the index
    * @size is the size of the experiement (why!?!?!?!!??!)
    */

    public void makeHTMLindex(String filename,String title,int size){
	makeHTMLindex(filename,_dirName,title,size);
    }


    ///////////////////
    // makeHTMLindex //
    ///////////////////
    @SuppressWarnings("static-access")
	public void makeHTMLindex(String fileName, String dirName, 
			      String title, int size) {
	BufferedReader index;
	String protocolDir;
	String tempIndexFilename = "(undefined)";
	String indexFilename;
	myAlert errorWindow;

	// Open the files
	PrintWriter newfile;
	protocolDir = dirName;
	//ICICICICICICICICICICICICI
	if (!protocolDir.endsWith(File.separator))
	    protocolDir += File.separator;
	//****ICICICICICICICICCII
	try {
	    tempIndexFilename = protocolDir + "tempIndex.html";
	    newfile = new PrintWriter(new FileOutputStream(tempIndexFilename)); 
	}
	catch (IOException exc) {
	    errorWindow = new myAlert("Can't open "+ tempIndexFilename);
	    errorWindow.show();
	    return;
	}
	try {
	    indexFilename = protocolDir + "index.html";
	    index = new BufferedReader(new FileReader(indexFilename)); 
	}
	catch (FileNotFoundException exc) {
	    errorWindow = new myAlert("Can't open index.html");
	    errorWindow.show();
	    newfile.close();
	    return;
	}

	// Run through the index.html file
	String line;
	try {
	    line = index.readLine(); }
	catch (IOException exc) {
	    errorWindow = new myAlert("Can't read from the file index.html");
	    errorWindow.show();
	    return;
	}
	while (line != null) {
	    if (line.indexOf("<!-- HOLE:NEXTENTRY //-->") != -1) {
		newfile.println("<!-- HOLE:NEXTENTRY //-->");
		newfile.print("     <li> <a href=\"" + fileName  + "_1" + ".html" + "\">" + title + "</a> ");

		// Write the date and time
		Calendar time = new GregorianCalendar().getInstance();
		String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", 
				   "Aug", "Sep", "Oct", "Nov", "Dec"};
		newfile.print("[");
		if (time.get(Calendar.DAY_OF_MONTH) < 10)
		    newfile.print("0");
		newfile.print(time.get(Calendar.DAY_OF_MONTH));
		newfile.print(" ");
		newfile.print(months[time.get(Calendar.MONTH)]);
		newfile.print(" ");
		newfile.print(time.get(Calendar.YEAR));
		newfile.print(" - ");
		if (time.get(Calendar.HOUR_OF_DAY) < 10)
		    newfile.print("0");
		newfile.print(time.get(Calendar.HOUR_OF_DAY));
		newfile.print(":");
		if (time.get(Calendar.MINUTE) < 10)
		    newfile.print("0");
		newfile.print(time.get(Calendar.MINUTE));
		newfile.print(".");
		if (time.get(Calendar.SECOND) < 10)
		    newfile.print("0");
		newfile.print(time.get(Calendar.SECOND));
		newfile.print("]");

		// Write the rest of the parameters
		//newfile.println("[size=" +  size + "]");

		// Make links to all detail levels
		//for (int i=1 ; i<=detailLevels ; i++)
		//    newfile.print("(<a href=\"" + fileName  + "_" + i + ".html" + "\">" + i + "</a>)");
		newfile.println();
	    }
	    else
		newfile.println(line);

	    try {
		line = index.readLine(); }
	    catch (IOException exc) {
		errorWindow = new myAlert("Can't read from the file index.html");
		errorWindow.show();
		return;
	    }
	}

	// Close the files
	try {
	    index.close(); 
	    File indF = new File(indexFilename);
	    indF.delete();
	}catch (IOException exc) {
	    errorWindow = new myAlert("Can't close the file or remove index.html");
	    errorWindow.show(); }
	newfile.close();
	// Overwrite the old indexfile
	File newIndexFile = new File(tempIndexFilename);
	newIndexFile.renameTo(new File(indexFilename));
    }


  
	
    ///////////////////
    // Main function //
    ///////////////////
    public static void main(String s[]) {
 	htmlGenerator panel = new htmlGenerator();
	
	frame = new JFrame("htmlGenerator");
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0); }
	});
	frame.getContentPane().add("Center", panel);
	frame.pack();
	frame.setVisible(true);
    }
}
