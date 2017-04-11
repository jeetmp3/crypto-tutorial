package demo.crypto.set1;

import demo.crypto.URLUtils;
import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Jitendra Singh.
 */
public class Challenge7 {
    public static void main(String[] args) throws IOException {
        String key = "YELLOW SUBMARINE";
        AES aes = AES.getInstance(EncryptionMode.AES_128_ECB);
        List<String> lines = URLUtils.readFile(new FileInputStream("/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/src/main/resources/challenge7.txt"));
        String message = String.join("", lines);
        byte[] result = aes.decrypt(Utils.decodeBase64(message), key.getBytes());
        System.out.println(new String(result));
    }
}
