package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchRequest;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.service.TwelveService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/stocks")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger("StockController");

    @Autowired
    private TwelveService service;

    @GetMapping
    public AssetPrice getStockPrice(@RequestHeader String apiKey, @RequestParam String symbol) {
        return service.getAssetPrice(apiKey, symbol);
    }

    @GetMapping("/historical")
    public AssetPrice getHistoricalStockPrice(@RequestHeader String apiKey, @RequestParam String symbol, @RequestParam LocalDate date) {
        return service.getHistoricalAssetPrice(apiKey, symbol, date);
    }

    @GetMapping("/batch")
    public BatchResponse getMultipleStockPrices(@RequestHeader String apiKey, @RequestParam @Size(min = 1, max = 5) List<String> symbols) {
        return service.getMultipleAssetPrices(apiKey, symbols);
    }

    @GetMapping("/batch/historical")
    public BatchResponse getMultipleHistoricalSpotPrices(@RequestHeader String apiKey, @RequestBody @Valid BatchRequest request) {
        return service.getMultipleHistoricalAssetPrices(apiKey, request);
    }

    @GetMapping("/statistics")
    public Statistic getStockPriceStatistics(@RequestHeader String apiKey, @RequestParam String symbol, @RequestParam LocalDate startDate) {
        return service.getAssetPriceStatistics(apiKey, symbol, startDate);
    }
}
