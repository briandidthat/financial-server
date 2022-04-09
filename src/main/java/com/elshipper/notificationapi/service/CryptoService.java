package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.Asset;
import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.domain.rest.DebankBalanceResponse;
import com.elshipper.notificationapi.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class CryptoService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${binance.paths.ticker}")
    private String tickerPath;
    @Value("${binance.baseUrls}")
    private String[] binanceUrls;
    @Value("${debank.baseUrl}")
    private String debankUrl;
    @Value("${debank.paths.totalBalance}")
    private String balancePath;

    public BinanceTickerResponse getTickerPrice(String ticker) {
        logger.info("Fetching current {} pair price", ticker);
        final ResponseEntity<BinanceTickerResponse> result = restTemplate.getForEntity(binanceUrls[0] +
                tickerPath + ticker, BinanceTickerResponse.class);
        logger.info("Fetched {} pair price. Price: {}", ticker, result.getBody());
        return result.getBody();
    }

    @Async
    private CompletableFuture<BinanceTickerResponse> getTickerPriceAsync(String ticker) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Fetching current {} pair price", ticker);
            final ResponseEntity<BinanceTickerResponse> result = restTemplate.getForEntity(binanceUrls[0] +
                    tickerPath + ticker, BinanceTickerResponse.class);
            return result.getBody();
        });
    }

    public List<BinanceTickerResponse> getTickerPricesSync(List<String> tickers) {
        if (RequestUtilities.validateAssets(tickers)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid ticker");
        }

        final Instant start = Instant.now();
        final List<BinanceTickerResponse> responses = new ArrayList<>();
        logger.info("Fetching prices synchronously {}", Arrays.toString(tickers.toArray()));
        for (String ticker : tickers) {
            ResponseEntity<BinanceTickerResponse> response = restTemplate.getForEntity(binanceUrls[0] +
                    tickerPath + ticker, BinanceTickerResponse.class);
            responses.add(response.getBody());
        }
        final Instant end = Instant.now();
        logger.info("Fetched prices for {}", Arrays.toString(tickers.toArray()));
        logger.info("Completed synchronous ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        return responses;
    }

    public List<BinanceTickerResponse> getTickerPricesAsync(List<String> tickers) {
        if (tickers.size() == 0 || RequestUtilities.validateAssets(tickers)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid ticker");
        }

        final List<BinanceTickerResponse> responses;
        final List<CompletableFuture<BinanceTickerResponse>> requests;

        logger.info("Fetching prices asynchronously {}", tickers);

        final Instant start = Instant.now();
        // create list of tickers requests to be run in parallel
        requests = tickers.stream().map(this::getTickerPriceAsync).collect(Collectors.toList());
        // wait for all requests to be finished
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();
        final Instant end = Instant.now();
        logger.info("Completed batch ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = requests.stream().map(c -> {
            BinanceTickerResponse response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }

    public DebankBalanceResponse getAccountBalances(String address) {
        logger.info("Fetching account balances for {}", address);
        final ResponseEntity<DebankBalanceResponse> response = restTemplate.getForEntity(debankUrl +
                balancePath + address, DebankBalanceResponse.class);
        logger.info("Fetched account balances for {}", address);
        return response.getBody();
    }
}
