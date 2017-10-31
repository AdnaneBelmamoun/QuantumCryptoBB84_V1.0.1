package qrypto.htmlgenerator;


import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/***
 * This class is used to store the object to fill holes
 * in html templates. An HoleValues object is used by the htmlGenerator
 * in order to generate the html pages correspoding to an execution
 * of some protocols.
 * File: HoleValues.java
 * created 13-Jul-00 1:11:49 PM by louis
 * @author Louis Salvail (salvail@brics.dk)
 */


public class HoleValues
{
public static String _ERROR_NO_HOLE_VALUE_FOUND = "<B>This hole couldn't be filled</B>";
public static String DEF_TARGET_DIR = "/Protocols/";


//_template[i] is the template name and directory for the i-th page
@SuppressWarnings("rawtypes")
private Vector _templateName = null;
//_h[i] is the set of holes to be filled for i-th page generated
@SuppressWarnings("rawtypes")
private Vector _h = null;
// The target directory where the generated pages will be stored.
@SuppressWarnings("unused")
private String _targetDir = DEF_TARGET_DIR;



/**
* Empty constructor.
*/

@SuppressWarnings("rawtypes")
public HoleValues(){
    _templateName = new Vector();
    _h = new Vector();
}

/**
* Add a new web page to this object. 
* @param newh is the set holes to be filled.
* @param newtemplate is where to find the new template. This is
* a directory+filename.
*/


@SuppressWarnings({ "rawtypes", "unchecked" })
public void add(Hashtable newh, String newtemplate){
    _templateName.addElement(newtemplate);
    _h.addElement(newh);
}

/**
* Gets the hole content for a given template.
* @param holeName is the name of the hole to look for
* @param forTemplate is the template where to look. Returns null if
* the hole and/or the template couldn't be found.
*/

@SuppressWarnings("rawtypes")
public toHtml getHole(String holeName, String forTemplate){
    toHtml res = null;
    boolean found = false;
    int i = 0;
    for(Enumeration e = _templateName.elements(); e.hasMoreElements() && !found;){
	String s = (String)e.nextElement();
	if(forTemplate.equals(s)){
	    found = true;
	}else{
	    i++;
	}
    }
    if(found){
	Hashtable h = (Hashtable)_h.elementAt(i);
	res = h.containsKey(holeName)?res = (toHtml)h.get(holeName):null ;
    }
    return res;
}


/**
* Returns an enumeration of the templates.
* @return the enumeration of template names. Should be cast to strings.
*/

@SuppressWarnings("rawtypes")
public Enumeration getTemplates(){
    return _templateName.elements();
}

/**
* Returns the html code produced by the hole value in some template.
* @param holeName is the name of the hole to be filled.
* @param forTemplate is the template to look for.
* @return the html code for filling the hole. If the hole value couldn't be
* found then the string "Couldn't plug the value" is returned.
*/

public String getHoleValue(String holeName, String forTemplate){
    String s;
    toHtml t = getHole(holeName, forTemplate);
    if(t == null){
	s = _ERROR_NO_HOLE_VALUE_FOUND;
    }else{
	s = t.tohtml(1);
    }
    return s;
}

/**
* Returns the template with the given hole name. 
* @param holeName is the name of the hole to look for.
* @return the template that contains the hole. Returns null if no 
* template were found. If several templates have the same holename then
* only the forst one is returned.
*/

@SuppressWarnings("rawtypes")
public String getTemplateName(String holeName){
    String s = null;
    boolean found = false;
    int i = 0;
    for(Enumeration e = _h.elements(); e.hasMoreElements() && !found;){
	found = ((Hashtable)e.nextElement()).containsKey(holeName);
	if(!found){i++;}
    }
    if(found){
	    s = (String)_templateName.elementAt(i);
    }
    return s;
}



}
