package com.elshipper.notificationapi.util;

import com.elshipper.notificationapi.domain.Asset;

import java.util.List;

public class RequestUtilities {
    private RequestUtilities() {}

    public static boolean validateAssets(List<String> tickers) {
        int count = 0;
        for (String ticker : tickers) {
            if (validateAsset(ticker)) {
                count++;
            }
        }
        return count == tickers.size();
    }

    public static boolean validateAsset(String ticker) {
        for (Asset asset : Asset.values()) {
            if (asset.getPair().equalsIgnoreCase(ticker)) {
                return true;
            }
        }
        return false;
    }
}
