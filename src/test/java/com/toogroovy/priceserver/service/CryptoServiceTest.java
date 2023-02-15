package com.toogroovy.priceserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.SpotPrice;
import com.toogroovy.priceserver.domain.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CryptoServiceTest {
    private final SpotPrice BTC = new SpotPrice(Cryptocurrency.BTC, "USD","40102.44");
    private final SpotPrice BNB = new SpotPrice(Cryptocurrency.BNB, "USD", "389.22");
    private final SpotPrice ETH = new SpotPrice(Cryptocurrency.ETH, "USD", "2900.24");
    private final List<SpotPrice> PRICES = List.of(BTC, BNB, ETH);

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CryptoService cryptoService;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        final String coinbaseEndpoint = "https://api.coinbase.com/v2";

        final Map<String, SpotPrice> BTC_RESPONSE = Map.of("data", BTC);
        final Map<String, SpotPrice> BNB_RESPONSE = Map.of("data", BNB);
        final Map<String, SpotPrice> ETH_RESPONSE = Map.of("data", ETH);

        final String btcJson = mapper.writeValueAsString(BTC_RESPONSE);
        final String bnbJson = mapper.writeValueAsString(BNB_RESPONSE);
        final String ethJson = mapper.writeValueAsString(ETH_RESPONSE);

        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.BTC + "-USD/spot", String.class)).thenReturn(ResponseEntity.ok(btcJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.BNB + "-USD/spot", String.class)).thenReturn(ResponseEntity.ok(bnbJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.ETH + "-USD/spot", String.class)).thenReturn(ResponseEntity.ok(ethJson));

        ReflectionTestUtils.setField(cryptoService, "coinbaseUrl", coinbaseEndpoint);
        ReflectionTestUtils.setField(cryptoService, "availableTokens", List.of(
                new Token("BTC", "Bitcoin", "blue", 1, 8, "crypto", "dsfas", "23df"),
                new Token("ETH", "Ethereum", "orange", 2, 8, "crypto", "dsfas", "23df"),
                new Token("BNB", "Binance Coin", "gold", 3, 8, "crypto", "dsfas", "23df")));
    }

    @Test
    void getSpotPrice() {
        SpotPrice tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);

        assertEquals(BTC, tickerResponse);
    }

    @Test
    void getMultipleSpotPrices() {
        List<SpotPrice> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }
}