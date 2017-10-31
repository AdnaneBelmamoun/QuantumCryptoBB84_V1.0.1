package qrypto.protocols;


import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Random;
/******************************************************
 * File: BigIntHash.java
 * created 15-May-00 3:14:19 PM by haddock
 */


public class BigIntHash
{


   protected int 	s;          // Security parameter, the length of the hashcode returned.
   protected int	n;          // Length of the BitSet that is to be hashed.
   protected BigInteger c[];        // Array of bigintegers. Each entry represents one column in the hashing matrix
   protected BigInteger seedVect;   // The seed vector is the initial vector in the array.
   protected BigInteger shiftVect;  // The shift vector contains the bits being shifted into place
				    // as we construct the matrix by right-shifting the seed vector

				    // Rule of thumb when passing parameters:
				    // The security parameter "s" is the size of the final output.
				    // It is also the size of the Shift Vector
				    // The size parameter "n" is the size of the input.
				    // It is also the size of the Seed Vector

   protected BigInteger t_in;  // Latest input to hashfunction
   protected BigInteger t_out; // Latest output from hashfunction


   @SuppressWarnings("unused")
public static void main(String[] args)
   {
     BigInteger bi_in, bi_out1, bi_out2;
     int        seed_size,shift_size;
     BigInteger seed,shift;
     BigIntHash hash1,hash2;

     hash1 = new BigIntHash(10,20);
     seed_size = hash1.getSeedSize();
     seed = hash1.getSeedVector();
     shift_size = hash1.getShiftSize();
     shift = hash1.getShiftVector();

     hash2 = new BigIntHash(shift_size, seed_size, shift, seed);

     bi_in = new BigInteger(20, new Random());

     hash1.output(System.out,2);

     bi_out1 = hash1.doHash(bi_in);
     bi_out2 = hash2.doHash(bi_in);

     System.out.println("Hash1");
     hash1.output(System.out,2);
     System.out.println("\n\nHash2");
     hash2.output(System.out,2);

   }



   // Constructors
   public BigIntHash(int s, int n, BigInteger shift_vector, BigInteger seed_vector)
   {
       initHash(s, n, shift_vector, seed_vector);
   }

   public BigIntHash(int s, int n, byte shift_bytes[], byte seed_bytes[])
   {
       BigInteger shift_vect = new BigInteger(shift_bytes);
       BigInteger seed_vect = new BigInteger(seed_bytes);
       initHash(s, n, shift_vect, seed_vect);
   }

   public BigIntHash(int s, int n)
   {
       newHash(s,n);
   }

   // newHash initializes the hash matrix randomly.
   public void newHash(int s, int n)
   {
       BigInteger	seed_vect;
       BigInteger	shift_vect;
       Random		rndSrc=new Random();

       seed_vect=new BigInteger(n, rndSrc);
       shift_vect=new BigInteger(s, rndSrc);
       initHash(s,n,shift_vect,seed_vect);

   };

   // initHash initializes the hash matrix with a hash function given by a seedVector and a shift vector.
   public void initHash(int s, int n, BigInteger shift_vect, BigInteger seed_vect)
   {
       int		column;

       this.n=n;
       this.s=s;
       this.c=new BigInteger[s];

       this.seedVect=seed_vect;
       this.shiftVect=shift_vect;
       c[0]=seed_vect;
       for (column=1; column<s; column++)
       {
	   seed_vect = seed_vect.shiftRight(1);    // Leftmost bit is now 0. We need to put one of the random
				                   // bits from shift_bits in that position.
	   if (shift_vect.testBit(column)) seed_vect = seed_vect.setBit(n-1);
	   else seed_vect = seed_vect.clearBit(n-1);
	   c[column]=seed_vect;
       }
   }

   // Methods for extracting the hash function. Constructor makes this always, so there's no chance
   // of accidentally referencing null objects.
   public int getSeedSize()
   {
       return this.n;
   }


   public int getShiftSize()
   {
       return this.s;
   }

   public BigInteger getSeedVector()
   {
       return this.seedVect;
   }


   public BigInteger getShiftVector()
   {
       return this.shiftVect;
   }
   public BigInteger getLastInput()
   {
       return this.t_in;
   }

   public BigInteger getLastOutput()
   {
       return this.t_out;
   }
   

   // doHash hashes a given BigInteger bitvector with the given hash function.
   @SuppressWarnings("unused")
public BigInteger doHash(BigInteger bitvector)
   {
       int		column,row;
       BigInteger	andVect;
       int              numBits;


       // Although hashfunctions N->S of arbitrary size reduction can be constructed, it
       // may not be desirable (for performance reaasons) to apply the hashing procedure
       // to e.g. a million bit BigInt.
       // Pending further investigation, we might anticipate this function to be called
       // with BigInts potentially larger than the n specified in the constructor.
       // Different solutions to this problem could be:
       // - fold the vector using xor until the desired size has been achieved, then hash.
       // - apply the hash on n-sized chunks of the input and xor the output's together.

       t_in  = bitvector;  // Store the latest input value
       t_out = new BigInteger(s,new Random());  // We just want an s-bit integer

       for (column=0; column<s; column++)
       {
	   andVect = bitvector.and(c[column]);  // "and", handily enough, corresponds to multiplication mod 2
	   numBits = andVect.bitCount();        // If parity of numBits is even, we clear the bit, otherwise set it.
	   if ((numBits & 1)==1) t_out = t_out.setBit(column);
	   else t_out = t_out.clearBit(column);
       }
       return t_out;
   };

   // doHash hashes a given boolean[] bitvector with the given hash function.
   public boolean[] doHash(boolean[] boolvector)
   {
       int        i;
       BigInteger bitvector_in  = new BigInteger(boolvector.length,new Random());
       BigInteger bitvector_out = new BigInteger(boolvector.length,new Random());
       //boolean[]  hashedboolvector = new boolean[boolvector.length];
       boolean[] hashedboolvector = new boolean[s];//corrige par moi calisse
       
       for (i=0; i<boolvector.length; i++)
       {
	 if (boolvector[i]) bitvector_in=bitvector_in.setBit(i);
	 else bitvector_in=bitvector_in.clearBit(i);
       }
       bitvector_out = doHash(bitvector_in);
       for (i=0; i<s; i++){//corrige par moi ciboire d'austi.
	 hashedboolvector[i] = bitvector_out.testBit(i);
       }
       return hashedboolvector;
   };

   public void output(PrintStream strm, int radix)
   {
     strm.println("SEEDVECTOR:  ("+this.seedVect.bitLength()+") "+this.seedVect.toString(radix));
     strm.println("SHIFTVECTOR: ("+this.shiftVect.bitLength()+") "+this.shiftVect.toString(radix));
     if (this.t_in!=null)
       strm.println("LAST IN:     ("+this.t_in.bitLength()+") "+this.t_in.toString(radix));
     if (this.t_out!=null)
       strm.println("LAST OUT:    ("+this.t_out.bitLength()+") "+this.t_out.toString(radix));
     strm.println("\n\nHash Matrix Transposed:");
     for (int i=0; i<s; i++)
       strm.println("("+this.c[i].bitLength()+") "+this.c[i].toString(radix));
   }
}
