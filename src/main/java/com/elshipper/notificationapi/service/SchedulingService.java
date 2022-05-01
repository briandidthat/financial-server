package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.domain.Stock;
import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.repository.NotificationRepository;
import com.elshipper.notificationapi.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulingService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private StockService stockService;

    @PostConstruct
    private void init() {
        Notification btc = new Notification(Cryptocurrency.BTC.getSymbol(), "42345.22", "down", "once", true);
        Notification ftm = new Notification(Cryptocurrency.FTM.getSymbol(), "1.34", "down", "once", false);
        Notification ibm = new Notification(Stock.IBM.getSymbol(), "330", "down", "once", false);
        Notification voo = new Notification(Stock.VOO.getSymbol(), "4200", "down", "once", false);
        notificationRepository.saveAll(List.of(btc, ftm, ibm, voo));
    }

    @Cacheable(value="notifications")
    public List<Notification> getNotifications() {
        return notificationRepository.findByTriggered(false);
    }

    @Scheduled(fixedRate = 60000L)
    public void checkNotifications() {
        final List<Notification> notifications = notificationRepository.findByTriggered(false);
        final Map<String, List<String>> symbols = splitVariables(notifications);
        final List<BinanceTickerResponse> cryptoPrices;
        final List<AlphaVantageQuoteResponse> stockPrices;

        logger.info("Fetching for notifications that should be triggered");
        try {
            logger.info("symbols: {}",symbols);
            cryptoPrices = cryptoService.getTickerPricesAsync(symbols.get("crypto"));
            stockPrices = stockService.getMultipleQuotes(symbols.get("stocks"));
            logger.info("cryptoPrices: {}", cryptoPrices);
            logger.info("stock prices {}", stockPrices);
        } catch (Exception e) {
            logger.info("Exception caught: {}", e.getMessage());
        }
    }


    public Map<String, List<String>> splitVariables(List<Notification> notifications) {
        final Map<String, List<String>> assets = new HashMap<>();
        List<String> crypto = new ArrayList<>();
        List<String> stocks = new ArrayList<>();
        notifications.forEach((notification -> {
            if (RequestUtilities.validateStockSymbol(notification.getAsset())) {
                stocks.add(notification.getAsset());
            } else {
                crypto.add(notification.getAsset());
            }
        }));
        assets.put("crypto", crypto);
        assets.put("stocks", stocks);
        return assets;
    }
}
