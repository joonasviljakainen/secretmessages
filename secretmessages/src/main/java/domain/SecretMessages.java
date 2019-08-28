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

    public SecretMessages() {

    }

    /*
     *
     * Proper instance methods
     * 
     */

    public void setWavfile(String path, String name) {
        try {
            wavFile = new domain.WavFile(IO.IOManager.readFileToBytes(path));
            fileName = name;
            System.out.println("WAVFILE INITIALIZED FOR DECODING");
        } catch (IOException e) {
            System.out.println("THERE WAS AN ERROR INITIALIZING SHITE");
        }
    }

    public boolean fileLoaded() {
        System.out.println("CHECKING FOR EXISTENCE OF WWAVFILE " + (this.wavFile != null));
        return this.wavFile != null;
    }

    /**
     * Fetches a saveable byte array for the current wav file.
     * @return
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
     * @return
     */
    public int getMaxLengthForLSB() {
        return this.wavFile.getDataChunkSize() / (this.wavFile.getBitsPerSample() / 8)
                / this.wavFile.getNumberOfChannels();
    }

    /**
     * Calculates the maximum possible length of messages to be hidden when using 
     * Echo Hiding and the current set of parameters.
     * @return 
     */
    public int getMaxLengthForEH() {
        return this.wavFile.getDataSize() / (this.wavFile.getBitsPerSample() / 8) / this.wavFile.getNumberOfChannels()
                / this.segmentLength;
    }

    public void encode(String message) {
        byte[] messageAsBytes = stringToBytes(message);
        byte[] data;
        if (this.alg == 1) {
            data = Steganography.EHEncoding.encode(wavFile.getChannelByNumber(this.channelNum), messageAsBytes,
                    this.zeroDelay, this.oneDelay, DEFAULT_ECHO_AMPLITUDE, this.segmentLength);
        } else {
            data = Steganography.LSBEncoder.interleaveMessageInBytes(wavFile.getChannelByNumber(this.channelNum),
                    messageAsBytes, 2);
            // TODO LSB Encoding
        }
        wavFile.setChannelByNumber(this.channelNum, data);
    }

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
            System.out.println("ERROR");
            System.out.println(e);
        }
        return null;
    }

    /**
     * Converts a string to a byte array. Any non-ASCII values will be lost.
     * 
     * @param s
     * @return
     */
    public static byte[] stringToBytes(String s) {
        byte[] b = new byte[s.length()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) s.charAt(i);
        }
        return b;
    }

    // GETTERS

    public String getFileName() {
        if (this.fileName == null) {
            return "No File Selected";
        }
        return this.fileName;
    }

    public int getSegmentLength() {
        return this.segmentLength;
    }

    public int getOneDelay() {
        return this.oneDelay;
    }

    public int getZeroDelay() {
        return this.zeroDelay;
    }

    /**
     * returns the number of the currently selected channel.
     * 
     * @return
     */
    public int getChannelNum() {
        return this.channelNum;
    }

    /**
     * 
     * @return
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
     * @param channelNum
     */
    public void setChannel(int channelNum) {
        this.channelNum = channelNum + 1;
    }


    public void setSegmentLength(int segmentLength) {
        this.segmentLength = segmentLength;
    }

    public void setZeroDelay(int delay) {
        this.zeroDelay = delay;
    }

    public void resetParameters() {
        segmentLength = DEFAULT_SEGMENT_LENGTH;
        zeroDelay = DEFAULT_ZERO_DELAY;
        oneDelay = DEFAULT_ONE_DELAY;
    }

    public void setOneDelay(int delay) {
        this.oneDelay = delay;
    }

    public String getAlg() {
        if (this.alg == 0) {
            return "LSB";
        } else {
            return "Echo Hiding";
        }
    }

    public void setAlg(int alg) {
        this.alg = alg;
    }
}
