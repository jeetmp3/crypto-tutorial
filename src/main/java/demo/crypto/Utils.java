package demo.crypto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Jitendra Singh.
 */
public class Utils {

    public static final int UNSIGNED_BYTE = 0xFF;
    private static Map<Character, Byte> hexMap;
    private static Map<Byte, Character> reverseHexMap;
    private static Map<Byte, Character> base64Map;
    private static Map<Character, Byte> reverseBase64Map;
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random r = new Random();

    static {
        initializeHexMap();
        initializeBase64Map();
    }

    private static void initializeHexMap() {
        hexMap = new LinkedHashMap<>();
        reverseHexMap = new LinkedHashMap<>();
        byte index = 0;
        for (char i = '0'; i <= '9'; i++) {
            hexMap.put(i, index);
            reverseHexMap.put(index, i);
            index++;
        }
        for (char i = 'A'; i <= 'F'; i++) {
            hexMap.put(i, index);
            reverseHexMap.put(index, i);
            index++;
        }
    }

    private static void initializeBase64Map() {
        base64Map = new LinkedHashMap<>();
        reverseBase64Map = new LinkedHashMap<>();
        byte index = 0;
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            base64Map.put(index, ch);
            reverseBase64Map.put(ch, index++);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            base64Map.put(index, ch);
            reverseBase64Map.put(ch, index++);
        }
        for (char ch = '0'; ch <= '9'; ch++) {
            base64Map.put(index, ch);
            reverseBase64Map.put(ch, index++);
        }
        base64Map.put(index, '+');
        reverseBase64Map.put('+', index++);
        base64Map.put(index, '/');
        reverseBase64Map.put('/', index);
    }

    public static Character byteToChar(byte value) {
        return reverseHexMap.get(value);
    }

    public static byte[] hexToBytes(String hex) {
        if (hex == null || "".equals(hex)) {
            return null;
        }
        int half = hex.length() / 2;
        int size = (hex.length() % 2 == 0) ? half : half + 1;
        byte[] result = new byte[size];
        int index = 0;
        for (int i = 0; i < hex.length(); i += 2) {
            char ch1 = hex.charAt(i);
            char ch2 = (i + 1) == hex.length() ? '0' : hex.charAt(i + 1);
            byte number1 = hexMap.get(Character.toUpperCase(ch1));
            byte number2 = hexMap.get(Character.toUpperCase(ch2));
            number1 = (byte) (number1 << 4);
            result[index++] = (byte) (number1 | number2);
        }
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(bytesToHex(bytes[i]));
        }
        return builder.toString();
    }

    public static String bytesToHex(byte input) {
        byte leftBits = firstN(input, 4);
        byte rightBits = lastN(input, 4);
        return Character.toLowerCase(reverseHexMap.get(leftBits)) + "" + Character.toLowerCase(reverseHexMap.get(rightBits));
    }

    public static String encodeBase64(byte[] data) {
        if (data != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.length; i += 3) {
                byte b1 = data[i];
                if ((i + 1) < data.length) {
                    byte b2 = data[i + 1];
                    if ((i + 2) < data.length) {
                        byte b3 = data[i + 2];

                        byte ind1 = firstN(b1, 6);
                        byte ind2 = octet(b1, 2, b2, 4);
                        byte ind3 = octet(b2, 4, b3, 2);
                        byte ind4 = lastN(b3, 6);
                        builder.append(base64Map.get(ind1)).append(base64Map.get(ind2))
                                .append(base64Map.get(ind3)).append(base64Map.get(ind4));
                    } else {
                        byte ind1 = firstN(b1, 6);
                        byte ind2 = octet(b1, 2, b2, 4);
                        byte ind3 = octet(b2, 4, (byte) 0, 2);
                        builder.append(base64Map.get(ind1)).append(base64Map.get(ind2))
                                .append(base64Map.get(ind3)).append('=');
                    }
                } else {
                    byte ind1 = firstN(b1, 6);
                    // 01001010 => (n << 4) => 10100000 => (n & 0x30)
                    byte ind2 = (byte) (((byte) (b1 << 4)) & 0x30);
                    builder.append(base64Map.get(ind1)).append(base64Map.get(ind2)).append("==");
                }
            }
            return builder.toString();
        }
        return "";
    }

    public static byte[] decodeBase64(String data) {
        if (data != null) {
            if (data.length() % 4 != 0) {
                throw new IllegalArgumentException("Bad padding");
            }
            int padding = data.endsWith("==") ? 2 : (data.endsWith("=") ? 1 : 0);
            byte[] result = new byte[((data.length() - padding) * 3) / 4];
            int index = 0;
            for (int i = 0; i < data.length(); i += 4) {
                byte b1 = reverseBase64Map.get(data.charAt(i));
                byte b2 = reverseBase64Map.get(data.charAt(i + 1));
                char padding1 = data.charAt(i + 2);
                char padding2 = data.charAt(i + 3);

                result[index++] = ascii(b1, 6, shiftLeftNbit(b2, 2), 2);

                if (padding1 != '=') {
                    byte b3 = reverseBase64Map.get(padding1);
                    result[index++] = ascii(b2, 4, shiftLeftNbit(b3, 2), 4);
                    if (padding2 != '=') {
                        byte b4 = reverseBase64Map.get(padding2);
                        result[index++] = ascii(b3, 2, shiftLeftNbit(b4, 2), 6);
                    }
                }
            }
            return result;
        }
        return null;
    }

    public static byte lastN(byte n, int totalLast) {
        byte shiftLeft = (byte) ((0xff & n) << (8 - totalLast));
        return (byte) ((0xff & shiftLeft) >> (8 - totalLast));
    }

    public static byte shiftLeftNbit(byte n, int totalBits) {
        return (byte) ((0xff & n) << totalBits);
    }

    public static byte firstN(byte n, int totalFirst) {
        return (byte) ((0xff & n) >> (8 - totalFirst));
    }

    public static byte octet(byte first, int totalLast, byte second, int totalFirst) {
        byte left = lastN(first, totalLast);
        byte right = firstN(second, totalFirst);
        byte adjustLeft = shiftLeftNbit(left, (6 - totalLast));
        return (byte) (adjustLeft | right);
    }

    public static byte ascii(byte first, int totalLast, byte second, int totalFirst) {
        byte left = lastN(first, totalLast);
        byte right = firstN(second, totalFirst);
        byte adjustLeft = shiftLeftNbit(left, (8 - totalLast));
        return (byte) (adjustLeft | right);
    }

    public static String bytesToString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte d : data) {
            builder.append((char) d);
        }
        return builder.toString();
    }

    public static byte[] xor(String hexString1, String hexString2) {
        byte[] b1 = hexToBytes(hexString1);
        byte[] b2 = hexToBytes(hexString2);
        return xor(b1, b2);
    }

    public static byte[] xor(byte[] b1, byte[] b2) {
        byte[] result = new byte[b1.length];
        for (int i = 0; i < b1.length; i++) {
            result[i] = (byte) (b1[i] ^ b2[i]);
        }
        return result;
    }

    public static byte[] xor(byte[] b1, char ch) {
        byte[] result = new byte[b1.length];
        for (int i = 0; i < b1.length; i++) {
            result[i] = (byte) (b1[i] ^ ch);
        }
        return result;
    }

    public static byte[] repeatingKeyXOR(String message, String key) {
        byte[] source = message.getBytes();
        byte[] data = new byte[source.length];
        byte[] keyBytes = key.getBytes();
        for (int i = 0; i < source.length; i += keyBytes.length) {
            for (int j = 0; j < keyBytes.length; j++) {
                if ((i + j) < source.length) {
                    data[i + j] = (byte) (source[i + j] ^ keyBytes[j]);
                }
            }
        }
        return data;
    }

    public static int hammingDistance(String s1, String s2) {
        if (s1.length() != s2.length()) {
            throw new IllegalArgumentException("Length is not equal");
        }
        return hammingDistance(s1.getBytes(), s2.getBytes());
    }

    public static int hammingDistance(byte[] array1, byte[] array2) {
        int distance = 0;

        for (int i = 0; i < array1.length; i++) {
            byte b = (byte) (array1[i] ^ array2[i]);
            while (b != 0) {
                distance++;
                b = (byte) (b & (byte) (b - 1));
            }
        }
        return distance;
    }

    public static byte[][] cloneArray(byte[][] input) {
        byte[][] result = new byte[input.length][];
        for (int i = 0; i < input.length; i++) {
            result[i] = new byte[input[i].length];
            System.arraycopy(input[i], 0, result[i], 0, input[i].length);
        }
        return result;
    }

    public static byte[] cloneArray(byte[] input) {
        byte[] result = new byte[input.length];
        System.arraycopy(input, 0, result, 0, input.length);
        return result;
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(r.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
