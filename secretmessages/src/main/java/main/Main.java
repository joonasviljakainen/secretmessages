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
            /*for (int i = 0; i < 40; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print((char) (bytes[4 * i + j]));
                }
                System.out.println();
            }*/

            IOManager.writeBytesToFile(bytes, "test.wav");

            WavFile big = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            System.out.println("");
            //WavFile medium = new WavFile("../samples/16kHz.wav");
            //System.out.println("");

            WavFile small = new WavFile(IOManager.readFileToBytes("../samples/8kHz.wav"));
            System.out.println(small.getSummary());
            System.out.println("Can save up to " + LSBEncoder.analyzeMaxNumberOfBytes(small.getAudioData(), small.getBlockAlign()) + " bytes");
            System.out.println(Integer.toBinaryString((byte) 120));
            byte test1 = LSBEncoder.interleaveBitToByte((byte) 120, 0);
            byte test2 = LSBEncoder.interleaveBitToByte((byte) 120, 1);
            
            System.out.println("120, 0:" + Integer.toBinaryString(test1));
            System.out.println("Extracted::" + Integer.toBinaryString(LSBEncoder.extractBitFromByte(test1)));
            System.out.println("120, 1:" + Integer.toBinaryString(test2));
            System.out.println("Extracted::" + Integer.toBinaryString(LSBEncoder.extractBitFromByte(test2)));

            System.out.println("0:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 0)));
            System.out.println("1:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 1)));
            System.out.println("2:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 2)));
            System.out.println("3:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 3)));
            System.out.println("120:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 120)));
            System.out.println("121:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 121)));
            System.out.println("122:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 122)));
            System.out.println("123:" + Integer.toBinaryString(LSBEncoder.extractBitFromByte((byte) 123)));

            byte[] byt = small.toSaveableByteArray();
            /*byte[] straight = IOManager.readFileToBytes("../samples/8kHz.wav");
            for (int i = 0; i < 100; i++) {
                System.out.println((char) byt[i] + " :: " + (char) straight[i]);
            }*/
            IOManager.writeBytesToFile(small.toSaveableByteArray(), "test-8khz.wav");

        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}
