package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
public interface SymmetricAlogrithm {

    byte[] encrypt(String message, String key);
    byte[] encrypt(String message, String key, String iv);
    byte[] encrypt(byte[] message, byte[] key);
    byte[] encrypt(byte[] message, byte[] key, byte[] iv);

    byte[] decrypt(String message, String key);
    byte[] decrypt(String message, String key, String iv);
    byte[] decrypt(byte[] message, byte[] key);
    byte[] decrypt(byte[] message, byte[] key, byte[] iv);
}
