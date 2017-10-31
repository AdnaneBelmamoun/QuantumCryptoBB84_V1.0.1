/*
 * PreMenu.java
 */

package cryptoQuantique;

public class PreMenu extends javax.swing.JFrame {

   public static final long serialVersionUID = 1L;
	/** Creates new form PreMenu */
    public PreMenu() {
        initComponents();
        panelRoles.setVisible(false);
        pack();
        setLocation(350,250);
        
    }

   
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        boutonLocal = new javax.swing.JButton();
        boutonReseau = new javax.swing.JButton();
        labelFonctionnement = new javax.swing.JLabel();
        panelRoles = new javax.swing.JPanel();
        boutonEmetteur = new javax.swing.JButton();
        boutonRecepteur = new javax.swing.JButton();
        boutonEspion = new javax.swing.JButton();
        labelRoles = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mode de fonctionnement");
        setResizable(false);

        boutonLocal.setText("Local");
        boutonLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonLocalActionPerformed(evt);
            }
        });

        boutonReseau.setText("Réseau");
        boutonReseau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonReseauActionPerformed(evt);
            }
        });

        labelFonctionnement.setText("Sélectionner le mode de fonctionnement :");

        boutonEmetteur.setText("Emetteur");

        boutonRecepteur.setText("Déstinataire");
        boutonRecepteur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonRecepteurActionPerformed(evt);
            }
        });

        boutonEspion.setText("Espion");

        labelRoles.setText("Choisir parmi les rôles disponibles :");

        javax.swing.GroupLayout panelRolesLayout = new javax.swing.GroupLayout(panelRoles);
        panelRoles.setLayout(panelRolesLayout);
        panelRolesLayout.setHorizontalGroup(
            panelRolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRolesLayout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(boutonEmetteur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boutonRecepteur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boutonEspion)
                .addGap(47, 47, 47))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRolesLayout.createSequentialGroup()
                .addContainerGap(98, Short.MAX_VALUE)
                .addComponent(labelRoles)
                .addGap(94, 94, 94))
        );

        panelRolesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {boutonEmetteur, boutonEspion, boutonRecepteur});

        panelRolesLayout.setVerticalGroup(
            panelRolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRolesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelRoles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(panelRolesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonEmetteur)
                    .addComponent(boutonRecepteur)
                    .addComponent(boutonEspion))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRoles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(labelFonctionnement)
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addComponent(boutonLocal)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(boutonReseau)
                .addGap(102, 102, 102))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {boutonLocal, boutonReseau});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFonctionnement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boutonLocal)
                    .addComponent(boutonReseau))
                .addGap(18, 18, 18)
                .addComponent(panelRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void boutonLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonLocalActionPerformed
    // TODO add your handling code here:
    this.setVisible(false);
    new Menu().setVisible(true);
}//GEN-LAST:event_boutonLocalActionPerformed

private void boutonRecepteurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonRecepteurActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_boutonRecepteurActionPerformed

private void boutonReseauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonReseauActionPerformed
    // TODO add your handling code here:
    panelRoles.setVisible(true);
    pack();
}//GEN-LAST:event_boutonReseauActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boutonEmetteur;
    private javax.swing.JButton boutonEspion;
    private javax.swing.JButton boutonLocal;
    private javax.swing.JButton boutonRecepteur;
    private javax.swing.JButton boutonReseau;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelFonctionnement;
    private javax.swing.JLabel labelRoles;
    private javax.swing.JPanel panelRoles;
    // End of variables declaration//GEN-END:variables

}
