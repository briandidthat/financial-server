package com.toogroovy.priceserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toogroovy.priceserver.domain.ApiResponse;
import com.toogroovy.priceserver.domain.Token;
import com.toogroovy.priceserver.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CryptoService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoService.class);
    @Value("${apis.coinbase.baseUrl}")
    private String coinbaseUrl;
    @Autowired
    private RestTemplate restTemplate;
    private volatile List<Token> availableTokens;

    public List<Token> getAvailableTokens() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + "/currencies/crypto", String.class);
            Map<String, Token[]> result = mapper.readValue(response.getBody(), new TypeReference<>() {});
            Token[] tokens = result.get("data");
            return Arrays.asList(tokens);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public ApiResponse getSpotPrice(String symbol) throws HttpClientErrorException {
        symbol = symbol.toUpperCase();

        if (!RequestUtilities.validateCryptocurrency(symbol, availableTokens)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid symbol");
        }

        try {
            logger.info("Fetching current price for {}", symbol);
            final ResponseEntity<ApiResponse> result = restTemplate.getForEntity(coinbaseUrl + "/prices/" + symbol + "-USD/spot", ApiResponse.class);
            logger.info("Fetched {} pair price. Response: {}", symbol, result.getBody());
            return result.getBody();
        } catch (NullPointerException e) {
            logger.error("Unable to fetch pair {}. Reason: {}", symbol, e.getMessage());
        }
        return null;
    }

    @Async
    private CompletableFuture<ApiResponse> getSpotPriceAsync(String symbol) {
        return CompletableFuture.supplyAsync(() -> getSpotPrice(symbol));
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
                logger.error(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @EventListener(ApplicationReadyEvent.class)
    protected void updateAvailableTokens() {
        availableTokens = getAvailableTokens();
        logger.info("Updated available tokens list");
    }
}
