/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author joonas
 */
public class ArrayUtils {

    /**
     *
     * @param data
     * @param first
     * @param last
     * @return
     */
    public static byte[] slice(byte[] data, int first, int last) {
        if (last <= first) {
            return null;
        }
        byte[] ret = new byte[last - first];
        for (int i = first, a = 0; i < last; i++, a++) {
            ret[a] = data[i];
        }
        return ret;
    }

    public static byte[] append(byte[] array1, byte[] array2) {
        byte[] newArray = new byte[array1.length + array2.length];

        for (int i = 0; i < array1.length; i++) {
            newArray[i] = array1[i];
        }
        for (int i = 0, a = array1.length; i < array2.length; i++, a++) {
            newArray[a] = array2[i];
        }
        return newArray;
    }
}
