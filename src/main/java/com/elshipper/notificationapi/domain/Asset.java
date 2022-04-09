package com.elshipper.notificationapi.domain;

import java.util.List;

public enum Asset {
    BTC("BTCUSDT"),
    BNB("BNBUSDT"),
    ETH("ETHUSDT"),
    FTM("FTMUSDT"),
    SOL("SOLUSDT"),
    AVAX("AVAXUSDT");

    private final String pair;

    Asset(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

}
