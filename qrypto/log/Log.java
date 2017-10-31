
package qrypto.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;



public class Log
{

public static final String CR = "\n";


@SuppressWarnings("unused")
private boolean _append = false;
private FileWriter _outputWriter = null;
private String _owner = null;
private Date _creation = null;

/**
* This is the constructor of a new log file.
* @param filename is the file name with the path.
* @param append is true if the log file is appended to an existing one.
* @param owner is the class or package owning this log file.
* @exception NullPointerExcetion if the path was null.
* @exception IOException if an I/O error occurs.
*/
public Log(String file, boolean append, String owner)throws IOException{
    _append = append;
    _outputWriter = new FileWriter(file,append);
    _owner = owner;
    _creation = new Date();
}



/**
* This includes a header in the logfile.
* 
*/


public void header(){
writeln("***************************************************");
writeln("*****************New Execution*********************");
writeln("DATE:"+_creation.toString());
writeln("MODULE:"+_owner);
writeln("***************************************************");
}



/**
* Writes a string to the logfile.
* @param s is the string to write.
* @excpetion NullPointerException if the writer was not initialised correctly.
* @exception RuntimeException if the I/O error occurs.
*/


public void write(String s){
  if(_outputWriter == null){
    throw new NullPointerException("Attempt to write to a null Writer.");
  }
  try{
      _outputWriter.write(s,0,s.length());
      _outputWriter.flush();
  }catch(IOException io){
    throw new RuntimeException(io.getMessage());
  }
}



public void writeln(String s){
    write(s+CR);
}


/**
* Writes an int to the logfile.
* @param i is the int to write.
* @excpetion NullPointerException is the writer was not initialised correctly.
* @exception RuntimeException if I/O error occurs.
*/

public void write(int i){
    String s = Integer.toString(i);
    write(s);
}


public void writeln(int i){
    write(i);
    write(CR);
}



/**
* This flushes the buffer.
* @exception RuntimeException whenever an I/O error occurs.
*/


public void flush(){
   try{
    _outputWriter.flush();
    }catch(IOException io){
	throw new RuntimeException("Couldn't flush into the logfile:"+io.getMessage());
    }
}


/**
* Writes a boolean to the log file
* @param i is the int to write.
* @excpetion NullPointerException is the writer was not initialised correctly.
* @exception RuntimeException if I/O error occurs.
*/


public void write(boolean b){
    if(b){write("true");}
    else{write("false");}
}


public void writeln(boolean b){
    write(b);
    write(CR);
}

public void writeln(){
    write(CR);
}



/**
* Static writeln for string. If the logfile is null then nothing is done.
* @param logfile is where the string will be added.
* @param s is the string to be added to the logfile.
* @param ln tells whether or not a line feed is added.
*/

public static void write(Log logfile, String s, boolean ln){
    if(logfile != null){
	if(ln){logfile.writeln(s);}
	else{logfile.write(s);}
    }
}


/**
* Static writeln for int. If the logfile is null then nothing is done.
* @param logfile is where the string will be added.
* @param i is the int to be added to the logfile.
* @param ln tells whether or not a line feed is added.
*/

public static void write(Log logfile, int i, boolean ln){
    if(logfile != null){
	if(ln){logfile.writeln(i);}
	else{logfile.write(i);}
    }
}


/**
* Static writeln for boolean. If the logfile is null then nothing is done.
* @param logfile is where the string will be added.
* @param b is the boolean to be added to the logfile.
* @param ln tells whether or not a line feed is added.
*/

public static void write(Log logfile, boolean b, boolean ln){
    if(logfile != null){
	if(ln){logfile.writeln(b);}
	else{logfile.write(b);}
    }
}

public static void write(Log logfile, byte[] b){
    for(int i = 0; i<b.length;i++){
	logfile.write(Byte.toString(b[i]));
	if(Math.abs(Math.IEEEremainder(i+1,25.0d))<0.5d){
		logfile.writeln();
	}
	if(Math.abs(Math.IEEEremainder(i+1,125.0d))<0.5d){
	    logfile.writeln();
	}
    }
}




/**
* Close the log file.
* @exception IOException when an I/O error occurs.
*/

public void close()throws IOException{
    _outputWriter.flush();
    _outputWriter.close();
}

/**
* This method close the logfile without throwing exception.
*/

public void closeAnyway(){
  try{
    _outputWriter.flush();
    _outputWriter.close();
    }catch(IOException io){
	System.out.println("excpetion trown while closing the logfile:"+io.getMessage());
    }
}



}
