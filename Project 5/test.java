import java.util.*;
import java.io.*;
import java.math.*; 
import java.nio.*; 
public class test {
	private static Random r = new Random(); 
	public static void main(String[] args)
	{
		byte[] pByt = RandomPrime.generate(256, r); 
		byte[] qByt = RandomPrime.generate(256, r); 
		MyBigInteger p = new MyBigInteger(pByt); 
		MyBigInteger q = new MyBigInteger(qByt);
		MyBigInteger n = p.multiply(q); 
		System.out.println("MyBigInteger Multiply: " + p.multiply(q).printArr()); 
		BigInteger pBI = new BigInteger(pByt); 
		BigInteger qBI = new BigInteger(qByt); 
		BigInteger nBi = pBI.multiply(qBI); 
		System.out.println("BigInteger Multiply: " + Arrays.toString(pBI.multiply(qBI).toByteArray())); 
		System.out.println("MyBigInteger Subtract: " + p.sub(q).printArr()); 
		System.out.println("BigInteger Subtract: " + Arrays.toString(pBI.subtract(qBI).toByteArray())); 
		System.out.println("MyBigInteger Add: " + p.add(q).printArr()); 
		System.out.println("BigInteger Add: " + Arrays.toString(pBI.add(qBI).toByteArray()));
		System.out.println("MyBigInteger Mod: " + p.mod(q).printArr()); 
		System.out.println("BigInteger Mod: " + Arrays.toString(pBI.mod(qBI).toByteArray()));
		MyBigInteger phi = p.sub(new MyBigInteger(1)).multiply(q.sub(new MyBigInteger(1))); 
		//BigInteger phiBI = pBI.subtract(BigInteger.ONE).multiply(qBI.subtract(BigInteger.ONE)); 
		//System.out.println("MyBigInteger phi: " + phi.printArr()); 
		//System.out.println("BigInteger phiBI: " + Arrays.toString(phiBI.toByteArray()));  
		int eInt = 3;
		//System.out.println("MyBigInteger d: " + p.divid(3).printArr()); 
		//System.out.println("BigInteger d: " + Arrays.toString(pBI.divide(new BigInteger("3")).toByteArray())); 
		
		
		System.out.println("MyBigInteger mod Int: " + p.mod(eInt)); 
		System.out.println("BigInteger mod int: " + Arrays.toString(pBI.mod(new BigInteger("3")).toByteArray())); 
		MyBigInteger ms = new MyBigInteger(41111111);
		BigInteger msg = new BigInteger("41111111"); 
		
		/* byte[] ebyte = RandomPrime.generate(256,r); 
		do{
			e = new MyBigInteger(ebyte);
			if(e.compareTo(p) != 0 && e.compareTo(q) != 0 && e.compareTo(phi) != 0 )
				break; 
			ebyte = RandomPrime.generate(256,r); 
		}while(true);  */
		
		/* eInt = 3; 
		while(phi.compareTo(new MyBigInteger(eInt)) == 1)
		{
			if(gcd(phi, eInt).compareTo(new MyBigInteger(1)) == 0)
			{
				e = new MyBigInteger(eInt); 
				break; 
			}
			eInt += 2; 
		}  */
		MyBigInteger e = new MyBigInteger(5);
		MyBigInteger mod = new MyBigInteger(0); 
		MyBigInteger num = e; 
		MyBigInteger exp = new MyBigInteger(1); 
		System.out.println("MyBigInteger Phi: " + phi.printArr()); 
		System.out.println("MyBigInteger e.mod(phi): " + e.mod(phi).printArr()); 
		//MyBigInteger phi = new MyBigInteger(49);
		/* outter: 
		while(true)
		{
			MyBigInteger numTemp = new MyBigInteger(1); 
			while(num.compareTo(phi) == -1)
			{
				numTemp = num; 
				num = num.multiply(e); 
				exp.add(new MyBigInteger(1)); 
				if(phi.compareTo(num) ==0)
				{
					phi = new MyBigInteger(0); 
					break outter; 
				}
			}
			num = numTemp; 
			numTemp = new MyBigInteger(1); 
			exp = new MyBigInteger(1); 
			phi = phi.sub(num); 
			num = e; 
			if(phi.compareTo(e) == -1)
				break; 
			else if(phi.compareTo(e) == 0)
				phi = new MyBigInteger(0); 
		}
		System.out.println("MyBigInteger Mod Ans: " + phi.printArr());  */
		
		System.out.println(e.printArr()); 
		MyBigInteger s = new MyBigInteger(48593749); 
		System.out.println("MyBigInteger PowMod: " + MyBigInteger.powMod(ms, e, n).printArr()); 
	//	System.out.println("MyBigInteger s: " + s.printArr()); 
	//	System.out.println("MyBigInteger s Shifted Left by 10: " + s.shift(1, 10).printArr());
	//	System.out.println("MyBigInteger s Shifted right by 10: " + s.shift(0, 10).printArr());
		//BigInteger eBI = new BigInteger(ebyte); 
/* 		BigInteger eBI = new BigInteger("" + eInt); 
		MyBigInteger d = e.modInverse(phi); 
		System.out.println("MyBigInteger e: " + e.printArr());
		System.out.println("MyBigInteger e - phi: " + e.sub(phi).printArr()); 
		System.out.println("MyBigInteger multi ms: " + ms.multiply(ms).printArr()); 
		System.out.println("MyBigInteger d: " + e.modInverse(phi).printArr()); 
		System.out.println("MyBigInteger n: " + n.printArr()); 
		System.out.println("MyBigInteger powMod: " + MyBigInteger.powMod(ms, e, n).printArr()); 
		System.out.println("BigInteger powMod: " + Arrays.toString(msg.modPow(eBI, nBi).toByteArray()));  */
		//System.out.println("MyBigInteger powMod: " + MyBigInteger.powMod(ms, d, n).printArr()); 
		//System.out.println("BigInteger powMod: " + Arrays.toString(msg.modPow(eBI.modInverse(phiBI), nBi).toByteArray())); 
		/* BigInteger eBI = new BigInteger("3"); 
		while(phiBI.compareTo(eBI) == 1)
		{
			if(phiBI.gcd(eBI).compareTo(BigInteger.ONE) == 0)
				break; 
			eBI.add(BigInteger.ONE);
			eBI.add(BigInteger.ONE); 
		}
		System.out.println("BigInteger e: " + Arrays.toString(eBI.toByteArray()));  */
	}
	public static MyBigInteger gcd(MyBigInteger a, int b)
	{
		if(b == 0)
			return a; 
		else 
			return gcd(new MyBigInteger(MyBigInteger.intToByteArr(b)), a.mod(b)); 
	}
}