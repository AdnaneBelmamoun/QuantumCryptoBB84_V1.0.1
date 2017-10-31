package cryptoQuantique;



public class Personne
{
    public String genre;
    public String nom;
    public String clef = "";
    public String clef_raw = ""; // Cette clef ne sera pas formatée  <br>
    public int compteurBR = 1; // Variables qui compte le nombre de utilisé pour formater la clef
    public Analyseur analyser;
    public Traceur trace;

    public Personne(String genre, String nom, Traceur traceur)
    {
        this.genre = genre;
        this.nom = nom;
        analyser = new Analyseur(traceur);
        trace = traceur;
    }

    public void genereClef(String _0_45, String _90_135)
    {
        int checkLength = 85; // Variable "compteur" pour le formatage de la chaine en HTML

        for (int i = 0 ; i < this.analyser.vdonneesVerifiees.size() ; i++)
        {
            if(this.analyser.vdonneesVerifiees.elementAt(i) == true)
            {
                /*
                On garde uniquement les photons ayant été correctement reçus
                par le recepteur.
                */

                if (clef.length() > checkLength)
                {
                    clef += "<br>";
                    compteurBR++;
                    checkLength += 90;
                }

                if( (this.analyser.vdonneesFinales.elementAt(i) == 0) || (this.analyser.vdonneesFinales.elementAt(i) == 45) )
                {
                    clef += _0_45;
                    clef_raw += _0_45;
                }
                else
                {
                    clef += _90_135;
                    clef_raw += _90_135;
                }
            }
        }

        clef += "<br>";

        System.out.println(nom + " génére sa clef.");
        trace.addTrace(" " + nom + " génére sa clef.\r\n");
    }

    public String getClefRaw()
    {
        return clef_raw;
    }

    public String afficheClef()
    {
        if(this.clef_raw.length() != 0) // Si la clef raw n'est pas vide, la clef n'est pas vide
        {
            return "<html><body>" + this.clef + " (" + (this.clef.length()-compteurBR*4) +" bits)</body></html>";
            // compteurBR*4 car un <br> prend 4 caractÃ¨res dans la chaÃ®ne
        }
        else
        {
            return "<html><body>aucune<br>(0 bit)</html></body>";
        }
    }
}
