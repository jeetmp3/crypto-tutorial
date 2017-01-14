package demo.crypto.set1;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class Challenge2 {

    public String fixedXor(String str1, String str2) {
        byte[] bytes = Utils.xor(str1, str2);;
        return Utils.bytesToHex(bytes);
    }
}
