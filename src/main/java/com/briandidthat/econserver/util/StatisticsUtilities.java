package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.coinbase.Statistic;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class StatisticsUtilities {
    private StatisticsUtilities() {
    }

    public static ChronoUnit getTimeFrame(LocalDate startDate, LocalDate endDate) {
        final long timeFrame = ChronoUnit.DAYS.between(startDate, endDate);

        if (timeFrame <= 7) return ChronoUnit.DAYS;
        if (timeFrame <= 30) return ChronoUnit.WEEKS;
        if (timeFrame <= 365) return ChronoUnit.MONTHS;

        return ChronoUnit.YEARS;
    }

    private static String formatTimeFrameString(LocalDate startDate, LocalDate endDate) {
        final ChronoUnit interval = getTimeFrame(startDate, endDate);

        return switch (interval) {
            case DAYS -> String.format("%d %s", ChronoUnit.DAYS.between(startDate, endDate), ChronoUnit.DAYS);
            case WEEKS -> String.format("%d %s", ChronoUnit.WEEKS.between(startDate, endDate), ChronoUnit.WEEKS);
            case MONTHS -> String.format("%d %s", ChronoUnit.MONTHS.between(startDate, endDate), ChronoUnit.MONTHS);
            default -> String.format("%d %s", ChronoUnit.YEARS.between(startDate, endDate), ChronoUnit.YEARS);
        };
    }

    public static Statistic buildStatistic(AssetPrice startPrice, AssetPrice endPrice) {
        final double startPriceDouble = Double.parseDouble(startPrice.getPrice());
        final double endPriceDouble = Double.parseDouble(endPrice.getPrice());

        final double priceChange = endPriceDouble - startPriceDouble;
        final double percentChange = ((endPriceDouble - startPriceDouble) / startPriceDouble) * 100;

        final String priceChangeString = String.format("%.2f", priceChange);
        final String percentChangeString = String.format("%.2f", percentChange);
        final String timeFrameString = formatTimeFrameString(startPrice.getDate(), endPrice.getDate());

        return new Statistic(startPrice.getSymbol(), startPrice.getPrice(), endPrice.getPrice(), priceChangeString,
                percentChangeString, startPrice.getDate(), endPrice.getDate(), timeFrameString);
    }
}
