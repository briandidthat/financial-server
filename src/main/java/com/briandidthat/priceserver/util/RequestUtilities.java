package com.briandidthat.priceserver.util;

import com.briandidthat.priceserver.domain.coinbase.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static String formatQueryString(String url, Map<String, Object> params) {
        final StringBuilder builder = new StringBuilder(url + "?");
        final Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        int count = 0;

        for (Map.Entry<String, Object> key : entrySet) {
            String currentParam = key.getKey() + "=" + key.getValue();
            builder.append(currentParam);
            count++;
            if (count < entrySet.size())
                builder.append("&");
        }
        return builder.toString();
    }
}
