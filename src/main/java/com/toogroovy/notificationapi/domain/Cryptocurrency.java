package com.toogroovy.notificationapi.domain;

public final class Cryptocurrency {
    public static final String BTC = "BTCUSD";
    public static final String BNB = "BNBUSD";
    public static final String DOT ="DOTUSD";
    public static final String ETH = "ETHUSD";
    public static final String FTM = "FTMUSD";
    public static final String SOL = "SOLUSD";
    public static final String ATOM = "ATOMUSD";
    public static final String AVAX = "AVAXUSD";

    private final String symbol;

    Cryptocurrency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Cryptocurrency{" +
                "pair='" + symbol + '\'' +
                '}';
    }
}
