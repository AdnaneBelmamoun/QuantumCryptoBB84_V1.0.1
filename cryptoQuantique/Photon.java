package cryptoQuantique;

import java.util.*;

public class Photon
{
    public int polarisation;
    public boolean isDoublon;
    
    public Photon(boolean doublon)
    {
        int [] tabPolar = {0,45,90,135};
        int polarRandom;
        
        Random impulsion = new Random();
        polarRandom = impulsion.nextInt(4);
        
        polarisation = tabPolar[polarRandom];
        isDoublon = doublon;
    }
}
