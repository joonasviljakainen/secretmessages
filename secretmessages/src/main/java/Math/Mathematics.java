/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

/**
 *
 * @author joonas
 */
public class Mathematics {

    public static int abs(int number) {
        if (number >= 0) {
            return number;
        }
        return -number;
    }

    public static byte powerOfTwo(int n) {
        if (n < 0) {
            throw new Error("can' calcluate negative powers");
        }
        if (n > 7) {
            throw new Error("Bytes only allow up to 7 powers of two");
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

}
