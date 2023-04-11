package com.briandidthat.priceserver.service;

import com.briandidthat.priceserver.domain.Statistic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.briandidthat.priceserver.domain.Cryptocurrency;
import com.briandidthat.priceserver.domain.SpotPrice;
import com.briandidthat.priceserver.domain.Token;
import org.apache.tomcat.jni.Local;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CryptoServiceTest {
    private final SpotPrice BTC = new SpotPrice(Cryptocurrency.BTC, "USD","40102.44", LocalDate.now());
    private final SpotPrice BNB = new SpotPrice(Cryptocurrency.BNB, "USD", "389.22", LocalDate.now());
    private final SpotPrice ETH = new SpotPrice(Cryptocurrency.ETH, "USD", "2900.00", LocalDate.now());
    private final LocalDate START_DATE = LocalDate.of(2021, 8, 1);
    private final LocalDate END_DATE = LocalDate.of(2023, 8, 1); // 2 yrs in between (24 months)
    private final SpotPrice HISTORICAL_ETH = new SpotPrice(Cryptocurrency.ETH, "USD", "4000.00", START_DATE);
    private final SpotPrice HISTORICAL_BTC = new SpotPrice(Cryptocurrency.BTC, "USD", "41000.00", START_DATE);

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
        final Map<String, SpotPrice> ETH_RESPONSE = Map.of("data", ETH);
        final Map<String, SpotPrice> BNB_RESPONSE = Map.of("data", BNB);
        final Map<String, SpotPrice> HISTORICAL_ETH_RESPONSE = Map.of("data", HISTORICAL_ETH);
        final Map<String, SpotPrice> HISTORICAL_BTC_RESPONSE = Map.of("data", HISTORICAL_BTC);


        final String btcJson = mapper.writeValueAsString(BTC_RESPONSE);
        final String ethJson = mapper.writeValueAsString(ETH_RESPONSE);
        final String bnbJson = mapper.writeValueAsString(BNB_RESPONSE);
        final String historicalEthJson = mapper.writeValueAsString(HISTORICAL_ETH_RESPONSE);
        final String historicalBtcJson = mapper.writeValueAsString(HISTORICAL_BTC_RESPONSE);

        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot", String.class, Map.of("symbol", Cryptocurrency.BTC))).thenReturn(ResponseEntity.ok(btcJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot", String.class, Map.of("symbol", Cryptocurrency.ETH))).thenReturn(ResponseEntity.ok(ethJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot", String.class, Map.of("symbol", Cryptocurrency.BNB))).thenReturn(ResponseEntity.ok(bnbJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot?date={date}", String.class, Map.of("symbol", Cryptocurrency.ETH, "date", START_DATE.toString()))).thenReturn(ResponseEntity.ok(historicalEthJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot?date={date}", String.class, Map.of("symbol", Cryptocurrency.BTC, "date", START_DATE.toString()))).thenReturn(ResponseEntity.ok(historicalBtcJson));
        when(restTemplate.getForEntity(coinbaseEndpoint + "/prices/{symbol}-USD/spot?date={date}", String.class, Map.of("symbol", Cryptocurrency.ETH, "date", END_DATE.toString()))).thenReturn(ResponseEntity.ok(ethJson));


        ReflectionTestUtils.setField(cryptoService, "coinbaseUrl", coinbaseEndpoint);
        ReflectionTestUtils.setField(cryptoService, "availableTokens", List.of(
                new Token("BTC", "Bitcoin", "blue", 1, 8, "crypto", "dsfas", "23df"),
                new Token("ETH", "Ethereum", "orange", 2, 8, "crypto", "dsfas", "23df"),
                new Token("BNB", "Binance Coin", "gold", 3, 8, "crypto", "dsfas", "23df")));
    }

    @Test
    void testGetSpotPrice() {
        SpotPrice tickerResponse = cryptoService.getSpotPrice(Cryptocurrency.BTC);
        assertEquals(BTC, tickerResponse);
    }

    @Test
    void testGetMultipleSpotPrices() {
        List<SpotPrice> responses = cryptoService.getSpotPrices(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB, Cryptocurrency.ETH));
        assertIterableEquals(PRICES, responses);
    }

    @Test
    void testGetHistoricalSpotPrice() {
        SpotPrice response = cryptoService.getHistoricalSpotPrice(Cryptocurrency.ETH, START_DATE);
        assertEquals(HISTORICAL_ETH, response);
    }

    @Test
    void testGetHistoricalSpotPrices() {
        Map<String, LocalDate> symbols = new HashMap<>();
        symbols.put(Cryptocurrency.ETH, START_DATE);
        symbols.put(Cryptocurrency.BTC, START_DATE);
        List<SpotPrice> response = cryptoService.getHistoricalSpotPrices(symbols);
        assertIterableEquals(List.of(HISTORICAL_BTC, HISTORICAL_ETH), response);
    }

    @Test
    void testGetPriceStatistics() {
        Statistic statistic = cryptoService.getPriceStatistics(Cryptocurrency.ETH, START_DATE, END_DATE);
        assertEquals("-1100.00", statistic.priceChange());
        assertEquals("-27.50", statistic.percentChange());
        assertEquals("24 months", statistic.timeDelta());
    }

}