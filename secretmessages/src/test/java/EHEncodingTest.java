package test.java;

import java.util.Random;
import Steganography.EHEncoding;
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

public class EHEncodingTest {

    WavFile bigUnencoded;
    WavFile bigEncoded;
    WavFile smallUnencoded;
    WavFile smallEncoded;

    public EHEncodingTest() throws IOException {

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
    public void signaldelayedappropriately() {
        short[] test = new short[100];
        int delayFrames = 21;
        Random r = new Random();

        for (int i = 0; i < test.length; i++) {
            short o = (short) r.nextInt();
            test[i] = o;
        }
        short[] delayed = EHEncoding.delaySignal(test, delayFrames);
        for (int i = 0; i < delayFrames; i++) {
            assertEquals(delayed[i], 0);
        }
        for (int i = delayFrames, a = 0; i < test.length; i++, a++) {

        }



    }

}

