/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

/**
 *
 * @author bowen
 */
public abstract class PCMUtils {

    public static short[] bigEndianConversion(byte[] b) {
        return bigEndianConversion(b, b.length);
    }

    public static short[] bigEndianConversion(byte[] b, int maxLength) {
        short[] s = new short[b.length / 2];
        for (int i = 0, j = 0; i < b.length && i < maxLength && j < s.length; i += 2, j++) {
            int firstByte = 255 & b[i];
            int secondByte = 255 & b[i + 1];
            short combined = (short) ((firstByte << 8) | secondByte);
            s[j] = combined;
        }
        return s;
    }
    
}
