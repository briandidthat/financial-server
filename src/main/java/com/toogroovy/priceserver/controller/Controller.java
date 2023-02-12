package com.toogroovy.priceserver.controller;

import com.toogroovy.priceserver.domain.SpotPrice;
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
    public SpotPrice getCryptoPrice(@RequestParam String symbol) {
        return service.getSpotPrice(symbol);
    }

    @GetMapping("/symbol/multiple")
    public List<SpotPrice> getMultipleCryptoPrices(@RequestBody List<String> symbols) {
        return service.getSpotPrices(symbols);
    }
}
