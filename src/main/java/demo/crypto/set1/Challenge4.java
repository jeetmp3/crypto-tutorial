package demo.crypto.set1;

import java.io.IOException;

/**
 * @author Jitendra Singh.
 */
public class Challenge4 {

    public static void main(String[] args) throws IOException {
//        List<String> lines = readLines("http://cryptopals.com/static/challenge-data/4.txt");
        Challenge3 challenge3 = new Challenge3();
        System.out.println(challenge3.decrypt("7b5a4215415d544115415d5015455447414c155c46155f4058455c5b523f", '5'));
//        char key = '\0';
//        int frequency = 0;
//        String l = "";
//        for (String line : lines) {
//            key = challenge3.singleByteXorCipher(line);
//            System.out.println("for [" + line + "] key is [" + key + "] and message is [" + challenge3.decrypt(line, key) + "]");
//        }
    }
}
