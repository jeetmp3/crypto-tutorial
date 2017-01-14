package demo.crypto.set1;

import demo.crypto.URLUtils;
import demo.crypto.algo.AES;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Jitendra Singh.
 */
public class Challenge8 {

    public static void main(String[] args) throws IOException {
        List<String> lines = URLUtils.readFile(new FileInputStream("/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/src/main/resources/challenge8.txt"));
        for(String line : lines) {
            if(AES.checkECB(line)) {
                System.out.println("ECB MODE DETECTED IN: "+line);
                break;
            }
        }
    }
}
