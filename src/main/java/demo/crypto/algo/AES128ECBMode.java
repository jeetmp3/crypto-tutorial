package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
class AES128ECBMode extends AES {

    @Override
    public byte[] encrypt(byte[] message, byte[] key) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return BlockCipherEncryptionProcess.getInstance(getEncryptionMode()).doFinal(data, expandedKey);
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return BlockCipherDecryptionProcess.getInstance(getEncryptionMode()).doFinal(data, expandedKey);
    }

    @Override
    public EncryptionMode getEncryptionMode() {
        return EncryptionMode.AES_128_ECB;
    }
}
