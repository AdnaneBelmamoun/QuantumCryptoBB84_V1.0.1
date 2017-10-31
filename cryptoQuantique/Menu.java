
package cryptoQuantique;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/*
 * Menu.java
 */



public class Menu extends javax.swing.JFrame
{
    public static final long serialVersionUID = 1L;
	public Parametres param = new Parametres();
    public APropos apropos = new APropos();
    public Traceur trace = new Traceur(param);
    private Desktop desktop;
    int nbImpulsion = 0;
    int tempo = 0;

    /** Creates new form Menu */
    public Menu()
    {super();
        initComponents();
        javax.swing.JSpinner.NumberEditor spinnerEditor = new javax.swing.JSpinner.NumberEditor(textNbImpulsion);
        textNbImpulsion.setEditor(spinnerEditor);
        spinnerEditor.getModel().setMaximum(2000);
        spinnerEditor.getModel().setStepSize(10);
        trace.setVisible(true);
        //setBackground(Color.yellow);
        setLocation(10, 30);
    }

    /** cette méthode est appelée avec le constructeur afin d'initialiser la forme 
     * Attention: ne modifiez en aucun  ce bout de code, car le contenu de cette
     * methode est automatiquement généré par l'editeur des formes.
     */

    private void initComponents() {

        panelGeneral = new javax.swing.JPanel();
        panelControle = new javax.swing.JPanel();
        labelNbImpulsion = new javax.swing.JLabel();
        labelVitesse1 = new javax.swing.JLabel();
        textNbImpulsion = new javax.swing.JSpinner();
        boutonGo = new javax.swing.JButton();
        sliderImpulse = new javax.swing.JSlider();
        labelVitesse2 = new javax.swing.JLabel();
        panelGraphique = new javax.swing.JPanel();
        labelImgAlice = new javax.swing.JLabel();
        labelTextAlice = new javax.swing.JLabel();
        barreProgression = new javax.swing.JProgressBar();
        labelTextEspion = new javax.swing.JLabel();
        labelImgEspion = new javax.swing.JLabel();
        labelImgBob = new javax.swing.JLabel();
        labelTextBob = new javax.swing.JLabel();
        labelInfo = new javax.swing.JLabel();
        labelCleBob2 = new javax.swing.JLabel();
        labelCleBob1 = new javax.swing.JLabel();
        labelCleAlice1 = new javax.swing.JLabel();
        labelCleAlice2 = new javax.swing.JLabel();
        labelCleCaroline1 = new javax.swing.JLabel();
        labelCleCaroline2 = new javax.swing.JLabel();
        barreMenu = new javax.swing.JMenuBar();
        menuFichier = new javax.swing.JMenu();
        optionNouvelleSimu = new javax.swing.JMenuItem();
        optionQuitter = new javax.swing.JMenuItem();
        menuOptions = new javax.swing.JMenu();
        optionParam = new javax.swing.JMenuItem();
        optionCrypto = new javax.swing.JMenuItem();
        optionPropos = new javax.swing.JMenuItem();
       

        //jScrollPane1.setViewportView(this);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulateur Cryptograhpie Quantique---------->Autheur: Belmamoun Adnane -- Master I.T 2010-2011");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBackground(Color.BLUE);
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
          
        	public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        labelNbImpulsion.setText("Nombre d'impulsions :");

        labelVitesse1.setText("Vitesse (impulsions / sec) :");

        textNbImpulsion.setValue(1000);

        boutonGo.setText("Commencer Simulation");
        boutonGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lanceImpulsion(evt);
            }
        });

        sliderImpulse.setMaximum(1000);
        sliderImpulse.setMinimum(1);
        sliderImpulse.setValue(100);
        sliderImpulse.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderImpulseStateChanged(evt);
            }
        });

        labelVitesse2.setText("100 imp / sec");

        javax.swing.GroupLayout panelControleLayout = new javax.swing.GroupLayout(panelControle);
        panelControle.setLayout(panelControleLayout);
        panelControleLayout.setHorizontalGroup(
            panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNbImpulsion, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelControleLayout.createSequentialGroup()
                        .addComponent(labelVitesse1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelControleLayout.createSequentialGroup()
                                .addComponent(textNbImpulsion, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boutonGo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sliderImpulse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelVitesse2, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                .addGap(341, 341, 341))
        );
        panelControleLayout.setVerticalGroup(
            panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNbImpulsion, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textNbImpulsion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boutonGo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelControleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelVitesse2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sliderImpulse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelVitesse1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelImgAlice.setIcon(new javax.swing.ImageIcon(getClass().getResource("zaynabInactif.png"))); // NOI18N

        labelTextAlice.setText("Zaynab");

        barreProgression.setForeground(new java.awt.Color(255, 0, 51));

        labelTextEspion.setText("Leila --> Espion [inactif]");

        labelImgEspion.setIcon(new javax.swing.ImageIcon(getClass().getResource("espionInactif.png"))); // NOI18N

        labelImgBob.setIcon(new javax.swing.ImageIcon(getClass().getResource("adnaneInactif.png"))); // NOI18N

        labelTextBob.setText("Adnane");

        labelInfo.setText("Génération en attente");

        javax.swing.GroupLayout panelGraphiqueLayout = new javax.swing.GroupLayout(panelGraphique);
        panelGraphique.setLayout(panelGraphiqueLayout);
        panelGraphiqueLayout.setHorizontalGroup(
            panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                .addGroup(panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGraphiqueLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelImgAlice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelGraphiqueLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(labelTextAlice)))
                        .addGroup(panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(barreProgression, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                                .addGap(144, 144, 144)
                                .addComponent(labelImgEspion))
                            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                                .addGap(148, 148, 148)
                                .addComponent(labelInfo)))
                        .addGap(26, 26, 26)
                        .addGroup(panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(labelImgBob))
                            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(labelTextBob))))
                    .addGroup(panelGraphiqueLayout.createSequentialGroup()
                        .addGap(316, 316, 316)
                        .addComponent(labelTextEspion, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(209, Short.MAX_VALUE))
        );
        panelGraphiqueLayout.setVerticalGroup(
            panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraphiqueLayout.createSequentialGroup()
                .addContainerGap()
             //   .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addGroup(panelGraphiqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGraphiqueLayout.createSequentialGroup()
                        .addComponent(labelImgBob)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTextBob))
                    .addGroup(panelGraphiqueLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(labelInfo)
                        .addGap(18, 18, 18)
                        .addComponent(barreProgression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelImgEspion))
                    .addGroup(panelGraphiqueLayout.createSequentialGroup()
                        .addComponent(labelImgAlice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTextAlice)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTextEspion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        labelCleBob2.setText("aucune");

        labelCleBob1.setText("Clef de Adnane :");

        labelCleAlice1.setText("Clef de Zaynab :");

        labelCleAlice2.setText("aucune");

        labelCleCaroline1.setText("Clef de Leila :");

        labelCleCaroline2.setText("aucune");

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCleCaroline1)
                    .addComponent(labelCleAlice1)
                    .addComponent(labelCleBob1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCleCaroline2)
                    .addComponent(labelCleAlice2)
                    .addComponent(labelCleBob2))
                .addContainerGap(624, Short.MAX_VALUE))
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelGraphique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panelGeneralLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {labelCleAlice1, labelCleBob1});

        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelControle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGraphique, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCleCaroline1)
                    .addComponent(labelCleCaroline2))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCleAlice2)
                    .addComponent(labelCleAlice1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCleBob1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCleBob2))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        menuFichier.setText("Fichier");

        optionNouvelleSimu.setIcon(new javax.swing.ImageIcon(getClass().getResource("new.png"))); // NOI18N
        optionNouvelleSimu.setText("Nouvelle simulation");
        optionNouvelleSimu.setToolTipText("Lancer une nouvelle simulation");
        optionNouvelleSimu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionNouvelleSimuActionPerformed(evt);
            }
        });
        menuFichier.add(optionNouvelleSimu);

        optionQuitter.setIcon(new javax.swing.ImageIcon(getClass().getResource("exit.png"))); // NOI18N
        optionQuitter.setText("Quitter");
        optionQuitter.setToolTipText("Fermer le simulateur");
        optionQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionQuitterActionPerformed(evt);
            }
        });
        menuFichier.add(optionQuitter);

        barreMenu.add(menuFichier);

        menuOptions.setText("Options");
        menuOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOptionsActionPerformed(evt);
            }
        });

        optionParam.setIcon(new javax.swing.ImageIcon(getClass().getResource("tools.png"))); // NOI18N
        optionParam.setText("Paramètres");
        optionParam.setToolTipText("Paramètres de la simulation");
        optionParam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionParamActionPerformed(evt);
            }
        });
        menuOptions.add(optionParam);

        optionCrypto.setIcon(new javax.swing.ImageIcon(getClass().getResource("cadenas.png"))); // NOI18N
        optionCrypto.setText("Voir Mon Rapport sur la Cryptographie Quantique");
        optionCrypto.setToolTipText("Pour mieux comprende");
        optionCrypto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionCryptoActionPerformed(evt);
            }
        });
        menuOptions.add(optionCrypto);

        optionPropos.setIcon(new javax.swing.ImageIcon(getClass().getResource("info.png"))); // NOI18N
        optionPropos.setText("A propos");
        optionPropos.setToolTipText("Au sujet de ce simulateur");
        optionPropos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionProposActionPerformed(evt);
            }
        });
        menuOptions.add(optionPropos);

        barreMenu.add(menuOptions);

        setJMenuBar(barreMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void lanceImpulsion(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lanceImpulsion
// TODO add your handling code here:

    final Emetteur Alice = new Emetteur("Mme", "Zaynab", trace);
    final Recepteur Bob = new Recepteur("M.", "Adnane", trace);
    final Espion Caroline = new Espion("Mme", "Leila", trace);

    java.awt.EventQueue.invokeLater(new Runnable()
    {
        public void run()
        {
            barreProgression.setValue(0);
            barreProgression.setIndeterminate(true);
            labelInfo.setText("Génération ds Clefs en cours ...");
            boutonGo.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    });

    try
    {
        nbImpulsion = Integer.parseInt(textNbImpulsion.getValue().toString());
    }
    catch (NumberFormatException e)
    {
    }

    genereEntete(param.espionChecked(), param.loisChecked(), nbImpulsion, sliderImpulse.getValue(), param.polar0_45(), param.polar90_135());
    tempo = ((nbImpulsion / sliderImpulse.getValue()) * 1000) / nbImpulsion;

    Thread boucle = new Thread(new Runnable()
    {
        public void run()
        {
            Impulsion impulse = null;

            for(int compteur = 0 ; compteur < nbImpulsion ; compteur++)
            {
                impulse = Alice.lanceImpulsion(param.loisChecked());

                if(impulse.nbPhotons != 0)
                {
                    if(param.espionChecked()) // Application de l'Espion s'il est coché
                    {
                        Caroline.recoitImpulsion(impulse);
                        impulse = Caroline.renvoiImpulsion(param.loisChecked());
                    }

                    Bob.RecoitImpulsion(impulse);
                }

                try
                {
                    // On endort le Thread pour temporiser et simuler le délai d'envoi
                    Thread.sleep(tempo);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Le Thread rend la main à  l'EDT pour que la couche graphique dispose de temps CPU
                Thread.yield();
            }

            /*
                Tous les photons ont été envoyés de Alice vers Bob.
                Cependant Bob a récuperé des valeurs qui ne sont pas forcément justes...
            */

            String transcrDroit = Integer.toString(param.polar0_45());
            String transcrIncline = Integer.toString(param.polar90_135());

            if(!param.espionChecked())
            {
                Bob.envoiReception(Alice);

                Alice.verifieValeurs();
                Alice.envoiValide(Bob);
            }
            else
            {
                Bob.envoiReception(Alice);
                System.out.println(" Leila(Espion) intercepte l'envoi de Bob(Adnane).");
                trace.addTrace(" Leila(Espion) intercepte l'envoi de Bob(Adnane).\r\n");

                Caroline.envoiReception(Alice);

                Alice.verifieValeurs();
                Alice.envoiValide(Caroline);

                Caroline.envoiValide(Bob);
                Caroline.genereClef(transcrDroit, transcrIncline);
            }

            Alice.genereClef(transcrDroit, transcrIncline);
            Bob.genereClef(transcrDroit, transcrIncline);

            labelInfo.setText("Génération terminée !");
            barreProgression.setIndeterminate(false);
            barreProgression.setValue(100);

            boutonGo.setEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            labelCleAlice2.setText(Alice.afficheClef());
            labelCleBob2.setText(Bob.afficheClef());

            if(param.espionChecked())
            {
                labelCleCaroline2.setText(Caroline.afficheClef());
            }

            if(param.espionChecked())
            {
                labelCleCaroline2.setText(Caroline.afficheClef());
                trace.addTrace(" Clef de leila(Espion) : " + Caroline.getClefRaw() + "\r\n");
            }
            
            // L'affichage en ligne des clefs dans le traceur 
            // est une opération nécessaire pour mieux pouvoir les comparer

            trace.addTrace("\r\n Clef de Zaynab    : " + Alice.getClefRaw() + "\r\n");
            trace.addTrace(" Clef de Adnane     : " + Bob.getClefRaw() + "\r\n");

            trace.addTrace("\r\n************************** FIN DE LA SIMULATION ************************\r\n\r\n");

            java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    pack();
                }
            });
        }
    });

    boucle.start();
}//GEN-LAST:event_lanceImpulsion

public void genereEntete(boolean espion, boolean lois, int nb_impulsion, int vitesse, int polar0_45, int polar90_135)
{
    trace.addTrace("************************* NOUVELLE SIMULATION ************************\r\n\r\n");

    trace.addTrace("   - " + nb_impulsion + " impulsions vont être générées à une vitesse de " + vitesse + " imp / sec\r\n");
    trace.addTrace("   - Les photons orientés à  0°  et 45°  seront interprétés en bits à :  " + polar0_45 + "\r\n");
    trace.addTrace("   - Les photons orientés à  90° et 135° seront interprétés en bits à :  " + polar90_135 + "\r\n");

    if(espion)
    {
        trace.addTrace("   - Espion : ACTIF\r\n");
    }
    else
    {
        trace.addTrace("   - Espion : INACTIF\r\n");
    }

    if(lois)
    {
        trace.addTrace("   - Respect des lois physiques : ACTIVE\r\n");
    }
    else
    {
        trace.addTrace("   - Respect des lois physiques : DESACTIVE\r\n");
    }

    trace.addTrace("\r\n");
}

private void optionQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionQuitterActionPerformed
    // TODO add your handling code here:
    param.dispose();
    apropos.dispose();
    System.exit(0);
}//GEN-LAST:event_optionQuitterActionPerformed

private void sliderImpulseStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderImpulseStateChanged
    // TODO add your handling code here:
      int value = sliderImpulse.getValue();
      
      String str = Integer.toString(value);
      labelVitesse2.setText(str + " impulsion / seconde");
}//GEN-LAST:event_sliderImpulseStateChanged

private void optionNouvelleSimuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionNouvelleSimuActionPerformed
    // TODO add your handling code here:
    final Integer i = new Integer(1000);

    java.awt.EventQueue.invokeLater(new Runnable()
    {
        public void run()
        {
            trace.videTextArea();
            labelInfo.setText("Attente de Génération des Clefs ");
            barreProgression.setIndeterminate(false);
            barreProgression.setValue(0);
            textNbImpulsion.setValue(i);
            labelVitesse2.setText("100 impulsion / seconde");
            sliderImpulse.setValue(100);
            labelCleAlice2.setText("aucune");
            labelCleBob2.setText("aucune");
            labelCleCaroline2.setText("aucune");
            boutonGo.setEnabled(true);

            pack();
        }
    });
}//GEN-LAST:event_optionNouvelleSimuActionPerformed

private void optionParamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionParamActionPerformed
    // TODO add your handling code here:
    param.setVisible(true);
}//GEN-LAST:event_optionParamActionPerformed

private void menuOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOptionsActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_menuOptionsActionPerformed

private void optionProposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionProposActionPerformed
    // TODO add your handling code here:
    apropos.setVisible(true);
}//GEN-LAST:event_optionProposActionPerformed

private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
    // TODO add your handling code here:
}//GEN-LAST:event_formFocusGained

private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
    // TODO add your handling code here:
    if(param.espionChecked())
    {
        labelTextEspion.setText("Leila : Espion [actif]");
        labelImgAlice.setIcon(new javax.swing.ImageIcon(getClass().getResource("zaynabActif.png")));
        labelImgBob.setIcon(new javax.swing.ImageIcon(getClass().getResource("adnaneActif.png")));
        labelImgEspion.setIcon(new javax.swing.ImageIcon(getClass().getResource("espionActif.png")));
    }
    else
    {
        labelTextEspion.setText("Leila : Espion [inactif]");
        labelImgAlice.setIcon(new javax.swing.ImageIcon(getClass().getResource("zaynabInactif.png")));
        labelImgBob.setIcon(new javax.swing.ImageIcon(getClass().getResource("adnaneInactif.png")));
        labelImgEspion.setIcon(new javax.swing.ImageIcon(getClass().getResource("espionInactif.png")));
    }
    if(param.traceChecked() && !trace.isVisible())
    {
        trace.setVisible(true);
    }
    else if(!param.traceChecked() && trace.isVisible())
    {
        trace.setVisible(false);
    }
}//GEN-LAST:event_formWindowGainedFocus

private void optionCryptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionCryptoActionPerformed
    // TODO add your handling code here:
    desktop = Desktop.getDesktop();
    
    String fileName = "src/Rapport.pdf";
    File file = new File(fileName);

    try
    {
        desktop.open(file);
    }
    catch (IOException ex)
    {
        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_optionCryptoActionPerformed

    /**
    * L'application commence d'ici
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
             Menu m =   new Menu();
            Image titreim = (new javax.swing.ImageIcon(getClass().getResource("labelApplication.png"))).getImage();
            m.setIconImage(titreim);
            //m.setForeground(Color.GREEN);
             m.setVisible(true);m.setResizable(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barreMenu;
    private javax.swing.JProgressBar barreProgression;
    private javax.swing.JButton boutonGo;
    private javax.swing.JLabel labelCleAlice1;
    private javax.swing.JLabel labelCleAlice2;
    private javax.swing.JLabel labelCleBob1;
    private javax.swing.JLabel labelCleBob2;
    private javax.swing.JLabel labelCleCaroline1;
    private javax.swing.JLabel labelCleCaroline2;
    private javax.swing.JLabel labelImgAlice;
    private javax.swing.JLabel labelImgBob;
    private javax.swing.JLabel labelImgEspion;
    private javax.swing.JLabel labelInfo;
    private javax.swing.JLabel labelNbImpulsion;
    private javax.swing.JLabel labelTextAlice;
    private javax.swing.JLabel labelTextBob;
    private javax.swing.JLabel labelTextEspion;
    private javax.swing.JLabel labelVitesse1;
    private javax.swing.JLabel labelVitesse2;
    private javax.swing.JMenu menuFichier;
    private javax.swing.JMenu menuOptions;
    private javax.swing.JMenuItem optionCrypto;
    private javax.swing.JMenuItem optionNouvelleSimu;
    private javax.swing.JMenuItem optionParam;
    private javax.swing.JMenuItem optionPropos;
    private javax.swing.JMenuItem optionQuitter;
    private javax.swing.JPanel panelControle;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelGraphique;
    private javax.swing.JSlider sliderImpulse;
    private javax.swing.JSpinner textNbImpulsion;

    // End of variables declaration//GEN-END:variables

}
