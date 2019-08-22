package main.java.domain;

import Steganography.EHEncoding;
import Steganography.EHDecoding;
import Steganography.LSBEncoder;
import java.io.IOException;
import java.lang.Exception;

public class SecretMessages {

    private final int DEFAULT_FRAME_LENGTH = 8 * 2048;
    private final int DEFAULT_ZERO_DELAY = 150;
    private final int DEFAULT_ONE_DELAY = 300;
    private final double DEFAULT_ECHO_AMPLITUDE = 0.7;

    private int samplingRate;
    private int frameLength = DEFAULT_FRAME_LENGTH;
    private int zeroDelay = DEFAULT_ZERO_DELAY;
    private int oneDelay = DEFAULT_ONE_DELAY;
    private double echoAmplitude = DEFAULT_ECHO_AMPLITUDE;

    private int alg = 1;
    private int channelNum = 1;
    private domain.WavFile wavFile;

    public SecretMessages() {

    }

    public void setWavfile(String path, String name) {
        try {
            wavFile = new domain.WavFile(IO.IOManager.readFileToBytes(path));
            System.out.println("WAVFILE INITIALIZED FOR DECODING");
        } catch(IOException e) {
            System.out.println("THERE WAS AN ERROR INITIALIZING SHITE");
        }
    }

    public int getFrameLength() {
        return this.frameLength;
    }

    public int getOneDelay() {
        return this.oneDelay;
    }

    public int getZeroDelay() {
        return this.zeroDelay;
    }

    public void setChannel(int channelNum) {
        this.channelNum = channelNum;
    }

    public int getChannelNum() {
        return this.channelNum;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

    public void setZeroDelay(int delay) {
        this.zeroDelay = delay;
    }

    public void resetParameters() {
        frameLength = DEFAULT_FRAME_LENGTH;
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

    public void encode(String message) {
        byte [] messageAsBytes = stringToBytes(message);
        // TODO string to bytes
        if (this.alg == 1) {
            System.out.println("ENCODING...");
            /*Steganography.EHEncoding.encode(wavFile.getChannelByNumber(this.channelNum),
                    messageAsBytes,
                    this.zeroDelay,
                    this.oneDelay,
                    DEFAULT_ECHO_AMPLITUDE,
                    44100);*/
            byte[] data = Steganography.EHEncoding.encode(wavFile.getChannelByNumber(this.channelNum), messageAsBytes);
            wavFile.setChannelByNumber(this.channelNum, data);
        } else {
            System.out.println("NOT ENCODING...");
            // TODO LSB Encoding
        }
    }

    public byte[] decode() {
        try {
        if (this.alg == 1) {
            /*byte[] b = Steganography.EHDecoding.decode(
                    wavFile.getChannelByNumber(this.channelNum),
                    this.zeroDelay,
                    this.oneDelay,
                    this.frameLength);*/
            byte[] b = Steganography.EHDecoding.decode(wavFile.getChannelByNumber(this.channelNum));
            return b;
        } else {
            System.out.println("no error, just bumming out");
 // TODO LSB Decoding
        }
        } catch(Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
        return null;
    }

    private byte[] stringToBytes(String s) {
        byte[] b = new byte[s.length()];
        for (int i = 0; i < b.length; i++) {

            b[i] = (byte) s.charAt(i);
            System.out.println((char) b[i]);
        }

        return b;
    }
}
