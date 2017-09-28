import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.io.*;

public class HashEx {
    public static void main(String [] args) {
        String arg;
        String filename = "";

        //Checks to make sure arguments are valid
        if(!(args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("v"))){
            throw new IllegalArgumentException("Unsupported Operation");
        }else{
            arg = args[0];
        }

        //Checks to make sure path is not null
        //We'll leave checking if it's valid up to the try catch block
        if(args.length < 2){
            throw new IllegalArgumentException("File needed");
        }else{
            filename = args[1];
        }

        //Sign
        if(arg.equalsIgnoreCase("s")){
            try {
                // read in the file to hash
                Path path = Paths.get(filename);
                byte[] data = Files.readAllBytes(path);

                // create class instance to create SHA-256 hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();

                //Create the signature of the file
                String sig = "";
                // print each byte in hex
                for (byte b : digest) {
                    sig += String.format("%02x", b);
                }
                // read object from file
                FileInputStream fis = new FileInputStream("privkey.rsa");
                ObjectInputStream ois = new ObjectInputStream(fis);
                MyBigInteger n = (MyBigInteger) ois.readObject();
                MyBigInteger d = (MyBigInteger) ois.readObject();
                ois.close();

                System.out.println("N: "+n.print());

                System.out.println("D: "+ d.print());

                /*
                Scanner test = new Scanner(new FileReader("pubkey.rsa"));
                String nn = test.next().replace(" ", "");
                String e = test.next().replace(" ", "");
                System.out.println("n: "+n);
                System.out.println("e: "+e);
                System.out.println("d: "+d);
                 */
                //Create an instance of big integer with our private key
                MyBigInteger biDigest = new MyBigInteger(digest);
                MyBigInteger encrypt = biDigest.modPow(d, n);
                /*
                byte decrypt[] = encrypt.modPow(biE, biN).toByteArray();
                String de = "";
                for (byte b : decrypt) {
                de += String.format("%02x", b);
                }
                System.out.println("Decrypted: "+de);
                 */
                // write object to file
                FileOutputStream fos = new FileOutputStream(filename+".sig");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(encrypt);
                oos.close();

                System.out.println("Signature: "+sig);
                System.out.println("OutPut signature: "+encrypt.print());

            } catch(Exception e) {
                System.out.println(e.toString());
            }
        }

        //Verify 
        if(arg.equalsIgnoreCase("v")){
            // lazily catch all exceptions...
            try {
                // read in the file to hash
                Path path = Paths.get(filename);
                byte[] data = Files.readAllBytes(path);

                // create class instance to create SHA-256 hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();

                //Create the signature of the file
                String sig = "";
                // print each byte in hex
                for (byte b : digest) {
                    sig += String.format("%02x", b);
                }

                //Read in our public key
                FileInputStream fis = new FileInputStream("pubkey.rsa");
                ObjectInputStream ois = new ObjectInputStream(fis);
                MyBigInteger n = (MyBigInteger) ois.readObject();
                MyBigInteger e = (MyBigInteger) ois.readObject();
                ois.close();

                System.out.println("N: "+n.print());

                System.out.println("E: "+ e.print());

                //Create an instance of big integer with our public key
                //Read in our sig
                FileInputStream fs = new FileInputStream(filename + ".sig");
                ObjectInputStream os = new ObjectInputStream(fs);
                MyBigInteger signature = (MyBigInteger) os.readObject();
                ois.close();

                System.out.println("Read in signature: "+signature.print());
                byte decrypt[] = signature.powMod(e, n).toByteArray();
                String de = "";
                for (byte b : decrypt) {
                    de += String.format("%02x", b);
                }
                System.out.println("Decrypted: "+de);

                System.out.println("Encrypted: "+de);
                System.out.println("Signature of file: "+sig);

                if(sig.equals(de)){
                    System.out.println("They are equal");
                }

            } catch(Exception e) {
                System.out.println(e.toString());
            }
        }

    }
}

