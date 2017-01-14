package demo.crypto.algo;

import demo.crypto.Utils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Jitendra Singh.
 */
public class AESTest {

    static AES aes = AES.getInstance(EncryptionMode.AES_128_ECB);

//    @Test
    public void testEncrypt() throws Exception {
        String message = "thethisisrealone";
        String key = "thethisisrealone";
        byte[] expectedArray = {
                (byte) 130, (byte) 171, (byte) 238, 3, (byte) 187, (byte) 207, 117,
                (byte) 138, (byte) 168, 77, 123, 127, (byte) 173, 89, (byte) 228, 32
        };

        SymmetricAlogrithm aes = AES.getInstance(EncryptionMode.AES_128_ECB);
        byte[] encryptedBytes = aes.encrypt(message, key);
        assertArrayEquals(expectedArray, encryptedBytes);
    }

    @Test
    public void testDecrypt() throws Exception {
        String key = "thethisisrealone";
        byte[] inputBytes = {
                (byte) 130, (byte) 171, (byte) 238, 3, (byte) 187, (byte) 207, 117,
                (byte) 138, (byte) 168, 77, 123, 127, (byte) 173, 89, (byte) 228, 32
        };
        String expectedMessage = "thethisisrealone";

        AES aes = AES.getInstance(EncryptionMode.AES_128_ECB);
        byte[] decryptBytes = aes.decrypt(inputBytes, key.getBytes());
        assertEquals(expectedMessage, new String(decryptBytes));
    }

    @Test
    public void testEncryptExample() throws Exception {
        String expectedHex = "b64b27bb1615a6f532186cc5fa94b55e5c54ea1bdf971e3de31bfc0275227652d57bd542ba0f6850cdfd59b8eb0e83d1";
        String message = "This is a message we will encrypt with AES!";
        byte[] key = {
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        };

        SymmetricAlogrithm aes = AES.getInstance(EncryptionMode.AES_128_ECB);
        byte[] encryptedBytes = aes.encrypt(message.getBytes(), key);
        String actualHex = Utils.bytesToHex(encryptedBytes);
        assertEquals(expectedHex, actualHex.toLowerCase());
    }

//    @Test
//    public void testDecryptExample() throws Exception {
//        String expectedMessage = "This is a message we will encrypt with AES!";
//        String inputHex = "b64b27bb1615a6f532186cc5fa94b55e5c54ea1bdf971e3de31bfc0275227652d57bd542ba0f6850cdfd59b8eb0e83d1";
//        byte[] key = {
//                1, 2, 3, 4,
//                5, 6, 7, 8,
//                9, 10, 11, 12,
//                13, 14, 15, 16
//        };
//        byte[] inputBytes = Utils.hexToBytes(inputHex);
//
//        AES aes = AES.getInstance(EncryptionMode.AES_128_ECB);
//        byte[] decryptedBytes = aes.decrypt(inputBytes, key);
//        assertEquals(expectedMessage, new String(decryptedBytes));
//    }

    @Test
    public void arrayToStateMatrixTest() {
        String text = "this is real one";
        byte[][] expected = {
                {'t', ' ', 'r', ' '},
                {'h', 'i', 'e', 'o'},
                {'i', 's', 'a', 'n'},
                {'s', ' ', 'l', 'e'}
        };

        byte[][] result = AES.arrayToStateMatrix(text.getBytes());
        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
        assertArrayEquals(expected[2], result[2]);
        assertArrayEquals(expected[3], result[3]);
    }

    @Test
    public void stateMatrixToArray() {
        String text = "this is real one";
        byte[][] input = {
                {'t', ' ', 'r', ' '},
                {'h', 'i', 'e', 'o'},
                {'i', 's', 'a', 'n'},
                {'s', ' ', 'l', 'e'}
        };

        byte[] result = AES.stateMatrixToArray(input);
        assertEquals(text, new String(result));
    }

    @Test
    public void generateArrayWithPaddedDataTest() {
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, aes.generateArrayWithPaddedData(15));
        assertArrayEquals(new byte[]{0, 0, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14}, aes.generateArrayWithPaddedData(2));
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6}, aes.generateArrayWithPaddedData(10));
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5}, aes.generateArrayWithPaddedData(11));
    }
}