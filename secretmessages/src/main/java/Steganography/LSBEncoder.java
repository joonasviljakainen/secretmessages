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

    /**
     *
     * @param source
     * @param blockAlign
     * @return
     */
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
                message[locInMsg] = (byte) (message[locInMsg] | bitpos);
            } else {

            }
            if (++locationInByte >= 8) {
                locationInByte = 0;
                if (++locInMsg >= message.length) {
                    locInMsg = 0;
                }
            }
        }
        return message;
    }
}
