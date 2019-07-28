/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author joonas
 */
public class IOManager {

    /**
     * 
     * @param fileLocation
     * @return
     * @throws IOException 
     */
    public static byte[] readFileToBytes(String fileLocation) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(fileLocation));
        return data;
    }
    /**
     * 
     * @param data
     * @param fileName
     * @throws IOException 
     */
    public static void writeBytesToFile(byte[] data, String fileName) throws IOException {
        Files.write(Paths.get(fileName), data);
    }
}
