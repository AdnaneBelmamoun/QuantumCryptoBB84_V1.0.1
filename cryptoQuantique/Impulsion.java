package cryptoQuantique;

import java.util.*;


public class Impulsion
{
    public int nbPhotons; // 0, 1, ou 2 (2 correspondant à  2 photons ou plus)
    public Photon photonImpulse;

    public Impulsion(boolean respectLois)
    {
        if(respectLois) // On respecte les lois physiques et mathématiques (loi de Poisson)
        {
            nbPhotons = genereImpulsion();

            if(nbPhotons == 1)
            {
                photonImpulse = new Photon(false);
            }
            else if(nbPhotons == 2)
            {
                photonImpulse = new Photon(true);
            }

            // else, il y a 0 photons dans l'impulsion
        }

        else // On fait une simulation parfaite dans laquelle toutes les impulsions ont un unique photon
        {
            nbPhotons = 1;

            photonImpulse = new Photon(false);
        }
    }
    
    public int genereImpulsion()
    {
        /*
        On ne peut pas décider du nombre de photons par impulsion mais seulement de sa moyenne.
        Sa rÃ©partition suit la loi de Poisson.
        On choisit alors une moyenne  = 0.1 photons par impulsion
         
            C'est à dire que 90 % des impulsions sont vides, 
            9 % contiennent un photon et
            1 % en contiennent plus de deux.
        */
        
        // Variables utiles
        int proba;
        Random impulsion = new Random();
        
        proba = impulsion.nextInt(100);
        
        if(proba == 0)
        {
            return 2;
        }
        if(proba <= 9) // Car si on se trouve ici c'est que proba != 0
        {
            return 1;
        }
        
        return 0;
    }
    
    public String toString()
    {
        StringBuffer strBuff = new StringBuffer();
        
        strBuff.append("Cette impulsion contient ");
        
        if(nbPhotons == 0)
        {
            strBuff.append("0 photon");
        }
        else if(nbPhotons == 1)
        {
            strBuff.append("1 photon. Polarisation du photon : " + photonImpulse.polarisation + "Â°");
        }
        else
        {
            strBuff.append("2 (ou plus) photons. Polarisation des photons : " + photonImpulse.polarisation + "Â°");
        }
                
        strBuff.append(".\n");
        
        return strBuff.toString();
    }
}