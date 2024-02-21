package com.briandidthat.econserver.domain.twelve;

import com.briandidthat.econserver.domain.AssetPrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

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
