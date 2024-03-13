package com.briandidthat.econserver.service;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.BatchRequest;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.domain.exception.BadRequestException;
import com.briandidthat.econserver.domain.twelve.StockDetails;
import com.briandidthat.econserver.domain.twelve.StockListResponse;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;
import com.briandidthat.econserver.domain.twelve.TimeSeriesResponse;
import com.briandidthat.econserver.util.RequestUtilities;
import com.briandidthat.econserver.util.StartupManager;
import com.briandidthat.econserver.util.StatisticsUtilities;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Size;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class TwelveService {
    private final Logger logger = LoggerFactory.getLogger("TwelveService");
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile Map<String, Boolean> availableStocks;
    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public AssetPrice getAssetPrice(String apiKey, String symbol) {
        RequestUtilities.validateSymbol(symbol, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apikey", apiKey);
        params.put("dp", 2);

        try {
            logger.info("Fetching current price for {}", symbol);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);
            final AssetPrice assetPrice = RequestUtilities.buildAssetPrice(symbol, response);
            logger.info(Markers.append("assetPrice", assetPrice), "Completed stock price request.");
            return assetPrice;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public AssetPrice getHistoricalAssetPrice(String apiKey, String symbol, LocalDate date) {
        RequestUtilities.validateSymbol(symbol, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apikey", apiKey);
        params.put("dp", 2);
        params.put("date", date);
        params.put("interval", "1day");

        try {
            logger.info("Fetching price of {} on {}", symbol, date);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", params);
            final TimeSeriesResponse response = restTemplate.getForObject(url, TimeSeriesResponse.class);
            final TimeSeriesResponse.Value value = response.getValues().get(0);
            final AssetPrice assetPrice = new AssetPrice(symbol, value.getClose(), date);

            logger.info(Markers.append("assetPrice", assetPrice), "Completed stock price request.");
            return assetPrice;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public BatchResponse getMultipleAssetPrices(String apiKey, @Size(min = 2, max = 5) List<String> symbols) {
        RequestUtilities.validateSymbols(symbols, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", String.join(",", symbols));
        params.put("apikey", apiKey);
        params.put("dp", 2);

        try {
            logger.info("Fetching current price for {}", symbols);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            final Map<String, Map<String, String>> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            final List<AssetPrice> results = new ArrayList<>();
            symbols.forEach(s -> {
                final Map<String, String> stock = result.get(s);
                final AssetPrice assetPrice = new AssetPrice(s, stock.get("price"), LocalDate.now());
                results.add(assetPrice);
            });
            return new BatchResponse(results);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Async
    private CompletableFuture<AssetPrice> getHistoricalAssetPriceAsync(String apiKey, String symbol, LocalDate date) {
        return CompletableFuture.supplyAsync(() -> getHistoricalAssetPrice(apiKey, symbol, date));
    }

    public BatchResponse getMultipleHistoricalAssetPrices(String apiKey, BatchRequest batchRequest) {
        final List<CompletableFuture<AssetPrice>> completableFutures = new ArrayList<>();

        logger.info(Markers.append("request", batchRequest), "Fetching historical prices asynchronously");

        final Instant start = Instant.now();
        // store list of completableFutures to be run in parallel
        batchRequest.getRequests().forEach((request -> completableFutures.add(getHistoricalAssetPriceAsync(apiKey, request.getSymbol(), request.getStartDate()))));
        // wait for all requests to be completed
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        // calculate the time it took for our request to be completed
        final Instant end = Instant.now();
        logger.info("Completed async historical spot price request in {}ms", end.minusMillis(start.toEpochMilli()).toEpochMilli());

        final List<AssetPrice> responses = completableFutures.stream().map(c -> {
            AssetPrice response = null;
            try {
                response = c.get();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            return response;
        }).toList();

        final Map<String, Object> logEntries = new HashMap<>();
        logEntries.put("runtime", end.minusMillis(start.toEpochMilli()).toEpochMilli());
        logEntries.put("responses", responses);
        logger.info(Markers.appendEntries(logEntries), "Completed batch historical stock price request");

        return new BatchResponse(responses);
    }

    public Statistic getAssetPriceStatistics(String apiKey, String symbol, LocalDate startDate) {
        RequestUtilities.validateSymbol(symbol, availableStocks);

        final AssetPrice startPrice = getHistoricalAssetPrice(apiKey, symbol, startDate);
        final AssetPrice endPrice = getAssetPrice(apiKey, symbol);

        return StatisticsUtilities.buildStatistic(startPrice, endPrice);
    }

    private List<StockDetails> getAvailableStocks() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("country", "USA");
        params.put("type", "common stock");
        try {
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/stocks", params);
            final StockListResponse response = restTemplate.getForObject(url, StockListResponse.class);
            return response.stocks();
        } catch (Exception e) {
            logger.error("Unable to retrieve stock list. Reason: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // this operation will run on startup, and at 3pm every Monday
    @PostConstruct
    @Scheduled(cron = "0 0 15 * * MON")
    protected void updateAvailableStocks() {
        final Map<String, Boolean> symbols = new HashMap<>();
        List<StockDetails> stocks = new ArrayList<>();
        boolean retry = true;
        int retryCount = 0;
        // continue to retry until we update the available tokens or fail 5 times
        // in which case we will shut down the application since it is unhealthy
        while (retry) {
            try {
                stocks = getAvailableStocks();
                retry = false;
            } catch (Exception e) {
                retryCount++;
                if (retryCount == 5) {
                    logger.info("Reached max retries. Count: {}", retryCount);
                    StartupManager.registerResult(this.getClass(), false);
                    return;
                }
            }
        }
        for (StockDetails details : stocks) {
            symbols.put(details.symbol().toUpperCase(), true);
        }
        availableStocks = symbols;
        logger.info("Updated available stocks list. Count: {}", availableStocks.size());
        StartupManager.registerResult(this.getClass(), true);
    }
}
