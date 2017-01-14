package demo.crypto.algo;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Jitendra Singh.
 */
public class EncryptionProcessTest {

    EncryptionProcess ep = EncryptionProcess.getInstance(EncryptionMode.AES_128_ECB);
    /**
     * {2, 3, 1, 1},
     * {1, 2, 3, 1},
     * {1, 1, 2, 3},
     * {3, 1, 1, 2}
     */
    @Test
    public void mixColumnsTest() {
        byte[][] input = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        byte[][] result = ep.mixColumns(input);
        assertEquals(9, result[0][0]);
        assertEquals(29, result[1][0]);
        assertEquals(1, result[2][0]);
        assertEquals(21, result[3][0]);
    }

    @Test
    public void shiftRowTest() {
        byte[][] state = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        byte[][] expected = {
                {1, 2, 3, 4},
                {6, 7, 8, 5},
                {11, 12, 9, 10},
                {16, 13, 14, 15}
        };

        byte[][] result = ep.shiftRows(state);
        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
        assertArrayEquals(expected[2], result[2]);
        assertArrayEquals(expected[3], result[3]);
    }
}
