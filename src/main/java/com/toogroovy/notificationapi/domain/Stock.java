package com.toogroovy.notificationapi.domain;

public enum Stock {
    IBM("IBM"),
    VOO("VOO"),
    VISA("V"),
    APPLE("AAPL"),
    GOOGLE("GOOG"),
    MICROSOFT("MSFT"),
    TESLA("TSLA"),
    NETFLIX("NFLX"),
    AMAZON("AMZN"),
    NVIDIA("NVDA"),
    MASTERCARD("MA");

    private final String symbol;
    private final String type = "STOCK";

    Stock(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
