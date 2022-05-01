package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@MockBean(SchedulingService.class)
class StockServiceTest {
    private final String alphaVantage = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=";
    private final String IBM_SYMBOL = "IBM";
    private final String VOO_SYMBOL = "VOO";
    private final AlphaVantageQuoteResponse IBM = new AlphaVantageQuoteResponse(new AlphaVantageQuoteResponse.Quote());
    private final AlphaVantageQuoteResponse VOO = new AlphaVantageQuoteResponse(new AlphaVantageQuoteResponse.Quote());
    private final List<AlphaVantageQuoteResponse> QUOTES = List.of(IBM, VOO);
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private StockService service;
    @Value("${alphaVantage.apiKey}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        when(restTemplate.getForEntity(alphaVantage + IBM_SYMBOL + apiKey, AlphaVantageQuoteResponse.class)).thenReturn(ResponseEntity.ok(IBM));
        when(restTemplate.getForEntity(alphaVantage + VOO_SYMBOL + apiKey, AlphaVantageQuoteResponse.class)).thenReturn(ResponseEntity.ok(VOO));
    }

    @Test
    void getQuote() {
        AlphaVantageQuoteResponse response = service.getQuote(IBM_SYMBOL);

        assertEquals(IBM, response);
    }

    @Test
    void getMultipleQuotes() {
        List<AlphaVantageQuoteResponse> responses = service.getMultipleQuotes(List.of(IBM_SYMBOL, VOO_SYMBOL));

        assertIterableEquals(responses, QUOTES);
    }
}