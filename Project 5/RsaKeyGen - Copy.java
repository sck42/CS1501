import java.util.*;
import java.io.*;
import java.nio.ByteBuffer; 
public class RsaKeyGen{
	
	private static final int NUMBYTES = 5; 
	private static final byte[] ONE = intToByte(1); 
	private static byte[] p; 
	private static byte[] q; 
	private static boolean isNegative = false; 
	public static void main(String[] args)
	{
		/*  p = RandomPrime.generate(32, new Random());
		q = RandomPrime.generate(32, new Random());
		System.out.println("p.length: " + p.length); 
		
		byte[] n = multiply(p, q); 
		byte[] e = intToByte(3); 
		System.out.println(byteArrayToLeInt(p)); 
		System.out.println(byteArrayToLeInt(q));
		System.out.print("P: "); 
		printArray(p); 
		System.out.print("Q: "); 
		printArray(q); 
		System.out.print("N: "); 
		printArray(n);  */
		//System.out.print("Phi: "); 
		//printArray(phi); 
 		/*for(byte[] i = ONE; compareTo(phi, i); i = add(i))
		{
			if(isEqual(gcd(i, phi), ONE))
			{
				e = i; 
				break; 
			}
		} */
		/*  byte[] phiPlusOne = add(phi); 
		while(compareTo(phiPlusOne, e))
		{
			if(isEqual(gcd(phi, e), ONE))
				break; 
			add(e, intToByte(2)); 
		}
		System.out.print("E: "); 
		printArray(e);  */
		byte[] a = intToByte(12345); 
		byte[] b = intToByte(54345); 
		byte[] c = intToByte(6); 
		byte[] d = intToByte(2);  
		printArray(a); 
		printArray(b); 
		printArray(multiply(a, b));

		/* byte[] phi = multiply(sub(b), sub(a)); 
		byte[] negativeD = multiply(d, (byte)-1); 
		printArray(mod(phi, e));  */
		//printArray(a); 
		//divide(a, b); 
		//printArray(multiply(c, d));
		//sub(c, c); 
		//printArray(a); 
		//printArray(b); 
		//add(a, b); 
		//sub(a); 
		//sub(b,a); 
		//mod(b, a); 
		//printArray(gcd(a, b, 0)); 
		//multiply(a, b); 
	}
	public static int byteArrayToLeInt(byte[] b) {
		final ByteBuffer bb = ByteBuffer.wrap(b);
		return bb.getInt();
	}
	public static byte[] intToByte(int m)
	{
		byte[] result = new byte[NUMBYTES]; 
		for(int i = NUMBYTES-1; i >=0; i--)
		{
			result[i] = (byte)m; 
			m = m >> 8; 
		}
		return result; //ByteBuffer.allocate(NUMBYTES).putInt(m).array();
	}
	public static byte[] add(byte[] a)
	{
		byte[] result = a; 
		for(int i = NUMBYTES - 1, overflow = 0; i >= 0; i--)
		{
			int x = (a[i] & 0xff) + 1 + overflow; 
			result[i] = (byte)x; 
			if(x < 256)
				break; 
			overflow = x >>> 8; 
		}
		return result; 
	}
	//YES IT WORKS
 	public static byte[] add(byte[] a, byte[] b)
	{
		byte[] result = new byte[NUMBYTES]; 
		for(int i = NUMBYTES - 1, overflow = 0; i >= 0; i--)
		{
			int x = (a[i] & 0xff) + (b[i] & 0xff) + overflow; 
			result[i] = (byte)x; 
			overflow = x >>> 8; 
		}
		printArray(result);  
		return result; 
	}  
	public static byte[] sub(byte[]a)
	{
		byte[] result = a;
		int res = (result[NUMBYTES - 1] & 0xff) - 1;
		result[NUMBYTES - 1] = (byte)res; 
		return result; 
	}
	//YES IT WORKS
	public static byte[] sub(byte[] a, byte[] b)
	{
		isNegative = false; 
		byte[] result = new byte[NUMBYTES]; 
		int overflow = 0; 
		for(int i = NUMBYTES-1; i >=0; i--)
		{
			int x = (a[i] & 0xff) - (b[i] & 0xff) - overflow; 
			if(x < 0)
				overflow = 1; 
			else 
				overflow = 0; 
			result[i] = (byte)x; 
		}
		if(overflow == 1)
			isNegative = true; 
		return result; 
	}
	//YES IT WORKS
	public static byte[] multiply(byte[] a, byte[] b)
	{
		byte[] result = new byte[NUMBYTES*NUMBYTES]; 
		int resultIndex = NUMBYTES-1;
		int place = 0; 
		for(int i = NUMBYTES - 1, overflow = 0; i >= 0; i--)
		{
			
			for(int j = NUMBYTES - 1; j >= 0; j--)
			{
				int x = ((a[j] & 0xff) * (b[i] & 0xff)) + overflow; 
				System.out.println("x: " + x); 
				System.out.println("Overflow: " + overflow); 
				int res = (result[resultIndex] & 0xff) + x;
				System.out.println("res: " + res); 				
				overflow = res >> 8; 
				result[j] = (byte)x;
				if(res != x)
					overflow += x >> 8; 
				resultIndex++; 
			}
			resultIndex = NUMBYTES-1; 
			place++; 
			resultIndex -= place; 
			
		}
		return result; 
	}
	public static byte[] multiply(byte[] a, byte b)
	{
		byte[] result = new byte[NUMBYTES]; 

		for(int i = NUMBYTES-1, overflow = 0; i >= 0; i--)
		{
			int x = ((a[i] & 0xff) * (b & 0xff)) + overflow; 
				int res = (result[i] & 0xff) + x; 
				overflow = res >> 8; 
				result[i] += (byte)x; 
				if(res != x)
					overflow += x>>8; 
		}
		return result; 
	}
	public static byte[] divide(byte[] a, byte[] b)
	{
		StringBuilder s = new StringBuilder(); 
		for(int i = 0; i < a.length; i++)
		{
			int x = a[i] & 0xff; 
			if(x < 0)
				x *=-1; 
			s.append(x); 
		}
		System.out.println(s.toString()); 
		System.out.println(Integer.parseInt(s.toString())); 
		return null; 
	}
	//YES IT WORKS
	public static byte[] mod(byte[] a, byte[] b)
	{
		System.out.println("MOD"); 
		printArray(a); 
		printArray(b); 
		byte[] remainder = sub(a, b); 
		byte[] multiple = multiply(b, sub(p)); 
		while(compareTo(remainder, b) && !isNegative) { remainder = sub(remainder, multiple); }  
		return remainder; 
	}

	//returns true if a >= b
	//else false
	public static boolean compareTo(byte[] a, byte[] b)
	{
		int aI = 0; 
		int bI = 0; 
		int i = 0; 
		while((a[i] & 0xff) == 0){ i++; aI++; if(i == NUMBYTES) break; } 
		i = 0; 
		while((b[i] & 0xff) == 0){ i++; bI++; if(i == NUMBYTES) break; }
		if(aI < bI)
			return true; 
		else if(aI > bI)
			return false; 
		if((a[aI] & 0xff) >= (b[bI] & 0xff))
			return true; 
		else 
			return false; 
	}
	public static boolean isEqual(byte[] a, byte[] b)
	{
		System.out.println("ISEQUAL"); 
		return Arrays.equals(a, b); 
	}
	public static boolean isZero(byte[] a)
	{
		for(byte x : a)
		{
			if((x & 0xff) != 0)
				return false; 
		}
		return true; 
	}
	public static byte[] gcd(byte[] a, byte[] b, int x)
	{
		if(compareTo(a, b))
			return gcd(a, b); 
		else 
			return gcd(b, a); 
	}
	public static byte[] gcd(byte[] a, byte[] b)
	{
		if(isZero(b))
			return a; 
		else 
			return gcd(b, mod(a, b)); 
	}

	public static void printArray(byte[] a)
	{
		//System.out.println(Arrays.toString(a)); 
		for(byte x : a)
			System.out.print((x & 0xff) + " "); 
		System.out.println("");  
	}

}