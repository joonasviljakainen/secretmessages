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
            /*byte[] bytes = IOManager.readFileToBytes("../samples/a2002011001-e02.wav");
            WavFile big = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            Long l;
            char[] test = "TOOT TOOT TOOTSIE TOOTSIE".toCharArray();
            //char[] test = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

            byte[] bigTestEncoded = LSBEncoder.interleaveMessageInBytes(big.getAudioData(), test, 4);
            big.setAudioData(bigTestEncoded);
            byte[] mes = LSBEncoder.extractMessageFromBytes(bigTestEncoded, 4);

            IOManager.writeBytesToFile(big.toSaveableByteArray(), "encoded-test-44kHz.wav");
            WavFile toDecode = new WavFile(IOManager.readFileToBytes("encoded-test-44kHz.wav"));

            l = System.currentTimeMillis();
            byte[] message = LSBEncoder.extractMessageFromBytes(toDecode.getAudioData(), 4);
            System.out.println("extracting BIG took " + (System.currentTimeMillis() - l) + "ms");

            System.out.println("Extracted messages:");
            for (int i = 0; i < message.length; i++) {
                System.out.print((char) (message[i]));
            }
            System.out.println("");
            System.out.println("");

            
            char[] test2 ="HIPSTERIBRUNSSI ON MAHTAVAAAA".toCharArray();
            //char[] test2 = "ASDFGHJKQWERTYUIOPZXFGHJK".toCharArray();
            //char[] test2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            //char[] test2 = "TOOT TOOT TOOTSIE TOOTSIE".toCharArray();
            WavFile big2 = new WavFile(IOManager.readFileToBytes("../samples/8kHz.wav"));
            byte[] encoded2 = LSBEncoder.interleaveMessageInBytes(big2.getAudioData(), test2, 4);
            big2.setAudioData(encoded2);
            byte[] mes2 = LSBEncoder.extractMessageFromBytes(big2.getAudioData(), 4);

            for (int i = 0; i < test2.length; i++) {
                System.out.print((char) mes2[i]);
            }*/
            WavFile toDelay = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            byte[] s = simpleEcho(toDelay.getChannelByNumber(1));
            toDelay.setChannelByNumber(1, s);
            IOManager.writeBytesToFile(toDelay.toSaveableByteArray(), "delaytest.wav");

            WavFile toAlternateEcho = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            byte[] messageToHide = new byte[15];
            for (int i = 0; i < messageToHide.length; i++) {
                messageToHide[i] = 105;
                System.out.print(messageToHide[i]);
            }
            byte[] t = EHEncoding.encode(toAlternateEcho.getChannelByNumber(1), messageToHide);
            toAlternateEcho.setChannelByNumber(1, t);
            IOManager.writeBytesToFile(toAlternateEcho.toSaveableByteArray(), "alternatingechotest.wav");
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
