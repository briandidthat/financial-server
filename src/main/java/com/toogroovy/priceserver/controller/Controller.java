package com.toogroovy.priceserver.controller;

import com.toogroovy.priceserver.domain.ApiResponse;
import com.toogroovy.priceserver.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class Controller {
    @Autowired
    private CryptoService service;

    @GetMapping("/symbol")
    public ApiResponse getCryptoPrice(@RequestParam String symbol) {
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/symbols/multiple")
    public List<ApiResponse> getMultipleCryptoPrices(@RequestBody List<String> symbols) {
        return service.getSpotPrices(symbols);
    }
}
