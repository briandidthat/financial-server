package com.toogroovy.priceserver.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Token (String code,
                     String name,
                     String color,
                     Integer sortIndex,
                     Integer exponent,
                     String type,
                     String addressRegex,
                     String assetId) {

    @Override
    public String toString() {
        return "Token{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sortIndex=" + sortIndex +
                ", exponent=" + exponent +
                ", type='" + type + '\'' +
                ", addressRegex='" + addressRegex + '\'' +
                '}';
    }
}
