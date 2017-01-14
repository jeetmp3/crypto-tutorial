package demo.crypto.set1;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class Challenge5 {

//    static String message = "Burning 'em, if you ain't quick and nimble\n" +
//            "I go crazy when I hear a cymbal";
    static String key = "ICE";

    public static void main(String[] args) {
        String message1 = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        String m1 = "le";
        String message2 = "";
//        System.out.println(Utils.bytesToHex(Utils.repeatingKeyXOR(message, key)));
        System.out.println(Utils.bytesToHex(Utils.repeatingKeyXOR(message1, key)));
//        System.out.println(Utils.bytesToHex(Utils.repeatingKeyXOR(m1, key)));
//        System.out.println(Utils.bytesToHex(Utils.repeatingKeyXOR(message2, key)));
    }
}
