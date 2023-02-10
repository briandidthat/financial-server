package com.toogroovy.priceserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Token(@JsonProperty("code") String code, @JsonProperty("name") String name,
                    @JsonProperty("color") String color, @JsonProperty("sort_index") Integer sortIndex,
                    @JsonProperty("exponent") Integer exponent, @JsonProperty("type") String type,
                    @JsonProperty("address_regex") String addressRegex,
                    @JsonProperty("asset_id") String assetId) implements Serializable {
}
