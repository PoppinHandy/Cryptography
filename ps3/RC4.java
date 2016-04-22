import java.util.*;
import java.lang.*;

public class RC4
{
	double [] ptable;
	public RC4()
	{
		ptable = new double[10];

		for (int counter = 0; counter < ptable.length; counter++)
		{
			ptable[counter] = 0.0;
		}//end for
	}//end RC4

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
			int unsignedByte = Byte.toUnsignedInt(key[i % key.length]);
			j = (j + stream[i] + key[i % key.length]) & 0xFF; //Have to convert to unsigned byte then to int
			byte temp = stream[i];
			stream[i] = stream[j];
			stream[j] = temp;	
		}//end for

		//Generate Stream
		j = 0;
		int a = 0;
		int k = 0;
		for(int i = 0; i < 256; i++)
		{
			a = (a + 1) & 0xFF;
			j = (j + stream[a]) & 0XFF;
			byte temp = stream[a];
			stream[a] = stream[j];
			stream[j] = temp;
			k = stream[stream[a] + stream[j] & 0XFF] & 0xFF; //Have to convert int to unsigned byte using 0xFF
		
			//Second byte detection
			if(i == 1)
			{	
				//Create probability table by splitting string into tokens and parsing them
				String hexa = Integer.toHexString(k);
				String [] second_byte = hexa.split("");
				if((second_byte.length == 1) && second_byte[0].matches("[0-9]"))
				{
					ptable[Integer.parseInt(second_byte[0])] += 1;
				}
			}//end if

		}//end for
		
		/*for (int count = 0; count < ptable.length; count++)
		{
			ptable[count] /= 256;
		}//end for*/			
	}//end generateKey

	public void resetpTable()
	{
		for (int count = 0; count < ptable.length; count++)
                {
                        ptable[count] = 0;
                }//end for	
	}//end resetpTable
		
	public static void main(String [] args)
	{
		
		RC4 encrypt = new RC4 ();
		double [] finalpt = new double [10];
	
		//Using an iteration of 10000
		for (int i = 0; i < 100000; i++)
		{
			byte [] b = new byte [16];
			new Random().nextBytes(b);
			encrypt.generateKey(b);
			for (int counter = 0; counter < 10; counter++)
                	{
                        	finalpt[counter] += encrypt.ptable[counter];
                	}//end for  
			encrypt.resetpTable();
		}//end for
	
		for (int counter = 0; counter < 10; counter++)
		{
			finalpt[counter] /= 100000;
			System.out.println("The probability of " + counter + " is " + finalpt[counter]);	
		}//end for	
	}//end main
}//end class RC4
