package com.elshipper.notificationapi.domain;

public enum Cryptocurrency {
    BTC("BTCUSDT"),
    BNB("BNBUSDT"),
    ETH("ETHUSDT"),
    FTM("FTMUSDT"),
    SOL("SOLUSDT"),
    NVIDIA("NVDA"),
    VISA("V"),
    MASTERCARD("MA"),
    AVAX("AVAXUSDT");

    private final String pair;

    Cryptocurrency(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

}
