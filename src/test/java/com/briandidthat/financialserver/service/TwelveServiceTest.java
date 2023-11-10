package com.briandidthat.financialserver.service;

import com.briandidthat.financialserver.domain.twelve.TwelveResponse;
import com.briandidthat.financialserver.util.RequestUtilities;
import com.briandidthat.financialserver.util.TestingConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
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

    @BeforeEach
    void setUp() {
        final String twelveBaseUrl = "https://api.twelvedata.com";
        final String mockApiKey = "ABCDEFG";

        final Map<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", "AAPL");
        params.put("apikey", mockApiKey);
        final String URL = RequestUtilities.formatQueryString(twelveBaseUrl + "/price", params);

        when(restTemplate.getForObject(URL, TwelveResponse.class)).thenReturn(TestingConstants.APPLE_PRICE_RESPONSE);

        ReflectionTestUtils.setField(service, "twelveBaseUrl", twelveBaseUrl);
        ReflectionTestUtils.setField(service, "twelveApiKey", mockApiKey);
        ReflectionTestUtils.setField(service, "availableStocks", TestingConstants.AVAILABLE_STOCKS);
    }

    @Test
    void getStockPrice() {
        TwelveResponse response = service.getStockPrice("AAPL");
        assertEquals(TestingConstants.APPLE_PRICE_RESPONSE, response);
    }
}