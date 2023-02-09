package com.toogroovy.notificationapi.service;

import com.toogroovy.notificationapi.domain.Cryptocurrency;
import com.toogroovy.notificationapi.domain.rest.ApiResponse;
import com.toogroovy.notificationapi.domain.rest.DebankBalanceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CryptoServiceTest {
    private final ApiResponse BTC_USD = new ApiResponse(Cryptocurrency.BTC, "40102.44", "coinbase");
    private final ApiResponse BNB_USD = new ApiResponse(Cryptocurrency.BNB, "389.22", "coinbase");
    private final ApiResponse ETH_USD = new ApiResponse(Cryptocurrency.ETH, "2900.24", "coinbase");
    private final DebankBalanceResponse GUPPY = new DebankBalanceResponse("21.234", new ArrayList<>());
    private final DebankBalanceResponse WHALE = new DebankBalanceResponse("3000000.23", new ArrayList<>());

    private final List<ApiResponse> PRICES = List.of(BTC_USD, BNB_USD, ETH_USD);
    private final List<DebankBalanceResponse> BALANCES = List.of(GUPPY, WHALE);
    private final String coinbaseEndpoint = "GET https://api.coinbase.com/v2";
    private final String debankEndpoint =  "https://openapi.debank.com/v1/user/total_balance?id=";

    private final String guppyAddress = "0x344532524";
    private final String whaleAddress = "0x344532512";
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private CryptoService cryptoService;
    
    @BeforeEach
    void setUp() {
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/${}/spot", ApiResponse.class)).thenReturn(ResponseEntity.ok(BTC_USD));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.BNB, ApiResponse.class)).thenReturn(ResponseEntity.ok(BNB_USD));
        when(restTemplate.getForEntity(coinbaseEndpoint + Cryptocurrency.ETH, ApiResponse.class)).thenReturn(ResponseEntity.ok(ETH_USD));
        when(restTemplate.getForEntity(debankEndpoint + guppyAddress, DebankBalanceResponse.class)).thenReturn(ResponseEntity.ok(GUPPY));
        when(restTemplate.getForEntity(debankEndpoint + whaleAddress, DebankBalanceResponse.class)).thenReturn(ResponseEntity.ok(WHALE));
    }

    @Test
    void getSpotPrice() {
        ApiResponse tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);

        assertEquals(BTC_USD, tickerResponse);
    }

    @Test
    void getSpotPricesSync() {
        List<ApiResponse> responses = cryptoService.getSpotPricesSync(List.of(Cryptocurrency.BTC,
                Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getSpotPricesAsync() {
        List<ApiResponse> responses = cryptoService.getSpotPricesAsync(List.of(Cryptocurrency.BTC,
                Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getAccountBalance() {
        DebankBalanceResponse response = cryptoService.getAccountBalance(guppyAddress);

        assertEquals(GUPPY, response);
    }

    @Test
    void getAccountBalances() {
        List<DebankBalanceResponse> responses = cryptoService.getAccountBalances(List.of(guppyAddress, whaleAddress));

        assertIterableEquals(BALANCES, responses);
    }
}