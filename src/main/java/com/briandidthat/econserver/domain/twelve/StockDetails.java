package com.briandidthat.econserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StockDetails(@JsonProperty("symbol") String symbol, @JsonProperty("name") String name,
                           @JsonProperty("currency") String currency, @JsonProperty("exchange") String exchange,
                           @JsonProperty("mic_code") String micCode, @JsonProperty("country") String country,
                           @JsonProperty("type") String type) {
}
