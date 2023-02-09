package com.toogroovy.notificationapi.service;

import com.toogroovy.notificationapi.domain.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CryptoService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    @Value("${apis.coinbase.baseUrl}")
    private String coinbaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public ApiResponse getSpotPrice(String symbol) {
        try {
            logger.info("Fetching current price for {}", symbol);
            final ResponseEntity<ApiResponse> result = restTemplate.getForEntity(coinbaseUrl + "/prices/" +
                    symbol + "/spot", ApiResponse.class);
            logger.info("Fetched {} pair price. Response: {}", symbol, result.getBody());
            return result.getBody();
        } catch (NullPointerException e) {
            logger.info("Unable to fetch pair {}. Reason: {}", symbol, e.getMessage());
            return null;
        }
    }

    @Async
    private CompletableFuture<ApiResponse> getSpotPriceAsync(String ticker) {
        return CompletableFuture.supplyAsync(() -> getSpotPrice(ticker));
    }

    public List<ApiResponse> getSpotPrices(List<String> symbols) {
        final List<ApiResponse> responses;
        final List<CompletableFuture<ApiResponse>> requests;

        logger.info("Fetching prices asynchronously {}", symbols);

        final Instant start = Instant.now();
        // store list of symbols requests to be run in parallel
        requests = symbols.stream().map(this::getSpotPriceAsync).collect(Collectors.toList());
        // wait for all requests to be completed
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();
        // calculate the time it took for our request to be completed
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
}
