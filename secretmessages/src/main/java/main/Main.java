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
import Math.NaturalLogarithm;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        NaturalLogarithm n = new NaturalLogarithm();


        try {

            /*
            WavFile big = new WavFile(IOManager.readFileToBytes("../samples/44kHz.wav"));
            char[] test = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            byte[] message = new byte[test.length];
            for (int i = 0; i < test.length; i++) {
                message[i] = (byte) test[i];
            }

            Long lo = System.currentTimeMillis();
            byte[] bigTestEncoded = LSBEncoder.interleaveMessageInBytes(big.getAudioData(), message, 4);
            System.out.println("LSB Encoded message in " + (System.currentTimeMillis() - lo) + "ms");

            big.setAudioData(bigTestEncoded);
            byte[] mes = LSBEncoder.extractMessageFromBytes(bigTestEncoded, 4);

            IOManager.writeBytesToFile(big.toSaveableByteArray(), "encoded-test-44kHz.wav");
            WavFile toDecode = new WavFile(IOManager.readFileToBytes("encoded-test-44kHz.wav"));


            Long l = System.currentTimeMillis();
            byte[] extractedMessage = LSBEncoder.extractMessageFromBytes(toDecode.getAudioData(), 4);
            System.out.println("extracting BIG took " + (System.currentTimeMillis() - l) + "ms");


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
            byte[] messageToHide = new byte[32];
            messageToHide[0] = (byte) 's';
            messageToHide[1] = (byte) 'p';
            messageToHide[2] = (byte) 'o';
            messageToHide[3] = (byte) 't';
            messageToHide[4] = (byte) 'i';
            messageToHide[5] = (byte) 'f';
            messageToHide[6] = (byte) 'y';
            messageToHide[7] = (byte) '-';

            messageToHide[8] = (byte) 's';
            messageToHide[9] = (byte) 'p';
            messageToHide[10] = (byte) 'o';
            messageToHide[11] = (byte) 't';
            messageToHide[12] = (byte) 'i';
            messageToHide[13] = (byte) 'f';
            messageToHide[14] = (byte) 'y';
            messageToHide[15] = (byte) '-';

            messageToHide[16] = (byte) 's';
            messageToHide[17] = (byte) 'p';
            messageToHide[18] = (byte) 'o';
            messageToHide[19] = (byte) 't';
            messageToHide[20] = (byte) 'i';
            messageToHide[21] = (byte) 'f';
            messageToHide[22] = (byte) 'y';
            messageToHide[23] = (byte) '-';
            

            messageToHide[24] = (byte) 's';
            messageToHide[25] = (byte) 'p';
            messageToHide[26] = (byte) 'o';
            messageToHide[27] = (byte) 't';
            messageToHide[28] = (byte) 'i';
            messageToHide[29] = (byte) 'f';
            messageToHide[30] = (byte) 'y';
            messageToHide[31] = (byte) '-';

            /*
            for (int i = 0; i < messageToHide.length; i++) {
                //messageToHide[i] = (byte)'i';
                //messageToHide[i] = (byte)'a';
                //messageToHide[i] = 0;
                messageToHide[i] = (byte) 0xFF;
            } */

            Long start = System.currentTimeMillis();

            byte[] t = EHEncoding.encode(toAlternateEcho.getChannelByNumber(1), messageToHide);
            System.out.println("EH Encoded message in " + (System.currentTimeMillis() - start) + "ms");

            long startDecoding = System.currentTimeMillis();
            Steganography.EHDecoding.decode(t);
            System.out.println("EH Decoded message in " + (System.currentTimeMillis() - startDecoding) + "ms");

            toAlternateEcho.setChannelByNumber(1, t);
            IOManager.writeBytesToFile(toAlternateEcho.toSaveableByteArray(), "echoHidingRealFile.wav");


            WavFile smoothStuff = new WavFile(IOManager.readFileToBytes("./echoHidingRealFile.wav"));
            byte[] stuf = smoothStuff.getChannelByNumber(1);
            byte[] res = Steganography.EHDecoding.decode(stuf);

            for (int i = 0; i < res.length; i++) {
                System.out.print((char) res[i]);
            }
        
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}
