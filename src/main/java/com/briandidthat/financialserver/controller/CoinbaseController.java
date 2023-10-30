package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.Request;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Statistic;
import com.briandidthat.financialserver.service.CoinbaseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequestMapping(value = "/spot")
public class CoinbaseController {
    private static final Logger logger = LoggerFactory.getLogger(CoinbaseController.class);

    @Autowired
    private CoinbaseService service;

    @PostMapping
    public SpotPrice getSpotPrice(@RequestHeader String caller, @RequestBody @Valid Request request) {
        logger.info("Spot request by {}", caller);
        return service.getSpotPrice(request.getSymbol());
    }

    @PostMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestHeader String caller, @RequestBody @Valid Request request) {
        logger.info("Historical spot request by {}", caller);
        return service.getHistoricalSpotPrice(request.getSymbol(), request.getStartDate());
    }

    @PostMapping("/statistics")
    public Statistic getPriceStatistics(@RequestHeader String caller, @RequestBody @Valid Request request) {
        logger.info("Spot statistics request by {}", caller);
        return service.getPriceStatistics(request.getSymbol(), request.getStartDate(), request.getEndDate());
    }

    @PostMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch spot request by {}", caller);
        return service.getSpotPrices(request);
    }

    @PostMapping("/historical/batch")
    public List<SpotPrice> getMultipleHistoricalSpotPrices(@RequestHeader String caller, @RequestBody @Valid BatchRequest request) {
        logger.info("Batch historical spot request by {}", caller);
        return service.getHistoricalSpotPrices(request);
    }

}
