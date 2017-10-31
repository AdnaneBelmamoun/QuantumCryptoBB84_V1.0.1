package qrypto.exception;


import java.awt.*;
import javax.swing.*;


/******************************************************
 * This is a static class used to dislpay a warning.
 * File: QryptoWarning.java
 * created 05-Sep-00 2:45:35 PM by louis
 */


public class QryptoWarning
{

/**
* Displays a warning in a window waitiing to be acknowledged.
* @param message is the message to be displayed.
* @param origin is the description of the origin of the warning/error.
* @param parent is the parent component. Null if there isn't any.
*/

public static void warning(String message,String origin, Component parent ){
    JOptionPane.showMessageDialog(parent, message,
			          origin+":Warning",JOptionPane.WARNING_MESSAGE);
}

}
