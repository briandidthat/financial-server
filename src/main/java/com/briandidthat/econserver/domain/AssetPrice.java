package com.briandidthat.econserver.domain;

import java.util.Objects;

public abstract class AssetPrice {
    private String symbol;
    private String price;

    public AssetPrice() {
    }

    public AssetPrice(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetPrice that = (AssetPrice) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, price);
    }
}
