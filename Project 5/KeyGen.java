import java.math.BigInteger;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.Serializable;
/**
 * Write a description of class KeyGen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KeyGen implements Serializable 
{
    public static void main(String [] args){
        Random random = new Random();
        MyBigInteger one = new MyBigInteger(new byte[]{0x1});
        RandomPrime rp = new RandomPrime();
        MyBigInteger p = new MyBigInteger(rp.generate(256, random));
        MyBigInteger q = new MyBigInteger(rp.generate(256, random));
        MyBigInteger n = p.multiply(q);
        MyBigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
        int count = 0;
        MyBigInteger e;

        for(;;){
            count++;
            System.out.println("It made it through in " +count+ " times");
            e = new MyBigInteger(7);
            if((e.compareTo(one) != 1)){
                System.out.println("Not greater than 1");
                continue;
            }
            if((e.compareTo(phi) != -1)){
                System.out.println("Not less than phi");
                continue;
            }
            
            if(!e.gcdOne(phi)){
                System.out.println("Greatest common multiple is not 1");
                continue;
            }
            

            break;
        }

        MyBigInteger d = e.modInverse(phi);
        
        /*
        System.out.println("n: "+n.print());
        System.out.println("e: "+e.print());
        System.out.println("d: "+d.print());
        */

        try {
            // write object to file
            FileOutputStream fos = new FileOutputStream("pubkey.rsa");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(n);
            oos.writeObject(e);
            oos.close();
            
            FileOutputStream f = new FileOutputStream("privkey.rsa");
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(n);
            o.writeObject(d);
            o.close();

            /*
            // read object from file
            FileInputStream fis = new FileInputStream("mybean.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            MyBean result = (MyBean) ois.readObject();
            ois.close();
             */

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());

        } catch (IOException r) {
            r.printStackTrace();
        } 

    }
}
