
import Steganography.LSBEncoder;

import static Utilities.BitManipulation.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author joonas
 */
public class BitUtilitiesTest {

    @Test
    public void singleBitsInterleavedCorrectly() {
        byte test1 = 120;
        byte test2 = 121;

        String test1res1 = Integer.toBinaryString(interleaveBitToByte(test1, 0));
        String test1res2 = Integer.toBinaryString(interleaveBitToByte(test1, 1));
        String test2res1 = Integer.toBinaryString(interleaveBitToByte(test2, 0));
        String test2res2 = Integer.toBinaryString(interleaveBitToByte(test2, 1));

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

            String test1res1 = Integer.toBinaryString((byte) interleaveBitToByte(test1, 0));
            String test1res2 = Integer.toBinaryString((byte) interleaveBitToByte(test1, 1));
            String test2res1 = Integer.toBinaryString((byte) interleaveBitToByte(test2, 0));
            String test2res2 = Integer.toBinaryString((byte) interleaveBitToByte(test2, 1));

            assertEquals(test1res1, test2res1);
            assertEquals(test1res2, test2res2);
        }
    }

    @Test
    public void LastByteExtractedCorrectlyFromAllBytes() {
        for (byte i = -126; i < 126; i += 2) {
            int test1 = extractFinalBitFromByte(i);
            int test2 = extractFinalBitFromByte((byte) (i + 1));

            assertEquals(test1, 0);
            assertEquals(test2, 1);
        }
    }

    @Test
    public void nthBitExtractedCorrectly() {

        int test1 = getNthBitFromByte((byte) 15, 3);
        assertEquals(1, test1);

        int test2 = getNthBitFromByte((byte) 31, 4);
        assertEquals(1, test2);

        int test3 = getNthBitFromByte((byte) 1, 0);
        assertEquals(1, test3);

        int test4 = getNthBitFromByte((byte) 16, 3);
        assertEquals(0, test4);
        for (int i = 1; i < 4; i++) {
            int test = getNthBitFromByte((byte) 16, i);
            assertEquals(0, test);
        }

        int test5 = getNthBitFromByte((byte) 16, 4);
        assertEquals(1, test5);

        int test6 = getNthBitFromByte((byte) 16, 5);
        assertEquals(0, test6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nthBitExtractionRequiresCorrectBoundaries() {
        int b1 = getNthBitFromByte((byte)8, 0);
        int b2 = getNthBitFromByte((byte) 8, 8);
    }

    @Test
    public void bytesToShortConversionMakesSense() {
        byte small1 = 8;
        byte big1 = 8; // --> 2048 when shifted
        short s1 = littleEndianBytesToShort(small1, big1);
        assertEquals(s1, 2056);
        assertEquals(Integer.toBinaryString(s1), "100000001000");
    }

    @Test
    public void secondSimpleTestWithShortMaker() {
        byte small2 = 63;
        byte big2 = 50; // --> 12800 when shifted
        short s2 = littleEndianBytesToShort(small2, big2);
        assertEquals(s2, 12863);
        assertEquals(Integer.toBinaryString(s2), "11001000111111");
    }

    @Test
    public void thingsWithManyOnesTranslateToShorts() {
        byte small3 = -127; // --> 129 in the unsigned world
        byte big3 = 50; // --> 12800 when shifted
        short s3 = littleEndianBytesToShort(small3, big3);
        /*System.out.println(small3);
        System.out.println(Integer.toBinaryString(small3));
        System.out.println(big3);
        System.out.println(Integer.toBinaryString(big3));
        System.out.println(s3);
        System.out.println(Integer.toBinaryString(s3));
        System.out.println((float)(s3));*/
        assertEquals(s3, 12929);
        assertEquals(Integer.toBinaryString(s3), "11001010000001");
    }

    @Test
    public void bytesToShortCompareEquallyToByteBuffer() {
        //byte[] data = [8, 8, 4, 4];
        
        // TODO compare my solution for little-endian shorts with ByteBuffer
    }
    
    @Test
    public void shortToLittleEndianWorks() {
        short s = 255; // -->  0000000011111111
        byte[] test1 = shortToLittleEndianBytes(s);
        assertEquals(test1[0], -1);
        assertEquals(test1[1], 0);
    }
    
    @Test
    public void anotherSmallTestForShortToLittleEndian() {
        short s = 12929; // --> 0011001010000001
        byte[] test2 = shortToLittleEndianBytes(s);
        assertEquals(test2[0], -127);
        assertEquals(test2[1], 50 );
    }

    @Test
    public void arrayOfBytesTurnedToShorts() {
        byte[] test = {-127, 50, -127, 50};
        short[] s = littleEndianByteArrayToShorts(test);
        for (int i = 0; i < s.length; i++) {
            assertEquals(s[i], 12929);
        }
    }

    @Test
    public void shortArrayTurnedToLittleEndianBytes() {
        short[] s = {12929, 12929};
        byte[] test = shortArrayToLittleEndianBytes(s);

        for (int i = 0; i < test.length; i += 2) {
            assertEquals(test[i], -127);
            assertEquals(test[i + 1], 50);
        }
    }
    
}
