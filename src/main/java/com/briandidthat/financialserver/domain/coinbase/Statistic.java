package com.briandidthat.financialserver.domain.coinbase;

import java.time.LocalDate;

public record Statistic(String symbol, String previousPrice, String currentPrice, String priceChange,
                        String percentChange, LocalDate startDate, LocalDate endDate, String timeDelta) {
    @Override
    public String toString() {
        return "Statistic{" +
                "symbol='" + symbol + '\'' +
                ", previousPrice='" + previousPrice + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", priceChange='" + priceChange + '\'' +
                ", percentChange='" + percentChange + '\'' +
                ", startDate=" + startDate +
                ", endDate='" + endDate + '\'' +
                ", timeDelta='" + timeDelta + '\'' +
                '}';
    }
}
