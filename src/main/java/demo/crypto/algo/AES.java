package demo.crypto.algo;

import demo.crypto.Utils;

import java.util.*;

/**
 * @author Jitendra Singh.
 */
public abstract class AES implements SymmetricAlogrithm {

    private static byte[][] galoisFieldVector = {
            {2, 3, 1, 1},
            {1, 2, 3, 1},
            {1, 1, 2, 3},
            {3, 1, 1, 2}
    };

    public static AES getInstance(EncryptionMode mode) {
        AES aes = null;
        switch (mode) {
            case AES_128_ECB:
                aes = new AES128ECBMode();
                break;
            case AES_192_ECB:
            case AES_256_ECB:
                break;

        }
        return aes;
    }

    @Override
    public byte[] encrypt(String message, String key) {
        return encrypt(message.getBytes(), key.getBytes());
    }

    @Override
    public byte[] encrypt(byte[] message, byte[] key) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(String message, String key) {
        return decrypt(message.getBytes(), key.getBytes());
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key) {
        return new byte[0];
    }

    /**
     * This method will ensure data padding
     *
     * @param data input data to encrypt
     * @return padded data
     */
    protected byte[] paddedData(byte[] data) {
        byte[] rawBytes;
        if (data.length % 16 != 0) {
            rawBytes = generateArrayWithPaddedData(data.length, 16);
        } else {
            rawBytes = new byte[data.length];
        }
        System.arraycopy(data, 0, rawBytes, 0, data.length);
        return rawBytes;
    }

    public static byte[] generateArrayWithPaddedData(int length, int blockSize) {
        byte paddedByte = (byte) (blockSize - (length % blockSize));
        byte[] rawBytes = new byte[(length / blockSize + 1) * blockSize];
        Arrays.fill(rawBytes, length, rawBytes.length, paddedByte);
        return rawBytes;
    }

    protected static byte[] removePaddedData(byte[] input) {
        byte lastValue = input[15];
        if (lastValue > 1) {
            boolean paddingApplied = true;
            for (int i = 15; i > (15 - lastValue); i--) {
                if (input[i] != lastValue) {
                    paddingApplied = false;
                    break;
                }
            }
            if (paddingApplied) {
                byte[] result = new byte[16 - lastValue];
                System.arraycopy(input, 0, result, 0, result.length);
                return result;
            }
        }

        return input;
    }

    /**
     * This method will take input key and it'll expand it for further use
     *
     * @param key input key from user
     * @return expanded key
     */
    protected abstract byte[] keyExpansion(byte[] key);

    /**
     * This method will build state in matrix form. e.g:
     * input = "this-is-real-one"
     * output matrix =
     * t  -  r  -
     * h  i  e  o
     * i  s  a  n
     * s  -  l  e
     *
     * @param input
     * @return
     */
    protected static byte[][] arrayToStateMatrix(byte[] input) {
        int index = 0;
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j][i] = input[index++];
            }
        }
        return result;
    }

    /**
     * input
     * ----------------------
     * t  -  r  -
     * h  i  e  o
     * i  s  a  n
     * s  -  l  e
     * ======================
     * output
     * ----------------------
     * this-is-real-one
     *
     * @param matrix input matrix
     * @return array of bytes
     */
    protected static byte[] stateMatrixToArray(byte[][] matrix) {
        byte[] result = new byte[16];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[index++] = matrix[j][i];
            }
        }
        return result;
    }

    protected static byte[][] transpose(byte[][] input) {
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = input[j][i];
            }
        }
        return result;
    }

    protected static byte[] getGeneratedKey(byte[] expandedKey, int start, int max) {
        byte[] key = new byte[max];
        System.arraycopy(expandedKey, start, key, 0, max);
        return key;
    }

    /**
     * check if AES-ECB mode detected in cipher text
     *
     * @param cipher hex encoded string
     * @return true if ecb mode detected
     */
    public static boolean checkECB(String cipher) {
        List<String> ciphers = breakHexCipherList(cipher);
        Set<String> set = new HashSet<>();
        boolean ecbDetected = false;
        for(String c : ciphers) {
            if(!set.add(c)) {
                ecbDetected = true;
            }
        }
        return ecbDetected;
    }

    private static List<String> breakHexCipherList(String cipher) {
        return Arrays.asList(cipher.split("(?<=\\G.{32})"));
    }
}
