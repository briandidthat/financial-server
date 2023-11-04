package com.briandidthat.financialserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class TwelveResponse {

    public String symbol;
    @JsonProperty("price")
    public String price;

    public TwelveResponse() {}

    public TwelveResponse(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }
}
