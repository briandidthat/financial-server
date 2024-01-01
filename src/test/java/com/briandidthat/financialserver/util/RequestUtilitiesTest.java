package com.briandidthat.financialserver.util;

import com.briandidthat.financialserver.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestUtilitiesTest {
    final Map<String, Boolean> availableTokens = TestingConstants.AVAILABLE_TOKENS;
    final Map<String, Boolean> availableStocks = Map.of("AAPL", true, "GOOG", true);

    @Test
    void testValidateTestingUtilities() {
        boolean validated = RequestUtilities.validateSymbol(TestingConstants.BTC, availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateTestingUtilitiesWithInvalidSymbol() {
        Assertions.assertThrows(ResourceNotFoundException.class, ()->RequestUtilities.validateSymbol("WMEMO", availableTokens));
    }

    @Test
    void testValidateCryptocurrencies() {
        boolean validated = RequestUtilities.validateSymbols(List.of(TestingConstants.BTC, TestingConstants.BNB), availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateCryptocurrenciesWithInvalidSymbols() {
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> RequestUtilities.validateSymbols(List.of("WMEMO", "MIM"), availableTokens));
    }

    @Test
    void formatQueryString() {
        final String baseUrl = "https://dummyurl.com/base";
        final String expectedQueryString = baseUrl + "?param1=param1&param2=param2";
        final LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("param1", "param1");
        params.put("param2", "param2");

        String queryString = RequestUtilities.formatQueryString(baseUrl, params);
        assertEquals(expectedQueryString, queryString);
    }

    @Test
    void testValidateStock() {
        boolean validated = RequestUtilities.validateSymbol("AAPL", availableStocks);
        assertTrue(validated);
    }

    @Test
    void testValidateStockWithInvalidSymbol() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> RequestUtilities.validateSymbol("NVDA", availableStocks));
    }

    @Test
    void testValidateStocks() {
        boolean validated = RequestUtilities.validateSymbols(List.of("AAPL", "GOOG"), availableStocks);
        assertTrue(validated);
    }

    @Test
    void testValidateStocksWithInvalidSymbols() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> RequestUtilities.validateSymbols(List.of("NVDA", "AAPL"), availableStocks));
    }
}