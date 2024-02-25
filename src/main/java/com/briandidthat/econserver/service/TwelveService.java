package com.briandidthat.econserver.service;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.exception.BadRequestException;
import com.briandidthat.econserver.domain.twelve.StockDetails;
import com.briandidthat.econserver.domain.twelve.StockListResponse;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;
import com.briandidthat.econserver.util.RequestUtilities;
import com.briandidthat.econserver.util.StartupManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class TwelveService {
    private final Logger logger = LoggerFactory.getLogger("TwelveService");
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile Map<String, Boolean> availableStocks;
    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    public AssetPrice getStockPrice(String apiKey, String symbol) {
        RequestUtilities.validateSymbol(symbol, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apikey", apiKey);
        try {
            logger.info("Fetching current price for {}", symbol);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);
            final AssetPrice assetPrice = RequestUtilities.buildAssetPrice(symbol, response);
            logger.info("Fetched current price for {}. Price: {}", symbol, response.getPrice());
            return assetPrice;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public BatchResponse getMultipleStockPrices(String apiKey, @Size(min = 2, max = 5) List<String> symbols) {
        RequestUtilities.validateSymbols(symbols, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", String.join(",", symbols));
        params.put("apikey", apiKey);
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
