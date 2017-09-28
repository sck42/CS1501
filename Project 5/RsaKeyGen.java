import java.io.*; 
import java.util.*; 
import java.nio.*; 
import java.math.BigInteger;


import java.io.Serializable;
public class RsaKeyGen implements Serializable 
{
	private static MyBigInteger ONE = new MyBigInteger(1);
	
    public static void main(String [] args){
        Random random = new Random();
        
        RandomPrime rp = new RandomPrime();
        MyBigInteger p = new MyBigInteger(rp.generate(256, random));
        MyBigInteger q = new MyBigInteger(rp.generate(256, random));
        MyBigInteger n = p.multiply(q);
        MyBigInteger phi = (p.sub(ONE)).multiply(q.sub(ONE));
        int count = 0;
        MyBigInteger e;
		while(true)
		{
			e = new MyBigInteger(rp.generate(256, random)); 
			if(e.compareTo(p) != 0 && e.compareTo(q) != 0 && e.gcd(phi).compareTo(ONE) == 0)
				break; 
			
		}
		
        MyBigInteger d = e.modInverse(phi);
        System.out.println("N: " + n.printArr());
        System.out.println("E: " + e.printArr());
        System.out.println("D: " + d.printArr());
        System.out.println("Phi: " + phi.printArr()); 
		
        try {
            // write object to file
            FileOutputStream pubFOS = new FileOutputStream("pubkey.rsa");
            ObjectOutputStream pubOut = new ObjectOutputStream(pubFOS);
            pubOut.writeObject(n);
            pubOut.writeObject(e);
            pubOut.close();
            
            FileOutputStream priFOS = new FileOutputStream("privkey.rsa");
            ObjectOutputStream priOut = new ObjectOutputStream(priFOS);
            priOut.writeObject(n);
            priOut.writeObject(d);
            priOut.close();

        } catch (IOException r) {
            r.printStackTrace();
        } 

    }
	public static MyBigInteger gcd(MyBigInteger a, int b)
	{
		if(b == 0)
			return a; 
		else 
			return gcd(new MyBigInteger(b), a.mod(b)); 
	}
}
