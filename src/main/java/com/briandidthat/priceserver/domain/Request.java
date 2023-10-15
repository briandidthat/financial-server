package com.briandidthat.priceserver.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public final class Request implements Serializable {
    @NotNull
    private final String symbol;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public Request(String symbol) {
        this.symbol = symbol.toUpperCase();
        this.date = LocalDate.now();
    }

    public Request(String symbol, LocalDate date) {
        this(symbol);
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(symbol, request.symbol) && Objects.equals(date, request.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, date);
    }

    @Override
    public String toString() {
        return "Request{" + "symbol='" + symbol + '\'' + ", date=" + date + '}';
    }
}
