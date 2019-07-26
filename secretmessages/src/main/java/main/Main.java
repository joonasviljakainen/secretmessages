package main;

import java.util.Scanner;
import IO.WavReader;
import Math.Mathematics;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        
        System.out.println("Printing the first four bytes:");
        try {
            byte[] bytes = WavReader.readWavFile("../samples/a2002011001-e02.wav");
            for (int i = 0; i < 4; i++) {
                System.out.println((char) (bytes[i]));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}
