
package cryptoQuantique;


public class Espion extends Personne
{
    public String role;

    Espion(String genre, String nom, Traceur trace)
    {
        super(genre, nom, trace);
    }

    public void recoitImpulsion(Impulsion impulse)
    {
        System.out.print(nom + " intercepte l'impulsion");
        trace.addTrace(" " + nom + " intercepte l'impulsion");
        analyser.reception(impulse);
    }

    public Impulsion renvoiImpulsion(boolean respectLois)
    {
        Impulsion impulse = new Impulsion(respectLois);

        while (impulse.nbPhotons == 0)
        {
            impulse = new Impulsion(true);
        }

        System.out.println(nom + " Emet une impulsion pirate orient�e � :" + impulse.photonImpulse.polarisation + "deg.");
        trace.addTrace(" " + nom + " Emet une impulsion pirate orient�e � :� " + impulse.photonImpulse.polarisation + "°.\r\n");
        
        return impulse;
    }

    public void envoiReception(Emetteur E)
    {
        // Le Recepteur transmet à l'Emetteur les valeurs "droit" et "incliné" qu'il a choisi tout au long des envois
        E.analyser.vdonneesInclinaisons = this.analyser.vdonneesInclinaisons;
        System.out.println(nom + " Envoi ses orientations pour contr�le par " + E.nom + ".");
        trace.addTrace(" " + nom + " Envoi ses orientations pour contr�le par " + E.nom + ".\r\n");
    }

    public void envoiValide (Personne R)
    {
        R.analyser.vdonneesVerifiees = this.analyser.vdonneesVerifiees;
        System.out.println(nom + " L'Espion Int�rcepte les valeurs control�es et fabrique sa clef.");
        System.out.println(nom + " L'Espion Fait un transfert des valeurs contr�l�es jusqu'� " + R.nom + ".");
        trace.addTrace(" " + nom + " L'Espion Intercepte les valeurs contr�l�es et fabrique sa clef.\r\n");
        trace.addTrace(" " + nom + " L'Espion Fait un transfert des valeurs contr�l�es jusqu'� " + R.nom + ".\r\n");
    }
}