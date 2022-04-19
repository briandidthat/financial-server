package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
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
public class StockService {
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${alphaVantage.baseUrl}")
    private String baseUrl;
    @Value("${alphaVantage.paths.quote}")
    private String quotePath;
    @Value("${alphaVantage.apiKey}")
    private String apiKey;

    public AlphaVantageQuoteResponse getQuote(String symbol) {
        try {
            logger.info("Fetching quote for {}", symbol);
            final ResponseEntity<AlphaVantageQuoteResponse> response = restTemplate.getForEntity(baseUrl +
                    quotePath + symbol + apiKey, AlphaVantageQuoteResponse.class);
            logger.info("Fetched quote for {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            logger.info("Unable to fetch quote. Reason: {}", e.getMessage());
            return null;
        }
    }

    @Async
    public CompletableFuture<AlphaVantageQuoteResponse> getQuoteAsync(String symbol) {
        return CompletableFuture.supplyAsync(() -> getQuote(symbol));
    }

    public List<AlphaVantageQuoteResponse> getMultipleQuotes(List<String> symbols) {
        final List<AlphaVantageQuoteResponse> responses;
        final List<CompletableFuture<AlphaVantageQuoteResponse>> requests;

        logger.info("Fetching quotes asynchronously {}", symbols);
        final Instant start = Instant.now();
        // store list of quote requests to be run in parallel
        requests = symbols.stream().map(this::getQuoteAsync).collect(Collectors.toList());
        // wait for all requests to be completed
        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();

        final Instant end = Instant.now();
        logger.info("Completed async ticker request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = requests.stream().map(c -> {
            AlphaVantageQuoteResponse response = null;
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
