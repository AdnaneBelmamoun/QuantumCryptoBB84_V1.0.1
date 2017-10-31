package qrypto.gui;

import qrypto.qommunication.Constants;

public class Main
{

public static final String NO_OPT = "def";
public static final String FAKE_DG_OPT  = "fakeDG"; 




public static void main(String[] args) {
    String[] argA = {NO_OPT, NO_OPT};
    String[] argB = {NO_OPT, NO_OPT, NO_OPT};
    if(args != null){
	if((args.length > 0) && !args[0].equalsIgnoreCase(NO_OPT)){
	    Constants.THISIP = args[0];
	}
	if(args.length > 1 && !args[1].equalsIgnoreCase(NO_OPT)){
	    Constants.PEERIP = args[1];
	}
	if(args.length > 2 && args[2].equalsIgnoreCase(FAKE_DG_OPT)){
	    argA[1] = FAKE_DG_OPT;
	    argB[2] = FAKE_DG_OPT;
	}
    }
    Alice.main(argA);
    Bob.main(argB);
}


}
