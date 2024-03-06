package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.coinbase.BatchRequest;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.service.CoinbaseService;
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
@RequestMapping(value = "/crypto/spot")
public class CryptoController {
    private static final Logger logger = LoggerFactory.getLogger("CryptoController");

    @Autowired
    private CoinbaseService service;

    @GetMapping
    public AssetPrice getCryptoPrice(@RequestHeader String caller, @RequestParam String symbol) {
        logger.info("Spot request by {}", caller);
        return service.getAssetPrice(symbol);
    }

    @GetMapping("/historical")
    public AssetPrice getHistoricalCryptoPrice(@RequestHeader String caller, @RequestParam String symbol, @RequestParam LocalDate date) {
        logger.info("Historical spot request by {}", caller);
        return service.getHistoricalAssetPrice(symbol, date);
    }

    @GetMapping("/statistics")
    public Statistic getCryptoPriceStatistics(@RequestHeader String caller,
                                        @RequestParam String symbol,
                                        @RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) {
        logger.info("Spot statistics request by {}", caller);
        return service.getAssetPriceStatistics(symbol, startDate, endDate);
    }

    @GetMapping("/batch")
    public BatchResponse getCryptoPrices(@RequestHeader String caller, @RequestParam @Size(min = 2, max = 5) List<String> symbols) {
        logger.info("Batch spot request by {}", caller);
        return service.getAssetPrices(symbols);
    }

    @PostMapping("/batch/historical")
    public BatchResponse getMultipleHistoricalCryptoPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch historical spot request by {}", caller);
        return service.getHistoricalAssetPrices(request);
    }

}
