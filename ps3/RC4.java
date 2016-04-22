import java.util.*;
import java.lang.*;

public class RC4
{

	public void generateKey(byte [] key)
	{
		//Initialize
		byte [] stream = new byte [256];
		for (int i = 0; i < 256; i++)
		{
			stream[i] = (byte)i;
		}//end for
		int j = 0;
		for (int i = 0; i < 256; i++)
		{
			j = (j + stream[i] + key[i % key.length]) % 256;
			System.out.println("J is: " + j);
			byte temp = stream[i];
			stream[i] = stream[j];
			stream[j] = temp;	
		}//end for

		//Generate Stream

		j = 0;
		for(int i = 0; i < 256; i++)
		{
			i = (i + 1) % 256;
			j = (j + stream[i]) % 256;
			byte temp = stream[i];
                        stream[i] = stream[j];
                        stream[j] = temp;
			int k = stream[stream[i] + stream[j]] % 256;
			System.out.println("K is: " + Integer.toHexString(k));
		}			
	}//end generateKey

	public static void main(String [] args)
	{
		
		RC4 encrypt = new RC4 ();
		for (int i = 0; i < 1; i++)
		{
			byte [] b = new byte [16];
			new Random().nextBytes(b);
			encrypt.generateKey(b);
		}//end for
	}//end main
}//end class RC4
