package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilitiesTest {
    private final LocalDate START = LocalDate.of(2023, 2, 20);
    private final LocalDate END = LocalDate.of(2023, 2, 22);

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCalculatePercentChange() {
        double oldPriceDouble = Double.parseDouble("20");
        double currentPriceDouble = Double.parseDouble("18");
        double expected = Double.parseDouble("-10"); // (18 - 20 ) = -2 / 20 = -.10 * 100 = -10.0%
        double percentChange = StatisticsUtilities.calculatePercentChange(oldPriceDouble, currentPriceDouble);

        assertEquals(expected, percentChange);
    }

    @Test
    void testCalculatePriceChange() {
        double oldPrice = Double.parseDouble("200");
        double currentPrice = Double.parseDouble("245");
        double expected = Double.parseDouble("45");
        double priceChange = StatisticsUtilities.calculatePriceChange(oldPrice, currentPrice);

        assertEquals(expected, priceChange);
    }

    @Test
    void testCalculateTimeDelta() {
        long timeDelta = StatisticsUtilities.calculateTimeDelta(START, END);

        assertEquals(2, timeDelta);
    }

    @Test
    void testBuildStatistic() {
        SpotPrice historicalEth = new SpotPrice(Cryptocurrency.ETH, "USD", "4000");
        SpotPrice currentEth = new SpotPrice(Cryptocurrency.ETH, "USD", "3000");

        final Statistic expected = new Statistic(Cryptocurrency.ETH, "-1000.00", "-25.00", "2");

        Statistic statistic = StatisticsUtilities.buildStatistic(START, END, historicalEth, currentEth);
        assertEquals(expected, statistic);
    }
}