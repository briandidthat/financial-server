package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.coinbase.BatchRequest;
import com.briandidthat.financialserver.domain.coinbase.SpotPrice;
import com.briandidthat.financialserver.domain.coinbase.Statistic;
import com.briandidthat.financialserver.domain.coinbase.Token;
import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.briandidthat.financialserver.util.StartupManager;
import com.briandidthat.financialserver.util.StatisticsUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CoinbaseService {
    private final String DATA = "data";
    private final Logger logger = LoggerFactory.getLogger("CoinbaseService");
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile Map<String, Boolean> availableTokens;
    @Value("${apis.coinbase.baseUrl}")
    private String coinbaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public SpotPrice getSpotPrice(String symbol) {
        RequestUtilities.validateSymbol(symbol, availableTokens);

        try {
            logger.info(Markers.append("symbol", symbol), "Fetching current price");
            final String queryString = "/prices/" + symbol + "-USD/spot";
            final ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + queryString, String.class);
            final Map<String, SpotPrice> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            final SpotPrice spotPrice = result.get(DATA);
            spotPrice.setDate(LocalDate.now());

            logger.info(Markers.append("spotPrice", spotPrice), "Fetched spot price");
            return spotPrice;
        } catch (Exception e) {
            logger.error("Unable to fetch {} spot price. Reason: {}", symbol, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public SpotPrice getHistoricalSpotPrice(String symbol, LocalDate date) {
        RequestUtilities.validateSymbol(symbol, availableTokens);

        try {
            final Map<String, Object> logEntries = Map.of("symbol", symbol, "date", date);
            logger.info(Markers.appendEntries(logEntries), "Fetching historical spot price");
            final String queryString = "/prices/" + symbol + "-USD/spot?date=" + date;
            final ResponseEntity<String> response = restTemplate.getForEntity(coinbaseUrl + queryString, String.class);
            final Map<String, SpotPrice> result = mapper.readValue(response.getBody(), new TypeReference<>() {});
            final SpotPrice spotPrice = result.get(DATA);
            spotPrice.setDate(date);

            logger.info(Markers.append("spotPrice", spotPrice), "Fetched historical spot price");
            return spotPrice;
        } catch (Exception e) {
            logger.error("Unable to fetch historical price for {}. Reason: {}", symbol, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public Statistic getPriceStatistics(String symbol, LocalDate startDate, LocalDate endDate) {
        RequestUtilities.validateSymbol(symbol, availableTokens);

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

    public List<SpotPrice> getSpotPrices(List<String> symbols) {
        final List<SpotPrice> responses;
        final List<CompletableFuture<SpotPrice>> completableFutures = new ArrayList<>();

        logger.info(Markers.append("symbols", symbols), "Fetching prices asynchronously");

        final Instant start = Instant.now();
        // store list of symbols requests to be run in parallel
        symbols.forEach(symbol -> completableFutures.add(getSpotPriceAsync(symbol)));
        // wait for all requests to be completed
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        // calculate the time it took for our request to be completed
        final Instant end = Instant.now();

        responses = completableFutures.stream().map(c -> {
            SpotPrice response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return response;
        }).collect(Collectors.toList());

        final Map<String, Object> logEntries = new HashMap<>();
        logEntries.put("runtime", end.minusMillis(start.toEpochMilli()).toEpochMilli());
        logEntries.put("spotPrices", responses);

        logger.info(Markers.appendEntries(logEntries), "Completed async spot price request");

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
            final Map<String, List<Token>> result = mapper.readValue(response.getBody(), new TypeReference<>() {});
            return result.get(DATA);
        } catch (Exception e) {
            logger.error("Unable to retrieve token list. Reason: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // this operation will run on startup, and at 3pm every Monday
    @PostConstruct
    @Scheduled(cron = "0 0 15 * * MON")
    protected void updateAvailableTokens() {
        List<Token> tokens = new ArrayList<>();
        final Map<String, Boolean> symbols = new HashMap<>();
        boolean retry = true;
        int retryCount = 0;
        // continue to retry until we update the available tokens or fail 5 times
        // in which case we will shut down the application since it is unhealthy
        while (retry) {
            try {
                tokens = getAvailableTokens();
                retry = false;
            } catch (Exception e) {
                retryCount++;
                if (retryCount == 5) {
                    logger.error("Reached max retries. Count: {}.", retryCount);
                    StartupManager.registerResult(this.getClass(), false);
                    return;
                }
            }
        }
        for (Token token : tokens) {
            symbols.put(token.symbol().toUpperCase(), true);
        }
        availableTokens = symbols;
        logger.info("Updated available tokens list. Count: {}", tokens.size());
        StartupManager.registerResult(this.getClass(), true);
    }
}
