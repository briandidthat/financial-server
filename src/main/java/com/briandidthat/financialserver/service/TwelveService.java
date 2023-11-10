package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.financialserver.domain.twelve.StockDetails;
import com.briandidthat.financialserver.domain.twelve.TwelveResponse;
import com.briandidthat.financialserver.domain.twelve.TwelveStocksResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.briandidthat.financialserver.util.StartupManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TwelveService {
    private static final Logger logger = LoggerFactory.getLogger(TwelveService.class);
    private volatile Map<String, Boolean> availableSymbols;
    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Value("${apis.twelve.apiKey}")
    private String twelveApiKey;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    public TwelveResponse getStockPrice(String symbol) {
        if (!RequestUtilities.validateStockSymbol(symbol, availableSymbols))
            throw new ResourceNotFoundException("Invalid symbol: " + symbol);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apikey", twelveApiKey);
        try {
            logger.info("Fetching current price for {}", symbol);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final TwelveResponse response = restTemplate.getForObject(url, TwelveResponse.class);
            response.setSymbol(symbol);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<TwelveResponse> getMultipleStockPrices(List<String> symbols) {
        if (!RequestUtilities.validateStockSymbols(symbols, availableSymbols))
            throw new ResourceNotFoundException("Invalid symbols: " + symbols);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", String.join(",", symbols));
        params.put("apikey", twelveApiKey);
        try {
            logger.info("Fetching current price for {}", symbols);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            final Map<String, Map<String, String>> result = mapper.readValue(response.getBody(), new TypeReference<>() {});
            final List<TwelveResponse> results = new ArrayList<>();
            symbols.forEach(s -> {
                final Map<String, String> stock = result.get(s);
                final TwelveResponse twelveResponse = new TwelveResponse(s, stock.get("price"));
                results.add(twelveResponse);
            });
            return results;
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
            final TwelveStocksResponse response = restTemplate.getForObject(url, TwelveStocksResponse.class);
            return response.stocks();
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    @PostConstruct
    @Scheduled(cron = "0 0 0 * * MON")
    protected void updateAvailableStocks() {
        List<StockDetails> availableStocks = getAvailableStocks();
        if (availableStocks == null) {
            logger.error("Error retrieving available stocks. Retrying...");

            boolean retry = true;
            int retryCount = 0;

            // continue to retry until we reach a maximum retry of 5 in which case the application is deemed unhealthy
            while (retry) {
                availableStocks = getAvailableStocks();
                if (availableStocks != null) {
                    logger.info("Successfully retrieved the available tokens on retry #{}", retryCount);
                    retry = false;
                } else {
                    if (retryCount == 5) {
                        logger.info("Reached max retries. Count {}", retryCount);
                        StartupManager.registerResult(TwelveService.class.getName(), false);
                        return;
                    }

                    retryCount++;
                    availableStocks = getAvailableStocks();
                    if (availableStocks != null) {
                        retry = false;
                    }
                }
            }
        }
        final Map<String, Boolean> stocks = new HashMap<>();
        for (StockDetails details : availableStocks) {
            stocks.put(details.symbol().toUpperCase(), true);
        }
        availableSymbols = stocks;
        logger.info("Updated available stocks list. Count: {}", availableStocks.size());
        StartupManager.registerResult(TwelveService.class.getName(),true);
    }
}
