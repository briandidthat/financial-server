package com.briandidthat.econserver.domain.twelve;

import com.briandidthat.econserver.domain.AssetPrice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StockPriceResponse extends AssetPrice {
    public StockPriceResponse() {
    }

    public StockPriceResponse(String symbol, String price) {
        super(symbol, price);
    }

}
