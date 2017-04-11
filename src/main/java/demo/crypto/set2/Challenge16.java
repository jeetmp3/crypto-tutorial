package demo.crypto.set2;

import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.util.List;

/**
 * @author Jitendra Singh.
 */
public class Challenge16 {

    private static String randomKey = Challenge14.generateRandomString(16);
    static AES aesCBC = AES.getInstance(EncryptionMode.AES_128_CBC);

    public static void main(String[] args) {
        String input = "nothing;user=true";
        String encrypted = encryptString(input);
        System.out.println("Cookie  : "+encrypted);
        encrypted = expoitCookie(encrypted);
        System.out.println("Modified: "+encrypted);
        System.out.println("User is admin: "+decryptString(encrypted));
    }

    private static String encryptString(String inputString) {
        String prepend = "comment1=cooking%20MCs;userdata=";
        String append = ";comment2=%20like%20a%20pound%20of%20bacon";
        String finalStringToEncrypt = prepend + inputString + append;
        finalStringToEncrypt = qouteString(finalStringToEncrypt);
        System.out.println(finalStringToEncrypt);
        byte[] encryptedData = aesCBC.encrypt(finalStringToEncrypt, randomKey, randomKey);
        return Utils.encodeBase64(encryptedData);
    }

    private static boolean decryptString(String base64) {
        byte[] data = aesCBC.decrypt(Utils.decodeBase64(base64), randomKey.getBytes(), randomKey.getBytes());
        String result = new String(data);
        System.out.println("DECRYPTED = "+result);
        return checkIfUserAdmin(result);
    }

    private static boolean checkIfUserAdmin(String cookie) {
        String[] array = cookie.split(";");
        String expected = "admin=true";
        boolean found = false;
        for(String str : array) {
            if(str.equals(expected)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private static String expoitCookie(String cookie) {
        String hexStr = Utils.bytesToHex(Utils.decodeBase64(cookie));
        List<String> hexCiphers = AES.breakHexCipherList(hexStr, 32);
        String targetCipher = hexCiphers.get(2);
        byte[] data = Utils.hexToBytes(targetCipher);
        byte[] targetBytes = {59, 97, 100, 109, 105, 110, 116, 114, 117, 101, 59};
        data[0] = (byte) (data[0] ^ 'u' ^ ';');
        data[1] = (byte) (data[1] ^ 's' ^ 'a');
        data[2] = (byte) (data[2] ^ 'e' ^ 'd');
        data[3] = (byte) (data[3] ^ 'r' ^ 'm');
        data[4] = (byte) (data[4] ^ '"' ^ 'i');
        data[5] = (byte) (data[5] ^ '=' ^ 'n');

        data[6] = (byte) (data[6] ^ '"' ^ '=');
        data[11] = (byte) (data[11] ^ '"' ^ ';');

        String modifiedCipher = Utils.bytesToHex(data);
        hexCiphers.set(2, modifiedCipher);
        StringBuilder modifiedHexCipher = new StringBuilder();
        hexCiphers.forEach(modifiedHexCipher::append);
        return Utils.encodeBase64(Utils.hexToBytes(modifiedHexCipher.toString()));
    }

    private static String qouteString(String str) {
        if(str != null && !"".equals(str) && str.trim().length() > 0) {
            str = str.replaceAll(";", "\";\"");
            str = str.replaceAll("=", "\"=\"");
        }
        return str;
    }
}
