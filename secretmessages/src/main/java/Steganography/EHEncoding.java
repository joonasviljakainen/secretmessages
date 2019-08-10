/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Steganography;

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
        for (int i = delayAsFrames, a = 0; i < data.length; i++, a++) {
            //int sampleToDelay = data[a]; // e.g. at position 0
            res[a] = (short) ( data[i] + (decay * data[a]));
        }

        return res;

    }

}
