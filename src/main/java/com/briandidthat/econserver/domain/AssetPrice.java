package com.briandidthat.econserver.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

public class AssetPrice {
    private String symbol;
    private String price;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public AssetPrice() {
    }

    public AssetPrice(String symbol, String price, LocalDate date) {
        this.symbol = symbol;
        this.price = price;
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetPrice that = (AssetPrice) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(price, that.price) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, price, date);
    }
}
