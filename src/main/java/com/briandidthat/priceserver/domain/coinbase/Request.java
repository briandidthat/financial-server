package com.briandidthat.priceserver.domain.coinbase;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Request implements Serializable {
    @NotNull
    private final String symbol;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate endDate;

    public Request() {
        this.symbol = "BTC";
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    public Request(String symbol) {
        this.symbol = symbol.toUpperCase();
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    public Request(String symbol, LocalDate startDate) {
        this.symbol = symbol.toUpperCase();
        this.startDate = startDate;
        this.endDate = LocalDate.now();
    }

    public Request(String symbol, LocalDate startDate, LocalDate endDate) {
        this.symbol = symbol.toUpperCase();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(symbol, request.symbol) && Objects.equals(startDate, request.startDate)
                && Objects.equals(endDate, request.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, startDate, endDate);
    }

    @Override
    public String toString() {
        return "Request{" + "symbol=" + symbol + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
