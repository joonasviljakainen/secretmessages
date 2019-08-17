package main;

import java.util.Scanner;
import IO.IOManager;
import Math.Mathematics;
import domain.WavFile;
import IO.IOManager;
import Steganography.EHEncoding;
import static Steganography.EHEncoding.simpleEcho;
import java.io.IOException;
import Steganography.LSBEncoder;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            WavFile big = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            char[] test = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

            Long lo = System.currentTimeMillis();
            byte[] bigTestEncoded = LSBEncoder.interleaveMessageInBytes(big.getAudioData(), test, 4);
            System.out.println("LSB Encoded message in " + (System.currentTimeMillis() - lo) + "ms");

            big.setAudioData(bigTestEncoded);
            byte[] mes = LSBEncoder.extractMessageFromBytes(bigTestEncoded, 4);

            IOManager.writeBytesToFile(big.toSaveableByteArray(), "encoded-test-44kHz.wav");
            WavFile toDecode = new WavFile(IOManager.readFileToBytes("encoded-test-44kHz.wav"));


            Long l = System.currentTimeMillis();
            byte[] message = LSBEncoder.extractMessageFromBytes(toDecode.getAudioData(), 4);
            System.out.println("extracting BIG took " + (System.currentTimeMillis() - l) + "ms");
/*

            char[] test2 ="HIPSTERIBRUNSSI ON MAHTAVAAAA".toCharArray();
            //char[] test2 = "TOOT TOOT TOOTSIE TOOTSIE".toCharArray();
            */


            // ECHO HIDING
            // ___________
            WavFile toDelay = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            byte[] s = simpleEcho(toDelay.getChannelByNumber(1));
            toDelay.setChannelByNumber(1, s);
            IOManager.writeBytesToFile(toDelay.toSaveableByteArray(), "delaytest.wav");

            WavFile toAlternateEcho = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            byte[] messageToHide = new byte[15];

            for (int i = 0; i < messageToHide.length; i++) {
                //messageToHide[i] = (byte)'i';
                //messageToHide[i] = 0;
                messageToHide[i] = (byte) 0xFF;
            }

            Long start = System.currentTimeMillis();

            byte[] t = EHEncoding.encode(toAlternateEcho.getChannelByNumber(1), messageToHide);
            System.out.println("EH Encoded message in " + (System.currentTimeMillis() - start) + "ms");

            long startDecoding = System.currentTimeMillis();
            Steganography.EHDecoding.decode(t);
            System.out.println("EH Decoded message in " + (System.currentTimeMillis() - startDecoding) + "ms");

            toAlternateEcho.setChannelByNumber(1, t);
            IOManager.writeBytesToFile(toAlternateEcho.toSaveableByteArray(), "echoHidingRealFile.wav");
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }

    public static byte abs(byte src) {
        if (src < 0) {
            return (byte) -src;
        }
        return src;
    }
}
