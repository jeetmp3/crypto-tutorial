package demo.crypto.set1

import spock.lang.Specification

/**
 * @author Jitendra Singh.
 */
class Challenge1Spec extends Specification {

    def "ConvertHexToBase64"() {
        String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String expectedResult = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";

        when:
        String result = new Challenge1().convertHexToBase64(input);

        then:
        result == expectedResult
    }
}
