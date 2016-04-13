import java.io.*;
import java.util.*;
import java.lang.*;

public class ps2
{
	/* Cipher-text attack using a known corpus of text
		1. Read in text
		2. Construct bigram with keys being 2 character and values in the set of [0, 1]	which are the probabilities of those two chars appearing
		3. Construct key f using random permutation of characters
		4. Construct Pl(f) by multiplying together M[f(Ci), f(Ci+1)] from beginning of cipher to end of cipher	
	*/

	Hashtable <String, Integer> bigram;
	Hashtable <String, Integer> cipher;
	char [] f;
	String c;
	public ps2(String ciphertext)
	{
		bigram = new Hashtable <String, Integer>();
		cipher = new Hashtable <String, Integer>();
		c = "";
		f = new char [27];
		int alphabet = 97;
		
		//initialize f
		for (int t = 1; t < f.length; t++)
		{
			f[t] = (char)alphabet;
			//System.out.println("Inserted " + f[t] + " into " + t);
			alphabet++;
		}
		 
		//shuffle f
		Random rand = new Random ();
		for(int a = f.length - 1; a >= 1; a--)
		{
			int rshuffle = rand.nextInt(a + 1) + 1;
			char temp = f[a];
			f[a] = f[rshuffle];
			f[rshuffle] = temp;
		}//end for

		//Initialize cipher string
		try{
			File cfile = new File(ciphertext);
			Scanner sc = new Scanner(cfile);
			while(sc.hasNext())
			{
				c += sc.next() + " ";
			}
		}
		catch (FileNotFoundException e){
			System.out.println("File not Found");
		}  
	}//end constructor


	public double generatePlausibility(String ciphertext, char [] key)
	{
		double plausibility = 0.00;
		double p = 0.00;
		String m = "";
		for (int cip = 0; cip < c.length() - 1; cip ++)
		{
			String c1 = c.substring(cip, cip + 1);
			String c2 = c.substring(cip + 1, cip + 2);
			
			//Converting cipher text into probabilities using bigram and key f
			if (c1.matches("[a-z]") && c2.matches("[a-z]"))
			{
				int toAlphabet1 = ((int) c1.charAt(0)) - 96;
				int toAlphabet2 = ((int) c2.charAt(0)) - 96;
				m = String.valueOf(key[toAlphabet1]) + String.valueOf(key[toAlphabet2]);
			}
			else if (c1.matches("[a-z]") && !c2.matches("[a-z]")) 
			{
				int toAlphabet1 = ((int) c1.charAt(0)) - 96;
				m = String.valueOf(key[toAlphabet1]) + c2;
			}
			else if (!c1.matches("[a-z]") && c2.matches("[a-z]"))
			{
				int toAlphabet2 = ((int) c2.charAt(0)) - 96;
				m = c1 + String.valueOf(key[toAlphabet2]);
			}
			else 
			{
				m = c1 + c2;
			}

			//System.out.println("C1 is " + c1 + " C2 is " + c2);

			//Now convert m into a probability value
			if(bigram.containsKey(m))
			{
				//System.out.println("Value of " + m + " is " + (double)bigram.get(m)/bigram.size());
				p = (double)bigram.get(m)/bigram.size();
			}
			else
			{
				//System.out.println("Not in hash table: " + (double)1/bigram.size());
				p = (double)1/bigram.size();
			}

			plausibility += Math.log(p);
			//System.out.println("Plausiblity is now: " + plausibility);
		}//end  for(cip...
			
		return plausibility;
	}//end generatePlausibility


	//Create f* by swapping two points in f and generate plausibility of each
	//Take f* if plausibility is greater than f and if not flip random coin
	public void runDiaconis(int iteration)
	{
		for (int i = 0; i < iteration; i++)
		{
			char [] f_star = new char [27];
			System.arraycopy(f, 0, f_star, 0, 27);
			Random chooseSwap = new Random();
			int swap = chooseSwap.nextInt(f_star.length) ;
			int swap2 = chooseSwap.nextInt(f_star.length) ;

			//Random permutation creates new f* to compare against f
			char temp = f_star[swap];
			f_star[swap] = f_star[swap2];
			f_star[swap2] = temp;
			//System.out.println("Switched " + swap + " with " + swap2);

			double f1 = generatePlausibility("cipher.txt", f_star);
			double f2 = generatePlausibility("cipher.txt", f);
				
			//if Pl(f*) is better than original Pl(f), then take the f*
			//else flip biased coin based on Pl(f*)/Pl(f) ratio to decide whether to take f* or f
			if (f1 > f2)
			{
				temp = f[swap];
				f[swap] = f[swap2];
				f[swap2] = temp; 
			}

			//Flip biased coin and decide on either f* or f
			else
			{
				//if coin flip is true, convert to f*
				if(flipBiasedCoin(f1/f2))
				{
					temp = f[swap];
					f[swap] = f[swap2];
					f[swap2] = temp;	
				}
			}//end else	
		}//end for
		//System.out.println(Arrays.toString(f));
	}//end runDiaconis

	public boolean flipBiasedCoin(double ratio)
	{
		boolean heads = true;
		double RAND_MAX = 32767;
		Random rand = new Random();
		double r = RAND_MAX * rand.nextDouble() % (double)RAND_MAX;
		return ( r <= ratio );
	}//end flipBaiasedCoin
	
	public void exchangeLetters()
	{
		String final_message = "";
		for(int i = 0; i < c.length(); i++)
		{
			char c1 = c.charAt(i);
			int char_value = (int) c1;
			if(char_value >= 97 && char_value <= 172)
			{
				final_message += f[char_value - 96];
			}
			else
			{
				final_message += c1;
			}
			
		}//end for
		System.out.println(final_message);
	}//end exchangeLetters

	public void loadHashtable(String text) {
		String s = "";
		File file = new File(text);
		try{
			Scanner sc = new Scanner(file);
			
			//Making bigram for corpus input
			while (sc.hasNext())
                	{
				s += sc.next() + " ";
                	}
			//System.out.println(s);
			for(int i = 0; i < s.length() - 1; i++)
			{
				String two = s.substring(i, i + 2);
				if(bigram.containsKey(two))
				{
					bigram.put(two, bigram.get(two) + 1);
				}
				else 
				{
					bigram.put(two, 1);
				}
			}
			//System.out.println(bigram);
			
			//Making bigram for cipher
			/*while(sc2.hasNext())
			{
				c += sc2.next() + " ";
			}

			for(int i = 0; i < c.length() - 1; i++)
                        {
                                String ctwo = c.substring(i, i + 2);
                                if(cipher.containsKey(ctwo))
                                {
                                        cipher.put(ctwo, cipher.get(ctwo) + 1);
                                }
                                else 
                                {
                                        cipher.put(ctwo, 1);
                                }
                        }
			//System.out.println("Cipher: " + cipher);*/
		}//end try
		catch(FileNotFoundException e){
			System.out.println(text + " not found " + e);
		}
	}
	public static void main(String [] args)
	{
		/*Scanner sc = new Scanner(System.in);
		System.out.print("Enter text name: ");
		String textFile = sc.next();*/
		
		ps2 decrypt = new ps2("cipher.txt");
		decrypt.loadHashtable("war_abridged.txt");
		decrypt.runDiaconis(100000);
		decrypt.exchangeLetters();
		//decrypt.generatePlausibility("cipher.txt");
	}//end main	

}//end ps1
