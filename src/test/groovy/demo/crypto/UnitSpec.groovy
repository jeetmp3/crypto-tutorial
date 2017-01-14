package demo.crypto

import spock.lang.Specification

/**
 * @author Jitendra Singh.
 */
class UnitSpec extends Specification {

    def "hexToByteWithNullStringTest"() {
        expect:
        !Utils.hexToBytes(null)
        !Utils.hexToBytes("")
    }

    def "hexToByteTest"() {
        // 2    A
        // 0010 1010 = 00101010
        setup:
        byte[] result = Utils.hexToBytes("2A")
        byte[] expected = [42] as byte[]

        expect:
        Arrays.equals(result, expected)
    }

    def "hexToByteWith3DigitHexNumberTest"() {
        // 2    A       A                   0
        // 0010 1010    1010 = 00101010 1010 0000 = 00101010 10100000
        setup:
        byte[] result = Utils.hexToBytes("2AA")
        byte[] expected = [42, (byte) 160] as byte[]
        println new String(result);
        println Utils.bytesToString(result)
        expect:
        Arrays.equals(result, expected)
    }

    def "first n bytes test"() {
        setup:
        byte i = 105;
        byte result = Utils.firstN(i, 4);
        byte result1 = Utils.firstN((byte) 200, 6);

        expect:
        result == (byte) 6
        result1 == (byte) 50
    }

    def "last n bytes test"() {
        setup:
        byte r1 = Utils.lastN((byte) 105, 5);
        byte r2 = Utils.lastN((byte) 200, 6);
        byte r3 = Utils.lastN((byte) 220, 3);
        byte r4 = Utils.lastN((byte) 222, 5);

        expect:
        r1 == (byte) 9
        r2 == (byte) 8
        r3 == (byte) 4
        r4 == (byte) 30
    }

    def "merge 2 bytes and get 6 bit ans test"() {
        setup:
        byte i = 105;
        byte j = 200;

        byte ans = Utils.octet(i, 4, j, 2);

        expect:
        ans == (byte) 39
    }

    def "encode base64 test"() {
        setup:
        String e1 = Utils.encodeBase64("jittu".bytes);
        String e2 = Utils.encodeBase64("jitt".bytes);

        expect:
        e1 == "aml0dHU=";
        e2 == "aml0dA==";
    }

    def "base64 to string test"() {
        setup:
        String base64 = "aml0dHU=";
        String jitt = "aml0dA==";
        byte []bytes = Utils.decodeBase64(base64)
        byte []bytes1 = Utils.decodeBase64(jitt);
        expect:
        "jittu" == new String(bytes);
        "jitt" == new String(bytes1);
    }

    def "test hamming distance"() {
        setup:
        String s1 = "t"; //116 = 01110100
        String s2 = "!"; // 33 = 00100001
        int expectedResult = 4; // 01010101

        when:
        int actual = Utils.hammingDistance(s1, s2)

        then:
        expectedResult == actual
    }

    def "test hamming distance for multi char string"() {
        setup:
        String s1 = "this is a test";
        String s2 = "wokka wokka!!!";
        int expectedResult = 37;

        when:
        int actual = Utils.hammingDistance(s1, s2)

        then:
        expectedResult == actual
    }
}
