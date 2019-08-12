/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Steganography;

import static Utilities.BitManipulation.littleEndianBytesToShort;
import static Utilities.BitManipulation.shortToLittleEndianBytes;

/**
 *
 * @author joonas
 */
public class EHEncoding {

    private static final double zeroDelay = 200.0;
    //private static final double oneDelay = 300.0;
    private static final double oneDelay = 1000.0;
    private static final int DEFAULT_FRAME_LENGTH = 8 * 1024;
    private static final double DEFAULT_ECHO_AMPLITUDE = 0.5;
    private static final short SIGNAL_LIMIT_MAGNITUDE = 32760;

    public static int[] encode(int[] data, char[] message) {
        int maxLength = data.length / DEFAULT_FRAME_LENGTH;
        int messageLengthInBits = message.length * 8;

        if (messageLengthInBits > maxLength) {
            throw new IllegalArgumentException("Message too long to hide!");
        }

        int zeroDelayAsNumberOfFrames = (int) (44100 * (zeroDelay / 1000.0)); // assuming 44100 Hz
        System.out.println("asdf " + zeroDelayAsNumberOfFrames);
        int oneDelayAsNumberOfFrames = (int) (44100 * (oneDelay / 1000.0)); //; -- || --
        System.out.println("asdf " + oneDelayAsNumberOfFrames);

        int[] d0 = delaySignal(data, zeroDelayAsNumberOfFrames);
        int[] d1 = delaySignal(data, oneDelayAsNumberOfFrames);

        int[] res = new int[data.length];

        // TODO: create mixer signal
        return res;
    }

    public static int[] delaySignal(int[] data, int delay) {
        int[] res = new int[data.length];
        for (int i = 0; i < delay; i++) {
            res[i] = 0;
        }
        for (int i = delay, a = 0; i < data.length; i++, a++) {
            res[i] = data[a];
        }
        return res;
    }

    /*
      public void filter(int[] samples, int delayAsFrames, int length, double decay) {

    for (int i = delayAsFrames; i < delayAsFrames + length; i += 2) {
      // update the sample
      int oldSample = samples[i];
      int newSample =  (oldSample + decay
          * delayBuffer[delayBufferPos]);
      setSample(samples, i, newSample);

      // update the delay buffer
      delayBuffer[delayBufferPos] = newSample;
      delayBufferPos++;
      if (delayBufferPos == delayBuffer.length) {
        delayBufferPos = 0;
      }
    }*/
    public static short[] delay(short[] data, int delayAsFrames, int length, double decay) {
        short[] res = new short[data.length];
        for (int i = 0; i < delayAsFrames; i++) {
            res[i] = data[i];
        }
        for (int i = delayAsFrames, a = 0; i < data.length; i++, a++) {
            // Range checks to prevent overflow (overflow -> distortion, and 
            // obviously we want the echo to be as unnoticeable as possible)
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
            res[i] = (short) (data[i] + (decay * data[a]));

        }

        return res;

    }

    public static byte[] simpleEcho(byte[] data) {
        byte[] res = new byte[data.length];
        // convert to shorts
        short[] toDelay = new short[res.length / 2];
        int cur;
        for (int i = 0; i < toDelay.length; i++) {
            cur = i * 2;
            toDelay[i] = littleEndianBytesToShort(data[cur], data[cur + 1]);
        }

        // Hold up, there's a delay
        //short[] delayed = delay(toDelay, 4, 1, 1.0);
        short[] delayed;
        delayed = delay(toDelay, 88200, 1, 0.5);

        // bak to basics... I mean bytes
        for (int i = 0; i < delayed.length; i++) {
            cur = 2 * i;
            byte[] ll = shortToLittleEndianBytes(delayed[i]);
            res[cur] = ll[0];
            res[cur + 1] = ll[1];
        }

        return res;
    }

}
