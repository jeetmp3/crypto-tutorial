package demo.crypto.algo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jitendra Singh.
 */
public class BlockCipherDecryptionProcessTest {

    BlockCipherDecryptionProcess dp = BlockCipherDecryptionProcess.getInstance(EncryptionMode.AES_128_ECB);
    @Test
    public void inverseShiftRowTest() {
        byte[][] state = {
                {1, 2, 3, 4},
                {6, 7, 8, 5},
                {11, 12, 9, 10},
                {16, 13, 14, 15}
        };

        byte[][] expected = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        byte[][] result = dp.inverseShiftRows(state);
        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
        assertArrayEquals(expected[2], result[2]);
        assertArrayEquals(expected[3], result[3]);
    }
}