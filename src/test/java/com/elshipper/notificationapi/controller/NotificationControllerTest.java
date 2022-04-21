package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    private final Notification TO_SAVE = new Notification(Cryptocurrency.AVAX.getPair(), "54.00", "down", "everytime", false);
    private final Notification AVAX_NOTIFICATION = new Notification(1, Cryptocurrency.AVAX.getPair(), "54.00", "down", "everytime", false);
    private final Notification BTC_NOTIFICATION = new Notification(2, Cryptocurrency.BTC.getPair(), "40000.00", "down", "everytime", false);
    private final Notification BNB_NOTIFICATION = new Notification(3, Cryptocurrency.BNB.getPair(), "380.00", "down", "everytime", true);
    private final Notification BNB_UPDATED = new Notification(3, Cryptocurrency.BNB.getPair(), "380.00", "down", "once", true);

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NotificationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveNotification() {
    }

    @Test
    void getAllNotifications() {
    }

    @Test
    void updateNotification() {
    }

    @Test
    void deleteNotification() {
    }

    @Test
    void getTriggeredNotifications() {
    }

    @Test
    void getNotificationsByAsset() {
    }
}