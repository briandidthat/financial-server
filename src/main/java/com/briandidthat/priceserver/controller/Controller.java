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

import java.util.List;


@Validated
@RestController
@RequestMapping("/spot")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private CryptoService service;

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
