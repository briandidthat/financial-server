package com.toogroovy.notificationapi.service;

import com.toogroovy.notificationapi.domain.Cryptocurrency;
import com.toogroovy.notificationapi.domain.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CryptoServiceTest {
    private final ApiResponse BTC_USD = new ApiResponse(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final ApiResponse BNB_USD = new ApiResponse(Cryptocurrency.BNB, "389.22", "coinbase");
    private final ApiResponse ETH_USD = new ApiResponse(Cryptocurrency.ETH, "2900.24", "coinbase");
    private final List<ApiResponse> PRICES = List.of(BTC_USD, BNB_USD, ETH_USD);

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private CryptoService cryptoService;
    
    @BeforeEach
    void setUp() {
        final String coinbaseEndpoint = "https://api.coinbase.com/v2/prices/";

        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.BNB + "/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(BTC_USD));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.BNB + "/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(BNB_USD));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.ETH + "/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(ETH_USD));
    }

    @Test
    void getSpotPrice() {
        ApiResponse tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);

        assertEquals(BTC_USD, tickerResponse);
    }

    @Test
    void getSpotPricesSync() {
        List<ApiResponse> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC,
                Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getSpotPricesAsync() {
        List<ApiResponse> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC,
                Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }
}