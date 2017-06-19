package demo.crypto.algo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Jitendra Singh.
 */
public class BlockCipherDecryptionProcess {

    private EncryptionMode algorithm;
    private byte[] iv;

    protected BlockCipherDecryptionProcess(EncryptionMode algo) {
        this.algorithm = algo;
    }

    public static BlockCipherDecryptionProcess getInstance(EncryptionMode algorithm) {
        return new BlockCipherDecryptionProcess(algorithm);
    }

    public EncryptionMode getAlgorithm() {
        return algorithm;
    }

    public byte[] getIv() {
        return iv;
    }

    public BlockCipherDecryptionProcess initVector(byte[] vector) {
        if (algorithm == EncryptionMode.AES_128_CBC && vector == null) {
            throw new NullPointerException("IV cannot be null in CBC Mode");
        }
        this.iv = vector;
        return this;
    }

    public byte[] doFinal(byte[] data, byte[] expandedKey) {
        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        byte[] tmp = new byte[16];
        int index = 0;
        int total = data.length / 16;
        byte[][] previousCipher = null;
        for (int i = 0; i < data.length; i += 16) {
            index++;
            System.arraycopy(data, i, tmp, 0, 16);
            byte[][] state = AES.arrayToStateMatrix(tmp);
            int j = (algorithm.getRounds() - 1);
            state = inverseAddRoundKey(state, AES.getGeneratedKey(expandedKey, (j + 1) * 16, 16));
            state = inverseShiftRows(state);
            state = inverseByteSubstitues(state);
            for (; j > 0; j--) {
                state = inverseAddRoundKey(state, AES.getGeneratedKey(expandedKey, j * 16, 16));
                state = inverseMixColumns(state);
                state = inverseShiftRows(state);
                state = inverseByteSubstitues(state);
            }

            state = inverseAddRoundKey(state, AES.getGeneratedKey(expandedKey, 0, 16));
            state = applyIV(state, previousCipher);
            previousCipher = AES.arrayToStateMatrix(tmp);
            byte[] result = AES.stateMatrixToArray(state);
//            if(index == total) {
//                result = AES.removePaddedData(result);
//            }
            try {
                cipherStream.write(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cipherStream.toByteArray();
    }

    /**
     * This method will use to addRoundKey/Key whitening
     *
     * @param state data bytes on which round key needs to be add
     * @param key   key bytes
     * @return result after xoring the data
     */
    protected byte[][] inverseAddRoundKey(byte[][] state, byte[] key) {
        byte[][] keyMatrix = AES.arrayToStateMatrix(key);
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = (byte) (state[i][j] ^ keyMatrix[i][j]);
            }
        }
        return result;
    }

    /**
     * This method will inverse of substitute byte ( {@link BlockCipherEncryptionProcess#byteSubstitues(byte[][])} )
     *
     * @param state input byte of 16 bytes
     * @return return substituted bytes by using Rijndael S Box
     */
    protected byte[][] inverseByteSubstitues(byte[][] state) {
        byte[][] result = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            result[i] = RijndaelAlgo.applyInvSBox(state[i]);
        }
        return result;
    }

    /**
     * Inverse of {@link BlockCipherEncryptionProcess#shiftRows(byte[][])}
     *
     * @param state
     * @return
     */
    protected byte[][] inverseShiftRows(byte[][] state) {
        byte[][] result = new byte[4][4];
        result[0] = state[0];
        for (int i = 1; i < 4; i++) {
            System.arraycopy(state[i], 4 - i, result[i], 0, i);
            System.arraycopy(state[i], 0, result[i], i, 4 - i);
        }
        return result;
    }

    /**
     * Inverse of {@link BlockCipherEncryptionProcess#mixColumns(byte[][])}
     *
     * @param state
     * @return
     */
    protected byte[][] inverseMixColumns(byte[][] state) {
        byte[][] result = AES.transpose(state);
        for (int i = 0; i < 4; i++) {
            result[i] = RijndaelAlgo.invMixColumn(result[i]);
        }
        return AES.transpose(result);
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
}
