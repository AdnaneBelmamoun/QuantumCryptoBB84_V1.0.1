package qrypto.htmlgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import qrypto.exception.QryptoWarning;



public class htmlManager
{


@SuppressWarnings("rawtypes")
private static Vector notFound = null;


/**
* This method collects all file that are reachable from some
* starting html file that has been generated by htmlGenerator.
* The starting file must be an output html  file that has been generated
* by the htmlGenerator. In particular,  it is located one directory
* below the <TEMPLATE_DIR> where the index.html file is located.
* This does not update the index.html file and should not be called
* with the index.html file as argument.
* @param startFile is the html file to start from. Locates at 
* <TEMPLATE_DIR>/<PROT_NAME>/startFile.html.
* @param indexPath is the path where to find the index.html file
* but not including the filename index.html.
* @return the files scheduled for removal.
*/
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public static synchronized Vector cleanFrom(File startFile, File indexPath)throws IOException{
  int n = 0;
  String basisDir = indexPath.getPath();
  Vector reachable = new Vector();
  notFound = new Vector();
  if(startFile.exists()){
    int i = 0;
    reachable.addElement(startFile.getPath());
    while(i<reachable.size()){
      String filename = (String)reachable.elementAt(i);
      File newFile = null;
      try{
	 newFile = new File(filename);
	 findAll(newFile,reachable,basisDir);
      }catch(IOException io){
	notFound.addElement(filename);
      }
      i++;
    }
  }else{
     throw new IOException("File"+startFile.getPath()+" couldn't be found.");
  }
  return reachable;
}

/**
* The same as before except that the input files can be 
* given by their names.
*/

@SuppressWarnings("rawtypes")
public static synchronized Vector cleanFrom(String startFile,String indexPath) throws IOException{
    File sFile = new File(startFile);
    File iFile = new File(indexPath);
    return cleanFrom(sFile,iFile);
}

/**
* Looks at all links to files with name output*.html and schedule
* them for removing. Only the links to *.html files are found.
* The gif files if any are not removed.
* @param from is the html file to start from.
* @param a vector in which all reachable files from the file from will
* be added. It only adds the files that do not already belong to it.
* @exception IOException when the file from couldn't be opened or read.
*/
@SuppressWarnings("unchecked")
private static synchronized void findAll(File from, @SuppressWarnings("rawtypes") Vector reachable,String basisDir) throws IOException{
  BufferedReader br = new BufferedReader(new FileReader(from));
  String line = br.readLine();
  while(line != null){
    int start = line.indexOf("<a href=");
    if(start != -1){
	int a = line.indexOf("output");
	int b   = line.indexOf(".html");
	if((a != -1) && (b != -1)){
	    String filename = line.substring(start+9,b+5).trim();
	    if(filename.startsWith("../")){
		filename = filename.substring(3);
	    }
	    filename = basisDir+"/"+filename;
	    if(!reachable.contains(filename)){
		reachable.addElement(filename);
	    }
	}
    }
    line = br.readLine();
  }
  br.close();
}


/**
* Removes one entry from the index file containing all executions.
* It looks for the enumeration environement containing 
* the tag <!-- HOLE:nextentry //--> and remove the line with the given
* link if it is there.
* @param file2remove is the name of the file that the link to be removed
* links to. The name should only contain <PROT_DIR>/<FILENAME>.
* @return true if the execution was successfull.
* 
*/

public static synchronized boolean removeFromIndex(File indexFile, String file2remove){
   @SuppressWarnings("unused")
boolean cannotclose = false;
   boolean out = true;
   String newindexpath = indexFile.getPath();
   newindexpath = newindexpath.substring(0,newindexpath.indexOf("index.html"));
   try{   
      BufferedReader br = new BufferedReader(new FileReader(indexFile));
      PrintWriter newIndexFile = new PrintWriter(new FileOutputStream(newindexpath+"newindex.html"));
      String line = br.readLine();
      boolean foundHole = false;
      while(!foundHole && (line != null)){
	foundHole = (line.indexOf("<!-- HOLE:NEXTENTRY //-->") != -1);
	newIndexFile.println(line);
	line = br.readLine();
      }
      while(line != null){
	boolean removeit = false;
	if(line.indexOf(file2remove) > -1){
	    removeit = true;
	}
	if(!removeit){
	    newIndexFile.println(line);
	}
	line = br.readLine();
      }
      try{
	  newIndexFile.close();
	  br.close();
      }catch(IOException io){
	throw new IOException("Couldn't close the index file");
      }
      //ICICICICICICI
      boolean res = indexFile.renameTo(new File(indexFile.getParent()+File.separator+"indexback.html"));
      File indexFileDesc = new File(indexFile.getParent()+File.separator+"index.html");
      //boolean res = indexFile.delete();
      File toMove = new File(newindexpath+"newindex.html");
      if(res){
	res = res && toMove.renameTo(indexFileDesc);
      }
      if(!res){
	 throw new IOException("Couldn't replace the index file");
      }
   }catch(IOException io){
	QryptoWarning.warning("Problem while updating the index file:\n"+
			      io.getMessage(),
			      "HTML Manager",
			      null);
	out = false;
   }
   return out;
}


/**
* Counts the number of links to protocol executions.
* @param indexFile is the index file.
* @return the number of links.
*/

public static synchronized int numberOfEntries(File indexFile){
   int out = 0;
   boolean done = false;
   try{   
      BufferedReader br = new BufferedReader(new FileReader(indexFile));
      String line = br.readLine();
      boolean foundHole = false;
      while(!foundHole && (line != null)){
	foundHole = (line.indexOf("<!-- HOLE:NEXTENTRY //-->") != -1);
	line = br.readLine();
      }
      if(foundHole){
	  while((line != null) && !done){
	    if(line.indexOf("<li>") != -1){
		out++;
	    }
	    if(line.indexOf("</ul>") != -1){
		done = true;
	    }
	    line = br.readLine();
	  }
      }
      br.close();
   }catch(IOException io){
	out = 0;
   }
   return out;
}



/**
* Removes all files which names appear in the input vector.
* @param files is a vector of filename.
* @return the number of removed files.
*/


@SuppressWarnings("rawtypes")
public static synchronized int removeAll(Vector files)throws IOException{
  int n = 0;
  for(Enumeration e = files.elements();e.hasMoreElements();){
    File f = new File((String)e.nextElement());
    if(f.exists()){
	f.delete();
	n++;
    }
  }
  return n;
}


/**
* If called after cleanFrom() then it returns all files that where
* scheduled for removal but couldn't be found or removed.
* @return a vector of filename (with their paths).
*/

@SuppressWarnings("rawtypes")
public static Vector notFound(){
    return notFound;
}



@SuppressWarnings("rawtypes")
public static void main(String[] argv){

    try{
	 System.out.println("Testing html Manager");
	 File startFile = new File("src/qrypto/templates/initiator/RecBB84/output13_1.html");
	 File indexPath = new File("src/qrypto/templates/initiator");
	 Vector r = htmlManager.cleanFrom(startFile,indexPath);
	 System.out.println("Not found:"+htmlManager.notFound().size());
	 System.out.println("Reachable:"+r.size());
	 for(Enumeration e = r.elements();e.hasMoreElements();){
	   String s = (String)e.nextElement();
	   System.out.println(s);
	 }
	 System.out.println("-------Removing the entry--------------------");
	 htmlManager.removeFromIndex(new File("src/qrypto/templates/initiator/index.html"),
				     "RecBB84/output13_1.html");
    }catch(IOException io){
	  System.out.println("ERROR:"+io.getMessage());
    }
}


}
