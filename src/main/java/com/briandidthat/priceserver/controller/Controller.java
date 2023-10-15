package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.BatchRequest;
import com.briandidthat.priceserver.domain.Request;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Statistic;
import com.briandidthat.priceserver.service.CryptoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Validated
@RestController
@RequestMapping("/spot")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private CryptoService service;

    @GetMapping
    public SpotPrice getSpotPrice(@RequestHeader String caller, @RequestParam String symbol) {
        logger.info("Spot request by {}", caller);
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch spot request by {}", caller);
        return service.getSpotPrices(request);
    }

    @GetMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestHeader String caller, @RequestBody Request request) {
        logger.info("Historical spot request by {}", caller);
        return service.getHistoricalSpotPrice(request.getSymbol(), request.getDate());
    }

    @GetMapping("/historical/batch")
    public List<SpotPrice> getMultipleHistoricalSpotPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch historical spot request by {}", caller);
        return service.getHistoricalSpotPrices(request);
    }

    @GetMapping("/statistics")
    public Statistic getPriceStatistics(@RequestHeader String caller, @RequestParam String symbol, @RequestParam LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        logger.info("Spot statistics request by {}", caller);
        final LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return service.getPriceStatistics(symbol, startDate, end);
    }
}
