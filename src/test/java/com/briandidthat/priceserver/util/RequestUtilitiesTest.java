package com.briandidthat.priceserver.util;

import com.briandidthat.priceserver.domain.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestUtilitiesTest {
    List<Token> availableTokens = TestingConstants.getAvailableTokens();

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

}