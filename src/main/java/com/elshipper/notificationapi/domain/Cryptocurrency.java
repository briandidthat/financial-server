package com.elshipper.notificationapi.domain;

public enum Cryptocurrency {
    BTC("BTCUSDT"),
    BNB("BNBUSDT"),
    DOT("DOTUSDT"),
    ETH("ETHUSDT"),
    FTM("FTMUSDT"),
    SOL("SOLUSDT"),
    ATOM("ATOMUSDT"),
    AVAX("AVAXUSDT");

    private final String symbol;
    private final String type = "CRYPTO";

    Cryptocurrency(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Cryptocurrency{" +
                "pair='" + symbol + '\'' +
                '}';
    }

    public String getSymbol() {
        return this.symbol;
    }
    public String getType() {
        return this.type;
    }
}
