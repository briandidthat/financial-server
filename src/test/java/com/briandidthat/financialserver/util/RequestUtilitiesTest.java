package com.briandidthat.financialserver.util;

import com.briandidthat.financialserver.domain.coinbase.Token;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestUtilitiesTest {
    List<Token> availableTokens = TestingConstants.AVAILABLE_TOKENS;

    @Test
    void testValidateTestingUtilities() {
        boolean validated = RequestUtilities.validateSymbol(TestingConstants.BTC, availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateTestingUtilitiesWithInvalidSymbol() {
        boolean validated = RequestUtilities.validateSymbol("WMEMO", availableTokens);
        assertFalse(validated);
    }

    @Test
    void testValidateCryptocurrencies() {
        boolean validated = RequestUtilities.validateSymbols(List.of(TestingConstants.BTC, TestingConstants.BNB), availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateCryptocurrenciesWithInvalidSymbols() {
        boolean validated = RequestUtilities.validateSymbols(List.of("WMEMO", "MIM"), availableTokens);
        assertFalse(validated);
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
}