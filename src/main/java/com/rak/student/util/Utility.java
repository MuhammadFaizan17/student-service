package com.rak.student.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class Utility {
    public static int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
