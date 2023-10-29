package com.briandidthat.priceserver.domain.coinbase;

public record Statistic(String symbol, String priceChange, String percentChange, String timeDelta) {
    @Override
    public String toString() {
        return "Statistic{" +
                "symbol='" + symbol + '\'' +
                ", priceChange='" + priceChange + '\'' +
                ", percentChange='" + percentChange + '\'' +
                ", timeDelta='" + timeDelta + '\'' +
                '}';
    }
}
