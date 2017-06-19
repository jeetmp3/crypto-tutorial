package demo.crypto.set3;

import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;
import demo.crypto.set2.Challenge15;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author Jitendra Singh.
 */
public class Challenge17 {

    private static String randomKey = Utils.generateRandomString(16);
    private static AES cbc = AES.getInstance(EncryptionMode.AES_128_CBC);

    private static String[] randomMessages = {
            "MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc=",
            "MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic=",
            "MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw==",
            "MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg==",
            "MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl",
            "MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA==",
            "MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw==",
            "MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8=",
            "MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g=",
            "MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93"
    };

    public static void main(String[] args) {
        int index = Utils.randomInt(randomMessages.length);
//        int index = 5;
        String source = randomMessages[index];
        System.out.println("String selected: " + source);
        String cookie = encrypt(source);
        System.out.println(new String(cbc.decrypt(Utils.decodeBase64(cookie), randomKey.getBytes(), randomKey.getBytes())));
        System.out.println("Cookie is      : " + cookie);
        hackCookie(cookie);
    }

    private static String encrypt(String base64Msg) {
        byte[] data = Utils.decodeBase64(base64Msg);
        System.out.println("Byte length is : " + data.length);
        byte[] cipher = cbc.encrypt(data, randomKey.getBytes(), randomKey.getBytes());
        return Utils.encodeBase64(cipher);
    }

    private static byte[] decrypt(byte[] data) {
        return cbc.decrypt(data, randomKey.getBytes(), randomKey.getBytes());
    }

    private static void hackCookie(String base64cookie) {
        byte[] resultBytes = Utils.decodeBase64(base64cookie);
        int totalBlocks = resultBytes.length / 16;
        ByteArrayOutputStream bios = new ByteArrayOutputStream();
        for (int block = 1; block <= (totalBlocks - 1); block++) {
//            int bytesTargeted = 16 + (16 * block);

            byte[] chunk = new byte[16 + (16 * block)];
            System.arraycopy(resultBytes, 0, chunk, 0, chunk.length);
            byte[] decrypted = decryptCookie(chunk);
            writeToByteString(bios, decrypted);
//
//            byte[] exploitBytes = new byte[bytesTargeted];
//            int targetByteIndex = 15 + (16 * (block - 1));
//            byte padding = 1;
//            System.arraycopy(resultBytes, 0, exploitBytes, 0, bytesTargeted);
//            exploitByte(exploitBytes, guessedData, targetByteIndex, padding);
            System.out.println("Block " + block + " Hacked: " + new String(bios.toByteArray()));
        }
    }

    private static void writeToByteString(OutputStream outputStream, byte[] bytes) {
        try {
            for (int i = (bytes.length - 1); i >= 0; i--)
                outputStream.write(bytes[i]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] decryptCookie(byte[] data) {
        ByteArrayOutputStream guessedBytes = new ByteArrayOutputStream(16);
        int blockSize = 16;
        int padding = 0x01;
        for (int blockIndex = 0; blockIndex < blockSize; blockIndex++) {
            byte[] tmp = Arrays.copyOf(data, data.length);
            int targetByteIndex = (tmp.length - 17);
            byte[] tmpGuessBytes = guessedBytes.toByteArray();
            for (int guessedIndex = 0; guessedIndex < guessedBytes.size(); guessedIndex++) {
                tmp[targetByteIndex] = (byte) (tmp[targetByteIndex] ^ tmpGuessBytes[guessedIndex] ^ padding);
                targetByteIndex--;
            }

            for (byte randomByte = 1; randomByte < 127; randomByte++) {
                byte[] payload = Arrays.copyOf(tmp, tmp.length);
                targetByteIndex = (tmp.length - 17) - tmpGuessBytes.length;
                for (int i = guessedBytes.size(); i < padding; i++) {
                    payload[targetByteIndex] = (byte) (payload[targetByteIndex] ^ randomByte ^ padding);
                    targetByteIndex--;
                }

                byte[] result = decrypt(payload);
                if (Challenge15.checkIfPKCS7Padding(result, (byte) padding)) {
                    guessedBytes.write(randomByte);
                    ++padding;
                    break;
                }
            }
        }
        return guessedBytes.toByteArray();
    }

    private static void exploitByte(byte[] sourceArray, byte[] guessedArray, int targetByteIndex, byte padding) {
        int guessedDataSize = (targetByteIndex + (padding - 1));
        for (int guessByte = 1; guessByte < 128; guessByte++) {
            byte[] tmpArray = new byte[sourceArray.length];
            System.arraycopy(sourceArray, 0, tmpArray, 0, sourceArray.length);
            for (int j = guessedDataSize; j >= targetByteIndex; ) {
                if (guessByte == padding) {
                    guessByte++;
                    continue;
                }
                if (j == targetByteIndex) {
                    tmpArray[j] = (byte) (tmpArray[j] ^ guessByte ^ padding);
                } else {
                    tmpArray[j] = (byte) (tmpArray[j] ^ guessedArray[j] ^ padding);
                }
                --j;
            }
            byte[] result = decrypt(tmpArray);
            if (Challenge15.checkIfPKCS7Padding(result)) {
                guessedArray[targetByteIndex] = (byte) guessByte;
//                System.out.println(new String(guessedArray));
                --targetByteIndex;
                ++padding;
                break;
            }
        }
        if (targetByteIndex > -1) {
            exploitByte(sourceArray, guessedArray, targetByteIndex, padding);
        }
    }
}
