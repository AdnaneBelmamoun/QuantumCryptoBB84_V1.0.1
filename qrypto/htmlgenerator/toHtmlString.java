package qrypto.htmlgenerator;

public class toHtmlString implements toHtml {
    String name;
    String value;
    
    // Constructor //
    public toHtmlString (String n, String val) {
	name = n;
	value = val;
    }

    // getName //
    public String getName() {
	return name;
    }

    // toHtml //
    public String tohtml(int detailLevel) {
	switch (detailLevel) {
	case 1: 
	    return "<b>" + value.toString() + "</b>";
	case 2:
	    return "<i>" + value.toString() + "</i>";
	default:
	    return value.toString();
	}
    }
}
