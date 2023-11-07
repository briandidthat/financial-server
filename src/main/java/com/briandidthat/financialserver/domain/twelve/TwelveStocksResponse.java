package com.briandidthat.financialserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TwelveStocksResponse(@JsonProperty("data") List<StockDetails> stocks, @JsonProperty("status") String status) { }
