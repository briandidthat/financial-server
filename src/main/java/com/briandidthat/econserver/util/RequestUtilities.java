package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class RequestUtilities {
    private RequestUtilities() {}

    public static boolean validateSymbol(String symbol, Map<String, Boolean> assets) throws ResourceNotFoundException {
        boolean isValid = assets.getOrDefault(symbol.toUpperCase(), false);
        if (!isValid) throw new ResourceNotFoundException("Invalid symbol: " + symbol);
        return true;
    }

    public static boolean validateSymbols(List<String> symbols, Map<String, Boolean> assets) throws ResourceNotFoundException {
        for (String symbol : symbols) {
            if (!validateSymbol(symbol, assets)) {
                return false;
            }
        }
        return true;
    }

    public static String formatQueryString(String url, Map<String, Object> params) {
        final int length = params.size();
        final AtomicInteger count = new AtomicInteger();
        final StringBuilder builder = new StringBuilder(url);

        if (!params.isEmpty()) {
            builder.append("?");
            params.forEach((key, value) -> {
                String currentParam = key + "=" + value;
                builder.append(currentParam);
                count.getAndIncrement();
                if (count.get() < length)
                    builder.append("&");
            });
        }
        return builder.toString();
    }
}
