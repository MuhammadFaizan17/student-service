package com.rak.student.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilityTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateRandom4DigitNumber() {
        int randomNumber = Utility.generateRandom4DigitNumber();
        assertTrue(randomNumber >= 1000 && randomNumber <= 9999);
    }

    @Test
    public void testGenerateRandom4DigitNumberNegative() {
        int randomNumber = Utility.generateRandom4DigitNumber();
        assertFalse(randomNumber < 1000 || randomNumber > 9999);
    }
}
