package test.java;

import Steganography.LSBEncoder;
import Math.Mathematics;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import domain.WavFile;
import IO.IOManager;
import java.io.File;
import static org.junit.Assert.*;
import main.java.domain.SecretMessages;

public class SecretMessagesTest {

    SecretMessages s;
    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("./44kHz.wav");
        File file = new File(url.getPath());
        s = new SecretMessages();
        s.setWavfile(file.getPath(), file.getName());
    }

    @After
    public void tearDown() {

    }

    @Test
    public void works() {
        assertNotNull(s);
    }

    @Test
    public void canSetAlgorithm() {
        assertNotEquals(s.getAlg(), "LSB");
        s.setAlg(0);
        assertEquals(s.getAlg(), "LSB");
    }

    @Test
    public void echoHidingWorks() {
        //byte[] message = {'s', 'p', 'o', 't', 'i', 'f', 'y', '-'};
        String message = "Spotify-";
        char[] chars = message.toCharArray();
        s.encode(message);

        byte[] decoded = s.decode();

        for (int i = 0; i < chars.length; i++) {
            assertEquals((byte) chars[i], decoded[i]);
        }
    }
    
 // TODO:  THIS SHOULD WORK ONCE I PLUG LSB IN
    @Test
    public void leastSignificantBitWorks() {
        s.setAlg(0);
        String message = "Spotify-";
        char[] chars = message.toCharArray();
        s.encode(message);

        byte[] decoded = s.decode();

        for (int i = 0; i < chars.length; i++) {
            assertEquals((byte) chars[i], decoded[i]);
        }
    }

    @Test
    public void stringToBytesIsCorrect() {
        String s = "ookoo my man";
        char[] c = s.toCharArray();
        byte[] b = SecretMessages.stringToBytes(s);

        for (int i = 0; i < c.length; i++ ){
            assertEquals((byte) c[i], b[i]);
        }
    }

    @Test
    public void gettersAndSettersFunctionCorrectly() {
        int initd0 = s.getZeroDelay();
        int initd1 = s.getOneDelay();
        int initSegmentlength = s.getSegmentLength();

        s.setZeroDelay(10);
        s.setOneDelay(200);
        s.setSegmentLength(2048);

        assertEquals(10, s.getZeroDelay());
        assertEquals(200, s.getOneDelay());
        assertEquals(2048, s.getSegmentLength());

        s.resetParameters();

        assertEquals(initd0, s.getZeroDelay());
        assertEquals(initd1, s.getOneDelay());
        assertEquals(initSegmentlength, s.getSegmentLength());

        assertEquals("44kHz.wav", s.getFileName());
    }

    @Test
    public void miscellaneousTests() {
        assertEquals(true, s.fileLoaded());
        assertEquals((int) 2, (int) s.getNumberOfChannels());
        assertEquals((int) 1, (int) s.getChannelNum());
        s.setChannel(1);
        assertEquals((int) 2, (int) s.getChannelNum());


    }
}
