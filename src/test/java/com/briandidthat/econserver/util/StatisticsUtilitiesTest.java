package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.domain.coinbase.SpotPriceResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilitiesTest {

    @Test
    void testBuildStatistic() {
        AssetPrice historicalEth = new AssetPrice(TestingConstants.ETH,  "4000", TestingConstants.START_DATE);
        AssetPrice currentEth = new AssetPrice(TestingConstants.ETH,  "3000", TestingConstants.END_DATE);

        final Statistic expected = new Statistic(TestingConstants.ETH, historicalEth.getPrice(), currentEth.getPrice(),
                "-1000.00", "-25.00", historicalEth.getDate(), currentEth.getDate(), "24 months");

        Statistic statistic = StatisticsUtilities.buildStatistic(historicalEth, currentEth);
        assertEquals(expected, statistic);
    }
}