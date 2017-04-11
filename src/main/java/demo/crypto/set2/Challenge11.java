package demo.crypto.set2;

import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.util.*;

/**
 * @author Jitendra Singh.
 */
public class Challenge11 {

    static Random r = new Random();
    static AES aesECB = AES.getInstance(EncryptionMode.AES_128_ECB);
    static AES aesCBC = AES.getInstance(EncryptionMode.AES_128_CBC);

    public static void main(String[] args) {
        String input = "ffffffffffffffffffffffffffggggggggggggggggggggggggggggggggggggggggggggggggg";
        String prefix = randomBytes(10);
        String suffix = randomBytes(10);
        input = prefix.concat(input).concat(suffix);

        byte[] data = encryption_oracle(input);
        System.out.println(Utils.bytesToHex(data));
        if (AES.checkECB(Utils.bytesToHex(data))) {
            System.out.println("------- ECB --------");
        } else {
            System.out.println("------- CBC --------");
        }
    }

    private static String randomBytes(int total) {
        String data = UUID.randomUUID().toString();
        if (total > 16)
            total = 16;
        return data.substring(0, total);
    }

    private static byte[] encryption_oracle(String input) {
        String key = randomBytes(16);
        String iv = randomBytes(16);
        boolean condition = r.nextBoolean();
        System.out.println("Requested: " + (condition ? "CBC" : "ECB"));
        if (condition) {
            return aesCBCEncrpyt(input, key, iv);
        } else {
            return aesECBEncrpyt(input, key);
        }
    }

    protected static byte[] aesECBEncrpyt(String input, String key) {
        return aesECB.encrypt(input, key);
    }

    protected static byte[] aesCBCEncrpyt(String input, String key, String iv) {
        return aesCBC.encrypt(input, key, iv);
    }

    private static boolean testIfECB(String input, byte[] cipher) {
        boolean ecbMode = true;
        byte[] inpBytes = input.getBytes();
        Map<Byte, Byte> data = new HashMap<>();
        for (int i = 0; i < inpBytes.length; i++) {
            if(data.containsKey(inpBytes[i])) {
                byte val = cipher[i];
                byte existing = data.get(inpBytes[i]);
                if(val != existing) {
                    ecbMode = false;
                    break;
                }
            } else {
                data.put(inpBytes[i], cipher[i]);
            }
        }
        return ecbMode;
    }
//    private static boolean testIfECB(String input, byte[] cipher) {
//        boolean ecbMode = false;
//        List<String> list = AES.breakHexCipherList(input, 16);
//        if (list.size() > 1) {
//            for (int i = 0; i < list.size(); i += 2) {
//                String str1 = list.get(i);
//                if ((i + 1) < list.size()) {
//                    String str2 = list.get(i + 1);
//                    byte[] b1 = new byte[16];
//                    byte[] b2 = new byte[16];
//                    System.arraycopy(cipher, 16 * i, b1, 0, 16);
//                    System.arraycopy(cipher, 16 * (i + 1), b2, 0, 16);
//                    ecbMode = testIfSameCipher(str1, str2, b1, b2);
//                    if (ecbMode)
//                        break;
//                }
//            }
//        }
//        return ecbMode;
//    }

    private static boolean testIfSameCipher(String s1, String s2, byte[] b1, byte[] b2) {
        boolean matched = false;
        byte[] tmp1 = s1.getBytes();
        byte[] tmp2 = s2.getBytes();
        for (int i = 0; i < tmp2.length; i++) {
            for (int j = 0; j < tmp1.length; j++) {
                if (tmp2[i] == tmp1[j] && b2[i] == b1[j]) {
                    matched = true;
                    break;
                }
            }
        }
        return matched;
    }
}
