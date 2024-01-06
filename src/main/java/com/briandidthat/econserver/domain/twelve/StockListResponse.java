package com.briandidthat.econserver.domain.twelve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StockListResponse(@JsonProperty("data") List<StockDetails> stocks, @JsonProperty("status") String status) { }
