package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.AssetType;
import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.domain.Stock;
import com.elshipper.notificationapi.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchedulingServiceTest {
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private CryptoService cryptoService;
    @MockBean
    private StockService stockService;

    private final Notification btc = new Notification(Cryptocurrency.BTC.getSymbol(), AssetType.CRYPTO.getType(),
            "42345.22", "down", "once", true);
    private final Notification ftm = new Notification(Cryptocurrency.FTM.getSymbol(), AssetType.CRYPTO.getType(),
            "1.34", "down", "once", false);
    private final Notification ibm = new Notification(Stock.IBM.getSymbol(), AssetType.STOCK.getType(),
            "330", "down", "once", false);
    private final Notification voo = new Notification(Stock.VOO.getSymbol(), AssetType.STOCK.getType(),
            "4200", "down", "once", false);

    @BeforeEach
    void setUp() {
    }

    @Test
    void checkNotifications() {

    }
}