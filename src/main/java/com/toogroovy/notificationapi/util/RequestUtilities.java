package com.toogroovy.notificationapi.util;

import java.util.List;

public final class RequestUtilities {
    private RequestUtilities() {}
    public static boolean validateCryptocurrency(String ticker) {
        return false;
    }

    public static boolean validateCryptocurrencies(List<String> symbols) {
        for (String ticker : symbols) {
            if (!validateCryptocurrency(ticker)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateStockSymbol(String symbol) {
        for (Stock stock : Stock.values()) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateStockSymbols(List<String> symbols) {
        for (String symbol : symbols) {
            if (!validateStockSymbol(symbol)) {
                return false;
            }
        }
        return true;
    }

}
