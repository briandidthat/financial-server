package com.briandidthat.financialserver.domain.coinbase;

import java.time.LocalDate;

public record Statistic(String symbol, String startPrice, String endPrice, String priceChange,
                        String percentChange, LocalDate startDate, LocalDate endDate, String timeFrame) {
    @Override
    public String toString() {
        return "Statistic{" +
                "symbol='" + symbol + '\'' +
                ", startPrice='" + startPrice + '\'' +
                ", endPrice='" + endPrice + '\'' +
                ", priceChange='" + priceChange + '\'' +
                ", percentChange='" + percentChange + '\'' +
                ", startDate=" + startDate +
                ", endDate='" + endDate + '\'' +
                ", timeFrame='" + timeFrame + '\'' +
                '}';
    }
}
