package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
public class AES128CBCMode extends AES {

    private EncryptionMode algorithm = EncryptionMode.AES_128_CBC;

    @Override
    public byte[] encrypt(byte[] message, byte[] key, byte[] iv) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return EncryptionProcess.getInstance(algorithm).initVector(iv).doFinal(data, expandedKey);
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key, byte[] iv) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return DecryptionProcess.getInstance(algorithm).initVector(iv).doFinal(data, expandedKey);
    }
}
