package com.toogroovy.priceserver.domain;

public record Statistic(String token, String priceChange, String percentChange, String timeDelta) {
    @Override
    public String toString() {
        return "Statistic{" +
                "token='" + token + '\'' +
                ", priceChange='" + priceChange + '\'' +
                ", percentChange='" + percentChange + '\'' +
                ", timeDelta='" + timeDelta + '\'' +
                '}';
    }
}
