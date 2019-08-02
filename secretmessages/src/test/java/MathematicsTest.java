/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Steganography.LSBEncoder;
import Math.Mathematics;
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
public class MathematicsTest {

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
    public void correctPowerOfTwo() {
        for (int i = 0; i < 7; i++) {
            assertEquals((int) Math.pow(2, i), Mathematics.powerOfTwo(i));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void powerOfTwoOnlyAcceptsValuesNonnegativeArguments() {
        int a = Mathematics.powerOfTwo(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void powerOfTwoOnlyAcceptsValuesBelowEight() {
        int a = Mathematics.powerOfTwo(8);
    }
    
    @Test
    public void correctAbsValue() {
        assertEquals(6, Mathematics.abs(6));
        assertEquals(Mathematics.abs(-6), Mathematics.abs(6));
        assertEquals(0, Mathematics.abs(0));
    }
}
