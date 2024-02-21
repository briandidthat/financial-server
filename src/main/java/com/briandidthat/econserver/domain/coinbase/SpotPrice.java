package com.briandidthat.econserver.domain.coinbase;

import com.briandidthat.econserver.domain.AssetPrice;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SpotPrice extends AssetPrice {
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    public SpotPrice() {}

    public SpotPrice(String symbol, String price, LocalDate date) {
        super(symbol, price);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @JsonSetter("base")
    public void setBase(String base) {
        this.setSymbol(base);
    }

    @JsonSetter("amount")
    public void setAmount(String amount) {
        this.setPrice(amount);
    }
}
