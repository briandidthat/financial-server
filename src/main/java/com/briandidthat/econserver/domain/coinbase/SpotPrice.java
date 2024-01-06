package com.briandidthat.econserver.domain.coinbase;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SpotPrice implements Serializable {
    @NotNull
    @JsonProperty("symbol")
    private String symbol;
    private String currency;
    private String amount;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public SpotPrice() {}

    public SpotPrice(String symbol, String currency, String amount, LocalDate date) {
        this.symbol = symbol;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    @JsonProperty("base")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SpotPrice{" + "symbol='" + symbol + '\'' + ", currency='" + currency + '\'' + ", amount='" + amount + '\'' + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPrice spotPrice = (SpotPrice) o;
        return Objects.equals(symbol, spotPrice.symbol) && Objects.equals(currency, spotPrice.currency)
                && Objects.equals(amount, spotPrice.amount) && Objects.equals(date, spotPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, currency, amount, date);
    }
}
