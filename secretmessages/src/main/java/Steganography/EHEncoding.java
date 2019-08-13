/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Steganography;

import Math.Mathematics;
import Utilities.BitManipulation;
import static Utilities.BitManipulation.littleEndianByteArrayToShorts;
import static Utilities.BitManipulation.shortArrayToLittleEndianBytes;

/**
 *
 * @author joonas
 */
public class EHEncoding {

    private static final double zeroDelay = 150.0;
    //private static final double oneDelay = 300.0;
    private static final double oneDelay = 2500.0;
    private static final int DEFAULT_FRAME_LENGTH = 8 * 4024;
    //private static final int DEFAULT_FRAME_LENGTH = 8 * 1024; --> Use this in real life
    private static final double DEFAULT_ECHO_AMPLITUDE = 0.5;
    private static final short SIGNAL_LIMIT_MAGNITUDE = 32760;

    /**
     * Encodes a message to an audio signal using Echo Hiding (EH).
     *
     * @param data The signal to which the message will be encoded. The data
     * must be 16-bit PCM format. Input data can be a raw byte stream: this
     * method will convert the data to 16-bit signed integers for encoding.
     * @param message The message to hide as bytes. Only 8-bit (ASCII) symbols are supported.
     * @return
     */
    public static byte[] encode(byte[] data, byte[] message) {
        // TODO add support for 16kHx and 8 kHz sampling rates
        // convert to something we can handle
        short[] pcmData = littleEndianByteArrayToShorts(data);
        // establish & check boundaries
        int maxLength = data.length / DEFAULT_FRAME_LENGTH;
        int messageLengthInBits = message.length * 8;
        if (messageLengthInBits > maxLength) {
            throw new IllegalArgumentException("Message too long to hide!");
        }

        int zeroDelayAsNumberOfFrames = (int) (44100 * (zeroDelay / 1000.0)); // assuming 44100 Hz
        int oneDelayAsNumberOfFrames = (int) (44100 * (oneDelay / 1000.0)); //; -- || --

        short[] d0 = delaySignal(pcmData, zeroDelayAsNumberOfFrames);
        short[] d1 = delaySignal(pcmData, oneDelayAsNumberOfFrames);

        double[] mixer = createMixerSignal(message, pcmData.length, DEFAULT_FRAME_LENGTH);
        short[] temp = convolveThreeSignals(pcmData, d0, d1, mixer, 0.5);

        return shortArrayToLittleEndianBytes(temp);
    }

    private static short[] convolveThreeSignals(short[] data, short[] zeroDelayed, short[] oneDelayed, double[] mixer, double decay) {
        for (int i = 0; i < mixer.length; i++) {
            //calculating the magnitude of the total echo
            short echo = (short) ((decay * zeroDelayed[i] * (Mathematics.abs(mixer[i] - 1))) + (decay * oneDelayed[i] * mixer[i]));
            // Range checks 
            if (data[i] > 0 && echo > 0) {
                if (SIGNAL_LIMIT_MAGNITUDE - echo < data[i]) {
                    data[i] = SIGNAL_LIMIT_MAGNITUDE;
                    continue;
                }
            } else if (data[i] < 0 && echo < 0) {
                if (-SIGNAL_LIMIT_MAGNITUDE - echo > data[i]) {
                    data[i] = -SIGNAL_LIMIT_MAGNITUDE;
                    continue;
                }
            }
            data[i] = (short) (data[i] + echo);
        }
        return data;
    }

    /**
     * Creates a mixer signal with a number of elements equal to the number of
     * frames (essentially samples) in the data being EH encoded. The decimal
     * numbers can be used for determining which echo (one or zero) should be
     * embedded at a particular moment.
     *
     * In addition, its purpose is to smoothen the transition between the two
     * echoes.
     *
     * @param message The message being hidden as bytes
     * @param messageContainerLengthInFrames Number of frames
     * @param numFramesForSingleBit Number of bits to be marked with a
     * particular bit (essentially, how long the signal for each bit should be)
     * @return The mixer signal with values varying from 0.0 to 1.0.
     */
    private static double[] createMixerSignal(byte[] message, int messageContainerLengthInFrames, int numFramesForSingleBit) {
        double[] res = new double[messageContainerLengthInFrames];

        int loc = 0;
        int i;
        for (i = 0; i < message.length; i++) {
            byte curChar = message[i];
            for (int j = 0; j < 8; j++) {
                int curBit = BitManipulation.getNthBitFromByte(curChar, j);
                for (int a = 0; a < numFramesForSingleBit && loc < res.length; a++, loc++) {
                    // TODO lerp this
                    res[loc] = curBit;
                }
            }
        }
        return res;
    }

    /**
     * Delays i.e. Zero-pads the signal at the start by the number of frames
     * provided. Note that the delayed signal is NOT added onto the original
     * source.
     *
     * @param data The signal to delay
     * @param delay Number of frames to delay.
     * @return The zero-padded/delayed signal.
     */
    public static short[] delaySignal(short[] data, int delay) {
        short[] res = new short[data.length];
        for (int i = 0; i < delay; i++) {
            res[i] = 0;
        }
        for (int i = delay, a = 0; i < data.length; i++, a++) {
            res[i] = data[a];
        }
        return res;
    }

    /**
     * Adds a single delay to a sound signal.
     *
     * @param data The data to delay as 16bit PCM audio (signed shorts).
     * @param delayAsFrames Number of frames to delay. E.g. if samples rate is
     * 44100 per second, giving the value 44100 will create a delay of one
     * second.
     * @param decay Magnitude i.e. volume of the echo compared to the original.
     * 1.0 stands for equal volume. signal.
     * @return
     */
    public static short[] delay(short[] data, int delayAsFrames, double decay) {
        short[] res = new short[data.length];
        for (int i = 0; i < delayAsFrames; i++) {
            res[i] = data[i];
        }
        for (int i = delayAsFrames, a = 0; i < data.length; i++, a++) {
            // Range checks to prevent overflow 
            double echo = decay * data[a];
            if (data[i] > 0 && echo > 0) {
                if (SIGNAL_LIMIT_MAGNITUDE - echo < data[i]) {
                    res[i] = SIGNAL_LIMIT_MAGNITUDE;
                    continue;
                }
            } else if (data[i] < 0 && echo < 0) {
                if (-SIGNAL_LIMIT_MAGNITUDE - echo > data[i]) {
                    res[i] = -SIGNAL_LIMIT_MAGNITUDE;
                    continue;
                }
            }
            res[i] = (short) (data[i] + echo);
        }
        return res;
    }

    public static byte[] simpleEcho(byte[] data) {

        // convert to shorts
        short[] toDelay = littleEndianByteArrayToShorts(data);

        // Hold up, there's a delay
        short[] delayed = delay(toDelay, 88200, 0.5);

        // bak to basics... I mean bytes
        return shortArrayToLittleEndianBytes(delayed);
    }

}
