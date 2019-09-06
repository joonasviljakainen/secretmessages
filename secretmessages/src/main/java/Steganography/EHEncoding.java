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

    private static final int DEFAULT_FRAME_LENGTH = 8 * 2048;
    private static final short SIGNAL_LIMIT_MAGNITUDE = 32760;

    /**
     * Encodes a message to an audio signal using Echo Hiding (EH). Uses default parameters.
     *
     * @param data The signal to which the message will be encoded. The data
     * must be 16-bit PCM format. Input data can be a raw byte stream: this
     * method will convert the data to 16-bit signed integers for encoding.
     * @param message The message to hide as bytes. Only 8-bit (ASCII) symbols are supported.
     * @return Steganographically encoded audio
     */
    public static byte[] encode(byte[] data, byte[] message) {
        return encode(data, message, 150, 300, 1.0, DEFAULT_FRAME_LENGTH);
    }


    /**
     * Encodes a watermark to an audio file using Echo Hiding technique.
     * @param data little-endian, single-channel byte data representing 16-bit PCM audio
     * @param message the message to hide. Must be 8-bit value.
     * @param zeroDelay Length of the delay for bit zero
     * @param oneDelay Length of the delay for bit one
     * @param decay The magnitude i.e. loudness of the echo used for hiding the data.
     * @param segmentlength length of the segment dedicated to each bit
     * @return The steganographically encoded audio
     */
    public static byte[] encode(byte[] data, byte[] message, int zeroDelay, int oneDelay, double decay, int segmentlength) {
        // TODO add support for 16kHx and 8 kHz sampling rates
        // convert to something we can handle
        short[] pcmData = littleEndianByteArrayToShorts(data);
        // establish & check boundaries
        int maxLength = data.length / DEFAULT_FRAME_LENGTH;
        int messageLengthInBits = message.length * 8;
        if (messageLengthInBits > maxLength) {
            throw new IllegalArgumentException("Message too long to hide!");
        }

        // Create delayed signals
        short[] d0 = delaySignal(pcmData, zeroDelay);
        short[] d1 = delaySignal(pcmData, oneDelay);

        // Create mixer signal and convolve
        double[] mixer = createMixerSignal(message, pcmData.length, segmentlength);
        short[] temp = convolveThreeSignals(pcmData, d0, d1, mixer, decay);

        return shortArrayToLittleEndianBytes(temp);
    }

    /**
     * Convolves i.e. combines three signals so that the audio is a combination of the three.
     * @param data The cover audio to convolve to.
     * @param zeroDelayed The audio signal delayed by d0, i.e. the zero delay.
     * @param oneDelayed Audio signal delayed by d1.
     * @param mixer The mixer signal is used for selecting between the two delayed signals, depending on the bit being encoded.
     * @param decay The magnitude of the convolved signals, i.e. how loud the echo should be.
     * @return The convolved audio with encoded bits inside.
     */
    public static short[] convolveThreeSignals(short[] data, short[] zeroDelayed, short[] oneDelayed, double[] mixer, double decay) {
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
    public static double[] createMixerSignal(byte[] message, int messageContainerLengthInFrames, int numFramesForSingleBit) {
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
     * @return array of shorts: The original audio with the added echo.
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

    /**
     * Takes a byte array of 16bit PCM data and adds an echo 
     * to it, using default parameters. Mostly used for testing
     * purposes.
     * @param data The data to add the delay to.
     * @return The byte array representing the audio with the added echo.
     */
    public static byte[] simpleEcho(byte[] data) {

        // convert to shorts
        short[] toDelay = littleEndianByteArrayToShorts(data);

        // Hold up, there's a delay
        short[] delayed = delay(toDelay, 88200, 0.5);

        // bak to basics... I mean bytes
        return shortArrayToLittleEndianBytes(delayed);
    }

}
