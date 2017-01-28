package demo.crypto.set2;

import demo.crypto.Utils;
import demo.crypto.algo.AES;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Jitendra Singh.
 */
public class Challenge12 {

    static String key = UUID.randomUUID().toString().substring(0, 16);

    public static void main(String[] args) throws IOException {
//        print(Utils.bytesToHex(encryptOracle("AAAAAAAAAAAAAAA")), 32);
//        print(Utils.bytesToHex(encryptOracle("AAAAAAAAAAAAAAAA")), 32);
//        print(Utils.bytesToHex(encryptOracle("AAAAAAAAAAAAAAAB")), 32);
        Challenge12 ch = new Challenge12();
        ch.decryptString();
        ch.checkAnswer();
    }

    private void checkAnswer() {
        String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
        System.out.println("-----------------------------------------------------------------");
        System.out.println(new String(Utils.decodeBase64(unknownString)));
    }

    protected byte[] encrypt(String string, String unknown, String key) {
        StringBuilder sb = new StringBuilder(string);
        sb.append(new String(Utils.decodeBase64(unknown)));
        return Challenge11.aesECBEncrpyt(sb.toString(), key);
    }

    private static void print(String str, int size) {
        System.out.println(AES.breakHexCipherList(str, size));
    }

    public byte[] encryptOracle(String yourString) {
        String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
        return encrypt(yourString, unknownString, key);
    }

    public void decryptString() throws IOException {
        int blockSize = blockSize();
        if(checkECB()) {
            decryptBytes(blockSize);
        } else {
            System.err.println("Encryption Algorithm is not ECB");
        }
    }

    private int blockSize() {
        StringBuilder sb = new StringBuilder("A");
        byte[] output = encryptOracle(sb.toString());
        int size = output.length;
        char ch = 50;
        int blockSize = 0;
        for (; ; ) {
            sb.append(ch++);
            output = encryptOracle(sb.toString());
            if (output.length > size) {
                blockSize = output.length - size;
                break;
            }
        }
        return blockSize;
    }

    private boolean checkECB() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            sb.append("A");
        }
        byte[] result = encryptOracle(sb.toString());
        return AES.checkECB(Utils.bytesToHex(result));
    }

    private String decryptBytes(int blockSize) throws IOException {
        StringBuilder decrypted = new StringBuilder();
        int currentBlock = 0;
        boolean breakLoop;
        for (; ; currentBlock++) {
            breakLoop = decryptBlock(currentBlock, decrypted, blockSize);
            if (breakLoop) {
                break;
            }
        }
        System.out.println(decrypted.toString());
        return "";
    }

    private boolean decryptBlock(int currentBlock, StringBuilder decrypted, int blockSize) {
        boolean breakLoop = false;
        for (int j = 1; j <= 16; j++) {
            Map<String, Byte> dictionary;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int total = blockSize - j;
            for (int i = 0; i < total; i++) {
                outputStream.write((byte) 'A');
            }
            byte[] tmp = buildTmpBytesToGenerateDictionary(outputStream.toByteArray(), decrypted.toString().getBytes());
            dictionary = buildDictionary(tmp);
            List<String> blocks = AES.breakHexCipherList(Utils.bytesToHex(encryptOracle(new String(outputStream.toByteArray()))), 32);
            if (currentBlock >= blocks.size()) {
                breakLoop = true;
                break;
            }
            String firstBlock = blocks.get(currentBlock);
            if (dictionary.containsKey(firstBlock))
                decrypted.append((char) dictionary.get(firstBlock).byteValue());
        }
        return breakLoop;
    }

    private Map<String, Byte> buildDictionary(byte[] tmp) {
        Map<String, Byte> dictionary = new HashMap<>();
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            tmp[15] = b;
            List<String> blocks = AES.breakHexCipherList(Utils.bytesToHex(encryptOracle(new String(tmp))), 32);
            dictionary.put(blocks.get(0), b);
        }
        return dictionary;
    }

    private byte[] buildTmpBytesToGenerateDictionary(byte[] input, byte[] decrypted) {
        byte[] tmp = new byte[input.length + decrypted.length];
        System.arraycopy(input, 0, tmp, 0, input.length);
        if (decrypted.length > 0) {
            System.arraycopy(decrypted, 0, tmp, input.length, decrypted.length);
        }
        byte[] result = new byte[16];
        int startPoint = tmp.length - 15;
        System.arraycopy(tmp, startPoint, result, 0, 15);
        return result;
    }
}
