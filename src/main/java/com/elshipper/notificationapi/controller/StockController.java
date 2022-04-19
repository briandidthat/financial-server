package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
import com.elshipper.notificationapi.service.StockService;
import com.elshipper.notificationapi.util.RequestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private StockService service;

    @GetMapping("/symbol")
    public AlphaVantageQuoteResponse getQuote(@RequestParam String symbol) {
        return service.getQuote(symbol);
    }

    @GetMapping("/symbol/multiple")
    public List<AlphaVantageQuoteResponse> getQuotes(@RequestBody List<String> symbols) {
        if (symbols.size() == 0 || !RequestUtilities.validateCryptocurrencies(symbols)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid symbol array");
        }
        return service.getMultipleQuotes(symbols);
    }

}
