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


    private final String pair;

    Cryptocurrency(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

    @Override
    public String toString() {
        return "Cryptocurrency{" +
                "pair='" + pair + '\'' +
                '}';
    }
}
