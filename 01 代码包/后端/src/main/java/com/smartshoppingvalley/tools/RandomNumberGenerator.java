package com.smartshoppingvalley.tools;

import java.util.Random;

public class RandomNumberGenerator {
    public static int generate() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generates a random number between 100000 and 999999
    }
}
