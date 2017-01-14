package demo.crypto.set1

import demo.crypto.Utils
import spock.lang.Specification

/**
 * @author Jitendra Singh.
 */
class Challenge6Spec extends Specification {

    def "test guess key size"() {
        byte[] data = encryptString("few")
        expect:
        Challenge6.guessKeySize(data) == 16
    }

    private byte[] encryptString(String key) {
        return Utils.repeatingKeyXOR("fuse fuel for falling flocks", key)
    }

}
