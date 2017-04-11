package demo.crypto.test;

import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Jitendra Singh.
 */
public class ECBVsCBC {

    public static void main(String[] args) throws IOException {
        String fileName = "bitmap";
        String extension = ".gif";
        String image = "/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/" + fileName + extension;
        String targetEcb = "/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/" + fileName + "Ecb" + extension;
        String targetCbc = "/media/jitendra/Media/code-world/osc/poc/crypto-tutorial/" + fileName + "Cbc" + extension;
        byte[] input = readFile(image);
        byte[] key = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

        byte[] ecbBytes = ecbMode(input, key);
        byte[] cbcBytes = cbcMode(input, key, key);

        writeFile(targetEcb, ecbBytes);
        writeFile(targetCbc, cbcBytes);
    }

    private static byte[] ecbMode(byte[] bytes, byte[] key) {
        return AES.getInstance(EncryptionMode.AES_128_ECB).decrypt(bytes, key);
    }

    private static byte[] cbcMode(byte[] bytes, byte[] key, byte[] iv) {
        return AES.getInstance(EncryptionMode.AES_128_CBC).decrypt(bytes, key, iv);
    }

    private static byte[] readFile(String file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File input = new File(file);
        FileInputStream fis = new FileInputStream(input);
        FileChannel fileChannel = fis.getChannel();
        fileChannel.transferTo(0, fileChannel.size(), Channels.newChannel(baos));
        fis.close();
        return baos.toByteArray();
    }

    private static void writeFile(String file, byte[] buffer) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

        ReadableByteChannel inputChannel = Channels.newChannel(bais);
        fos.getChannel().transferFrom(inputChannel, 0, bais.available());
        fos.close();
    }
}
