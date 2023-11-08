package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Statistic;
import com.briandidthat.financialserver.domain.coinbase.Token;
import com.briandidthat.financialserver.domain.exception.BackendClientException;
import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.briandidthat.financialserver.util.StartupManager;
import com.briandidthat.financialserver.util.StatisticsUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CoinbaseService {
    private static final String DATA = "data";
    private static final Logger logger = LoggerFactory.getLogger(CoinbaseService.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private volatile List<Token> availableTokens;

    @Value("${apis.coinbase.baseUrl}")
    private String coinbaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public SpotPrice getSpotPrice(String symbol) throws HttpClientErrorException, BackendClientException {
        if (!RequestUtilities.validateSymbol(symbol, availableTokens))
            throw new ResourceNotFoundException("Invalid symbol: " + symbol);

        try {
            logger.info("Fetching current price for {}", symbol);
            final String queryString = "/prices/" + symbol + "-USD/spot";
            final ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + queryString, String.class);
            final Map<String, SpotPrice> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            final SpotPrice spotPrice = result.get(DATA);
            spotPrice.setDate(LocalDate.now());

            logger.info("Fetched {} spot price. Response: {}", symbol, spotPrice.getAmount());
            return spotPrice;
        } catch (Exception e) {
            logger.error("Unable to fetch {} spot price. Reason: {}", symbol, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public SpotPrice getHistoricalSpotPrice(String symbol, LocalDate date) {
        if (!RequestUtilities.validateSymbol(symbol, availableTokens))
            throw new ResourceNotFoundException("Invalid symbol: " + symbol);

        try {
            logger.info("Fetching historical price for {} on date {}", symbol, date);
            final String queryString = "/prices/" + symbol + "-USD/spot?date=" + date;
            final ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + queryString, String.class);
            final Map<String, SpotPrice> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            final SpotPrice spotPrice = result.get(DATA);
            spotPrice.setDate(date);

            logger.info("Fetched historical spot price for {}. Response: {}", symbol, spotPrice.getAmount());
            return spotPrice;
        } catch (Exception e) {
            logger.error("Unable to fetch historical price for {}. Reason: {}", symbol, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public Statistic getPriceStatistics(String symbol, LocalDate startDate, LocalDate endDate) {
        if (!RequestUtilities.validateSymbol(symbol, availableTokens))
            throw new ResourceNotFoundException("Invalid symbol: " + symbol);

        final boolean isToday = endDate.isEqual(LocalDate.now());
        final SpotPrice startPrice = getHistoricalSpotPrice(symbol, startDate);
        // if the end date is today, get current spot price. Else get historical price
        final SpotPrice endPrice = isToday ? getSpotPrice(symbol) : getHistoricalSpotPrice(symbol, endDate);

        return StatisticsUtilities.buildStatistic(startPrice, endPrice);
    }

    @Async
    private CompletableFuture<SpotPrice> getSpotPriceAsync(String symbol) {
        return CompletableFuture.supplyAsync(() -> getSpotPrice(symbol));
    }

    @Async
    private CompletableFuture<SpotPrice> getHistoricalSpotPriceAsync(String symbol, LocalDate date) {
        return CompletableFuture.supplyAsync(() -> getHistoricalSpotPrice(symbol, date));
    }

    public List<SpotPrice> getSpotPrices(BatchRequest batchRequest) {
        final List<SpotPrice> responses;
        final List<CompletableFuture<SpotPrice>> completableFutures = new ArrayList<>();

        logger.info("Fetching prices asynchronously {}", batchRequest.getRequests());

        final Instant start = Instant.now();
        // store list of symbols requests to be run in parallel
        batchRequest.getRequests().forEach(request -> completableFutures.add(getSpotPriceAsync(request.getSymbol())));
        // wait for all requests to be completed
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        // calculate the time it took for our request to be completed
        final Instant end = Instant.now();
        logger.info("Completed async spot price request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = completableFutures.stream().map(c -> {
            SpotPrice response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }

    public List<SpotPrice> getHistoricalSpotPrices(BatchRequest batchRequest) {
        final List<SpotPrice> responses;
        final List<CompletableFuture<SpotPrice>> completableFutures = new ArrayList<>();

        logger.info("Fetching historical prices asynchronously {}", batchRequest.getRequests());

        final Instant start = Instant.now();
        // store list of completableFutures to be run in parallel
        batchRequest.getRequests().forEach((request) -> completableFutures.add(getHistoricalSpotPriceAsync(request.getSymbol(), request.getStartDate())));
        // wait for all completableFutures to be completed
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        // calculate the time it took for our request to be completed
        final Instant end = Instant.now();
        logger.info("Completed async historical spot price request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        responses = completableFutures.stream().map(c -> {
            SpotPrice response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        return responses;
    }

    private List<Token> getAvailableTokens() {
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + "/currencies/crypto", String.class);
            final Map<String, List<Token>> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            return result.get(DATA);
        } catch (Exception e) {
            logger.error("Unable to retrieve token list. Reason: {}", e.getMessage());
            return null;
        }
    }

    // this operation will run on startup, and at 12:00am every day after
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * *")
    protected void updateAvailableTokens() {
        availableTokens = getAvailableTokens();
        if (availableTokens == null) {
            logger.error("Error retrieving available tokens. Retrying...");

            boolean retry = true;
            int retryCount = 0;

            // continue to retry until we update the available tokens or fail 5 times
            // in which case we will wait till the next request or next day
            while (retry) {
                List<Token> tokens = getAvailableTokens();
                if (tokens != null) {
                    availableTokens = tokens;
                    logger.info("Successfully retrieved the available tokens on attempt # {}.", retryCount);
                    retry = false;
                } else {
                    if (retryCount == 5) {
                        logger.error("Reached max retries {}.", retryCount);
                        StartupManager.registerResult(CoinbaseService.class.getName(),false);
                        return;
                    }

                    retryCount++;
                    tokens = getAvailableTokens();
                    if (tokens != null) {
                        retry = false;
                    }
                }
            }
        }
        StartupManager.registerResult(CoinbaseService.class.getName(), true);
        logger.info("Updated available tokens list. Count: {}", availableTokens.size());
    }
}
