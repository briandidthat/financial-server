package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchRequest;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.service.CoinbaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Validated
@RestController
@RequestMapping(value = "/crypto/spot")
public class CryptoController {
    @Autowired
    private CoinbaseService service;

    @GetMapping
    public AssetPrice getCryptoPrice(@RequestParam String symbol) {
        return service.getAssetPrice(symbol);
    }

    @GetMapping("/historical")
    public AssetPrice getHistoricalCryptoPrice(@RequestParam String symbol, @RequestParam LocalDate date) {
        return service.getHistoricalAssetPrice(symbol, date);
    }

    @GetMapping("/statistics")
    public Statistic getCryptoPriceStatistics(@RequestParam String symbol, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return service.getAssetPriceStatistics(symbol, startDate, endDate);
    }

    @GetMapping("/batch")
    public BatchResponse getCryptoPrices(@RequestParam @Size(min = 2, max = 5) List<String> symbols) {
        return service.getAssetPrices(symbols);
    }

    @PostMapping("/batch/historical")
    public BatchResponse getMultipleHistoricalCryptoPrices(@RequestBody @Valid BatchRequest request) {
        return service.getHistoricalAssetPrices(request);
    }
}
