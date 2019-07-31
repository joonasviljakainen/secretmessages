package main;

import java.util.Scanner;
import IO.IOManager;
import Math.Mathematics;
import domain.WavFile;
import IO.IOManager;
import java.io.IOException;
import Steganography.LSBEncoder;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            byte[] bytes = IOManager.readFileToBytes("../samples/a2002011001-e02.wav");
            WavFile big = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            Long l;
            char[] test = "TOOT TOOT TOOTSIE TOOTSIE".toCharArray();

            byte[] bigTestEncoded = LSBEncoder.interleaveMessageInBytes(big.getAudioData(), test, 4);
            big.setAudioData(bigTestEncoded);
            IOManager.writeBytesToFile(big.toSaveableByteArray(), "encoded-test-44kHz.wav");
            WavFile toDecode = new WavFile(IOManager.readFileToBytes("encoded-test-44kHz.wav"));

            l = System.currentTimeMillis();
            byte[] message = LSBEncoder.extractMessageFromBytes(toDecode.getAudioData(), 4);
            byte[] mes = LSBEncoder.extractMessageFromBytes(bigTestEncoded, 4);
            System.out.println("extracting BIG took " + (System.currentTimeMillis() - l) + "ms");

            System.out.println("Extracted messages:");
            for (int i = 0; i < message.length; i++) {
                System.out.print((char) (message[i]));
            }

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
