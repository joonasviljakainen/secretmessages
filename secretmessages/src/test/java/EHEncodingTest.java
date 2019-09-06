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

import static Steganography.EHEncoding.*;
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
    public void signalDelayedAppropriately() {
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
            assertEquals(delayed[i], test[a]);
        }
    }

    @Test
    public void tinySignalEchoedAppropriately() {
        short[] test = {260, -260};
        short[] echoed = delay(test, 1, 1.0);
        assertEquals(0, echoed[1]);
    }

    @Test
    public void signalEchoValuesLimited() {
        short[] test = {32000, -32000, 32000, -32000};
        short[] echoed = delay(test, 2, 1.0);
        assertEquals(32760, echoed[2]);
        assertEquals(-32760, echoed[3]);
    }

    @Test
    public void echoMagnitudeAdjustedAccordingToProvidedValue() {
        short[] test = {500, -500, 20000, -12345, 0, 0, 0, 0};
        short[] delayed = delay(test, 4, 0.5);
        assertEquals(delayed[4], 250);
        assertEquals(delayed[5], -250);
        assertEquals(delayed[6], 10000);
        assertEquals(delayed[7], -12345 / 2);

    }


    @Test
    public void simpleEchoTurnsOutFine() {
        byte[] testCoverData = new byte[88200 * 4];
        Random r = new Random(222);
        for (int i = 0; i < testCoverData.length/2; i++) {
            testCoverData[i] = (byte) r.nextInt();
        }
        for (int i = testCoverData.length/2; i < testCoverData.length; i++) {
            testCoverData[i] = 0;
        }

        byte[] echoed = simpleEcho(testCoverData);

        for(int i = testCoverData.length / 2, a = 0; i < testCoverData.length; i+=2, a+=2) {
            assertEquals(Utilities.BitManipulation.littleEndianBytesToShort((testCoverData[a]), testCoverData[a + 1]) / 2,
                    Utilities.BitManipulation.littleEndianBytesToShort(echoed[i], echoed [i + 1]));
        }
    }

    @Test
    public void signalConvolutionGeneratesRationalData() {
        int testDataLength = 2000;
        Random r = new Random(1338);
        int zeroDelay = 50;
        int oneDelay = 150;
        short[] testData = new short[testDataLength];
        short[] emptyTestData = new short[testDataLength];
        double[] mixer = new double[testDataLength];

        for (int i = 0; i < testData.length; i++ ){
            testData[i] = (short) r.nextInt();
            emptyTestData[i] = 0;
            mixer[i] = i % 2;
        }

        short[] d0 = delaySignal(testData, zeroDelay);
        short[] d1 = delaySignal(testData, oneDelay);

        short[] convolved = EHEncoding.convolveThreeSignals(emptyTestData, d0, d1, mixer, 1.0);

        for (int i = 0; i < convolved.length; i++) {
            if (mixer[i] == 1.0) {
                assertEquals(d1[i], convolved[i]);
            } else {
                assertEquals(d0[i], convolved[i]);
            }
        }
    }
}

