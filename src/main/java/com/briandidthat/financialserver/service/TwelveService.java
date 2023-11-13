package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.twelve.StockDetails;
import com.briandidthat.financialserver.domain.twelve.StockListResponse;
import com.briandidthat.financialserver.domain.twelve.StockPriceResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.briandidthat.financialserver.util.StartupManager;
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

import java.util.*;

@Service
public class TwelveService {
    private final Logger logger = LoggerFactory.getLogger(TwelveService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile Map<String, Boolean> availableStocks;
    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Value("${apis.twelve.apiKey}")
    private String twelveApiKey;
    @Autowired
    private RestTemplate restTemplate;

    public StockPriceResponse getStockPrice(String symbol) {
        RequestUtilities.validateSymbol(symbol, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apikey", twelveApiKey);
        try {
            logger.info("Fetching current price for {}", symbol);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final StockPriceResponse response = restTemplate.getForObject(url, StockPriceResponse.class);
            response.setSymbol(symbol);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<StockPriceResponse> getMultipleStockPrices(@Size(min = 2, max = 5) List<String> symbols) {
        RequestUtilities.validateSymbols(symbols, availableStocks);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", String.join(",", symbols));
        params.put("apikey", twelveApiKey);
        try {
            logger.info("Fetching current price for {}", symbols);
            final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
            final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            final Map<String, Map<String, String>> result = mapper.readValue(response.getBody(), new TypeReference<>() {
            });
            final List<StockPriceResponse> results = new ArrayList<>();
            symbols.forEach(s -> {
                final Map<String, String> stock = result.get(s);
                final StockPriceResponse stockPriceResponse = new StockPriceResponse(s, stock.get("price"));
                results.add(stockPriceResponse);
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
            final StockListResponse response = restTemplate.getForObject(url, StockListResponse.class);
            return response.stocks();
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    @PostConstruct
    @Scheduled(cron = "0 0 9 * * MON") // run every monday at 9AM
    protected void updateAvailableStocks() {
        List<StockDetails> stocks = getAvailableStocks();
        if (stocks == null) {
            logger.error("Error retrieving available stocks. Retrying...");

            boolean retry = true;
            int retryCount = 0;
            // continue to retry until we reach a maximum retry of 5 in which case the application is deemed unhealthy
            while (retry) {
                stocks = getAvailableStocks();
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
                    stocks = getAvailableStocks();
                    if (availableStocks != null) {
                        retry = false;
                    }
                }
            }
        }
        final Map<String, Boolean> symbols = new HashMap<>();
        for (StockDetails details : stocks) {
            symbols.put(details.symbol().toUpperCase(), true);
        }
        availableStocks = symbols;
        logger.info("Updated available stocks list. Count: {}", availableStocks.size());
        StartupManager.registerResult(TwelveService.class.getName(), true);
    }
}
