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
    public StockPriceResponse getStockPrice(@RequestParam String symbol, @RequestHeader(required = false) String caller) {
        logger.info("Stock price request by {}", caller);
        return service.getStockPrice(symbol);
    }

    @GetMapping("/batch")
    public List<StockPriceResponse> getBatchStockPrice(@RequestParam @Size(min= 1, max = 5) List<String> symbols, @RequestHeader(required = false) String caller) {
        logger.info("Batch stock price by {}", caller);
        return service.getMultipleStockPrices(symbols);
    }

}
