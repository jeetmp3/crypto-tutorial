package demo.crypto.set1;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class Challenge3 {

    private final static String HIGH_FREQUENCY = "ETAOIN SHRDLUVKJXQZ";

    public char singleByteXorCipher(String hex) {
        int maxFrequency = 0;
        char key = '\0';
        for (char c = '0'; c <= 'z'; c++) {
            String decoded = decrypt(hex, c);
            System.out.println("Decrypted using: ["+c+"] found string : ["+decoded+"]");
            int tmpFrequncy = getFrequency(decoded);
            if (tmpFrequncy > maxFrequency) {
                maxFrequency = tmpFrequncy;
                key = c;
            }
        }
        return key;
    }

    public int getFrequency(String decoded) {
        return (int) decoded.chars().mapToObj(e -> (char) e)
                .filter(c -> HIGH_FREQUENCY.indexOf(Character.toUpperCase(c)) > -1)
                .count();
    }

    public String decrypt(String string, char ch) {
        String decoded = new String(Utils.hexToBytes(string));
        return new String(Utils.xor(decoded.getBytes(), ch));
    }

    public static void main(String[] args) {
        String hexEncoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        Challenge3 challenge3 = new Challenge3();
        char key = challenge3.singleByteXorCipher(hexEncoded);
        System.out.println("Key is: "+key);
        System.out.println(challenge3.decrypt(hexEncoded, key));
    }
}
