package com.elshipper.notificationapi.util;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Stock;

import java.util.List;

public class RequestUtilities {
    private RequestUtilities() {}
    public static boolean validateCryptocurrency(String ticker) {
        for (Cryptocurrency cryptocurrency : Cryptocurrency.values()) {
            if (cryptocurrency.getSymbol().equalsIgnoreCase(ticker)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateCryptocurrencies(List<String> tickers) {
        int count = 0;
        for (String ticker : tickers) {
            if (validateCryptocurrency(ticker)) {
                count++;
            }
        }
        return count != tickers.size();
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
        int counter = 0;
        for (String symbol : symbols) {
            if (validateStockSymbol(symbol)) {
                counter++;
            }
        }
        return counter == symbols.size();
    }

}
