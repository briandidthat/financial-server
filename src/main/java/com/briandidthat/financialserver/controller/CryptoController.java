package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Statistic;
import com.briandidthat.financialserver.service.CoinbaseService;
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
    public SpotPrice getSpotPrice(@RequestHeader String caller, @RequestParam String symbol) {
        logger.info("Spot request by {}", caller);
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestHeader String caller, @RequestParam String symbol, @RequestParam LocalDate date) {
        logger.info("Historical spot request by {}", caller);
        return service.getHistoricalSpotPrice(symbol, date);
    }

    @GetMapping("/statistics")
    public Statistic getPriceStatistics(@RequestHeader String caller,
                                        @RequestParam String symbol,
                                        @RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) {
        logger.info("Spot statistics request by {}", caller);
        return service.getPriceStatistics(symbol, startDate, endDate);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestHeader String caller, @RequestParam @Size(min = 2, max = 5) List<String> symbols) {
        logger.info("Batch spot request by {}", caller);
        return service.getSpotPrices(symbols);
    }

    @PostMapping("/batch/historical")
    public List<SpotPrice> getMultipleHistoricalSpotPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch historical spot request by {}", caller);
        return service.getHistoricalSpotPrices(request);
    }

}
