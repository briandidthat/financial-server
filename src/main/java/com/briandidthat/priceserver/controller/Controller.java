package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.BatchRequest;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Statistic;
import com.briandidthat.priceserver.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Validated
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
    public SpotPrice getHistoricalSpotPrice(@RequestParam String symbol, @RequestParam LocalDate date) {
        return service.getHistoricalSpotPrice(symbol, date);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestBody @Valid BatchRequest request) {
        return service.getSpotPrices(request.symbols());
    }

    @GetMapping("/statistics")
    public Statistic getPriceStatistics(@RequestParam String symbol, @RequestParam LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        final LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return service.getPriceStatistics(symbol, startDate, end);
    }

}
