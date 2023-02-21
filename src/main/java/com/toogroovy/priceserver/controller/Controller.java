package com.toogroovy.priceserver.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/spot")
public class Controller {
    @Autowired
    private CryptoService service;

    @GetMapping
    public SpotPrice getSpotPrice(@RequestParam String symbol) {
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestParam String symbol, @JsonFormat(pattern = "yyyy-MM-dd") Date date) {
        return service.getHistoricalSpotPrice(symbol, date);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestBody List<String> symbols) {
        return service.getSpotPrices(symbols);
    }
}
