package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;

import java.time.temporal.ChronoUnit;

public final class StatisticsUtilities {
    private StatisticsUtilities() {}

    public static Statistic buildStatistic(SpotPrice startPrice, SpotPrice endPrice) {
        final double oldPriceDouble = Double.parseDouble(startPrice.getAmount());
        final double currentPriceDouble = Double.parseDouble(endPrice.getAmount());

        final long timeDelta = ChronoUnit.DAYS.between(startPrice.getDate(), endPrice.getDate());
        final double priceChange = currentPriceDouble - oldPriceDouble;
        final double percentChange = ((currentPriceDouble - oldPriceDouble) / oldPriceDouble) * 100;

        final String priceChangeString = String.format("%.2f", priceChange);
        final String percentChangeString = String.format("%.2f", percentChange);
        final String timeDeltaString = String.valueOf(timeDelta);

        return new Statistic(startPrice.getBase(), priceChangeString, percentChangeString, timeDeltaString);
    }
}
