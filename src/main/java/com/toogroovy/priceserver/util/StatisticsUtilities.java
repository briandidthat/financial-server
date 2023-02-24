package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class StatisticsUtilities {
    private StatisticsUtilities() {}

    public static double calculatePercentChange(double oldValue, double currentValue) {
        return ((currentValue - oldValue) / oldValue) * 100;
    }

    public static double calculatePriceChange(double oldValue, double currentValue) {
        return currentValue - oldValue;
    }

    public static long calculateTimeDelta(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static Statistic buildStatistic(LocalDate start, LocalDate end, SpotPrice oldPrice, SpotPrice currentPrice) {
        final double oldPriceDouble = Double.parseDouble(oldPrice.amount());
        final double currentPriceDouble = Double.parseDouble(currentPrice.amount());

        final long timeDelta = calculateTimeDelta(start, end);
        final double priceChange = calculatePriceChange(oldPriceDouble, currentPriceDouble);
        final double percentChange = calculatePercentChange(oldPriceDouble, currentPriceDouble);

        final String priceChangeString = String.format("%.2f", priceChange);
        final String percentChangeString = String.format("%.2f", percentChange);
        final String timeDeltaString = String.valueOf(timeDelta);

        return new Statistic(oldPrice.base(), priceChangeString, percentChangeString, timeDeltaString);
    }
}
