package Steganography;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author joonas
 */
public class LSBEncoder {

    /**
     *
     * @param bytes
     * @param chunkSize
     * @return
     */
    public static int analyzeMaxNumberOfBytes(byte[] bytes, int chunkSize) {
        return bytes.length / chunkSize;
    }

    public static byte interleaveBitToByte(byte target, int data) {
        int bitmask = Integer.MAX_VALUE - 1;
        int temp = target & bitmask;
        return (byte) (temp | data);
    }

    public static int extractBitFromByte(byte source) {
        int temp = source << 31;
        return (temp >> 31) & 1;
        //int temp = (byte) (source ^ 1);
        //System.out.println("temp and source: " + temp + ", " + source);
        //return source ^ temp;
    }

}
