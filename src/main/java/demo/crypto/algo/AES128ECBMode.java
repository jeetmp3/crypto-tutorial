package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
class AES128ECBMode extends AES {

    private EncryptionMode algorithm = EncryptionMode.AES_128_ECB;

    @Override
    protected byte[] keyExpansion(byte[] key) {
        byte[] key16byte = new byte[algorithm.getKeyBytes()];
        int totalBytesToCopy = algorithm.getKeyBytes();
        if (key.length < totalBytesToCopy) {
            totalBytesToCopy = key.length;
        }
        System.arraycopy(key, 0, key16byte, 0, totalBytesToCopy);
        return RijndaelAlgo.keySchedule(key16byte, EncryptionMode.AES_128_ECB);
    }

    @Override
    public byte[] encrypt(byte[] message, byte[] key) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return EncryptionProcess.getInstance(algorithm).doFinal(data, expandedKey);
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key) {
        byte[] expandedKey = keyExpansion(key);
        byte[] data = paddedData(message);
        return DecryptionProcess.getInstance(algorithm).doFinal(data, expandedKey);
    }
}
