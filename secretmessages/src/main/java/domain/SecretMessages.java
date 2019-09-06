package main.java.domain;

import Steganography.EHEncoding;
import Steganography.EHDecoding;
import Steganography.LSBEncoder;
import java.io.IOException;
import java.lang.Exception;

public class SecretMessages {

    private final int DEFAULT_SEGMENT_LENGTH = 8 * 2048;
    private final int DEFAULT_ZERO_DELAY = 150;
    private final int DEFAULT_ONE_DELAY = 300;
    private final double DEFAULT_ECHO_AMPLITUDE = 0.7;

    private String fileName;

    private int samplingRate;
    private int segmentLength = DEFAULT_SEGMENT_LENGTH; // The length of the frame devoted to a single bit
    private int zeroDelay = DEFAULT_ZERO_DELAY;
    private int oneDelay = DEFAULT_ONE_DELAY;
    private double echoAmplitude = DEFAULT_ECHO_AMPLITUDE;

    private int alg = 1;
    private int channelNum = 1; // either 1 or 2;
    private domain.WavFile wavFile;

    /**
     * Initiates the object used for steganographic processing.
     */
    public SecretMessages() {

    }

    /*
     *
     * Proper instance methods
     * 
     */

     /**
      * used for invoking the appropriate IO managers and loading a wav file for processing.
      * @param path Path to the current WAV file.
      * @param name Name of the file.
      */
    public void setWavfile(String path, String name) {
        try {
            wavFile = new domain.WavFile(IO.IOManager.readFileToBytes(path));
            fileName = name;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Can be used to check if a wav file has been loaded for processing.
     * @return true if file loaded, false if not.
     */
    public boolean fileLoaded() {
        return this.wavFile != null;
    }

    /**
     * Fetches a saveable byte array for the current wav file.
     * @return The current wav file as byte array.
     */
    public byte[] getSaveableByteArray() {
        if (this.wavFile != null) {
            return this.wavFile.toSaveableByteArray();
        }

        return null;
    }

    /**
     * Calculates maximum possible length for messages when using Least-Significant Bit 
     * encoding.
     * @return Maximum possible message length using LSB.
     */
    public int getMaxLengthForLSB() {
        return this.wavFile.getDataChunkSize() / (this.wavFile.getBitsPerSample() / 8)
                / this.wavFile.getNumberOfChannels();
    }

    /**
     * Calculates the maximum possible length of messages to be hidden when using 
     * Echo Hiding and the current set of parameters.
     * @return Max length of message as bytes.
     */
    public int getMaxLengthForEH() {
        return this.wavFile.getDataSize() / (this.wavFile.getBitsPerSample() / 8) / this.wavFile.getNumberOfChannels()
                / this.segmentLength / 8;
    }

    /**
     * Encodes the currently selected audio channel using the currently selected algorithm.
     * @param message String to hide
     */
    public void encode(String message) {
        byte[] messageAsBytes = stringToBytes(message);
        byte[] data;
        if (this.alg == 1) {
            data = Steganography.EHEncoding.encode(wavFile.getChannelByNumber(this.channelNum), messageAsBytes,
                    this.zeroDelay, this.oneDelay, DEFAULT_ECHO_AMPLITUDE, this.segmentLength);
        } else {
            data = Steganography.LSBEncoder.interleaveMessageInBytes(wavFile.getChannelByNumber(this.channelNum),
                    messageAsBytes, 2);
        }
        wavFile.setChannelByNumber(this.channelNum, data);
    }

    /**
     * Decodes the currently selected audio channel using the currently selected algorithm.
     * @return Decoded message as byte array
     */
    public byte[] decode() {
        try {
            byte[] b;
            if (this.alg == 1) {
                b = Steganography.EHDecoding.decode(wavFile.getChannelByNumber(this.channelNum), this.zeroDelay,
                        this.oneDelay, this.segmentLength);
            } else {
                b = Steganography.LSBEncoder.extractMessageFromBytes(wavFile.getChannelByNumber(this.channelNum), 2);
            }
            return b;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Converts a string to a byte array. Each character is replaced with 
     * a single byte, meaning any non-ASCII values will be lost.
     * 
     * @param s String to convert
     * @return Byte array corresponding to the original string.
     */
    public static byte[] stringToBytes(String s) {
        byte[] b = new byte[s.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) s.charAt(i);
        }
        return b;
    }

    // GETTERS

    /**
     * 
     * @return The name of the currently loaded file.
     */
    public String getFileName() {
        if (this.fileName == null) {
            return "No File Selected";
        }
        return this.fileName;
    }

    /**
     * 
     * @return The length (number of frames/samples) of a segment dedicated to a single bit.
     */
    public int getSegmentLength() {
        return this.segmentLength;
    }

    /**
     * 
     * @return The delay (as frames) for bit 1 as it is currently set.
     */
    public int getOneDelay() {
        return this.oneDelay;
    }

    /**
     * 
     * @return The delay (as frames) for bit 0 as it is currently set.
     */
    public int getZeroDelay() {
        return this.zeroDelay;
    }

    /**
     * returns the number of the currently selected channel.
     * 
     * @return number of the currently selected channel.
     */
    public int getChannelNum() {
        return this.channelNum;
    }

    /**
     * Returns the number of channels available in the WAV file.
     * @return Number of channels.
     */
    public Integer getNumberOfChannels() {
        if (this.wavFile != null) {
            return new Integer(this.wavFile.getNumberOfChannels());
        } else {
            return null;
        }

    }

    /*
     *
     * SETTERS 
     * 
     */

    /**
     * Sets the currently selected channel number.
     * 
     * @param channelNum Number of the channel. 0 for left-most channel.
     */
    public void setChannel(int channelNum) {
        this.channelNum = channelNum + 1;
    }


    /**
     * Sets the length of a segment, i.e. number of frames/samples 
     * used for encoding a single bit when using Echo Hiding.
     * @param segmentLength Number of frames per single data bit.
     */
    public void setSegmentLength(int segmentLength) {
        this.segmentLength = segmentLength;
    }

    /**
     * 
     * @param delay number of frames to delay for bit 0. Must be smaller than segmentLength / 2
     */
    public void setZeroDelay(int delay) {
        if (delay < this.segmentLength / 2) {
            this.zeroDelay = delay; 
        }
    }

    /**
     * Sets segment length, d0 and d1 to their default values.
     */
    public void resetParameters() {
        segmentLength = DEFAULT_SEGMENT_LENGTH;
        zeroDelay = DEFAULT_ZERO_DELAY;
        oneDelay = DEFAULT_ONE_DELAY;
    }

    /**
     * 
     * @param delay number of frames to delay for bit 1. Must be smaller than segmentLength / 2
     */
    public void setOneDelay(int delay) {
        if (delay < this.segmentLength / 2) {
            this.oneDelay = delay;
        }
    }

    /**
     * Returns the currently selected algorithm. 1 = LSB, 2 = Echo hiding
     * @return 1 = LSB, 2 = Echo hiding.
     */
    public String getAlg() {
        if (this.alg == 0) {
            return "LSB";
        } else {
            return "Echo Hiding";
        }
    }

    /**
     * Sets the currently selected algorithm. 1 = LSB, 2 = Echo hiding.
     * @param alg
     */
    public void setAlg(int alg) {
        this.alg = alg;
    }
}
