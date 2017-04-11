package demo.crypto.set1

import spock.lang.Specification

/**
 * @author Jitendra Singh.
 */
class Challenge2Spec extends Specification {
    def "FixedXor"() {
        given:
        String s1 = "1c0111001f010100061a024b53535009181c";
        String s2 = "686974207468652062756c6c277320657965";

        String expected = "746865206b696420646f6e277420706c6179";

        when:
        String result = new Challenge2().fixedXor(s1, s2);

        then:
        result == expected
    }
}
