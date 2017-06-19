package demo.crypto.algo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Jitendra Singh.
 */
public class BlockToStreamCipherDecryptionProcess extends BlockCipherDecryptionProcess {
    private int counter = 0;

    private BlockToStreamCipherDecryptionProcess(EncryptionMode algo) {
        super(algo);
    }

    public static BlockToStreamCipherDecryptionProcess getInstance(EncryptionMode algorithm) {
        return new BlockToStreamCipherDecryptionProcess(algorithm);
    }

    public byte[] doFinal(byte[] data, byte[] key) {
        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        int index = 0;
        int total = data.length / 16;
        byte[][] previousCipher = null;
        for (int i = 0; i < data.length; i += 16) {
            index++;
            byte[] messageDataBlock = new byte[16];
            System.arraycopy(data, i, messageDataBlock, 0, 16);
            byte[] tmp = BlockToStreamCipherEncryptionProcess.merge(getIv(), counter++);
            byte[][] state = AES.arrayToStateMatrix(tmp);
            int j = (getAlgorithm().getRounds() - 1);
            state = inverseAddRoundKey(state, key);
            state = inverseShiftRows(state);
            state = inverseByteSubstitues(state);
            for (; j > 0; j--) {
                state = inverseAddRoundKey(state, key);
                state = inverseMixColumns(state);
                state = inverseShiftRows(state);
                state = inverseByteSubstitues(state);
            }

            state = inverseAddRoundKey(state, key);
//            state = applyIV(state, previousCipher);
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
}
