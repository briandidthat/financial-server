package com.elshipper.notificationapi.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageQuoteResponse {
    @JsonProperty(value = "Global Quote")
    private Quote quote;

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
    }

    public AlphaVantageQuoteResponse() {}

    public AlphaVantageQuoteResponse(Quote quote) {
        this.quote = quote;
    }

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

    public String getCurrentPrice() {
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
        return "AlphaVantageQuoteResponse{" +
                "symbol=" + quote.symbol + ", " +
                "open=" + quote.open + ", " +
                "high=" + quote.high + ", " +
                "low=" + quote.low + ", " +
                "price=" + quote.price + ", " +
                "previousClose=" + quote.previousClose + ", " +
                "change=" + quote.change + ", " +
                "percentChange=" + quote.percentChange +
                '}';
    }
}
