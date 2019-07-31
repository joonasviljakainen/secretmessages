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

    /*
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
     */
    public LSBEncoderTest() {

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
    public void singleBitsInterleavedCorrectly() {
        byte test1 = 120;
        byte test2 = 121;

        String test1res1 = Integer.toBinaryString(LSBEncoder.interleaveBitToByte(test1, 0));
        String test1res2 = Integer.toBinaryString(LSBEncoder.interleaveBitToByte(test1, 1));
        String test2res1 = Integer.toBinaryString(LSBEncoder.interleaveBitToByte(test2, 0));
        String test2res2 = Integer.toBinaryString(LSBEncoder.interleaveBitToByte(test2, 1));

        assertEquals(test1res1, "1111000");
        assertEquals(test1res2, "1111001");
        assertEquals(test2res1, "1111000");
        assertEquals(test2res2, "1111001");
    }

    @Test
    public void allBytesInterleavedCorrectly() {
        for (byte i = -126; i < 126; i += 2) {
            byte test1 = i;
            byte test2 = (byte) (i + 1);

            String test1res1 = Integer.toBinaryString((byte) LSBEncoder.interleaveBitToByte(test1, 0));
            String test1res2 = Integer.toBinaryString((byte) LSBEncoder.interleaveBitToByte(test1, 1));
            String test2res1 = Integer.toBinaryString((byte) LSBEncoder.interleaveBitToByte(test2, 0));
            String test2res2 = Integer.toBinaryString((byte) LSBEncoder.interleaveBitToByte(test2, 1));

            assertEquals(test1res1, test2res1);
            assertEquals(test1res2, test2res2);
        }
    }

    @Test
    public void LastByteExtractedCorrectlyFromAllBytes() {
        for (byte i = -126; i < 126; i += 2) {
            int test1 = LSBEncoder.extractFinalBitFromByte(i);
            int test2 = LSBEncoder.extractFinalBitFromByte((byte) (i + 1));

            assertEquals(test1, 0);
            assertEquals(test2, 1);
        }
    }

    @Test
    public void nthBitExtractedCorrectly() {

        int test1 = LSBEncoder.getNthBitFromByte((byte) 15, 3);
        assertEquals(1, test1);

        int test2 = LSBEncoder.getNthBitFromByte((byte) 31, 4);
        assertEquals(1, test2);

        int test3 = LSBEncoder.getNthBitFromByte((byte) 1, 0);
        assertEquals(1, test3);
 
        int test4 = LSBEncoder.getNthBitFromByte((byte) 16, 3);
        assertEquals(0, test4);
        for (int i = 1; i < 4; i++) {
            int test = LSBEncoder.getNthBitFromByte((byte) 16, i);
            assertEquals(0, test);
        }

        int test5 = LSBEncoder.getNthBitFromByte((byte) 16, 4);
        assertEquals(1, test5);

        int test6 = LSBEncoder.getNthBitFromByte((byte) 16, 5);
        assertEquals(0, test6);

    }

    @Test
    public void correctPowerOfTwo() {
        for (int i = 0; i < 7; i++) {
            assertEquals((int) Math.pow(2, i), LSBEncoder.powerOfTwo(i));
        }
    }

}
