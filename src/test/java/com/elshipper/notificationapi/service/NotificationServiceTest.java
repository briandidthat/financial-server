package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.Asset;
import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class NotificationServiceTest {
    private final Notification TO_SAVE = new Notification(Asset.AVAX.getPair(), "54.00", "down", "everytime", false);
    private final Notification AVAX_NOTIFICATION = new Notification(1, Asset.AVAX.getPair(), "54.00", "down", "everytime", false);
    private final Notification BTC_NOTIFICATION = new Notification(2, Asset.BTC.getPair(), "40000.00", "down", "everytime", false);
    private final Notification BNB_NOTIFICATION = new Notification(3, Asset.BTC.getPair(), "380.00", "down", "everytime", true);

    @MockBean
    private NotificationRepository repository;
    @Autowired
    private NotificationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveNotification() {
        when(repository.save(TO_SAVE)).thenReturn(AVAX_NOTIFICATION);

        Notification avaxNotification = service.saveNotification(TO_SAVE);

        assertEquals(avaxNotification, AVAX_NOTIFICATION);
    }

    @Test
    void updateNotification() {
    }

    @Test
    void deleteNotification() {
    }

    @Test
    void findAllNotifications() {
        when(repository.findAll()).thenReturn(List.of(AVAX_NOTIFICATION, BTC_NOTIFICATION));

        List<Notification> notifications = service.findAllNotifications();

        assertEquals(2, notifications.size());
    }

    @Test
    void findNotificationsByTriggered() {
        when(repository.findByTriggeredTrue()).thenReturn(List.of(BNB_NOTIFICATION));

        List<Notification> notifications = service.findNotificationsByTriggered(BNB_NOTIFICATION.getAsset());

        assertEquals(1, notifications.size());
    }

    @Test
    void findNotificationsByAsset() {
        when(repository.findNotificationsByAsset(Asset.AVAX.getPair())).thenReturn(List.of(AVAX_NOTIFICATION));

        List<Notification> notifications = service.findNotificationsByAsset(AVAX_NOTIFICATION.getAsset());

        assertEquals(1, notifications.size());
    }
}