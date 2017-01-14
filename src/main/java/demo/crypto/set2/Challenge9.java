package demo.crypto.set2;

import demo.crypto.algo.AES;

import java.util.Arrays;

/**
 * @author Jitendra Singh.
 */
public class Challenge9 {

    public static void main(String[] args) {
        String text = "YELLOW SUBMARINE";
        byte[] paddedData = AES.generateArrayWithPaddedData(text.getBytes().length, 20);
        System.arraycopy(text.getBytes(), 0, paddedData, 0, text.getBytes().length);
        System.out.println(Arrays.toString(paddedData));
        System.out.println(new String(paddedData));
    }
}
