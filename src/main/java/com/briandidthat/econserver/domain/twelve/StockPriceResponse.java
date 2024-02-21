package com.briandidthat.econserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class StockPriceResponse {
    private String price;

    public StockPriceResponse() {
    }

    public StockPriceResponse(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
