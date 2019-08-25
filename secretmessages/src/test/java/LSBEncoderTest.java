/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Steganography.LSBEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import domain.WavFile;
import IO.IOManager;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import static org.junit.Assert.*;
import java.util.Random;

/**
 *
 * @author joonas
 */
public class LSBEncoderTest {

    WavFile bigUnencoded;
    WavFile bigEncoded;
    WavFile smallUnencoded;
    WavFile smallEncoded;

    byte[] messageArr1;
    byte[] messageArr2;

    public LSBEncoderTest() throws IOException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("./44kHz.wav");
        File file = new File(url.getPath());
        byte[] stuff = Files.readAllBytes(file.toPath());
        bigUnencoded = new WavFile(stuff);

        URL smolUrl = Thread.currentThread().getContextClassLoader().getResource("./44kHz.wav");
        File smolFile = new File(url.getPath());
        byte[] smolStuff = Files.readAllBytes(file.toPath());
        smallUnencoded = new WavFile(smolStuff);
        //bigUnencoded = new WavFile(IOManager.readFileToBytes("./java/resources/44kHz.wav"));
        //smallUnencoded = new WavFile(IOManager.readFileToBytes("./java/resources/8kHz.wav"));
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        char[] a1 = "Hipsteribrunssi".toCharArray();
        char[] a2 = "Kesabiisit".toCharArray();

        messageArr1 = new byte[a1.length];
        messageArr2 = new byte[a2.length];

        for (int i = 0; i < a1.length; i++) {
            messageArr1[i] = (byte) a1[i];
        }

        for (int i = 0; i < a2.length; i++) {
            messageArr2[i] = (byte) a2[i];
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageHiddenWithoutCrashing() {
        byte[] test1 = LSBEncoder.interleaveMessageInBytes(bigUnencoded.getAudioData(), messageArr1, 4);
        byte[] test2 = LSBEncoder.interleaveMessageInBytes(smallUnencoded.getAudioData(), messageArr2, 4);
    }

    @Test
    public void decoderCanDecodeHiddenMessage() {

        byte[] test1 = LSBEncoder.interleaveMessageInBytes(bigUnencoded.getAudioData(), messageArr1, 4);
        byte[] test2 = LSBEncoder.interleaveMessageInBytes(smallUnencoded.getAudioData(), messageArr2, 4);

        byte[] extracted1 = LSBEncoder.extractMessageFromBytes(test1, 4);
        byte[] extracted2 = LSBEncoder.extractMessageFromBytes(test2, 4);
        for (int i = 0; i < messageArr1.length; i++) {
            assertEquals(messageArr1[i], (char) extracted1[i]);
        }
        for (int i = 0; i < messageArr2.length; i++) {
            assertEquals(messageArr2[i], (char) extracted2[i]);
        }
    }

    @Test
    public void decoderCanDecodeMidLengthMessage() {
        String text = "n this section, you will learn how to use bitwise XOR \"^\" operator in Java. The Java programming language has operators that perform bitwise operations. In the example below we have shown the usage of  bitwise XOR \"^\" operator.\n"
                + "\n"
                + "Description of code:\n"
                + "\n"
                + "The bitwise XOR \"^\" operator  produces 1 if both of the bits in its operands are different. However, if both of the bits are same then this operator produces 0. Moreover if both of the bits are 1 i.e. 1^1 then also it produces 1.\n"
                + "\n"
                + "In the program code given below, the corresponding bits of both operands are 1 and 0, hence we get 1 as output because the bits are different. ";
        char[] arr = text.toCharArray();
        byte[] messageAsBytes = new byte[arr.length];

        for (int i = 0; i < messageAsBytes.length; i++) {
            messageAsBytes[i] = (byte) arr[i];
        }
        byte[] test2 = LSBEncoder.interleaveMessageInBytes(smallUnencoded.getAudioData(), messageAsBytes, 4);
        byte[] decoded = LSBEncoder.extractMessageFromBytes(test2, 4);
        for (int i = 0; i < text.length(); i++) {
            assertEquals(arr[i], (char) decoded[i]);
        }
    }
}
