package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.domain.coinbase.SpotPrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class StatisticsUtilities {
    private StatisticsUtilities() {}

    private static String formatDateString(LocalDate startDate, LocalDate endDate) {
        final long timeFrame = ChronoUnit.DAYS.between(startDate, endDate);
        if (timeFrame < 365) {
            return timeFrame + " days";
        } else {
            return ChronoUnit.MONTHS.between(startDate, endDate) + " months";
        }
    }

    public static Statistic buildStatistic(SpotPrice startPrice, SpotPrice endPrice) {
        final double startPriceDouble = Double.parseDouble(startPrice.getPrice());
        final double endPriceDouble = Double.parseDouble(endPrice.getPrice());

        final double priceChange = endPriceDouble - startPriceDouble;
        final double percentChange = ((endPriceDouble - startPriceDouble) / startPriceDouble) * 100;

        final String priceChangeString = String.format("%.2f", priceChange);
        final String percentChangeString = String.format("%.2f", percentChange);
        final String timeFrameString = formatDateString(startPrice.getDate(), endPrice.getDate());

        return new Statistic(startPrice.getSymbol(), startPrice.getPrice(), endPrice.getPrice(),
                priceChangeString, percentChangeString, startPrice.getDate(), endPrice.getDate(), timeFrameString);
    }
}
