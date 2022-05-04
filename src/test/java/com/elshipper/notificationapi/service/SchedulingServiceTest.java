package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.AssetType;
import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.domain.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;

@SpringBootTest
class SchedulingServiceTest {
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private CryptoService cryptoService;
    @MockBean
    private StockService stockService;
    @Autowired
    private SchedulingService schedulingService;

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
        when(notificationService.findTriggeredNotifications()).thenReturn(List.of(btc, ftm, ibm, voo));
    }

}