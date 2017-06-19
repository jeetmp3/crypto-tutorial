package demo.crypto.algo;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class AES128CTRMode extends AES {

    @Override
    public byte[] encrypt(byte[] message, byte[] key, byte[] nonce) {
        key = keyExpansion(key);
        return BlockToStreamCipherEncryptionProcess.getInstance(getEncryptionMode())
                .initVector(nonce)
                .doFinal(message, key);
    }

    @Override
    public byte[] decrypt(byte[] message, byte[] key, byte[] nonce) {
        key = keyExpansion(key);
        return BlockToStreamCipherEncryptionProcess.getInstance(getEncryptionMode())
                .initVector(nonce)
                .doFinal(message, key);
    }

    @Override
    public EncryptionMode getEncryptionMode() {
        return EncryptionMode.AES_128_CTR;
    }

    public static void main(String[] args) {
        AES aes = AES.getInstance(EncryptionMode.AES_128_CTR);
        String str = "L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ==";
        byte[] nonce = {
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00
        };
        String key = "YELLOW SUBMARINE";
        byte[] array = aes.decrypt(Utils.decodeBase64(str), key.getBytes(), nonce);
        System.out.println(new String(array));

        String msg = "Yo, VIP Let's kick it Ice, Ice, baby Ice, Ice, baby ";
        byte[] result = aes.encrypt(msg.getBytes(), key.getBytes(), nonce);
        System.out.println(str);
        System.out.println(Utils.encodeBase64(result));
    }

//    @Override
//    protected byte[] keyExpansion(byte[] key) {
//        byte[] finalKey = new byte[16];
//        System.arraycopy(key, 0, finalKey, 0, key.length);
//        return finalKey;
//    }
}
