package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.twelve.StockPriceResponse;
import com.briandidthat.financialserver.service.TwelveService;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/stocks")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger("StockManager");

    @Autowired
    private TwelveService service;

    @GetMapping
    public StockPriceResponse getStockPrice(@RequestHeader String apiKey, @RequestHeader(required = false) String caller,
                                            @RequestParam String symbol) {
        logger.info("Stock price request by {}", caller);
        return service.getStockPrice(apiKey, symbol);
    }

    @GetMapping("/batch")
    public List<StockPriceResponse> getBatchStockPrice(@RequestHeader String apiKey, @RequestHeader(required = false) String caller,
                                                       @RequestParam @Size(min= 1, max = 5) List<String> symbols) {
        logger.info("Batch stock price request by {}", caller);
        return service.getMultipleStockPrices(apiKey, symbols);
    }

}
