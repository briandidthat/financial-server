package com.elshipper.notificationapi.repository;

import com.elshipper.notificationapi.domain.Cryptocurrency;
import com.elshipper.notificationapi.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository repository;
    private Notification notification1, notification2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        notification1 = new Notification(Cryptocurrency.BTC.getPair(), "42345.22",
                "down", "once", true);
        notification2 = new Notification(Cryptocurrency.FTM.getPair(), "1.34",
                "down", "once", false);
    }

    @Test
    void findByTriggeredTrue() {
        repository.save(notification1);
        repository.save(notification2);

        List<Notification> triggered = repository.findByTriggeredTrue();

        assertEquals(1, triggered.size());
    }

    @Test
    void findNotificationsByAsset() {
        repository.save(notification1);
        repository.save(notification2);

        List<Notification> notifications = repository.findNotificationsByAsset(Cryptocurrency.FTM.getPair());

        assertEquals(1, notifications.size());
    }
}