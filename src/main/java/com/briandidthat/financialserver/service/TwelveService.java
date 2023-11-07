package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.controller.HealthCheckController;
import com.briandidthat.financialserver.domain.exception.BadRequestException;
import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.financialserver.domain.twelve.StockDetails;
import com.briandidthat.financialserver.domain.twelve.TwelveResponse;
import com.briandidthat.financialserver.domain.twelve.TwelveStocksResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TwelveService {
    private static final Logger logger = LoggerFactory.getLogger(TwelveService.class);
    protected volatile List<StockDetails> availableStocks;
    @Value("${apis.twelve.baseUrl}")
    private String twelveBaseUrl;
    @Value("${apis.twelve.apiKey}")
    private String twelveApiKey;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;

    public TwelveResponse getStockPrice(String symbol) {
        if (!RequestUtilities.validateStockSymbol(symbol, availableStocks))
            throw new ResourceNotFoundException("Invalid symbol: {}" + symbol);

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("apiKey", twelveApiKey);
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
    @Scheduled(cron = "0 0 0 * * *")
    protected void updateAvailableStocks() {
        availableStocks = getAvailableStocks();
        if (availableStocks == null) {
            logger.error("Error retrieving available stocks. Retrying...");

            boolean retry = true;
            int retryCount = 0;

            // continue to retry until we reach a maximum retry of 5 in which case the application is deemed unhealthy
            while (retry) {
                List<StockDetails> available = getAvailableStocks();
                if (available != null) {
                    availableStocks = available;
                    logger.info("Successfully retrieved the available tokens on retry #{}", retryCount);
                    retry = false;
                } else {
                    if (retryCount == 5) {
                        logger.info("Reached max retries. Count {}", retryCount);
                        HealthCheckController.setAvailable(false);
                        return;
                    }

                    retryCount++;
                    available = getAvailableStocks();
                    if (available != null) {
                        retry = false;
                    }
                }
            }
        }
        logger.info("Updated available stocks list. Count: {}", availableStocks.size());
    }
}
