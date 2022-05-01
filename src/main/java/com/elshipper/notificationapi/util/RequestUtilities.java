package com.elshipper.notificationapi.util;

import com.elshipper.notificationapi.domain.AssetType;
import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.domain.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for (String ticker : tickers) {
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


    public static Map<String, List<String>> extractSymbols(List<Notification> notifications) {
        final Map<String, List<String>> assets = new HashMap<>();
        List<String> crypto = new ArrayList<>();
        List<String> stocks = new ArrayList<>();
        notifications.forEach((notification -> {
            switch (notification.getAssetType()) {
                case "CRYPTO":
                    crypto.add(notification.getAsset());
                    break;
                case "STOCK":
                    stocks.add(notification.getAsset());
                default:
                    break;
            }
        }));
        assets.put(AssetType.CRYPTO.getType(), crypto);
        assets.put(AssetType.STOCK.getType(), stocks);
        return assets;
    }

}
