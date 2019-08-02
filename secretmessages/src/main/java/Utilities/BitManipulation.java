/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import static Math.Mathematics.powerOfTwo;

/**
 * Class containing static methods for extracting and embedding bits.
 * @author joonas
 */
public class BitManipulation {
    public static int getNthBitFromByte(byte source, int n) {
        if (n < 0 || n > 7) {
            throw new Error("Number " + n + " is outside of range 0-7!");
        }
        return (powerOfTwo(n) & source) >>> n;
    }

    public static int setNthBitInByte(int n, int value) {

        if (n < 0 || n > 7) {
            throw new Error("Number " + n + " is outside of range 0-7!");
        }
        // TODO fix
        //byte temp = (byte) (2 ^ n);
        return 2 ^ n;
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
     *
     * @param target
     * @param data
     * @return
     */
    public static byte interleaveBitToByte(byte target, int data) {
        int bitmask = Integer.MAX_VALUE - 1;
        int temp = target & bitmask;
        return (byte) (temp | data);
    }

    /**
     *
     * @param source
     * @return
     */
    public static int extractFinalBitFromByte(byte source) {
        return source & 1; // either 1 or 0
    }
}
