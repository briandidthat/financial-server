package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.BatchRequest;
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

import static com.briandidthat.priceserver.util.RequestUtilities.getCurrentRequest;

@Validated
@RestController
@RequestMapping("/spot")
public class Controller {
    private static final String CALLER = "caller";
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private CryptoService service;

    @GetMapping
    public SpotPrice getSpotPrice(@RequestParam String symbol) {
        final String caller = getCurrentRequest().getHeader(CALLER);
        LOGGER.info("Spot request by {}", caller);
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/batch")
    public List<SpotPrice> getMultipleSpotPrices(@RequestBody @Valid BatchRequest request) {
        final String caller = getCurrentRequest().getHeader(CALLER);
        LOGGER.info("Batch spot request by {}", caller);
        return service.getSpotPrices(request);
    }

    @GetMapping("/historical")
    public SpotPrice getHistoricalSpotPrice(@RequestParam String symbol, @RequestParam LocalDate date) {
        final String caller = getCurrentRequest().getHeader(CALLER);
        LOGGER.info("Historical spot request by {}", caller);
        return service.getHistoricalSpotPrice(symbol, date);
    }

    @GetMapping("/historical/batch")
    public List<SpotPrice> getMultipleHistoricalSpotPrices(@RequestBody @Valid BatchRequest request) {
        final String caller = getCurrentRequest().getHeader(CALLER);
        LOGGER.info("Batch historical spot request by {}", caller);
        return service.getHistoricalSpotPrices(request);
    }

    @GetMapping("/statistics")
    public Statistic getPriceStatistics(@RequestParam String symbol, @RequestParam LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {
        final String caller = getCurrentRequest().getHeader(CALLER);
        LOGGER.info("Batch historical spot request by {}", caller);

        final LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return service.getPriceStatistics(symbol, startDate, end);
    }
}
