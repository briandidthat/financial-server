package com.elshipper.notificationapi.domain.rest;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageQuoteResponse implements AssetResponse {
    @JsonProperty(value = "Global Quote")
    private Quote quote;

    public AlphaVantageQuoteResponse() {
    }

    public AlphaVantageQuoteResponse(Quote quote) {
        this.quote = quote;
    }

    @Override
    public String getSymbol() {
        return quote.symbol;
    }

    public String getOpenPrice() {
        return quote.open;
    }

    public String getHighestPrice() {
        return quote.high;
    }

    public String getLowestPrice() {
        return quote.low;
    }

    @Override
    public String getPrice() {
        return quote.price;
    }

    public String getPreviousClose() {
        return quote.previousClose;
    }

    public String getChangeUSD() {
        return quote.change;
    }

    public String getChangePercent() {
        return quote.change;
    }

    @Override
    public String toString() {
        return quote.toString();
    }

    public static class Quote {
        @JsonProperty("01. symbol")
        private String symbol;
        @JsonProperty("02. open")
        private String open;
        @JsonProperty("03. high")
        private String high;
        @JsonProperty("04. low")
        private String low;
        @JsonProperty("05. price")
        private String price;
        @JsonProperty("08. previous close")
        private String previousClose;
        @JsonProperty("09. change")
        private String change;
        @JsonProperty("10. change percent")
        private String percentChange;

        @Override
        public String toString() {
            return "Quote{" +
                    "symbol='" + symbol + '\'' +
                    ", open='" + open + '\'' +
                    ", high='" + high + '\'' +
                    ", low='" + low + '\'' +
                    ", price='" + price + '\'' +
                    ", previousClose='" + previousClose + '\'' +
                    ", change='" + change + '\'' +
                    ", percentChange='" + percentChange + '\'' +
                    '}';
        }
    }
}
