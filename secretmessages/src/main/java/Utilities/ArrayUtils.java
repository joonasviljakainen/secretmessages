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
}
