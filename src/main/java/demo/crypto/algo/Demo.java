package demo.crypto.algo;

import demo.crypto.Utils;

/**
 * @author Jitendra Singh.
 */
public class Demo {

    static AES aes = AES.getInstance(EncryptionMode.AES_128_ECB);
    static BlockCipherEncryptionProcess ep = BlockCipherEncryptionProcess.getInstance(EncryptionMode.AES_128_ECB);

    public static void main(String[] args) {
        byte[][] state = {
                {0x32, (byte) 0x88, 0x31, (byte) 0xe0},
                {0x43, 0x5a, 0x31, 0x37},
                {(byte) 0xf6, 0x30, (byte) 0x98, 0x07},
                {(byte) 0xa8, (byte) 0x8d, (byte) 0xa2, 0x34}
        };
        byte[][] key = {
                {0x2b, 0x28, (byte) 0xab, 0x09},
                {0x7e, (byte) 0xae, (byte) 0xf7, (byte) 0xcf},
                {0x15, (byte) 0xd2, 0x15, 0x4f},
                {0x16, (byte) 0xa6, (byte) 0x88, 0x3c}
        };
        System.out.println(new String(AES.stateMatrixToArray(key)));
        byte[] orignal = AES.stateMatrixToArray(key);
        byte[] expKey = aes.keyExpansion(orignal);
//        for (int i = 0; i < expKey.length / 16; i++) {
//            byte[] tmp = aes.getGeneratedKey(expKey, i * 16, 16);
//            print(aes.arrayToStateMatrix(tmp));
//        }

        byte[][] result = ep.addRoundKey(state, AES.stateMatrixToArray(key));
        print(result);
        for(int i = 1; i <= 9; i++) {
            System.out.println("=============================== Round "+i+" =================================");
            result = ep.byteSubstitues(result);
            print(result);
            result = ep.shiftRows(result);
            print(result);
            result = ep.mixColumns(result);
            print(result);
            result = ep.addRoundKey(result, AES.getGeneratedKey(expKey, i * 16, 16));
//            print(aes.arrayToStateMatrix(aes.getGeneratedKey(expKey, i * 16, 16)));
            print(result);
        }

        System.out.println("=============================== Round 10 =================================");
        result = ep.byteSubstitues(result);
        print(result);
        result = ep.shiftRows(result);
        print(result);
        result = ep.addRoundKey(result, AES.getGeneratedKey(expKey, 10 * 16, 16));
        print(result);
    }

    public static void print(byte[][] result) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.printf("%6s", Utils.bytesToHex(result[i][j]));
            }
            System.out.println("");
        }
        System.out.println("------------------------------------------------------------------------");
    }
}
