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
    
    @Test
    public void powerOfTwoOnlyAcceptsvaluesbetweenZeroAndEight() {
        // TODO
    }
    
    @Test
    public void correctAbsValue() {
        
    }
}
