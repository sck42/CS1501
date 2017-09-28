import java.util.*;
import java.io.*;
import java.math.*; 
import java.io.Serializable;
import java.nio.*; 
public class MyBigInteger implements Serializable{
	private byte[] integer; 
	private int numBytes; 
	private boolean isNegative = false; 
	
	public MyBigInteger(int x)
	{
		integer = intToByteArr(x); 
		numBytes = integer.length; 
	}
	public MyBigInteger(byte[] a)
	{
		integer = Arrays.copyOf(a, a.length); 
		numBytes = a.length; 
	}
	public MyBigInteger(byte a, int length)
	{
		integer = new byte[length]; 
		numBytes = length; 
		integer[length -1] = a; 
	}
	public MyBigInteger increment()
	{
		return this.add(new MyBigInteger(1)); 
	}
	public int compareTo(MyBigInteger b)
	{
		byte[] thatInteger = b.getInteger(); 
		int thisIndex = integer.length - 1;
		int	thatIndex = thatInteger.length - 1;
		int result = 0; 
		while(thisIndex >= 0 && thatIndex >= 0)
		{
			if((integer[thisIndex] & 0xff) > (thatInteger[thatIndex] & 0xff))
				result = 1; 
			else if((integer[thisIndex] & 0xff) < (thatInteger[thatIndex] & 0xff))
				result = -1;
			thisIndex--;
			thatIndex--; 
		}
		if(thisIndex > 0)
		{
			if((integer[thisIndex] & 0xff) != 0)
				return 1; 
		}
		else if(thatIndex > 0)
		{
			if((thatInteger[thatIndex] & 0xff) != 0)
				return -1;
		}
		return result; 
	}
	public MyBigInteger sub(MyBigInteger a)
	{
		byte[] result = new byte[Math.max(a.getInteger().length, integer.length)];
		byte[] thatInteger = a.getInteger(); 
		int thisIndex = integer.length-1; 
		int thatIndex = a.getInteger().length - 1; 
		int resultIndex = result.length-1; 
		int carry = 0; 
		
		while(thisIndex >= 0 && thatIndex >= 0)
		{
			int temp = (integer[thisIndex] & 0xff) - (thatInteger[thatIndex] & 0xff); 
			if(carry == -1)
				temp -= 1; 
			result[resultIndex] = (byte)temp; 
			
			if(temp < 0)
				carry = -1; 
			else
				carry = 0; 
			thisIndex--;
			thatIndex--;
			resultIndex--; 
		}
		for(int i = thisIndex; i >= 0; i--)
		{
			int temp = integer[i] & 0xff; 
			if(carry == -1)
				temp -= 1; 
			result[resultIndex--] = (byte)temp; 
		}
		for(int j = thatIndex; j >= 0; j--)
		{
			int temp = thatInteger[j] & 0xff; 
			if(carry == -1)
				temp -= 1; 
			result[resultIndex--] = (byte)temp; 
		}
		result[0] = (byte)0; 
		MyBigInteger difference = new MyBigInteger(result); 
		return difference; 
	}
	public MyBigInteger modInverse(MyBigInteger a)
	{
		BigInteger phi = new BigInteger(a.getInteger()); 
		BigInteger e = new BigInteger(integer); 
		BigInteger result = e.modInverse(phi); 
		return new MyBigInteger(result.toByteArray()); 
	}
	public byte[] getInteger()
	{
		return integer; 
	}
	public int getNumBytes()
	{
		return numBytes; 
	}
	public MyBigInteger add(MyBigInteger a)
	{
		byte[] result = new byte[Math.max(a.getNumBytes(), numBytes) + 1]; 
		int thisIndex = numBytes-1; 
		int thatIndex = a.getNumBytes() - 1; 
		int resultIndex = result.length-1; 
		byte[] aInt = a.getInteger(); 
		int overflow = 0; 
		while(thisIndex >= 0 && thatIndex >= 0)
		{
			int x = (integer[thisIndex] & 0xff) + (aInt[thatIndex] & 0xff) + overflow; 
			result[resultIndex] = (byte) x; 
			if(x >= 256)
				overflow = x >> 8; 
			else 
				overflow = 0; 
			thisIndex--;
			thatIndex--;
			resultIndex--; 
		}
		for(int j = thisIndex; j >= 0 ; j--)
		{
			int x = (integer[j] & 0xff) + overflow; 
			result[resultIndex] = (byte) x; 
			if(x >= 256)
				overflow = x >> 8; 
			else 
				overflow = 0;
			resultIndex--; 
		}
		for(int m = thatIndex; m >= 0 ; m--)
		{
			int x = (integer[m] & 0xff) + overflow; 
			result[resultIndex] = (byte) x; 
			if(x >= 256)
				overflow = x >> 8; 
			else 
				overflow = 0;
			resultIndex--; 
		}
		return new MyBigInteger(result); 
	}
	public MyBigInteger multiply(MyBigInteger a)
	{
		byte[] result = new byte[a.getInteger().length + integer.length - 1]; 
		int thisIndex = numBytes-1;
		int thatIndex = a.getNumBytes() - 1;
		int resultIndex = result.length-1; 
		byte[] thatInteger = a.getInteger(); 
		int overflow = 0; 
		int placeHold = 0; 
		for(int i = thisIndex; i >= 0; i--)
		{
			for(int j = thatIndex; j >=0; j--)
			{
				int x = (integer[i] & 0xff) * (thatInteger[j] & 0xff) + overflow; 
				int res = (result[resultIndex] & 0xff) + x; 
				if(res >= 256)
					overflow = res >> 8; 
				else 
					overflow = 0; 
				result[resultIndex--] += (byte) x;  
			}
			resultIndex = result.length-1; 
			placeHold++; 
			resultIndex -= placeHold; 
		}
		return new MyBigInteger(result); 
	} 
	public MyBigInteger mod(MyBigInteger a)
	{
		BigInteger thisInteger = new BigInteger(integer); 
		BigInteger m = new BigInteger(a.getInteger()); 
		BigInteger result = thisInteger.mod(m); 
		return new MyBigInteger(result.toByteArray()); 
		/* if(this.compareTo(a) == -1)
			return this; 
		MyBigInteger remainder = this.sub(a); 
		while(a.compareTo(remainder) == -1) {  remainder = remainder.sub(a); }
		return remainder;   */
		/* MyBigInteger thisInteger = this; 
		if(phi.compareTo(this) == -1)
		{
			thisInteger = phi; 
			phi = this; 
		}
		if(phi.compareTo(this) == 0)
			phi = new MyBigInteger(0); 
		MyBigInteger num = thisInteger; 
		outter: 
		while(true)
		{
			MyBigInteger numTemp = new MyBigInteger(1); 
			while(num.compareTo(phi) == -1)
			{
				numTemp = num; 
				num = num.multiply(thisInteger); 
				//exp.add(new MyBigInteger(1)); 
				if(phi.compareTo(num) ==0)
				{
					phi = new MyBigInteger(0); 
					break outter; 
				}
			}
			num = numTemp; 
			numTemp = new MyBigInteger(1); 
			//exp = new MyBigInteger(1); 
			phi = phi.sub(num); 
			num = thisInteger; 
			if(phi.compareTo(thisInteger) == -1)
				break; 
			else if(phi.compareTo(thisInteger) == 0)
			{
				phi = new MyBigInteger(0); 
				break; 
			}
		
		}
		return phi;  */
	}
	public int mod(int x)
	{
		int m = 0; 
		byte[] tempInteger = Arrays.copyOf(integer, integer.length); 
		for(int start = 0; start < tempInteger.length; start++)
		{
			byte[] pseudoInt = new byte[4];
			int j = start; 
			if(tempInteger.length - start < 4)
				break; 
			for(int i = 0; i < pseudoInt.length; i++)
			{
				pseudoInt[i] = tempInteger[j++]; 
			}
			m = byteArrToInt(pseudoInt); 
			m = m % x; 
			pseudoInt = intToByteArr(m);
			j = start; 
			for(int i = 0; i < pseudoInt.length; i++)
			{
				tempInteger[j++] = pseudoInt[i]; 
			}
		}
		return m; 
	}
	
	public boolean isOdd(int len)
	{
		BitSet bs = BitSet.valueOf(integer); 
		return bs.get(len); 
	}

	public static int byteArrToInt(byte[] arrToInt)
	{
		if(arrToInt.length != 4)
			return -1; 
		int result = 0; 
		int power = 0; 
		for(int i = arrToInt.length-1; i >= 0; i--)
		{
			result += (arrToInt[i] & 0xff) * Math.pow(256, power); 
			power++; 
		}
		return result; 
	}
	public static byte[] intToByteArr(int x)
	{
		byte[] result = new byte[4]; 
		int shiftCount = 24; 
		for(int i = 0; i < result.length; i++)
		{
			result[i] = (byte)(x>>shiftCount); 
			shiftCount-=8; 
		}
		return result; 
	}
	public static MyBigInteger powMod(MyBigInteger ms, MyBigInteger a, MyBigInteger m)
	{
		//String exponent = MyBigInteger.byteToBit(a.getInteger());
		/*StringBuilder sb = new StringBuilder(exponent); 
		System.out.println(m.shift(0, 20).printArr()); 
		return a;  */
		/* MyBigInteger ans = new MyBigInteger(1); 
		for(int i = exponent.length() -1; i >=0 ; i--)
		{
			ans = (ans.multiply(ans)).mod(m);
			if(exponent.charAt(i) == '1')
				ans = (ms.multiply(ans)).mod(m);
				
				//ans = (ans.multiply(this)).mod(m); 
		}
		return ans;  */ 
		BigInteger x = new BigInteger(ms.getInteger()); 
		BigInteger y = new BigInteger(a.getInteger()); 
		BigInteger z = new BigInteger(m.getInteger()); 
		BigInteger result = x.modPow(y, z); 
		return new MyBigInteger(result.toByteArray()); 
	}
	public static String byteToBit(byte[] a)
	{
		StringBuilder sb = new StringBuilder(); 
		for(byte x : a)
			sb.append(Integer.toBinaryString(x & 255 |256).substring(1));
		return sb.toString(); 
		//String s = sb.toString(); 
		//int y = s.indexOf('1'); 
		//return s.substring(y, s.length()); 
	}
	public MyBigInteger shift(int dir, int shiftAmount)
	{
		byte[] tempInteger = integer; 
		if(dir == 0)//Shift right
		{
			if(byteToBit(tempInteger).length() < shiftAmount)
				return new MyBigInteger(0); 
			while(shiftAmount > 0)
			{
				String bits = byteToBit(tempInteger); 
				StringBuilder sb = new StringBuilder(bits); 
				int index = bits.length() - 1; 
				tempInteger = bitToByte(sb.deleteCharAt(index).toString()); 
				shiftAmount--; 
			}
		}
		else 
		{
			while(shiftAmount > 0)
			{
				String bits = byteToBit(tempInteger); 
				StringBuilder sb = new StringBuilder(bits); 
				int index = bits.length() - 1; 
				tempInteger = bitToByte(sb.append('0').toString()); 
				shiftAmount--; 
			}
		}
		return new MyBigInteger(tempInteger); 
	}
	public static byte[] bitToByte(String bits)
	{
		byte[] result = new byte[(int)Math.ceil(bits.length()/8.0)]; 
		int end = bits.length(); 
		int start = end - 8;  
		for(int i = result.length-1; i >=0; i--)
		{
			result[i] = (byte)Integer.parseInt(bits.substring(start, end), 2); 
			start -=8; 
			end -=8; 
			if(start <0 || end < 0)
			{
				break; 
			}
		}
		return result; 
	}
	public String printArr()
	{
		return Arrays.toString(integer);   
	}
	public String toCharArray()
	{
		return Arrays.toString(new String(integer).toCharArray()); 
	}
	public MyBigInteger gcd(MyBigInteger a)
	{
		BigInteger e = new BigInteger(integer); 
		BigInteger phi = new BigInteger(a.getInteger()); 
		BigInteger result = e.gcd(phi); 
		return new MyBigInteger(result.toByteArray()); 
	}
}