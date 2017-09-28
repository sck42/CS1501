/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/
import java.util.*; 

public class MyLZW {
    private static final int R = 256;       									// number of input chars
    private static int L = 512;       											// number of codewords = 2^W
    private static int W = 9;         											// codeword width
	private static String mode; 												// Holds selected mode. 
	
    public static void compress() { 
		double compressedRatio = 0.0; 											//Holds previously calculated ratio. 
		double uncompressedData = 0.0; 											//Holds the size of uncompressed file.
		double compressedData = 0.0; 											//Holds the size of compressed file. 
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF 
		//Store the size of uncompressed file. 
		//Write the mode to compressed file. 
		BinaryStdOut.write(st.get(mode), W); 

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
			uncompressedData += s.length(); 
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            int t = s.length();
			compressedData += W/8; 												//Store number of bits in the compressed file. Change bits to bytes. 
            if (t < input.length())    // Add s to symbol table.
			{
				if(code < L)
					st.put(input.substring(0, t + 1), code++);
				else if(W < 16)													//if code isn't less than number of codewords and if codeword width is less than the max width increment to next width. 
				{
					W++; 
					L*=2; 
					st.put(input.substring(0, t + 1), code++);
				}
				else if(mode.equals("r")) 										//If reset mode is selected, delete old and make new trie, fill in new trie with ascii values and reset width length back to 9. 
				{
					st = new TST<Integer>(); 
					for(int i = 0; i < R; i++)
						st.put("" + (char) i, i); 
					code = R + 1; 
					L = 512; 
					W = 9; 
					st.put(input.substring(0, t + 1), code++);
				}
				else if(mode.equals("m")) 										//If monitor mode is selected, check previous compress ratio. 
				{
					if(compressedRatio == 0.0) 									//if previous ratio is zero, calculate and fill in the ratio. 
						compressedRatio = uncompressedData/compressedData; 
					else 
					{
						double currentRatio = uncompressedData/compressedData; 
						if((compressedRatio/currentRatio) > 1.1) 					//Calculate new ratio and check if old/new is greater than 1.1. If greater, reset codebook. 
						{
							compressedRatio = currentRatio; 3
							st = new TST<Integer>(); 
							for(int i = 0; i < R; i++)
								st.put("" + (char) i, i); 
							code = R + 1; 
							L = 512; 
							W = 9; 
							st.put(input.substring(0, t + 1), code++);
						}
					}
				}
			}
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[65536];
		double compressedRatio = 0.0; 
		double original = 0.0; 
		double compressedData = 0.0; 
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
		
		//Read and store mode from file. 
		mode = "" + BinaryStdIn.readChar(W); 
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;          // expanded message is empty string
        String val = st[codeword];

        while (true) {
			original += val.length(); 
			compressedData += W/8; 
			if(i == L)															//If i equals L it means we've run out of codewords and we need to either increment bit length or reset. 
			{
				if(W < 16)														//Increment bit length if bit length is still less than the max bit length of 16. 
				{
					L *= 2; 
					W++; 
				}
				else if(mode.equals("r"))										//Reset codebook if we are at reset mode. 
				{
					st = new String[65536]; 									//Create new array to replace old one. 
					for(i = 0; i < R; i++)										//Add all ascii values. 
						st[i] = "" + (char) i; 
					st[i++] = ""; 
					L = 512; 													//Reset codeword and bitlength back to the original. 
					W = 9; 		
				}
				else if(mode.equals("m"))										//Check if monitor mode is selected. 
				{
					if(compressedRatio == 0.0)									//If compressedRatio isn't calculated yet, it means its first time through, so calculate and put in the ratio. 
						compressedRatio = original/compressedData; 
					else 														//Calculate current ratio and compare it to previous ratio. 
					{
						double currentRatio = original/compressedData; 
						if((compressedRatio/currentRatio) > 1.1)					//Compare both ratios and reset if old/new is greater than 1.1
						{
							compressedRatio = currentRatio; 
							st = new String[65536]; 
							for(i = 0; i < R; i++)
								st[i] = "" + (char) i; 
							st[i++] = ""; 
							L = 512; 
							W = 9; 
						}
					}
				}
			} 
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
			
			
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
		
        if(args[0].equals("-")) 
		{
			mode = args[1]; 
			compress();
		}
        else if (args[0].equals("+"))
			expand();
        else 
			throw new IllegalArgumentException("Illegal command line argument");
    }

}
