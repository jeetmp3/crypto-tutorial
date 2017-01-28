package demo.crypto.algo;

import demo.crypto.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jitendra Singh.
 */
public class AES128CBCModeTest {

//    @Test
    public void testEncrypt() throws Exception {
        AES aes = AES.getInstance(EncryptionMode.AES_128_CBC);
        String message = "thethisisrealone";
        String key = "thethisisrealone";
        String iv = "1234";

        String expectedHex = "e5e3e8c934c17551f4891db16dfcadef520132ae2d10223bcb833996ee807d3c";
        byte[] result = aes.encrypt(message, key, iv);
        String hex = Utils.bytesToHex(result);
        assertEquals(expectedHex, hex);
    }

    @Test
    public void testDecrypt() throws Exception {
        AES aes = AES.getInstance(EncryptionMode.AES_128_CBC);
        String input = "e5e3e8c934c17551f4891db16dfcadef";
        String iv = "1234";
        String key = "thethisisrealone";

        String expectedMessage = "thethisisrealone";

        byte[] result = aes.encrypt(Utils.hexToBytes(input), key.getBytes(), iv.getBytes());
        String message = new String(result);
        assertEquals(expectedMessage, message);
    }
}