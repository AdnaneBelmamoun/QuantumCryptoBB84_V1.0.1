package executionSimulataion;
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
public class Execution_Simulation {

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		(new Menu_Simulateur_Cryptographie_Quantique()).show();

	}

}
