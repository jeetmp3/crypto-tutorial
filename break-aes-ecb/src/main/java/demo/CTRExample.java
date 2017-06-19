package demo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @author Jitendra Singh.
 */
public class CTRExample {

//    public static void main(String[] args) {
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("AES/CTR/NoPadding");
//            String key = "jitendrajitendra";
//            String msg = "thisisreal";
//            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
//
//            System.out.println("Input: " + msg.getBytes().length);
//            byte[] result = cipher.doFinal(msg.getBytes());
//            System.out.println("Output: " + result.length);
//            System.out.println(Arrays.toString(result));
//            System.out.println(new String(result));
//            char b[] = new char[result.length];
//            char b1[] = new char[result.length];
//            for(int i = 0; i < result.length; i++) {
//                b[i] = (char)(0xff & result[i]);
//                b1[i] = (char)(result[i]);
//            }
//            System.out.println(new String(b));
//            System.out.println(new String(b1));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//        Long i = 23434456L;
//        System.out.println(i);
//        i = Long.reverseBytes(i);
//        System.out.println(Long.toHexString(i));
//        System.out.println(Long.toBinaryString(i));
//    }

    public static void main(String[] args) {
        String name = "mohitbhutani";
        byte xorByte = 5;
        System.out.println("Input: "+name + "  : "+name.length());
        String encode = encode(name, xorByte);
        System.out.println(encode + " : "+encode.length());
        System.out.println(encode + " : "+Arrays.toString(encode.getBytes()));
        String decode = decode(encode, xorByte);
        System.out.println(decode + "  : "+decode.length());
    }

    public static String encode(String input, byte xorByte) {
        byte[] bytes = input.getBytes();
        char[] ch = new char[bytes.length / 2];
        int index = 0;
        for (int i = 0; i < bytes.length; i += 2) {
            byte b1 = (byte) (bytes[i] ^ xorByte);
            byte b2 = (byte) (bytes[i + 1] ^ xorByte);
            ch[index] = (char) b1;
            ch[index] = (char) (ch[index] << 8);
            char tmp = (char) b2;
            ch[index] = (char) (ch[index] | tmp);
            index++;
        }
        return new String(ch);
    }

    public static String decode(String input, byte xorByte) {
        char[] ch = input.toCharArray();
        byte[] b = new byte[ch.length * 2];
        int index = 0;
        for (char c : ch) {
            b[index++] = (byte) (((byte) (c >> 8)) ^ xorByte);
            b[index++] = (byte) (((byte) ((c << 8) >> 8)) ^ xorByte);
        }
        return new String(b);
    }
}
