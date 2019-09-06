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
     * Slices a byte array i.e. returns a piece of it, 
     * 'including element at index 'first' but not at index 'last.'
     * @param data data to slice
     * @param first Index of first element to include
     * @param last Ending index. This item will not be included
     * @return The sliced array.
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

    /**
     * Combines two byte arrays.
     * @param array1 First array
     * @param array2 Second array
     * @return Combined array
     */
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
