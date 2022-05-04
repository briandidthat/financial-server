package com.elshipper.notificationapi.domain.rest;

public class BinanceTickerResponse implements AssetResponse {
    private String symbol;
    private String price;

    public BinanceTickerResponse() {

    }

    public BinanceTickerResponse(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BinanceTickerResponse{" + "symbol='" + symbol + '\'' + ", price='" + price + '\'' + '}';
    }
}
