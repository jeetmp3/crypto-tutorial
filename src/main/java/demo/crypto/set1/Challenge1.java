package demo.crypto.set1;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class Challenge1 {


    public String convertHexToBase64(String hex) {
        return Utils.encodeBase64(Utils.hexToBytes(hex));
    }
}
