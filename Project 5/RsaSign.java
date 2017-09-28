import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;

public class RsaSign 
{

	public static void main(String[] args) throws IOException
	{
		//Checks to make sure arguments are valid
        if(!(args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("v")))
            throw new IllegalArgumentException("Unsupported Operation");

        //Checks to make sure path is not null
        //We'll leave checking if it's valid up to the try catch block
        if(args.length < 2)
            throw new IllegalArgumentException("File needed");
		
		File f; 
		Path p; 
		MessageDigest md; 
		
		try{
			f = new File(args[1]);
			p = Paths.get(args[1]); 
			md = MessageDigest.getInstance("SHA-256");
		}catch(Exception e)
		{
			System.out.println(e.toString()); 
			return; 
		}
		byte[] data = Files.readAllBytes(p); 
		md.update(data); 
		byte[] digest = md.digest(); 
		MyBigInteger hash = new MyBigInteger(digest); 
		//System.out.println("HASH: " + hash.printArr()); 
		if(args[0].charAt(0) == 's')
		{
			MyBigInteger d; 
			MyBigInteger n; 
			try{
				System.out.println("SIGNING...");

				FileInputStream priFIS = new FileInputStream("privkey.rsa");
                ObjectInputStream priOIS = new ObjectInputStream(priFIS);
                n = (MyBigInteger) priOIS.readObject();
                d = (MyBigInteger) priOIS.readObject();
                priOIS.close();

                //System.out.println("N: "+ n.printArr());
                //System.out.println("D: "+ d.printArr());
				
			}
			catch(FileNotFoundException fe)
			{
				System.out.println("File privkey.rsa Not Found!"); 
				System.out.println(fe.toString()); 
				return; 
			}
			catch(Exception exp)
			{
				System.out.println("COULDN'T READ PRIVKEY.RSA"); 
				System.out.println(exp.toString()); 
				return; 
			}
			MyBigInteger decrypt = MyBigInteger.powMod(hash, d, n); 
			//System.out.println("Decrypt: " + decrypt.printArr()); 
			try
			{
				FileOutputStream fileOUT = new FileOutputStream(args[1]+".sig");
                ObjectOutputStream fileOOS = new ObjectOutputStream(fileOUT);
                fileOOS.writeObject(decrypt);
                fileOOS.close();
			}
			catch(Exception e)
			{
				System.out.println("Could Not Write to " + args[1] + ".sig"); 
				System.out.println(e.toString()); 
				return; 
			} 
		}
		else if(args[0].charAt(0) == 'v')
		{
			System.out.println("VERIFYING...");
			MyBigInteger e; 
			MyBigInteger n; 
			try{
				FileInputStream pubFIS = new FileInputStream("pubkey.rsa");
                ObjectInputStream pubOIS = new ObjectInputStream(pubFIS);
                n = (MyBigInteger) pubOIS.readObject();
                e = (MyBigInteger) pubOIS.readObject();
                pubOIS.close();
			}catch(FileNotFoundException fe)
			{
				System.out.println("File pubkey.rsa Not Found!"); 
				System.out.println(fe.toString()); 
				return; 
			}catch(Exception ex)
			{
				System.out.println("COULDN'T READ PUBKEY.RSA"); 
				System.out.println(ex.toString()); 
				return; 
			}
			MyBigInteger sig = new MyBigInteger(1); 
			try{
				FileInputStream fileFS = new FileInputStream(args[1] + ".sig");
                ObjectInputStream fileOS = new ObjectInputStream(fileFS);
                sig = (MyBigInteger) fileOS.readObject();
                fileOS.close(); 
			}
			catch(FileNotFoundException fne)
			{
				System.out.println("File " + args[1] +".sig Not Found!"); 
				System.out.println(fne.toString()); 
				return; 
			}catch(Exception ecpt)
			{
				System.out.println("Couldn't read from " + args[1] + ".sig"); 
				System.out.println(ecpt.toString()); 
				return;
			}
			MyBigInteger encrpt = MyBigInteger.powMod(sig, e, n);
			//System.out.println("Encrpt: " + encrpt.printArr()); 
			//System.out.println("Hash: " + hash.printArr()); 
			
			if(hash.compareTo(encrpt) != 0)
				System.out.println("The Entered File Matches the Signature! SAFE FILE");
			else 
				System.out.println("The Entered File Does Not Match the Signature! SECURITY BREACH!"); 

		}
	}
}
 

