import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/*
    TODO: There needs to be a patch that  can makes every character lowercase and reverts them back. I suppose an iself statement will help this
    TODO: I created a getencryptedstring this should go to the huffman.java code then it should decrypt on the other side
    TODO: Make sure to add the space part in RSA it turns your letter to -32 if
            it finds that it is -32 that means there is a space which is wrong

    //PATCHNOTES: It is better to use wrapper classes in to avoid random null error's I found

 */

//The encrypted backpack is to help with re-spacing the integers back to regular
//Example: The encrypted code will turn to a string 01827 from the arraylist [0][1][8][27]
// The lengthofEachNum will let us know how to store the array back to the arraylist after the huffman is done being used.

public class RSA implements Serializable {

    // private EncryptedBackpack backpack = new EncryptedBackpack();

    private ArrayList<Integer> lengthOfEachNum = new ArrayList<>();

    // private String message;
    private String message;
    private int[] numMsg;
    private int d = 0;

    private ArrayList<Integer> encrpytMessage = new ArrayList<>();
    private int sizeOfMsg;
    private ArrayList<Character> decryptedMsg;
    // constructor

    RSA() {

    }

    RSA(String msg) {
        this.message = msg;
        this.numMsg = new int[message.length()];
        sizeOfMsg = message.length();
        changeMessageToAsciiArray();
        range1_26();
        encrpyt();
        // decrypt(); user should choose to decrpyt
    }

    private void encrpyt() {
        // this will encrypt the message to rsa
        for (int i = 0; i < numMsg.length; i++) {
            // this will have an array the revolves around it.
            RsaAlgorithmn(numMsg[i]);
        }
        for (int i = 0; i < encrpytMessage.size(); i++) {
            // System.out.print(encrpytMessage.get(i) + " ");
        }
        // System.out.println(); //new line
    }

    public String getEncrpytMessageString() {
        String encryptMsgString = "";
        for (int i = 0; i < encrpytMessage.size(); i++) {
            encryptMsgString += encrpytMessage.get(i);
            lengthOfEachNum.add(String.valueOf(encrpytMessage.get(i)).length()); // storing the size of that number
        }
        // backpack.setEncryptedMessage(encryptMsgString);
        return encryptMsgString;
    }

    public String decrypt(@NotNull String encryptedString) {
        this.decryptedMsg = new ArrayList<>();
        ArrayList<Integer> encryptedMsg = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String holder = "";
        int i = 0;
        int k = 0;
        // grabs the number of the encrypted message
        while (i < encryptedString.length()) {
            int numLength = lengthOfEachNum.get(k);
            for (int j = 0; j < numLength; j++) {
                holder += encryptedString.charAt(i);
                i++;
            }
            encryptedMsg.add(Integer.valueOf(holder)); // once the holder has one of the encrypted numbers we add it to
                                                       // the encrypted msg arrlist
            holder = ""; // reset
            k++;
        }

        // TODO: figure out how to use the decrypt function with ur encrypt message I
        // would look at your default decrypt

        for (i = 0; i < encryptedMsg.size(); i++) {
            decrypt((double) (encrpytMessage.get(i).intValue()), i);
        }

        for (i = 0; i < decryptedMsg.size(); i++) {

            sb.append(decryptedMsg.get(i));
        }

        String msg = sb.toString();

        return msg;

    }

    private void decrypt(double c, int i) {

        int minAscii = 97;

        BigInteger C = BigDecimal.valueOf(c).toBigInteger();
        BigInteger msgBack = (C.pow(d)).mod(N);
        // System.out.println("Decrypted message is : " + msgBack);

        if (c == (-32.0)) {
            decryptedMsg.add((char) 32); // this adds a space
            return;
        }

        int charVal = msgBack.intValue() + minAscii;

        decryptedMsg.add((char) charVal);
    }

    int n; // The reason I put n here because it will always be the same number since we
           // aren't changing any primes yet.
    BigInteger N; // same goes here as like n

    private void RsaAlgorithmn(int msg) {
        int p, q, z, e, i;

        // NOTE: if you do plan to randomize the prime number p and q make sure to save
        // the n in a safe array.
        // We might need to keep other things in an array too if we choose to do this

        // The number to be encrypted and decrypted
        double c;
        BigInteger msgBack;

        // 1st prime number p
        p = 3;

        // 2nd prime number q
        q = 11;
        n = p * q;
        z = (p - 1) * (q - 1);

        for (e = 2; e < z; e++) {

            // e is for public key exponent
            if (gcd(e, z) == 1) {
                break;
            }
        }

        // System.out.println("the value of e = " + e);

        for (i = 0; i <= 9; i++) {
            int x = 1 + (i * z);

            // d is for private key exponent
            if (x % e == 0) {
                d = x / e;
                break;
            }
        }
        // System.out.println("the value of d = " + d);
        c = (Math.pow(msg, e)) % n;

        // the value c will be inside an array

        this.encrpytMessage.add((int) c);

        // converting int value of n to BigInteger
        N = BigInteger.valueOf(n);

    }

    private int gcd(int e, int z) {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

    // changes msg to ascii array
    private void changeMessageToAsciiArray() {
        for (int i = 0; i < message.length(); i++) {
            numMsg[i] = (int) message.charAt(i); // changing the code to their ascii code
        }
    }

    // makes sure that the ascii code is converted through 0 to 25
    // 0 to 25 = a b c ... z
    private void range1_26() {
        // int maxAscii = 122; // z
        int minAscii = 97; // a

        for (int i = 0; i < message.length(); i++) {
            numMsg[i] = numMsg[i] - minAscii;
        }
        // converts every single ascii to the doable range which is 0 to 25;
    }

    public static void main(String[] args) {
        RSA msg = new RSA("hi how are you".toLowerCase());
        System.out.println(msg.decrypt(msg.getEncrpytMessageString()));

    }

}
