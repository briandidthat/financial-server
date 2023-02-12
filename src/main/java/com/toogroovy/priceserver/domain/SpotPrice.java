package com.toogroovy.priceserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotPrice(String base, String currency, String amount) implements Serializable {
}
