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
        bigUnencoded = new WavFile(IOManager.readFileToBytes("./java/resources/44kHz.wav"));
        smallUnencoded = new WavFile(IOManager.readFileToBytes("./java/resources/8kHz.wav"));
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
    
    
}
