/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import static Math.Mathematics.powerOfTwo;

/**
 * Class containing static methods for extracting and embedding bits.
 *
 * @author joonas
 */
public class BitManipulation {

    /**
     * Gets the nth bit of a byte, starting from the right. Index begins at 0
     *
     * @param source Byte to extract the bit from.
     * @param n How many-eth bit to extract, from 0 (rightmost,
     * least-significant) to 7 (leftmost).
     * @return The bit at position n of the source byte (as integer 0 or 1).
     */
    public static int getNthBitFromByte(byte source, int n) {
        if (n < 0 || n > 7) {
            throw new IllegalArgumentException("Number " + n + " is outside of range 0-7!");
        }
        return (powerOfTwo(n) & source) >>> n;
    }

    /**
     *
     * @param bytes
     * @param chunkSize
     * @return
     */
    public static int analyzeMaxNumberOfBytes(byte[] bytes, int chunkSize) {
        return bytes.length / chunkSize;
    }

    /**
     * Inserts the given data bit to the final (rightmost, least significant)
     * bit of the target byte. The final bit of the target byte will be the data
     * bit.
     *
     * @param target The byte to interleave the data to
     * @param data The data to embed (either 0 or 1 in integer form)
     * @return The byte interleaved with the data bit.
     */
    public static byte interleaveBitToByte(byte target, int data) {
        int bitmask = Integer.MAX_VALUE - 1;
        int temp = target & bitmask;
        return (byte) (temp | data);
    }

    /**
     * Extracts the final (rightmost, least-significant) bit from the source
     * byte.
     *
     * @param source The source byte to extract from
     * @return The rightmost bit of the source byte as integer.
     */
    public static int extractFinalBitFromByte(byte source) {
        return source & 1; // either 1 or 0
    }

    /**
     * Combines two bytes in little-endian order to form a short.
     *
     * @param littleEnd The least-significant byte
     * @param start The most significant byte
     * @return A short representing the combination of the two bytes.
     */
    public static short littleEndianBytesToShort(byte littleEnd, byte start) {
        return (short) ((start) << 8 | littleEnd & 0xff);
    }

    /**
     * Converts a single short to two bytes, ordered in little-endian fashion.
     *
     * @param num
     * @return
     */
    public static byte[] shortToLittleEndianBytes(short num) {
        byte[] res = new byte[2];
        res[1] = (byte) (num >>> 8);
        res[0] = (byte) (num & 255);
        return res;
    }

    /**
     * Converts an array of bytes comprising little-endian pairs to 16-bit
     * signed integers.
     *
     * @param source 16-bit PCM data (byte array with little-endian byte pairs
     * @return 16-bit signed integers representing the PCM data.
     */
    public static short[] littleEndianByteArrayToShorts(byte[] source) {
        short[] toDelay = new short[source.length / 2];
        int cur;
        for (int i = 0; i < toDelay.length; i++) {
            cur = i * 2;
            toDelay[i] = littleEndianBytesToShort(source[cur], source[cur + 1]);
        }
        return toDelay;
    }

    /**
     * Converts an array of shorts (16-bit PCM samples) to a byte array with
     * little-endian arrangement.
     *
     * @param src 16-bit integers to convert
     * @return 16-bit PCM data as little-endian bytes
     */
    public static byte[] shortArrayToLittleEndianBytes(short[] src) {
        byte[] res = new byte[src.length * 2];
        int cur;
        for (int i = 0; i < src.length; i++) {
            cur = 2 * i;
            byte[] ll = shortToLittleEndianBytes(src[i]);
            res[cur] = ll[0];
            res[cur + 1] = ll[1];
        }
        return res;
    }
}
