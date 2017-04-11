package demo.crypto.set2;

/**
 * @author Jitendra Singh.
 */
public class Challenge15 {
    public static void main(String[] args) {
        String message = "ICE ICE BABY";
        byte[] data = new byte[message.length() + 3];
        for (int i = 0; i < data.length; i++) {
            if (i < message.length()) {
                data[i] = message.getBytes()[i];
            } else {
                data[i] = 0x04;
            }
        }
        System.out.println(checkIfPKCS7Padding(data));
    }

    public static boolean checkIfPKCS7Padding(String text) {
        return checkIfPKCS7Padding(text.getBytes());
    }

    public static boolean checkIfPKCS7Padding(byte[] dataBytes) {
        if (dataBytes != null && dataBytes.length > 0) {
            byte lastByte = dataBytes[dataBytes.length - 1];
            if (dataBytes.length < lastByte) {
                return false;
            }
            boolean padding = true;
            for(int i = dataBytes.length - lastByte; i < dataBytes.length; i++) {
                if(dataBytes[i] != lastByte) {
                    padding = false;
                    break;
                }
            }
            return padding;
        }
        return false;
    }
}
