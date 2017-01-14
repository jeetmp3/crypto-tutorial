package demo.crypto.set1;

import demo.crypto.URLUtils;
import demo.crypto.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * @author Jitendra Singh.
 */
public class Challenge6 {

    static Challenge3 challenge3 = new Challenge3();

    public static void main(String[] args) throws IOException {
        List<String> lines = URLUtils.readLines("http://cryptopals.com/static/challenge-data/6.txt");
        for (String line : lines) {
            System.out.println(decodeMessage(line));
        }
//        System.out.println(decodeMessage("Iyo9Nzw="));
//        System.out.println(decodeMessage("Iyo9JicnOyI="));
//        System.out.println(decodeMessage("Iyo9JicnOyI="));

    }

    public static String decodeMessage(String decodedString) {
        byte[] data = Utils.decodeBase64(decodedString);
        int keySize = guessKeySize(data);
        StringBuilder exactKey = new StringBuilder();
        for (int i = 0; i < keySize; i++) {
            List<Byte> singleKeyXorBytes = new ArrayList<>();
            for (int j = i; j < data.length; j += keySize) {
                if (j < data.length)
                    singleKeyXorBytes.add(data[j]);
            }
            String hex = Utils.bytesToHex(byteArray(singleKeyXorBytes));
            exactKey.append(challenge3.singleByteXorCipher(hex));
        }
//        System.out.println("XOR Key is: " + exactKey.toString());
//        System.out.println(new String(Utils.repeatingKeyXOR(new String(data), exactKey.toString())));
//        System.out.println(new String(Utils.repeatingKeyXOR(new String(data), "IC")));
        return "Guessed key: \"" + exactKey.toString() + "\" = " + new String(Utils.repeatingKeyXOR(new String(data), exactKey.toString()));
    }

    private static byte[] byteArray(List<Byte> bytes) {
        byte[] array = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            array[i] = bytes.get(i);
        }
        return array;
    }

    public static int guessKeySize(byte[] data) {
        float distance = Float.MAX_VALUE;
        int total = data.length / 2;
        int keySize = 0;
        for (int keyLength = 2; keyLength <= total; keyLength++) {
            float tmpDistance = normalizedDistance(data, keyLength);
            if (tmpDistance < distance) {
                distance = tmpDistance;
                keySize = keyLength;
            }
        }
        return keySize;
    }

    public static float normalizedDistance(byte[] data, int keysize) {
        float normalized = 0;

        byte[] b1 = new byte[keysize];
        byte[] b2 = new byte[keysize];
        List<Float> hammingDistances = new ArrayList<>();
        for (int i = 0; i < data.length; i += keysize) {
            if ((i + (keysize * 2)) < data.length) {
                System.arraycopy(data, i, b1, 0, keysize);
                System.arraycopy(data, i + keysize, b2, 0, keysize);
            } else {
                b1 = new byte[keysize];
                b2 = new byte[keysize];
                int total = data.length - i;
                if (total > 0) {
                    if (total > keysize) {
                        System.arraycopy(data, i, b1, 0, keysize);
                        System.arraycopy(data, i + keysize, b2, 0, (total - keysize));
                    } else {
                        System.arraycopy(data, i, b1, 0, total);
                    }
                }
            }

            hammingDistances.add(Utils.hammingDistance(b1, b2) * 1.0f);
        }
        OptionalDouble result = hammingDistances.stream().mapToDouble(Float::doubleValue).average();
        if (result.isPresent()) {
            normalized = new Double(result.getAsDouble()).floatValue();
        }
        System.out.println("Checking keySize: " + keysize + " - normalized distance: " + (normalized / keysize) +" - distance "+(normalized % keysize));
        return (normalized / keysize);
    }

    private static char guessKey(byte[] dataBytes) {
        int maxFrequency = 0;
        char key = '\0';
        for (char c = '0'; c <= 'z'; c++) {
            String decoded1 = new String(Utils.xor(dataBytes, c));
            int tmpFrequency = challenge3.getFrequency(decoded1);
            if (tmpFrequency > maxFrequency) {
                maxFrequency = tmpFrequency;
                key = c;
            }
        }
        return key;
    }

    private static byte[] getBytes(byte[] source, int keySize, int start) {
        List<Byte> byteList = new ArrayList<>();
        for (int i = start; i < source.length; i += keySize) {
            byteList.add(source[i]);
        }

        byte[] result = new byte[byteList.size()];
        int index = 0;
        for (byte b : byteList) {
            result[index++] = b;
        }
        return result;
    }

}
