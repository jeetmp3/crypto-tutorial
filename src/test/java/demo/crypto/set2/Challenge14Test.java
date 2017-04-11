package demo.crypto.set2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Jitendra Singh.
 */
@RunWith(Parameterized.class)
public class Challenge14Test {

    private int randomStrSize;
    private int blockNumber;

    @Parameterized.Parameters
    public static Collection<Integer[]> parameters() {
        int MAX_TESTS = 1000;
        Integer[][] params = new Integer[MAX_TESTS][];
        for (int i = 1; i <= MAX_TESTS; i++) {
            params[i - 1] = new Integer[]{i};
        }
        return Arrays.asList(params);
    }

    public Challenge14Test(int randomStrSize) {
        this.blockNumber = this.randomStrSize = randomStrSize;
    }

    @Test
    public void testGuessRandomByteSizeTestMethod() throws Exception {
        assertEquals(blockNumber, new Challenge14().guessRandomByteSize());
    }
}