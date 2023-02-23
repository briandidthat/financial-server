package com.toogroovy.priceserver.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilitiesTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void calculatePercentChange() {
        final double oldPriceDouble = Double.parseDouble("20.00");
        final double currentPriceDouble = Double.parseDouble("18.00");
        final double expected = Double.parseDouble("-10.0"); // (18 - 20 ) = -2 / 20 = -.10 * 100 = 10.0%
        final double percentChange = StatisticsUtilities.calculatePercentChange(oldPriceDouble, currentPriceDouble);

        System.out.println(percentChange);

        assertEquals(expected, percentChange);
    }

    @Test
    void calculatePriceChange() {
        final double oldPrice = Double.parseDouble("200.40");
        final double currentPrice = Double.parseDouble("245.90");
        final double expected = Double.parseDouble("45.50");
        final double priceChange = StatisticsUtilities.calculatePriceChange(oldPrice, currentPrice);

        assertEquals(expected,priceChange);
    }

    @Test
    void calculateTimeDelta() {
    }

    @Test
    void buildStatistic() {
    }
}