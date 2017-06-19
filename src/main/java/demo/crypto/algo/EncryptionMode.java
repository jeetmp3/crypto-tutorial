package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
public enum EncryptionMode {

    AES_128_ECB(10, 16, 176),
    AES_192_ECB(12, 24, 208),
    AES_256_ECB(14, 32, 240),

    AES_128_CBC(10, 16, 176),

    AES_128_CTR(10, 16, 176);

    EncryptionMode(int rounds, int keyBytes, int expandedKeyBytes) {
        this.rounds = rounds;
        this.keyBytes = keyBytes;
        this.expandedKeyBytes = expandedKeyBytes;
    }

    private int rounds;
    private int keyBytes;
    private int expandedKeyBytes;

    public int getRounds() {
        return this.rounds;
    }

    public int getKeyBytes() {
        return keyBytes;
    }

    public int getExpandedKeyBytes() {
        return expandedKeyBytes;
    }
}
