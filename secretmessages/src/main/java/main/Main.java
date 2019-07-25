package main;

import java.util.Scanner;
import Math.Mathematics;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Give us a small-ish number.");
        try {
            int i = scanner.nextInt();
            System.out.println("Great job!");
            System.out.println("Here's the absolute value: " + Mathematics.abs(i));
            System.out.println("Bye now.");
        } catch (Exception e) {
            System.out.println("How hard can a number be?");
        }

        return;
    }
}
