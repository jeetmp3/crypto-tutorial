package demo.crypto.set2;

import demo.crypto.URLUtils;
import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jitendra Singh.
 */
public class Challenge10 {

    public static void main(String[] args) throws IOException {
        String key = "YELLOW SUBMARINE";
        byte[] iv = new byte[16];
        List<String> lines = URLUtils.readFile(new FileInputStream("/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/src/main/resources/challenge10.txt"));
        AES aes = AES.getInstance(EncryptionMode.AES_128_CBC);
        String message = String.join("", lines);
        byte[] result = aes.decrypt(Utils.decodeBase64(message), key.getBytes(), iv);
        System.out.println(new String(result));
    }
}
