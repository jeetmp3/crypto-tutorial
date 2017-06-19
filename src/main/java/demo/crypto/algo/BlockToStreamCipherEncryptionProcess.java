package demo.crypto.algo;

import demo.crypto.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jitendra Singh.
 */
public class BlockToStreamCipherEncryptionProcess extends BlockCipherEncryptionProcess {
    private int counter = 0;

    protected BlockToStreamCipherEncryptionProcess(EncryptionMode algorithm) {
        super(algorithm);
    }

    public static BlockToStreamCipherEncryptionProcess getInstance(EncryptionMode algorithm) {
        return new BlockToStreamCipherEncryptionProcess(algorithm);
    }

    static byte[] merge(byte[] nonce, int counter) {
        byte[] finalNonce = new byte[16];
        System.arraycopy(nonce, 0, finalNonce, 0, nonce.length);
        StringBuilder counterStr = new StringBuilder(Integer.toHexString(counter));
        counterStr.reverse();
        List<String> tokens = AES.breakHexCipherList(counterStr.toString(), 1);

        int index = nonce.length;
        for (String token : tokens) {
            finalNonce[index++] = Byte.valueOf(token, 16);
        }
        System.out.println(Arrays.toString(finalNonce));
        return finalNonce;
    }

    public byte[] doFinal(byte[] data, byte[] expandedKey) {
        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        byte[][] previousState = null;
        for (int i = 0; i < data.length; i += 16) {
            int bytesToCopy = (i + 16) < data.length ? 16 : data.length - i;
            byte[] messageDataBlock = new byte[bytesToCopy];
            System.arraycopy(data, i, messageDataBlock, 0, bytesToCopy);
            byte[] tmp = merge(getIv(), counter++);
            byte[][] state = AES.arrayToStateMatrix(tmp);
            state = applyIV(state, previousState);
            state = addRoundKey(state, AES.getGeneratedKey(expandedKey, 0, 16));
            int j = 0;
            for (; j < (getAlgorithm().getRounds() - 1); j++) {
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
                byte[] result = AES.stateMatrixToArray(state);
                cipherStream.write(Utils.xor(messageDataBlock, result));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cipherStream.toByteArray();
    }
}
