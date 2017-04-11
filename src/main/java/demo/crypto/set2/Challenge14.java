package demo.crypto.set2;

import demo.crypto.Utils;
import demo.crypto.algo.AES;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Jitendra Singh.
 */
public class Challenge14 extends Challenge12 {

    static Random r = new Random();
    static String randomString = generateRandomString();



    static String generateRandomString() {
        int total = r.nextInt(100);
        return Utils.generateRandomString(total);
    }

    public byte[] encryptOracle(String yourString) {
        String unknownString = "Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK";
        return encrypt(randomString.concat(yourString), unknownString, key);
    }

    // todo: supply 48-byte string
    // todo: check which two blocks equals (e.g. block #3 & #4 equals)
    // todo: now focus on block #2 supply identical chars one by one and check which 2 consecutive blocks are equal
    public int guessRandomByteSize() {
        int randomStringGuessedSize = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 48; i++) {
            sb.append('a');
        }
        byte[] results = encryptOracle(sb.toString());
        String hexString = Utils.bytesToHex(results);
        List<String> hexBlocks = AES.breakHexCipherList(hexString, 32);
        int randomStringLastBlockIndex = -1;
        for (int i = 0; i < hexBlocks.size(); i++) {
            if ((i + 1) < hexBlocks.size()) {
                if (hexBlocks.get(i).equals(hexBlocks.get(i + 1))) {
                    randomStringLastBlockIndex = i - 1;
                    break;
                }
            }
        }

        if (randomStringLastBlockIndex != -1) {
            sb.delete(0, sb.length());
            String previousHex = "";
            for (int i = 0; i <= 16; i++) {
                results = encryptOracle(sb.toString());
                hexString = Utils.bytesToHex(results);
                hexBlocks = AES.breakHexCipherList(hexString, 32);
                String targetBlock = hexBlocks.get(randomStringLastBlockIndex);
                if (previousHex.equals(targetBlock)) {
                    randomStringGuessedSize = (16 * randomStringLastBlockIndex) + (17 - i);
                    break;
                }
                previousHex = targetBlock;
                sb.append('a');
            }
        }
        return randomStringGuessedSize;
    }

    public static void main(String[] args) throws IOException {
        Challenge14 ch14 = new Challenge14();
        ch14.decryptString();
    }

    @Override
    protected String decryptBytes(int blockSize) throws IOException {
        StringBuilder decrypted = new StringBuilder();
        int guessRandomPrefixBytesSize = guessRandomByteSize();
        int prefixChars = guessRandomPrefixBytesSize % 16;
        if(prefixChars != 0) {
            prefixChars = 16 - prefixChars;
        }
        int currentBlock = (guessRandomPrefixBytesSize / 16) + 1;
        int actualDataBlockIndex = (guessRandomPrefixBytesSize / 16) + 1;
        boolean breakLoop;
        for (; ; currentBlock++) {
            breakLoop = decryptBlock(currentBlock, decrypted, blockSize, prefixChars, actualDataBlockIndex);
            if (breakLoop) {
                break;
            }
        }
        System.out.println(decrypted.toString());
        return "";
    }

    protected boolean decryptBlock(int currentBlock, StringBuilder decrypted, int blockSize, int prefixChars, int actualDataBlockIndex) {
        boolean breakLoop = false;
        StringBuilder prefix = new StringBuilder();
        for(int i = 0; i < prefixChars; i++) {
            prefix.append('A');
        }
        for (int j = 1; j <= 16; j++) {
            Map<String, Byte> dictionary;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int total = blockSize - j;
            for (int i = 0; i < total; i++) {
                outputStream.write((byte) 'A');
            }
            byte[] tmp = buildTmpBytesToGenerateDictionary(outputStream.toByteArray(), decrypted.toString().getBytes());
            dictionary = buildDictionary(tmp, prefix.toString(), actualDataBlockIndex);
            String payload = prefix.toString().concat(new String(outputStream.toByteArray()));
            byte[] cipherBytes = encryptOracle(payload);
            String hexCipher = Utils.bytesToHex(cipherBytes);
            List<String> blocks = AES.breakHexCipherList(hexCipher, 32);
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

    protected Map<String, Byte> buildDictionary(byte[] tmp, String prefix, int actualDataBlockIndex) {
        Map<String, Byte> dictionary = new HashMap<>();
        for (byte b = 0; b < Byte.MAX_VALUE; b++) {
            tmp[15] = b;
            String rawString = prefix.concat(new String(tmp));
            List<String> blocks = AES.breakHexCipherList(Utils.bytesToHex(encryptOracle(rawString)), 32);
            dictionary.put(blocks.get(actualDataBlockIndex), b);
        }
        return dictionary;
    }
}
