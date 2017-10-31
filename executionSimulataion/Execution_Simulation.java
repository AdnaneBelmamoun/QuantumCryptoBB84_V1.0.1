package executionSimulataion;
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
public class Execution_Simulation {

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		(new Menu_Simulateur_Cryptographie_Quantique()).show();

	}

}
