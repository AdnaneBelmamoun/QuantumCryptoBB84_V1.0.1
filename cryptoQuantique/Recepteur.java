package cryptoQuantique;

public class Recepteur extends Personne
{
    public String role;

    Recepteur(String genre, String nom, Traceur trace)
    {
        super(genre, nom, trace);
    }
    
    public void RecoitImpulsion(Impulsion impulse)
    {
        System.out.print(nom + " Reçoit l'impulsion");
        trace.addTrace(" " + nom + " Reçoit l'impulsion");
        analyser.reception(impulse);
    }

    public void afficheReception()
    {
        System.out.println(analyser.vdonneesFinales.toString());
    }

    public void envoiReception(Emetteur E)
    {
        // Le Recepteur transmet Ã  l'Emetteur les valeurs "droit" et "inclinÃ©" qu'il a choisi tout au long des envois
        E.analyser.vdonneesInclinaisons = this.analyser.vdonneesInclinaisons ;
        System.out.println(nom + " Envoi ses orientations pour les faire contrôler par " + E.nom + ".");
        trace.addTrace(" " + nom + " Envoi ses orientations pour les faire contrôler par " + E.nom + ".\r\n");
    }
}
