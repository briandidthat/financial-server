package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.twelve.StockPriceResponse;
import com.briandidthat.financialserver.service.TwelveService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private TwelveService service;

    @GetMapping
    public StockPriceResponse getStockPrice(@RequestParam String symbol) {
        return service.getStockPrice(symbol);
    }

    @GetMapping("/batch")
    public List<StockPriceResponse> getBatchStockPrice(@RequestParam @Size(min= 1, max = 5) List<String> symbols) {
        return service.getMultipleStockPrices(symbols);
    }

}
