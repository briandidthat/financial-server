package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationServiceTest {

    @MockBean
    private NotificationRepository repository;
    @Autowired
    private NotificationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveNotification() {
    }

    @Test
    void updateNotification() {
    }

    @Test
    void deleteNotification() {
    }

    @Test
    void findAllNotifications() {
    }

    @Test
    void findNotificationsByTriggered() {
    }

    @Test
    void findNotificationsByAsset() {
    }
}