package main;

import java.util.Scanner;
import IO.IOManager;
import Math.Mathematics;
import domain.WavFile;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            byte[] bytes = IOManager.readFileToBytes("../samples/a2002011001-e02.wav");
            /*for (int i = 0; i < 40; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print((char) (bytes[4 * i + j]));
                }
                System.out.println();
            }*/

            IOManager.writeBytesToFile(bytes, "test.wav");

            WavFile big = new WavFile("../samples/44kHz.wav");
            System.out.println("");
            WavFile medium = new WavFile("../samples/16kHz.wav");
            System.out.println("");
            WavFile small = new WavFile("../samples/8kHz.wav");
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}
