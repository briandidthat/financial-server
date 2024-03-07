package com.briandidthat.econserver.service;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;
import com.briandidthat.econserver.util.RequestUtilities;
import com.briandidthat.econserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TwelveServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private TwelveService service;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        final String twelveBaseUrl = "https://api.twelvedata.com";

        // params for single stock price request
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "AAPL");
        params.put("apikey", TestingConstants.TEST_API_KEY);
        params.put("dp", 2);

        // params for multiple stock price request
        final Map<String, Object> batchParams = new LinkedHashMap<>();
        batchParams.put("symbol", "AAPL,GOOG");
        batchParams.put("apikey", TestingConstants.TEST_API_KEY);
        batchParams.put("dp", 2);

        // params for multiple historical stock price requests
        final Map<String, Object> batchHistoricalParams = new LinkedHashMap<>();
        batchHistoricalParams.put("apikey", TestingConstants.TEST_API_KEY);
        batchHistoricalParams.put("dp", 2);

        // URLs for test requests
        final String URL = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
        final String batchUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", batchParams);
        final String batchHistoricalUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", batchHistoricalParams);

        final Map<String, Map<String, String>> response = new HashMap<>();
        response.put("AAPL", Map.of("price", "108.50"));
        response.put("GOOG", Map.of("price", "128.50"));
        final String batchResponse = mapper.writeValueAsString(response);

        when(restTemplate.getForObject(URL, StockPriceResponse.class)).thenReturn(TestingConstants.APPLE_PRICE_RESPONSE);
        when(restTemplate.getForEntity(batchUrl, String.class)).thenReturn(ResponseEntity.ok(batchResponse));

        ReflectionTestUtils.setField(service, "twelveBaseUrl", twelveBaseUrl);
        ReflectionTestUtils.setField(service, "availableStocks", TestingConstants.AVAILABLE_STOCKS);
    }

    @Test
    void getAssetPrice() {
        AssetPrice response = service.getAssetPrice(TestingConstants.TEST_API_KEY, "AAPL");
        assertEquals(TestingConstants.APPLE_PRICE, response);
    }

    @Test
    void testGetMultipleStockPrices() {
        BatchResponse response = service.getMultipleAssetPrices(TestingConstants.TEST_API_KEY, List.of("AAPL", "GOOG"));
        assertEquals(TestingConstants.BATCH_STOCK_RESPONSE, response);
    }

    @Test
    void getMultipleHistoricalAssetPrices() {
    }

    @Test
    void getAssetPriceStatistics() {
    }
}