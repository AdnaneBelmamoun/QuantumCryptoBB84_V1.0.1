package cryptoQuantique;

import java.util.*;



public class Analyseur
{
    /*
     * L'analyseur est une classe de traitement. elle s'occupe de stocker les
     * donn�es dans des vecteurs. les vecteurs ont une fonction differente
     * en fonction du r�le de la personne. (Emetteur, Recepteur ou Espion)
     *-------------
     * EMETTEUR :
     *-------------
     * Role du vdonneesFinales : stocke les photons qui ont �t� envoy�s
     *                            au Recepteur par le canal s�curis�.
     *
     * Role du vdonneesInclinaisons : stocke l'inclinaison du Recepteur pour
     *                                chaque photon recu par ce dernier.
     *
     * Role du vdonneesVerifiees : stocke vrai ou faux pour les photons qui
     *                      avaient une bonne inclinaison chez le Recepteur.
     *------------
     * RECEPTEUR :
     *------------
     * Role du vDonneesFinales : stocke les donn�es envoy�s par l'Emetteur
     *                           par le canal s�curis�.
     *
     * Role du vdonneesInclinaisons : stocke l'inclinaison du Recepteur pour
     *                                chaque photon recu.
     *
     * Role du vdonneesVerifiees : donn�es transmises par l'Emetteur. 
     *                            Ce qui permet de calculer la clef.
     *----------
     * ESPION :
     *----------
     * Role du vDonneesFinales : stocke les données interceptées qui étaient
     *                          à destination du Recepteur
     *
     * Role du vdonneesInclinaisons : stocke l'inclinaison prise au moment de
     *                              l'interception
     *
     * Role du vdonneesVerifiees : données interceptées avant l'arrivée au
     *                             l'Emetteur. Permet de calculer la clef.
     *  
     */

    String [] tabOrientation = {"droit", "incline"};
    Vector <Integer> vdonneesFinales;
    Vector <String> vdonneesInclinaisons;
    Vector <Boolean> vdonneesVerifiees;
    Traceur trace;
    
    public Analyseur(Traceur traceur)
    {  
        vdonneesInclinaisons = new Vector<String>();
        vdonneesVerifiees = new Vector<Boolean>();
        vdonneesFinales = new Vector<Integer>();
        trace = traceur;
    }

    public void emission(Impulsion impulse)
    {
        vdonneesFinales.add(impulse.photonImpulse.polarisation);
    }

    public void reception(Impulsion impulse)
    {
        Random indiceOrientation = new Random();
        int orientation = indiceOrientation.nextInt(2);
        int valeurLue = verifieinclinaison(impulse.photonImpulse.polarisation, tabOrientation[orientation]);

        vdonneesFinales.add(valeurLue);
        vdonneesInclinaisons.add(tabOrientation[orientation]);

        System.out.println(" En position " + tabOrientation[orientation] + " et lit la valeur " + valeurLue + "deg.");
        trace.addTrace(" En position " + tabOrientation[orientation] + " et lit la valeur " + valeurLue + "°.\r\n");
    }

    public int verifieinclinaison(int polarisation, String orientation)
    {
        Random indice = new Random();
        int alea = indice.nextInt(2);

        if( orientation.equals("droit") )
        {
            switch(polarisation)
            {
                case 0:
                    return 0;
                case 90:
                    return 90;
                case 45:
                    if( alea == 0 )
                    {
                        return 45;
                    }
                    return 135;
                case 135:
                    if( alea == 0 )
                    {
                        return 135;
                    }
                    return 45;
                default:
                    break;
            }
        }
        else
        {
            switch(polarisation)
            {
                case 0:
                    if( alea == 0 )
                    {
                        return 0;
                    }
                    return 90;
                case 90:
                    if( alea == 0 )
                    {
                        return 90;
                    }
                    return 0;
                case 45:
                    return 45;
                case 135:
                    return 135;
                default:
                    break;
            }
        }

        return 0; // Normalement on n'arrive jamais ici c'est juste pour �viter une erreur de compilation
    }
}