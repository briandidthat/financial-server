package com.briandidthat.financialserver.controller;

import com.briandidthat.financialserver.domain.twelve.TwelveResponse;
import com.briandidthat.financialserver.service.TwelveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private TwelveService service;

    @GetMapping
    public TwelveResponse getStockPrice(@RequestParam String symbol) {
        return service.getStockPrice(symbol);
    }

}
