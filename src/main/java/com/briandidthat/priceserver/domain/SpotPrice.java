package com.briandidthat.priceserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SpotPrice implements Serializable {
    @NotNull
    private String base;
    private String currency;
    private String amount;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public SpotPrice() {}

    public SpotPrice(String base, String currency, String amount, LocalDate date) {
        this.base = base;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SpotPrice{" +
                "base='" + base + '\'' +
                ", currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPrice spotPrice = (SpotPrice) o;
        return Objects.equals(base, spotPrice.base) && Objects.equals(currency, spotPrice.currency) &&
                Objects.equals(amount, spotPrice.amount) && Objects.equals(date, spotPrice.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, currency, amount, date);
    }
}
