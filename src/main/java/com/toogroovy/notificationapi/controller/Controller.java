package com.toogroovy.notificationapi.controller;

import com.toogroovy.notificationapi.domain.ApiResponse;
import com.toogroovy.notificationapi.service.CryptoService;
import com.toogroovy.notificationapi.util.RequestUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class Controller {
    @Autowired
    private CryptoService service;

    @GetMapping("/symbol")
    public ApiResponse getCryptoPrice(@RequestParam String symbol) {
        if (!RequestUtilities.validateCryptocurrency(symbol)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid symbol");
        }
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/symbols/multiple")
    public List<ApiResponse> getMultipleCryptoPrices(@RequestBody List<String> symbols) {
        if (symbols.size() == 0 || !RequestUtilities.validateCryptocurrencies(symbols)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid symbol");
        }
        return service.getSpotPrices(symbols);
    }
}
