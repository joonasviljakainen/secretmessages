/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Steganography;

import org.jtransforms.fft.DoubleFFT_1D;
import java.util.List;
import java.util.ArrayList;
import static Math.Mathematics.abs;

/**
 *
 * @author joonas
 */
public class EHDecoding {

    private static final int DEFAULT_FRAME_LENGTH = 8 * 2048;
    private static final int zeroDelay = 150;
    private static final int oneDelay = 300;

    /**
     * Decodes a message from audio that has been modified with echo hiding technique.
     * A convenience method for using default parameters. Mostly used for
     * testing.
     * @param data The audio data to decode
     * @return The decoded message as byte array
     */
    public static byte[] decode(byte[] data) {
        return decode(data, zeroDelay, oneDelay, DEFAULT_FRAME_LENGTH);
    }

    /**
     * Decodes a message from audio that has been modified with echo hiding technique.
     * @param data The audio from which to decode the message.
     * @param zeroDelayAsFrames The anticipated delay for bit 0
     * @param oneDelayAsFrames Anticipated delay for bit 1
     * @param segmentLength Anticipated number of frames dedicated for each encoded bit
     * @return The decoded message as byte array.
     */
    public static byte[] decode(byte[] data, int zeroDelayAsFrames, int oneDelayAsFrames, int segmentLength) {
        short[] dataAsShorts = Utilities.BitManipulation.littleEndianByteArrayToShorts(data);
        List<short[]> segments = new ArrayList<>();
        int numberOfSegments = dataAsShorts.length/segmentLength;

        // Dividing the audio into segments
        for (int i = 0; i < numberOfSegments; i++) {
            short[] segment = new short[segmentLength];
            for (int j = 0, a = i * segmentLength; j < segmentLength; a++, j++ ) {
                segment[j] = dataAsShorts[a];
            }
            segments.add(segment);
        }

        int d0 = zeroDelayAsFrames;
        int d1 = oneDelayAsFrames;

        int[] segBits = new int[segments.size()];
        for (int i = 0; i < segments.size(); i++) {
            double[] rceps = rceps(segments.get(i));
            if (rceps[d0] >= rceps[d1]) {
                segBits[i] = 0;
            } else {
                segBits[i] = 1;
            }
        }
        byte[] result = new byte[segBits.length];
        int location = 0;
        for (int i = 0; i < segBits.length; i+= 8) {
            int cur = 0x00;
            for (int j = 0; j < 8 && i + j < segBits.length; j++) {
                byte bitpos = (byte) (segBits[j + i] << j);
                cur  = (byte) (cur | bitpos);
            }
            result[location++] = (byte) cur;
        }
        return result;
    }

    private static double[] rceps(short[] data) {
        double[] rceps = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            rceps[i] = data[i];
        }

        DoubleFFT_1D f = new DoubleFFT_1D(data.length);
        f.realForward(rceps);
        for(int i = 0; i < rceps.length; i++) {
            rceps[i] = abs(rceps[i]);
            rceps[i] = Math.log(rceps[i]);
            //rceps[i] = ln(rceps[i]);
        }
        f.realInverse(rceps, false);
        return rceps;
    }
}
