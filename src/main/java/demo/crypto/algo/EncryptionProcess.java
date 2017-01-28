package demo.crypto.algo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Jitendra Singh.
 */
public class EncryptionProcess {
    private EncryptionMode algorithm;
    private byte[] iv;

    private EncryptionProcess(EncryptionMode algorithm) {
        this.algorithm = algorithm;
    }

    public static EncryptionProcess getInstance(EncryptionMode algorithm) {
        return new EncryptionProcess(algorithm);
    }

    public EncryptionProcess initVector(String vector) {
        if (algorithm == EncryptionMode.AES_128_CBC && (vector == null || "".equals(vector))) {
            throw new NullPointerException("IV cannot be null in CBC Mode");
        }
        return initVector(vector.getBytes());
    }

    public EncryptionProcess initVector(byte[] vector) {
        if (algorithm == EncryptionMode.AES_128_CBC && vector == null) {
            throw new NullPointerException("IV cannot be null in CBC Mode");
        }
        this.iv = vector;
        return this;
    }

    public byte[] doFinal(byte[] data, byte[] expandedKey) {
        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        byte[] tmp = new byte[16];
        byte[][] previousState = null;
        for (int i = 0; i < data.length; i += 16) {
            System.arraycopy(data, i, tmp, 0, 16);
            byte[][] state = AES.arrayToStateMatrix(tmp);
            state = applyIV(state, previousState);
            state = addRoundKey(state, AES.getGeneratedKey(expandedKey, 0, 16));
            int j = 0;
            for (; j < (algorithm.getRounds() - 1); j++) {
                state = byteSubstitues(state);
                state = shiftRows(state);
                state = mixColumns(state);
                state = addRoundKey(state, AES.getGeneratedKey(expandedKey, (j + 1) * 16, 16));
            }

            state = byteSubstitues(state);
            state = shiftRows(state);
            state = addRoundKey(state, AES.getGeneratedKey(expandedKey, (j + 1) * 16, 16));
            previousState = state;
            try {
                cipherStream.write(AES.stateMatrixToArray(state));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cipherStream.toByteArray();
    }

    private byte[][] applyIV(byte[][] state, byte[][] previousState) {
        if (algorithm == EncryptionMode.AES_128_CBC) {
            if (previousState == null) {
                byte[] ivBytes = iv;
                int totalBytesToCopy = algorithm.getKeyBytes();
                byte[] key16byte = new byte[totalBytesToCopy];
                if (ivBytes.length < totalBytesToCopy) {
                    totalBytesToCopy = ivBytes.length;
                }
                System.arraycopy(ivBytes, 0, key16byte, 0, totalBytesToCopy);
                previousState = AES.arrayToStateMatrix(key16byte);
            }
            byte[][] result = new byte[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    result[i][j] = (byte) (state[i][j] ^ previousState[i][j]);
                }
            }
            return result;
        }
        return state;
    }

    /**
     * This method will substitute byte
     *
     * @param state input byte of 16 bytes
     * @return return substituted bytes by using Rijndael S Box
     */
    protected byte[][] byteSubstitues(byte[][] state) {
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            result[i] = RijndaelAlgo.applySBox(state[i]);
        }
        return result;
    }

    /**
     * This method will shift rows. Input byte sequence will be
     * state = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
     * matrix =
     * 0  4  8   12    [ row 1]
     * 1  5  9   13    [ row 2]
     * 2  6  10  14    [ row 3]
     * 3  7  11  15    [ row 4]
     * <p>
     * then shifting process will be
     * row 1 -> nothing
     * row 2 -> 1 position left
     * row 3 -> 2 position left
     * row 4 -> 3 position left
     * <p>
     * 0   4   8   12    [ row 1]
     * 5   9   13  1     [ row 2]
     * 10  14  2   6     [ row 3]
     * 15  3  7   11    [ row 4]
     *
     * @param state input array with length 16 bytes
     * @return shifted byte array
     */
    protected byte[][] shiftRows(byte[][] state) {
        byte[][] result = new byte[4][4];
        result[0] = state[0];
        for (int i = 1; i < 4; i++) {
            System.arraycopy(state[i], i, result[i], 0, 4 - i);
            System.arraycopy(state[i], 0, result[i], 4 - i, i);
        }
        return result;
    }

    /**
     * This method will mix the columns. Column mixing is the process of matrix multiplication.
     * state bytes will be multiply with galoisFieldVector.
     * <p>
     * e.g:
     * | 1 |  | 2 3 1 1 |
     * | 2 |  | 1 2 3 1 |
     * | 3 |  | 1 1 2 3 |
     * | 4 |  | 3 1 1 2 |
     * <p>
     * mulOf2[1] ^ mulOf3[2] ^ 3 ^ 4 = output byte 0
     * 1 ^ mulOf2[2] ^ mulOf3[3] ^ 4 = output byte 1
     * 1 ^ 2 ^ mulOf2[3] ^ mulOf3[4] = output byte 2
     * mulOf3[1] ^ 2 ^ 3 ^ mulOf3[4] = output byte 3
     *
     * @param state input byte array
     * @return multiplied byte
     */
    protected byte[][] mixColumns(byte[][] state) {
        byte[][] result = AES.transpose(state);
        for (int i = 0; i < 4; i++) {
            result[i] = RijndaelAlgo.mixColumn(result[i]);
        }
        return AES.transpose(result);
    }

    /**
     * This method will use to addRoundKey/Key whitening
     *
     * @param state data bytes on which round key needs to be add
     * @param key   key bytes
     * @return result after xoring the data
     */
    protected byte[][] addRoundKey(byte[][] state, byte[] key) {
        byte[][] keyMatrix = AES.arrayToStateMatrix(key);
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = (byte) (state[i][j] ^ keyMatrix[i][j]);
            }
        }
        return result;
    }
}
