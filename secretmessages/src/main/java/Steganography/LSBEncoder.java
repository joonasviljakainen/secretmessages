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
     * @param target
     * @param message
     * @param blockLength
     * @return
     */
    public static byte[] interleaveMessageInBytes(byte[] target, char[] message, int blockLength) {
        int maxLength = target.length / blockLength;
        long messageLengthInBits = message.length * 8;
        if (target.length / blockLength < messageLengthInBits) {
            throw new Error("Message has " + messageLengthInBits + " bits, but only " + maxLength + " bits can be fitted.");
        }

        int locationInByte = 0;                         // how many-th bit of the current byte is being looked at
        int locInMsg = 0;                               // how many-th byte of the message is being looked at
        int curBit;                                     // The current bit value to embed in the audio
        for (int i = 0; i < target.length; i += blockLength) {
            curBit = getNthBitFromByte((byte) message[locInMsg], locationInByte); // the bit to embed
            int mask = 0xfe;                            // 11111110
            int temp = mask & target[i];                // ensure that last bit is 0
            target[i] = (byte) (temp | curBit);         // Add last bit, whatever it may be

            if (++locationInByte >= 8) {
                locationInByte = 0;
                if (++locInMsg >= message.length) {
                    locInMsg = 0;
                }
            }
        }
        return target;
    }

    public static byte[] extractMessageFromBytes(byte[] source, int blockAlign) {
        byte[] message = new byte[(source.length / blockAlign) / 200];

        for (int i = 0; i < message.length; i++) {
            message[i] = (byte) 0x00;
        }

        int locationInByte = 0;                 // how many-th bit of the current byte is being looked at
        int locInMsg = 0;                       // how many-th byte in the message array
        int curBit;                             // current bit to embed
        for (int i = 0; i < source.length; i += blockAlign) {
            curBit = (byte) extractFinalBitFromByte(source[i]);
            if (curBit == 1) {
                byte bitpos = (byte) (curBit << locationInByte);
                message[locInMsg] = (byte)(message[locInMsg] | bitpos);
            } else {
                
            }
            //byte bitpos = (byte) (curBit << locationInByte);
            //byte temp = (byte) (curBit * powerOfTwo(locationInByte));
            //message[locInMsg] += (byte) (curBit * powerOfTwo(locationInByte));

            if (++locationInByte >= 8) {
                locationInByte = 0;
                //message[locInMsg] = (byte) (message[locInMsg] >>> 1);
                //System.out.println(message[locInMsg]);
                if (++locInMsg >= message.length) {
                    locInMsg = 0;
                }
            }
        }
        return message;
    }

    public static int getNthBitFromByte(byte source, int n) {
        if (n < 0 || n > 7) {
            throw new Error("Number " + n + " is outside of range 0-7!");
        }
        
        return (powerOfTwo(n) & source) >>> n;
    }

    public static byte powerOfTwo(int n) {
        if (n < 0) {
            throw new Error("can' calcluate negative powers");
        }
        if (n == 0) {
            return 1;
        }
        int accum = 1;
        for (int i = 0; i < n; i++) {
            accum *= 2;
        }
        return (byte) accum;
    }

    public static int setNthBitInByte(int n, int value) {

        if (n < 0 || n > 7) {
            throw new Error("Number " + n + " is outside of range 0-7!");
        }
        // TODO fix
        byte temp = (byte) (2 ^ n);
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
