package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.domain.rest.DebankBalanceResponse;
import com.elshipper.notificationapi.service.CryptoService;
import com.elshipper.notificationapi.util.RequestUtilities;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;


@RestController
@RequestMapping("/crypto")
public class CryptoController {
    @Autowired
    private CryptoService service;

    @GetMapping("/tickers/{ticker}")
    public BinanceTickerResponse getAssetPrice(@PathVariable String ticker) {
        if (!RequestUtilities.validateAsset(ticker)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid ticker");
        }
        return service.getTickerPrice(ticker);
    }

    @GetMapping("/tickers/batch")
    public List<BinanceTickerResponse> getAssetPricesAsync(@RequestBody List<String> tickers) {
        if (tickers.size() == 0 || !RequestUtilities.validateAssets(tickers)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid ticker");
        }
        return service.getTickerPricesAsync(tickers);
    }

    @GetMapping("/tickers/sequential")
    public List<BinanceTickerResponse> getAssetPricesSync(@RequestBody List<String> tickers) {
        if (tickers.size() == 0 || !RequestUtilities.validateAssets(tickers)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid ticker");
        }
        return service.getTickerPricesSync(tickers);
    }

    @GetMapping("/accounts/total-balance")
    public DebankBalanceResponse getAccountBalance(@RequestParam @NotNull String address) {

        return service.getAccountBalance(address);
    }

    @GetMapping("/accounts/total-balance/batch")
    public List<DebankBalanceResponse> getAccountBalancesAsync(@RequestBody List<String> addresses) {
        if (addresses.size() == 0) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Empty address list");

        return service.getAccountBalances(addresses);
    }
}
