package demo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author Jitendra Singh.
 */
public class BreakECB {

    private static HttpClient httpClient;

    static {
        httpClient = HttpClients.createMinimal(new BasicHttpClientConnectionManager());
    }

    private String encryptOracle(String strMsg) {
        HttpPost httpPost = new HttpPost("http://abc.com:8080/crypto/encryptOracle/AES_128_ECB");
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(
                    Collections.singletonList(new BasicNameValuePair("message", strMsg))
            ));
            HttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Byte> generateDictionary(String input) {
        Map<String, Byte> dictionary = new HashMap<>();
        byte[] tmp = new byte[16];
//        System.out.println("INPUT IS : "+input +" [ "+input.getBytes().length+" ]");
        System.arraycopy(input.getBytes(), input.getBytes().length - 15, tmp, 0, 15);

        for (byte ch = 0; ch < Byte.MAX_VALUE; ch++) {
            tmp[15] = ch;
            String ciphertext = encryptOracle(new String(tmp));
            List<String> cipherBlocks = breakCiphertextToBlocks(ciphertext, 32);
            dictionary.put(cipherBlocks.get(0), ch);
        }
        return dictionary;
    }

    private List<String> breakCiphertextToBlocks(String ciphertext, int blocksize) {
        return Arrays.asList(ciphertext.split("(?<=\\G.{" + blocksize+ "})"));
    }

    private String decryptOracle() {
        StringBuilder decryptedMessage = new StringBuilder();
        int currentBlock = 0;
        boolean breakLoop;
        for (; ; currentBlock++) {
            breakLoop = decryptBlock(currentBlock, decryptedMessage);
            if (breakLoop) {
                break;
            }
        }
        return decryptedMessage.toString();
    }

    private boolean decryptBlock(int currentBlock, StringBuilder decrypted) {
        boolean breakLoop = false;
        for (int chars = 1; chars <= 16; chars++) {
            Map<String, Byte> dictionary;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StringBuilder identicalChar = new StringBuilder();
            int totalIdenticalChars = 16 - chars;
            for (int i = 0; i < totalIdenticalChars; i++) {
                outputStream.write((byte) 'A');
                identicalChar.append('A');
            }
            String tmp = buildRawStringToGenerateDictionary(identicalChar.toString(), decrypted.toString());
            dictionary = generateDictionary(tmp);
            String cipherText = encryptOracle(identicalChar.toString());
            List<String> blocks = breakCiphertextToBlocks(cipherText, 32);
            if (currentBlock >= blocks.size()) {
                breakLoop = true;
                break;
            }
            String firstBlock = blocks.get(currentBlock);
            if (dictionary.containsKey(firstBlock))
                decrypted.append((char) dictionary.get(firstBlock).byteValue());
            System.out.println("String so far: "+decrypted.toString());
        }
        return breakLoop;
    }

    private String buildRawStringToGenerateDictionary(String identicalString, String decryptedString) {
        StringBuilder rawString = new StringBuilder(identicalString);
        if (decryptedString.length() > 0) {
            rawString.append(decryptedString);
        }
        return rawString.toString();
    }

    public static void main(String[] args) {
        BreakECB ecb = new BreakECB();
        System.out.println(ecb.decryptOracle());
        httpClient = null;
    }

}
