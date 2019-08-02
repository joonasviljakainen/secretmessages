
import Steganography.LSBEncoder;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static Utilities.BitManipulation.getNthBitFromByte;
import static Utilities.BitManipulation.extractFinalBitFromByte;
import static Utilities.BitManipulation.interleaveBitToByte;

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
}
