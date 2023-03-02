package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilitiesTest {
    private final LocalDate START = LocalDate.of(2021, 2, 20);
    private final LocalDate END = LocalDate.of(2023, 2, 20);

    @BeforeEach
    void setUp() {
    }

    @Test
    void testBuildStatistic() {
        SpotPrice historicalEth = new SpotPrice(Cryptocurrency.ETH, "USD", "4000", START);
        SpotPrice currentEth = new SpotPrice(Cryptocurrency.ETH, "USD", "3000", END);

        final Statistic expected = new Statistic(Cryptocurrency.ETH, "-1000.00", "-25.00", "24 months");

        Statistic statistic = StatisticsUtilities.buildStatistic(historicalEth, currentEth);
        assertEquals(expected, statistic);
    }
}