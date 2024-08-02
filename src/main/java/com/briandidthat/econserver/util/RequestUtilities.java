package com.briandidthat.econserver.util;

import com.briandidthat.econserver.domain.AssetPrice;
import com.briandidthat.econserver.domain.coinbase.SpotPriceResponse;
import com.briandidthat.econserver.domain.exception.ResourceNotFoundException;
import com.briandidthat.econserver.domain.twelve.StockPriceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class RequestUtilities {
    private RequestUtilities() {
    }

    public static synchronized boolean validateSymbol(String symbol, Map<String, Boolean> assets) throws ResourceNotFoundException {
        boolean isValid = assets.getOrDefault(symbol.toUpperCase(), false);
        if (!isValid) throw new ResourceNotFoundException("Invalid symbol: " + symbol);
        return true;
    }

    public static synchronized boolean validateSymbols(List<String> symbols, Map<String, Boolean> assets) throws ResourceNotFoundException {
        for (String symbol : symbols) {
            if (!validateSymbol(symbol, assets)) {
                return false;
            }
        }
        return true;
    }

    public static synchronized String formatQueryString(String url, Map<String, Object> params) {
        final int length = params.size();
        final AtomicInteger count = new AtomicInteger();
        final StringBuilder builder = new StringBuilder(url);

        if (!params.isEmpty()) {
            builder.append("?");
            params.forEach((key, value) -> {
                String currentParam = key + "=" + value;
                builder.append(currentParam);
                count.getAndIncrement();
                if (count.get() < length) builder.append("&");
            });
        }
        return builder.toString();
    }

    public static synchronized AssetPrice buildAssetPrice(SpotPriceResponse response) {
        LocalDate date = response.getDate() == null ? LocalDate.now() : response.getDate();
        return new AssetPrice(response.getBase(), response.getAmount(), date);
    }

    public static synchronized AssetPrice buildAssetPrice(String symbol, StockPriceResponse response) {
        return new AssetPrice(symbol, response.getPrice(), LocalDate.now());
    }

}
