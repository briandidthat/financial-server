package com.toogroovy.priceserver.service;

import com.toogroovy.priceserver.domain.ApiResponse;
import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CryptoServiceTest {
    private final ApiResponse BTC = new ApiResponse(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final ApiResponse BNB = new ApiResponse(Cryptocurrency.BNB, "389.22", "coinbase");
    private final ApiResponse ETH = new ApiResponse(Cryptocurrency.ETH, "2900.24", "coinbase");
    private final List<ApiResponse> PRICES = List.of(BTC, BNB, ETH);

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        final String coinbaseEndpoint = "https://api.coinbase.com/v2/prices/";

        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.BNB + "-USD/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(BTC));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.BNB + "-USD/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(BNB));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.ETH + "-USD/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(ETH));

        ReflectionTestUtils.setField(cryptoService, "availableTokens", List.of(
                new Token("11", "AVAX", "red", 1, 8, "crypto", "dsfas", "23df"),
                new Token("12", "BTC", "orange", 2, 8, "crypto", "dsfas", "23df"),
                new Token("13", "BNB", "gold", 3, 8, "crypto", "dsfas", "23df")));
    }

    @Test
    void getSpotPrice() {
        ApiResponse tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);

        assertEquals(BTC, tickerResponse);
    }

    @Test
    void getSpotPricesSync() {
        List<ApiResponse> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getSpotPricesAsync() {
        List<ApiResponse> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }
}