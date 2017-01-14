package demo.crypto.algo;

/**
 * @author Jitendra Singh.
 */
public interface SymmetricAlogrithm {

    byte[] encrypt(String message, String key);
    byte[] encrypt(byte[] message, byte[] key);

    byte[] decrypt(String message, String key);
    byte[] decrypt(byte[] message, byte[] key);
}
