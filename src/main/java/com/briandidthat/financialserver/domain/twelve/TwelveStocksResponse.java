package com.briandidthat.financialserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TwelveStocksResponse(@JsonProperty("data") List<StockDetails> stocks, @JsonProperty("status") String status) { }
