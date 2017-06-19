package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
public class AES128CBCMode extends AES {

    @Override
    public byte[] encrypt(byte[] message, byte[] key, byte[] iv) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return BlockCipherEncryptionProcess.getInstance(getEncryptionMode()).initVector(iv).doFinal(data, expandedKey);
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key, byte[] iv) {
        byte[] expandedKey = keyExpansion(key);
        return BlockCipherDecryptionProcess.getInstance(getEncryptionMode()).initVector(iv).doFinal(message, expandedKey);
    }

    @Override
    public EncryptionMode getEncryptionMode() {
        return EncryptionMode.AES_128_CBC;
    }
}
