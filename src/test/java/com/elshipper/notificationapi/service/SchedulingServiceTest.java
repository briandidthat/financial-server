package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SchedulingServiceTest {
    @MockBean
    private NotificationRepository notificationRepository;
    @MockBean
    private CryptoService cryptoService;
    @MockBean
    private StockService stockService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void checkNotifications() {
    }

    @Test
    void splitVariables() {
    }
}