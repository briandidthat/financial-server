package com.briandidthat.financialserver.util;

import com.briandidthat.financialserver.domain.coinbase.Token;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class RequestUtilities {
    private RequestUtilities() {
    }

    public static boolean validateSymbol(String symbol, List<Token> tokens) {
        for (Token token : tokens) {
            if (symbol.equalsIgnoreCase(token.code())) {
                return true;
            }
        }
        return false;
    }

    public static boolean validateSymbols(List<String> symbols, List<Token> tokens) {
        for (String symbol : symbols) {
            if (!validateSymbol(symbol, tokens)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateStockSymbol(String symbol, Map<String, Boolean> stocks) {
        return stocks.getOrDefault(symbol.toUpperCase(), false);
    }

    public static boolean validateStockSymbols(List<String> symbols, Map<String, Boolean> stocks) {
        for (String symbol : symbols) {
            if (!validateStockSymbol(symbol, stocks)) {
                return false;
            }
        }
        return true;
    }

    public static String formatQueryString(String url, Map<String, Object> params) {
        final StringBuilder builder = new StringBuilder(url + "?");
        final AtomicInteger count = new AtomicInteger();
        final int length = params.size();

        params.forEach((key, value) -> {
            String currentParam = key + "=" + value;
            builder.append(currentParam);
            count.getAndIncrement();
            if (count.get() < length)
                builder.append("&");
        });
        return builder.toString();
    }
}
