package com.elshipper.notificationapi.service;

import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private NotificationRepository repository;

    public Notification findNotification(Integer id) {
        logger.info("Fetching notification for id {}", id);
        Optional<Notification> notification = repository.findById(id);
        return notification.orElse(null);
    }

    public Notification saveNotification(Notification notification) {
        logger.info("Storing new notification for {}", notification.getAsset());
        return repository.save(notification);
    }

    public List<Notification> saveAllNotifications(List<Notification> notifications) {
        logger.info("Storing multiple notifications for {}", notifications.toString());
        return repository.saveAll(notifications);
    }

    public Notification updateNotification(Notification notification) {
        Optional<Notification> fromRepo = repository.findById(notification.getId());
        if (fromRepo.isPresent()) {
            logger.info("Updating notification for {}", notification.getAsset());
            return repository.save(notification);
        }
        return null;
    }

    public boolean deleteNotification(Integer id) {
        Optional<Notification> notification = repository.findById(id);
        if (notification.isPresent()) {
            logger.info("Deleting notification for {}", id);
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Notification> findAllNotifications() {
        logger.info("Fetching all notifications");
        return repository.findAll();
    }

    public List<Notification> findTriggeredNotifications() {
        logger.info("Fetching all triggered notifications");
        return repository.findByTriggered(true);
    }

    public List<Notification> findUntriggeredNotifications() {
        logger.info("Fetching all untriggered notifications");
        return repository.findByTriggered(false);
    }

    public List<Notification> findTriggeredNotificationsForAsset(String asset) {
        logger.info("Fetching triggered notifications for {}", asset);
        final List<Notification> triggered = repository.findByTriggered(true);
        return triggered.stream().filter(f -> f.getAsset().equalsIgnoreCase(asset)).collect(Collectors.toList());
    }

    public List<Notification> findUntriggeredNotificationsForAsset(String asset) {
        logger.info("Fetching untriggered notifications for {}", asset);
        final List<Notification> untriggered = repository.findByTriggered(false);
        return untriggered.stream().filter(f -> f.getAsset().equalsIgnoreCase(asset)).collect(Collectors.toList());
    }

    public List<Notification> findNotificationsByAsset(String asset) {
        logger.info("Fetching all notifications for {}", asset);
        return repository.findByAsset(asset);
    }

}
