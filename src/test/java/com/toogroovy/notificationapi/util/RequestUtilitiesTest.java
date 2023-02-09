package com.toogroovy.notificationapi.util;

import com.toogroovy.notificationapi.domain.Cryptocurrency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestUtilitiesTest {

    @Test
    void validateCryptocurrency() {
        boolean validated = RequestUtilities.validateCryptocurrency(Cryptocurrency.AVAX);
        assertTrue(validated);
    }

    @Test
    void validateCryptocurrencies() {
        boolean validated = RequestUtilities.validateCryptocurrencies(List.of(Cryptocurrency.BTC, Cryptocurrency.BNB));
        assertTrue(validated);
    }

    @Test
    void validateStockSymbol() {
        boolean validated = RequestUtilities.validateStockSymbol(Stock.APPLE.getSymbol());
        assertTrue(validated);
    }

    @Test
    void validateStockSymbols() {
        boolean validated = RequestUtilities.validateStockSymbols(List.of(Stock.APPLE.getSymbol(), Stock.IBM.getSymbol()));
        assertTrue(validated);
    }
}