package com.toogroovy.priceserver.controller;

import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Statistic;
import com.toogroovy.priceserver.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/spot")
public class Controller {
    @Autowired
    private CryptoService service;

    @GetMapping
    public SpotPrice getSpotPrice(@RequestParam @Size(min = 3) String symbol) {
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestParam @Size(min = 3) String symbol, @RequestParam LocalDate date) {
        return service.getHistoricalSpotPrice(symbol, date);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestBody List<String> symbols) {
        return service.getSpotPrices(symbols);
    }

    @GetMapping("/statistics")
    public Statistic getPriceStatistics(@RequestParam @Size(min = 3) String symbol, @RequestParam LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        final LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return service.getPriceStatistics(symbol, startDate, end);
    }
}
