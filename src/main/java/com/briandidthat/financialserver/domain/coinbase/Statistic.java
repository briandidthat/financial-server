package com.briandidthat.financialserver.domain.coinbase;

public record Statistic(String symbol, String previousPrice, String currentPrice, String priceChange,
                        String percentChange, String timeDelta) {
    @Override
    public String toString() {
        return "Statistic{" +
                "symbol='" + symbol + '\'' +
                ", previousPrice='" + previousPrice + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", priceChange='" + priceChange + '\'' +
                ", percentChange='" + percentChange + '\'' +
                ", timeDelta='" + timeDelta + '\'' +
                '}';
    }
}
