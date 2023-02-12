package com.toogroovy.priceserver.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    @BeforeEach
    void setUp() {
        final String coinbaseEndpoint = "https://api.coinbase.com/v2";

        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.BTC + "-USD/spot", SpotPrice.class)).thenReturn(ResponseEntity.ok(BTC));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.BNB + "-USD/spot", SpotPrice.class)).thenReturn(ResponseEntity.ok(BNB));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/"+ Cryptocurrency.ETH + "-USD/spot", SpotPrice.class)).thenReturn(ResponseEntity.ok(ETH));

        ReflectionTestUtils.setField(cryptoService, "coinbaseUrl", coinbaseEndpoint);
        ReflectionTestUtils.setField(cryptoService, "availableTokens", List.of(
                new Token("11", "ETH", "blue", 1, 8, "crypto", "dsfas", "23df"),
                new Token("12", "BTC", "orange", 2, 8, "crypto", "dsfas", "23df"),
                new Token("13", "BNB", "gold", 3, 8, "crypto", "dsfas", "23df")));
    }

    @Test
    void getSpotPrice() {
        SpotPrice tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);

        assertEquals(BTC, tickerResponse);
    }

    @Test
    void getSpotPricesSync() {
        List<SpotPrice> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }

    @Test
    void getSpotPricesAsync() {
        List<SpotPrice> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));

        assertIterableEquals(PRICES, responses);
    }
}