package com.toogroovy.priceserver.util;

import com.toogroovy.priceserver.domain.Cryptocurrency;
import com.toogroovy.priceserver.domain.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestUtilitiesTest {
    private static final List<Token> availableTokens = List.of(
            new Token("AVAX", "Avalanche","red", 1, 8, "crypto", "dsfas", "23df"),
            new Token( "BTC", "Bitcoin", "orange", 2, 8, "crypto", "dsfas", "23df"),
            new Token("BNB","Binance Coin", "gold", 3, 8, "crypto", "dsfas", "23df"));

    @Test
    void testValidateCryptocurrency() {
        boolean validated = RequestUtilities.validateCryptocurrency(Cryptocurrency.AVAX, availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateCryptocurrencyWithInvalidSymbol() {
        boolean validated = RequestUtilities.validateCryptocurrency("WMEMO", availableTokens);
        assertFalse(validated);
    }

    @Test
    void testValidateCryptocurrencies() {
        boolean validated = RequestUtilities.validateCryptocurrencies(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB), availableTokens);
        assertTrue(validated);
    }

    @Test
    void testValidateCryptocurrenciesWithInvalidSymbols() {
        boolean validated = RequestUtilities.validateCryptocurrencies(List.of("WMEMO", "MIM"), availableTokens);
        assertFalse(validated);
    }

}