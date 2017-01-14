package demo.crypto;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jitendra Singh.
 */
public class UtilsTest {

    @Test
    public void hexToByteWithNullStringTest() {
        assert Utils.hexToBytes(null) == null;
        assert Utils.hexToBytes("") == null;
    }

    @Test
    public void hexToByteTest() {
        // 2    A
        // 0010 1010 = 00101010
        byte[] result = Utils.hexToBytes("2A");
        byte[] expected = new byte[]{42};
        assert Arrays.equals(result, expected);
    }
}
