
/*
 * Traceur.java
 */

package cryptoQuantique;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Image;
import java.io.FileWriter;
import java.io.IOException;

public class Traceur extends javax.swing.JFrame{
	
	public static final long serialVersionUID = 1L;
	private Parametres param;

    /** Creates new form Traceur */
    public Traceur(Parametres param)
    {
        initComponents();
        setLocation(800,100);
        this.param = param;
    }

    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaTrace = new javax.swing.JTextArea();
        boutonExport = new javax.swing.JButton();
        boutonMasquer = new javax.swing.JButton();
        boutonClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Traceur des Èchanges de Photons --> BELMAMOUN ADNANE");
        Image titreim = (new javax.swing.ImageIcon(getClass().getResource("labelApplication.png"))).getImage();
        setIconImage(titreim);
        setBackground(Color.CYAN);
        setResizable(false);

        textAreaTrace.setColumns(20);
        textAreaTrace.setEditable(false);
        textAreaTrace.setRows(5);
        jScrollPane1.setViewportView(textAreaTrace);

        boutonExport.setText("Exporter");
        boutonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonExportActionPerformed(evt);
            }
        });

        boutonMasquer.setText("Fermer");
        boutonMasquer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonMasquerActionPerformed(evt);
            }
        });

        boutonClear.setText("Effacer");
        boutonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(boutonExport, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boutonClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boutonMasquer, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {boutonClear, boutonExport, boutonMasquer});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonClear)
                    .addComponent(boutonMasquer)
                    .addComponent(boutonExport))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boutonMasquerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonMasquerActionPerformed
        // TODO add your handling code here:
        param.setTraceurOff();
        this.setVisible(false);
    }//GEN-LAST:event_boutonMasquerActionPerformed

    private void boutonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonExportActionPerformed
        // TODO add your handling code here:

        String save = textAreaTrace.getText();

        FileDialog fenetre = new FileDialog(this, "Choisissez où éxporter", FileDialog.SAVE );
        fenetre.setVisible(true);

        if((fenetre.getDirectory() != null) && (fenetre.getFile() != null))
        {
            try
            {
                FileWriter ecrire = new FileWriter(fenetre.getDirectory() + fenetre.getFile());
                ecrire.write(save);
                ecrire.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        return;
    }//GEN-LAST:event_boutonExportActionPerformed

private void boutonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonClearActionPerformed
// TODO add your handling code here:
    this.videTextArea();
}//GEN-LAST:event_boutonClearActionPerformed

    /**
    * @param args the command line arguments
    */
    public void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Traceur(param).setVisible(true);
            }
        });
    }

    public void videTextArea()
    {
        textAreaTrace.setText("");

        return;
    }

    public void addTrace(String chaine)
    {
        textAreaTrace.append(chaine);
        textAreaTrace.setCaretPosition(textAreaTrace.getDocument().getLength());

        return;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonClear;
    private javax.swing.JButton boutonExport;
    private javax.swing.JButton boutonMasquer;
    @SuppressWarnings("unused")
	private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textAreaTrace;
    // End of variables declaration//GEN-END:variables

}
