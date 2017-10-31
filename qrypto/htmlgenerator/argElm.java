package qrypto.htmlgenerator;

public class argElm {
    // Fields //
    String protocolName;
    int instanceNo;
    int[] qubits;
    toHtml[] objects; 
    
    // Constructor //
    public argElm(String protocolNameArg, int instanceNoArg, int[] qubitsArg, toHtml[] objectsArg) {
	protocolName = protocolNameArg;
	instanceNo = instanceNoArg;
	qubits = qubitsArg;
	objects = objectsArg;
    }
}

