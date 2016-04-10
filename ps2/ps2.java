import java.io.*;
import java.util.*;
import java.lang.*;

public class ps2
{
	/* Cipher-text attack using a known corpus of text
		1. Read in text
		2. Construct bigram with keys being 2 character and values in the set of [0, 1]	which are the probabilities of those two chars appearing
		3. Construct Pl(f) by multiplying together f(Ci, Ci+1) from beginning of cipher to end of cipher	
	*/

	Hashtable <String, Integer> bigram;
	Hashtable <String, Integer> cipher;
	public ps2()
	{
		bigram = new Hashtable <String, Integer>();
		cipher = new Hashtable <String, Integer>();
	}//end constructor

	public void loadHashtable(String text, String ciphertext)
	{
		String s = "";
		String c = "";
		File file = new File(text);
		File cfile = new File(ciphertext);
		try{
			Scanner sc = new Scanner(file);
			Scanner sc2 = new Scanner(cfile);
			
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
			System.out.println(bigram);
			System.out.println(bigram.size());
			
			//Making bigram for cipher
			while(sc2.hasNext())
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
			System.out.println("Cipher: " + cipher);

			
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
		
		ps2 decrypt = new ps2();
		decrypt.loadHashtable("war_abridged.txt", "cipher.txt");
	}//end main	

}//end ps1
