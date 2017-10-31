package qrypto.htmlgenerator;

public class toHtmlInt implements toHtml {
    String name;
    int value;
    
    // Constructor //
    public toHtmlInt (String n, int val) {
	name = n;
	value = val;
    }

    // getName //
    public String getName() {
	return name;
    }

    // toHtml //
    public String tohtml(int detailLevel) {
	    return Integer.toString(value);
    }
}

