package test.java;

import Steganography.EHDecoding;
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
public class EHDecoderTest {

    WavFile bigToDecode;

    public EHDecoderTest() throws IOException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("./echoHidingTestFile.wav");
        File file = new File(url.getPath());
        byte[] stuff = Files.readAllBytes(file.toPath());
        bigToDecode = new WavFile(stuff);

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
    public void messageDecodedCorrectly() {
        byte[] data = bigToDecode.getChannelByNumber(1);
        byte[] message = Steganography.EHDecoding.decode(data);
        byte[] stuff = {'s', 'p', 'o', 't', 'i', 'f', 'y', '-'};
        for (int i = 0; i < stuff.length; i++) {
            assertEquals(stuff[i], message[i]);
        }
    }

}
