package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.domain.rest.DebankBalanceResponse;
import com.elshipper.notificationapi.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/crypto")
public class CryptoController {
    @Autowired
    private CryptoService service;

    @GetMapping("/tickers/{ticker}")
    public BinanceTickerResponse getAssetPrice(@PathVariable String ticker) {
        return service.getTickerPrice(ticker);
    }

    @GetMapping("/tickers/batch")
    public List<BinanceTickerResponse> getAssetPricesAsync(@RequestBody List<String> tickers) {
        return service.getTickerPricesAsync(tickers);
    }

    @GetMapping("/tickers/sequential")
    public List<BinanceTickerResponse> getAssetPricesSync(@RequestBody List<String> tickers) {
        return service.getTickerPricesSync(tickers);
    }

    @GetMapping("/accounts/total-balance")
    public DebankBalanceResponse getAccountBalance(@RequestParam String address) {
        return service.getAccountBalances(address);
    }
}
