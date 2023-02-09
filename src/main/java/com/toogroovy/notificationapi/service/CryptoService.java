package com.toogroovy.notificationapi.service;

import com.toogroovy.notificationapi.domain.rest.ApiResponse;
import com.toogroovy.notificationapi.domain.rest.DebankBalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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

    @Value("${apis.coinbase.baseUrl}")
    private String coinbaseUrl;

    public ApiResponse getSpotPrice(String ticker) {
        try {
            logger.info("Fetching current {} pair price", ticker);
            final ResponseEntity<ApiResponse> result = restTemplate.getForEntity(binanceUrls[0] +
                    tickerPath + ticker, ApiResponse.class);
            logger.info("Fetched {} pair price. Response: {}", ticker, result.getBody());
            return result.getBody();
        } catch (NullPointerException e) {
            logger.info("Unable to fetch pair {}. Reason: {}", ticker, e.getMessage());
            return null;
        }
    }

    @Async
    private CompletableFuture<ApiResponse> getSpotPriceAsync(String ticker) {
        return CompletableFuture.supplyAsync(() -> getSpotPrice(ticker));
    }

    public List<ApiResponse> getSpotPricesSync(List<String> tickers) {
        final List<ApiResponse> responses = new ArrayList<>();
        logger.info("Fetching prices synchronously {}", Arrays.toString(tickers.toArray()));

        final Instant start = Instant.now();
        for (String ticker : tickers) {
            ApiResponse response = getSpotPrice(ticker);
            responses.add(response);
        }
        final Instant end = Instant.now();

        logger.info("Fetched prices for {}", Arrays.toString(tickers.toArray()));
        logger.info("Completed synchronous ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        return responses;
    }

    public List<ApiResponse> getSpotPricesAsync(List<String> tickers) {
        final List<ApiResponse> responses;
        final List<CompletableFuture<ApiResponse>> requests;

        logger.info("Fetching prices asynchronously {}", tickers);

        final Instant start = Instant.now();
        // store list of tickers requests to be run in parallel
        requests = tickers.stream().map(this::getSpotPriceAsync).collect(Collectors.toList());
        // wait for all requests to be completed
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();
        final Instant end = Instant.now();
        logger.info("Completed async ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = requests.stream().map(c -> {
            ApiResponse response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }

    public DebankBalanceResponse getAccountBalance(String address) {
        try {
            logger.info("Fetching account balances for {}", address);
            final ResponseEntity<DebankBalanceResponse> response = restTemplate.getForEntity(debankUrl + balancePath
                    + address, DebankBalanceResponse.class);
            logger.info("Fetched account balances for {}", address);
            return response.getBody();
        } catch (Exception e) {
            logger.info("Unable to fetch balances for {}. Reason: {}", address, e.getMessage());
            return null;
        }
    }

    @Async
    public CompletableFuture<DebankBalanceResponse> getAccountBalanceAsync(String address) {
        return CompletableFuture.supplyAsync(() -> getAccountBalance(address));
    }

    public List<DebankBalanceResponse> getAccountBalances(List<String> addresses) {
        final List<DebankBalanceResponse> responses;
        final List<CompletableFuture<DebankBalanceResponse>> requests;

        logger.info("Fetching balances asynchronously {}", addresses);

        final Instant start = Instant.now();
        // create list of balance requests to be run in parallel
        requests = addresses.stream().map(this::getAccountBalanceAsync).collect(Collectors.toList());
        // wait for all requests to be finished
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();
        final Instant end = Instant.now();

        logger.info("Completed async ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = requests.stream().map(c -> {
            DebankBalanceResponse response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.info("Unable to fetch balance. Reason: {}", e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }
}
