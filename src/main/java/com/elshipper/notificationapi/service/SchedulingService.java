package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.AssetType;
import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.domain.Stock;
import com.elshipper.notificationapi.domain.rest.AlphaVantageQuoteResponse;
import com.elshipper.notificationapi.domain.rest.AssetResponse;
import com.elshipper.notificationapi.domain.rest.BinanceTickerResponse;
import com.elshipper.notificationapi.util.RequestUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class SchedulingService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private StockService stockService;

    @PostConstruct
    private void init() {
        Notification btc = new Notification(Cryptocurrency.BTC.getSymbol(), AssetType.CRYPTO.getType(),
                "42345.22", "down", "once", false);
        Notification ftm = new Notification(Cryptocurrency.FTM.getSymbol(), AssetType.CRYPTO.getType(),
                "1.34", "down", "once", false);
        Notification ibm = new Notification(Stock.IBM.getSymbol(), AssetType.STOCK.getType(),
                "330", "down", "once", false);
        Notification voo = new Notification(Stock.VOO.getSymbol(), AssetType.STOCK.getType(),
                "4200", "down", "once", false);
        notificationService.saveAllNotifications(List.of(btc, ftm, ibm, voo));
    }

    @Cacheable(value="notifications")
    public List<Notification> getNotifications() {
        return notificationService.findTriggeredNotifications();
    }

    @Scheduled(fixedRate = 60000L)
    public void run() {
        final List<Notification> notifications = notificationService.findUntriggeredNotifications();
        final Map<String, List<String>> symbols = RequestUtilities.extractSymbols(notifications);
        final List<BinanceTickerResponse> cryptoPrices;
        final List<AlphaVantageQuoteResponse> stockPrices;

        logger.info("Fetching for notifications that should be triggered");
        try {
            logger.info("symbols: {}",symbols);
            cryptoPrices = cryptoService.getTickerPricesAsync(symbols.get(AssetType.CRYPTO.getType()));
            stockPrices = stockService.getMultipleQuotes(symbols.get(AssetType.STOCK.getType()));



            logger.info("cryptoPrices: {}", cryptoPrices);
            logger.info("stock prices {}", stockPrices);
        } catch (Exception e) {
            logger.info("Exception caught: {}", e.getMessage());
        }
    }
}
