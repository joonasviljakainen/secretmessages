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

/**
 *
 * @author joonas
 */
public class LSBEncoderTest {

    WavFile bigUnencoded;
    WavFile bigEncoded;
    WavFile smallUnencoded;
    WavFile smallEncoded;

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

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageHiddenWithoutCrashing() {
        byte[] test1 = LSBEncoder.interleaveMessageInBytes(bigUnencoded.getAudioData(), "Hipsteribrunssi".toCharArray(), 4);
        byte[] test2 = LSBEncoder.interleaveMessageInBytes(smallUnencoded.getAudioData(), "Kesabiisit".toCharArray(), 4);
    }
    
    @Test
    public void decoderCanDecodeHiddenMessage() {
        char[] arr1 = "Hipsteribrunssi".toCharArray();
        char[] arr2 = "Kesabiisit".toCharArray();
        
        
        byte[] test1 = LSBEncoder.interleaveMessageInBytes(bigUnencoded.getAudioData(), arr1, 4);
        byte[] test2 = LSBEncoder.interleaveMessageInBytes(smallUnencoded.getAudioData(), arr2, 4);
        
        byte[] extracted1 = LSBEncoder.extractMessageFromBytes(test1, 4);
        byte[] extracted2 = LSBEncoder.extractMessageFromBytes(test2, 4);
        System.out.println("MÖS");
        for (int i = 0; i < arr1.length; i++) {
            System.out.println(i);
            assertEquals(arr1[i], (char) extracted1[i]);
        }
        
        System.out.println("MÖS2");
        for (int i = 0; i < arr2.length; i++) {
            assertEquals(arr2[i], (char) extracted2[i]);
        }
        
    }

}
