package cryptoQuantique;

/*
 * L'analyseur est une classe de traitement. elle s'occupe de stocker les
 * données dans des vecteurs. les vecteurs ont une fonction differente
 * en fonction du rôle de la personne. (Emetteur, Recepteur ou Espion)
 *-------------
 * EMETTEUR :
 *-------------
 * Role du vdonneesFinales : stocke les photons qui ont été envoyés
 *                            au Recepteur par le canal sécurisé.
 *
 * Role du vdonneesInclinaisons : stocke l'inclinaison du Recepteur pour
 *                                chaque photon recu par ce dernier.
 *
 * Role du vdonneesVerifiees : stocke vrai ou faux pour les photons qui
 *                      avaient une bonne inclinaison chez le Recepteur.
 *------------
 * RECEPTEUR :
 *------------
 * Role du vDonneesFinales : stocke les données envoyés par l'Emetteur
 *                           par le canal sécurisé.
 *
 * Role du vdonneesInclinaisons : stocke l'inclinaison du Recepteur pour
 *                                chaque photon recu.
 *
 * Role du vdonneesVerifiees : données transmises par l'Emetteur. 
 *                            Ce qui permet de calculer la clef.
 *----------
 * ESPION :
 *----------
 * Role du vDonneesFinales : stocke les donnÃ©es interceptÃ©es qui Ã©taient
 *                          Ã  destination du Recepteur
 *
 * Role du vdonneesInclinaisons : stocke l'inclinaison prise au moment de
 *                              l'interception
 *
 * Role du vdonneesVerifiees : donnÃ©es interceptÃ©es avant l'arrivÃ©e au
 *                             l'Emetteur. Permet de calculer la clef.
 *  
 */
public class Emetteur extends Personne
{
    public String role;

    Emetteur(String genre, String nom, Traceur trace)
    {
        super(genre, nom, trace);
    }

    public Impulsion lanceImpulsion(boolean respectLois)
    {
        Impulsion impulse = new Impulsion(respectLois);

        if (impulse.nbPhotons != 0)
        {
            analyser.emission(impulse);
            System.out.println(nom + " Emet une impulsion orientée à : " + impulse.photonImpulse.polarisation + "deg.");
            trace.addTrace(" " + nom + " Emet une impulsion orientée à :  " + impulse.photonImpulse.polarisation + "Â°.\r\n");
        }
        else
        {
            System.out.println(nom + " Emet une impulsion vide.");
            trace.addTrace(" " + nom + " Emet une impulsion vide.\r\n");
        }
        
        return impulse;
    }

    public void afficheEmission()
    {
        System.out.println(analyser.vdonneesFinales.toString());
    }

    public void verifieValeurs()
    {
        // On regarde quelles sont les valeurs vraie des fausses
        for(int i = 0 ; i < analyser.vdonneesFinales.size() ; i++)
        {
            if(analyser.vdonneesFinales.elementAt(i) == 0 || analyser.vdonneesFinales.elementAt(i) == 90)
            { 
                // Si le photon envoyÃ© est droit
                if(analyser.vdonneesInclinaisons.elementAt(i).equals("droit"))
                {
                    // La reception est bonne
                    analyser.vdonneesVerifiees.insertElementAt(true, i);
                }
                else
                {
                    analyser.vdonneesVerifiees.insertElementAt(false, i);
                }
            }
            else
            {
                // Si le photon envoyé est incliné
                if(analyser.vdonneesInclinaisons.elementAt(i).equals("incline"))
                {
                    // La reception est bonne
                    analyser.vdonneesVerifiees.insertElementAt(true, i);
                }
                else
                {
                    analyser.vdonneesVerifiees.insertElementAt(false, i);
                }
            }
        }

        System.out.println(nom + " verifie les donnees.");
        trace.addTrace(" " + nom + " vérifie les données.\r\n");
    }

    public void envoiValide (Personne R)
    {
        R.analyser.vdonneesVerifiees = this.analyser.vdonneesVerifiees;
        System.out.println(nom + " envoie les resultats des valeurs controlees.");
        trace.addTrace(" " + nom + " envoie les résultats des valeurs contrôlées.\r\n");
    }
}
