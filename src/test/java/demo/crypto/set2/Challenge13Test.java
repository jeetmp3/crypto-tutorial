package demo.crypto.set2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Jitendra Singh.
 */
public class Challenge13Test {

    @Test
    public void testParseQueryString() throws Exception {
        String qs = "foo=bar&baz=qux&zap=zazzle";

        String expected = "{" +
                "\n'foo':'bar'," +
                "\n'baz':'qux'," +
                "\n'zap':'zazzle'" +
                "\n}";
        String result = new Challenge13().parseQueryString(qs);
        assertEquals(expected, result);
    }
}