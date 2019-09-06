package Steganography;

import static Utilities.BitManipulation.getNthBitFromByte;
import static Utilities.BitManipulation.extractFinalBitFromByte;

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
     * Encodes a byte array to target audio using Least-Significant Bit encoding.
     * @param target The audio to encode the message to 
     * @param message The message to hide as byte array.
     * @param blockLength Length of each frame, i.e. all samples 
     * 'for a single point in the audio signal. E.g. if there are 
     * 2 channels, this value is 4 (2 * 2, where 2 is the number 
     * of bytes per sample, the other 2 the number of samples per 
     * frame),
     * @return The steganographically modified audio as byte array
     */
//    public static byte[] interleaveMessageInBytes(byte[] target, char[] message, int blockLength) {
    public static byte[] interleaveMessageInBytes(byte[] target, byte[] message, int blockLength) {
        int maxLength = target.length / blockLength;
        long messageLengthInBits = message.length * 8;
        if (target.length / blockLength < messageLengthInBits) {
            throw new Error("Message has " + messageLengthInBits
                    + " bits, but only " + maxLength + " bits can be fitted.");
        }

        int locationInByte = 0;                         // how many-th bit of the current byte is being looked at
        int locInMsg = 0;                               // how many-th byte of the message is being looked at
        int curBit;                                     // The current bit value to embed in the audio
        for (int i = 0; i < target.length; i += blockLength) {
            //curBit = getNthBitFromByte((byte) message[locInMsg], locationInByte); // the bit to embed
            curBit = getNthBitFromByte(message[locInMsg], locationInByte); // the bit to embed
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

    /**
     *
     * @param source Source audio, 16-bit PCM data
     * @param blockAlign Size of a single frame, i.e. if 2 channels, this equals 4.
     * @return The hidden message a sbyte array
     */
    public static byte[] extractMessageFromBytes(byte[] source, int blockAlign) {
        byte[] message = new byte[(source.length / blockAlign) / 8];

        for (int i = 0; i < message.length; i++) {
            message[i] = (byte) 0x00;
        }

        int locationInByte = 0;                 // how many-th bit of the current byte is being looked at
        int locInMsg = 0;                       // how many-th byte in the message array
        int curBit;                             // current bit to embed
        for (int i = 0; locInMsg < message.length; i += blockAlign) {
            curBit = (byte) extractFinalBitFromByte(source[i]);
            if (curBit == 1) {
                byte bitpos = (byte) (curBit << locationInByte);
                message[locInMsg] = (byte) (message[locInMsg] | bitpos);
            }

            if (++locationInByte >= 8) {
                locationInByte = 0;
                locInMsg++;
            }
        }
        return message;
    }
}
