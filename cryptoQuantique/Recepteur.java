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
        System.out.print(nom + " Re�oit l'impulsion");
        trace.addTrace(" " + nom + " Re�oit l'impulsion");
        analyser.reception(impulse);
    }

    public void afficheReception()
    {
        System.out.println(analyser.vdonneesFinales.toString());
    }

    public void envoiReception(Emetteur E)
    {
        // Le Recepteur transmet à l'Emetteur les valeurs "droit" et "incliné" qu'il a choisi tout au long des envois
        E.analyser.vdonneesInclinaisons = this.analyser.vdonneesInclinaisons ;
        System.out.println(nom + " Envoi ses orientations pour les faire contr�ler par " + E.nom + ".");
        trace.addTrace(" " + nom + " Envoi ses orientations pour les faire contr�ler par " + E.nom + ".\r\n");
    }
}
