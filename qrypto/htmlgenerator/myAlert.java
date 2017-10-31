package qrypto.htmlgenerator;

import javax.swing.JOptionPane;

public class myAlert extends Object{ 
    //JDialog {
    //JPanel panel;
    
    private String _message = "No message";
    
    // Constructor //
    public myAlert (String message) {

	_message = message;
	// Message
	//panel = new JPanel();
	//JLabel messageLabel = new JLabel(message);

	// OK button
	//JButton okButton = new JButton("OK");
	//okButton.addActionListener(new ActionListener() {
	//	public void actionPerformed(ActionEvent e) {
	//	    setVisible(false);
	//	}
	 //   }
	//);

	// Put objects on panel	
	//GridBagLayout gridbag = new GridBagLayout();
	//GridBagConstraints c = new GridBagConstraints();	
	//getContentPane().setLayout(gridbag);
	//c.fill = GridBagConstraints.BOTH;
	//c.gridheight = 2;
	//c.gridwidth = 1;

	//gridbag.setConstraints(messageLabel, c);
	//panel.add(messageLabel);
	//gridbag.setConstraints(okButton, c);
	//panel.add(okButton);

	//setContentPane(panel);
	//pack();
    }
    /**
    * Shows the error window.
    */
    public void show(){
	JOptionPane.showMessageDialog(null,_message,
	                              "HTML Generation Warning",
	                              JOptionPane.ERROR_MESSAGE);
    }
}

