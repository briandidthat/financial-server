package com.toogroovy.notificationapi.util;

import com.toogroovy.notificationapi.domain.AssetType;
import com.toogroovy.notificationapi.domain.Cryptocurrency;
import com.toogroovy.notificationapi.domain.Notification;
import com.toogroovy.notificationapi.domain.Stock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestUtilitiesTest {

    @Test
    void validateCryptocurrency() {
        boolean validatd = RequestUtilities.validateCryptocurrency(Cryptocurrency.AVAX);
        assertTrue(validatd);
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

    @Test
    void extractSymbols() {
        Notification ibm = new Notification(Stock.IBM.getSymbol(), AssetType.STOCK.getType(),
                "330", "down", "once", false);
        Notification voo = new Notification(Stock.VOO.getSymbol(), AssetType.STOCK.getType(),
                "4200", "down", "once", false);
        Notification btc = new Notification(Cryptocurrency.BTC, AssetType.CRYPTO.getType(),
                "42345.22", "down", "once", true);
        Notification ftm = new Notification(Cryptocurrency.FTM, AssetType.CRYPTO.getType(),
                "1.34", "down", "once", false);


        Map<String, List<String>> expected = new HashMap<>();
        expected.put(AssetType.CRYPTO.getType(), List.of(btc.getAsset(), ftm.getAsset()));
        expected.put(AssetType.STOCK.getType(), List.of(ibm.getAsset(), voo.getAsset()));

        Map<String, List<String>> actual = RequestUtilities.extractSymbols(List.of(btc, ftm, ibm, voo));

        assertEquals(expected, actual);
    }
}