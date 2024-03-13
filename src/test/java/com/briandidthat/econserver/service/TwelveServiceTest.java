package com.briandidthat.econserver.service;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.BatchResponse;
import com.briandidthat.econserver.domain.coinbase.Statistic;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;
import com.briandidthat.econserver.domain.twelve.TimeSeriesResponse;
import com.briandidthat.econserver.util.RequestUtilities;
import com.briandidthat.econserver.util.TestingConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TwelveServiceTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String twelveBaseUrl = "https://api.twelvedata.com";
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private TwelveService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "twelveBaseUrl", twelveBaseUrl);
        ReflectionTestUtils.setField(service, "availableStocks", TestingConstants.AVAILABLE_STOCKS);
    }

    @Test
    void getAssetPrice() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "AAPL");
        params.put("apikey", TestingConstants.TEST_API_KEY);
        params.put("dp", 2);

        final String URL = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
        when(restTemplate.getForObject(URL, StockPriceResponse.class)).thenReturn(TestingConstants.APPLE_PRICE_RESPONSE);

        AssetPrice response = service.getAssetPrice(TestingConstants.TEST_API_KEY, TestingConstants.APPLE);
        assertEquals(TestingConstants.APPLE_PRICE, response);
    }

    @Test
    void testGetMultipleStockPrices() throws JsonProcessingException {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "AAPL,GOOG");
        params.put("apikey", TestingConstants.TEST_API_KEY);
        params.put("dp", 2);

        final Map<String, Map<String, String>> responseMap = new HashMap<>();
        responseMap.put("AAPL", Map.of("price", "150.00"));
        responseMap.put("GOOG", Map.of("price", "200.00"));
        final String batchResponse = mapper.writeValueAsString(responseMap);

        final String batchUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
        when(restTemplate.getForEntity(batchUrl, String.class)).thenReturn(ResponseEntity.ok(batchResponse));

        BatchResponse response = service.getMultipleAssetPrices(TestingConstants.TEST_API_KEY, List.of("AAPL", "GOOG"));
        assertEquals(TestingConstants.BATCH_STOCK_RESPONSE, response);
    }

    @Test
    void testGetHistoricalStockPrice() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", TestingConstants.APPLE);
        params.put("apikey", TestingConstants.TEST_API_KEY);
        params.put("dp", 2);
        params.put("date", TestingConstants.START_DATE);
        params.put("interval", "1day");

        final String historicalAppleUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", params);

        when(restTemplate.getForObject(historicalAppleUrl, TimeSeriesResponse.class)).thenReturn(TestingConstants.HISTORICAL_APPLE_PRICE_RESPONSE);

        AssetPrice response = service.getHistoricalAssetPrice(TestingConstants.TEST_API_KEY, TestingConstants.APPLE, TestingConstants.START_DATE);
        assertEquals(TestingConstants.HISTORICAL_APPLE_PRICE, response);
    }

    @Test
    void getMultipleHistoricalAssetPrices() {
        final Map<String, Object> appleParams = new LinkedHashMap<>();
        appleParams.put("symbol", TestingConstants.APPLE);
        appleParams.put("apikey", TestingConstants.TEST_API_KEY);
        appleParams.put("dp", 2);
        appleParams.put("date", TestingConstants.START_DATE);
        appleParams.put("interval", "1day");


        final Map<String, Object> googleParams = new LinkedHashMap<>(appleParams);
        googleParams.put("symbol", TestingConstants.GOOGLE);

        final String historicalAppleUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", appleParams);
        final String historicalGoogleUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", googleParams);

        when(restTemplate.getForObject(historicalAppleUrl, TimeSeriesResponse.class)).thenReturn(TestingConstants.HISTORICAL_APPLE_PRICE_RESPONSE);
        when(restTemplate.getForObject(historicalGoogleUrl, TimeSeriesResponse.class)).thenReturn(TestingConstants.HISTORICAL_GOOGLE_PRICE_RESPONSE);

        BatchResponse response = service.getMultipleHistoricalAssetPrices(TestingConstants.TEST_API_KEY, TestingConstants.BATCH_STOCK_REQUEST);
        assertEquals(TestingConstants.HISTORICAL_BATCH_STOCK_RESPONSE, response);
    }

    @Test
    void getAssetPriceStatistics() {
        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", TestingConstants.APPLE);
        params.put("apikey", TestingConstants.TEST_API_KEY);
        params.put("dp", 2);

        final Map<String, Object> historicalParams = new LinkedHashMap<>(params);
        historicalParams.put("date", TestingConstants.START_DATE);
        historicalParams.put("interval", "1day");

        final String url = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);
        final String historicalUrl = RequestUtilities.formatQueryString(twelveBaseUrl + "/time_series", historicalParams);

        when(restTemplate.getForObject(url, StockPriceResponse.class)).thenReturn(TestingConstants.APPLE_PRICE_RESPONSE);
        when(restTemplate.getForObject(historicalUrl, TimeSeriesResponse.class)).thenReturn(TestingConstants.HISTORICAL_APPLE_PRICE_RESPONSE);

        Statistic statistic = service.getAssetPriceStatistics(TestingConstants.TEST_API_KEY, TestingConstants.APPLE, TestingConstants.START_DATE);
        assertEquals(TestingConstants.APPLE_STATISTICS, statistic);
    }
}