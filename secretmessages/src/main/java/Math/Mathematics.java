/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

/**
 * Contains various mathematical functions.
 *
 * @author joonas
 */
public class Mathematics {

    /**
     * Returns the absolute value of the provided integer.
     *
     * @param number
     * @return Absolute value of the parameter.
     */
    public static int abs(int number) {
        if (number >= 0) {
            return number;
        }
        return -number;
    }
    
    /**
     * Returns the absolute value of a double.
     * @param number
     * @return Absolute value of the parameter.
     */
    public static double abs(double number) {
        if (number >= 0) {
            return number;
        }
        return -number;
    }

    /**
     * Returns the nth power of two in byte range (i.e. 1-8). For indexing, 0 is
     * the the first power of two, i.e. in the rightmost bit position.
     *
     * @param n The power to which two should be elevated.
     * @return The power of 2
     * @throws IllegalArgumentException
     */
    public static byte powerOfTwo(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("can' calcluate negative powers");
        }
        if (n > 7) {
            throw new IllegalArgumentException("Bytes only allow up to 7 powers of two");
        }
        if (n == 0) {
            return 1;
        }
        int accum = 1;
        for (int i = 0; i < n; i++) {
            accum *= 2;
        }
        return (byte) accum;
    }

    public static double pow(double base, int exponent) {
        double res = 1;
        for (int i = 0; i < exponent; i++) {
            res *= base;
        }
        return res;
    }

}
