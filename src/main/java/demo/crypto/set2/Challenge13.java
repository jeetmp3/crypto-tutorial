package demo.crypto.set2;

import demo.crypto.Utils;
import demo.crypto.algo.AES;
import demo.crypto.algo.EncryptionMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * @author Jitendra Singh.
 */
public class Challenge13 {

    static String key = UUID.randomUUID().toString().substring(0, 16);
    static AES aesECB = AES.getInstance(EncryptionMode.AES_128_ECB);

    protected String parseQueryString(String qa) {
        StringBuilder builder = new StringBuilder("{");
        char singleQuote = '\'';
        char colon = ':';
        if(qa != null && qa.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(qa, "&");
            while (tokenizer.hasMoreElements()) {
                String token = tokenizer.nextToken();
                if(token != null) {
                    String[] keyValue = token.split("=");
                    if(keyValue.length > 1) {
                        builder.append("\n")
                                .append(singleQuote).append(keyValue[0]).append(singleQuote)
                                .append(colon)
                                .append(singleQuote).append(keyValue[1]).append(singleQuote);
                        if(tokenizer.countTokens() > 0) {
                            builder.append(",");
                        }
                    } else if(keyValue.length > 0) {
                        builder.append("\n")
                                .append(singleQuote).append(keyValue[0]).append(singleQuote)
                                .append(colon)
                                .append(singleQuote).append(singleQuote);
                        if(tokenizer.countTokens() > 0) {
                            builder.append(",");
                        }
                    }
                }
            }
        }
        builder.append("\n}");
        return builder.toString();
    }

    protected String profileFor(String email) throws UnsupportedEncodingException {
        email = URLEncoder.encode(email, "UTF-8");
        return "email=".concat(email).concat("&uid=10&role=user");
    }

    protected String encrypted(String encodedString) {
        return Utils.bytesToHex(aesECB.encrypt(encodedString, key));
    }

    protected String attacker(String cipher, String email) throws UnsupportedEncodingException {
        String test = "email="+URLEncoder.encode(email, "UTF-8")+"&uid=10&role=admin";
        System.out.println(test);
        String updated = encrypted(test);
        List<String> original = AES.breakHexCipherList(cipher, 32);
        List<String> tmp = AES.breakHexCipherList(updated, 32);
        List<String> modified = new ArrayList<>(original);
        System.out.println(modified);
        int last = modified.size() - 1;
        modified.remove(last);
        modified.add(tmp.get(last));
        System.out.println(modified);
        StringBuilder sb = new StringBuilder();
        modified.forEach(sb::append);
        return sb.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Challenge13 ch = new Challenge13();
        String email = "foo@bar.com";
        String encodedString = ch.profileFor("foo@bar.com");
        String cipher = ch.encrypted(encodedString);
        String modifiedHex = ch.attacker(cipher, email);
        String decrypted = new String(aesECB.decrypt(Utils.hexToBytes(modifiedHex), key.getBytes()));
        System.out.println(decrypted);
        System.out.println(ch.parseQueryString(decrypted));
    }

}
