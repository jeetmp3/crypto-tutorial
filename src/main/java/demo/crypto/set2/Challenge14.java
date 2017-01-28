package demo.crypto.set2;

import java.io.IOException;
import java.util.Random;

/**
 * @author Jitendra Singh.
 */
public class Challenge14 extends Challenge12 {

    static Random r = new Random();
    static String randomString = generateRandomString();

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public byte[] encryptOracle(String yourString) {
        String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
        System.out.println("-------------------- GENERATED STRING ----------------------------");
        System.out.println(randomString);
        System.out.println("-------------------- GENERATED STRING ----------------------------");
        return encrypt(randomString.concat(yourString), unknownString, key);
    }

    protected static String generateRandomString() {
        int total = r.nextInt(100);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < total; i++) {
            sb.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        Challenge14 ch14 = new Challenge14();
        ch14.decryptString();
    }
}
