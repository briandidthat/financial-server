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

    public List<Notification> findNotificationsByTriggered(String asset) {
        logger.info("Fetching triggered notifications for {}", asset);
        List<Notification> triggered = repository.findByTriggeredTrue();
        return triggered.stream().filter(f -> f.getAsset().equalsIgnoreCase(asset)).collect(Collectors.toList());
    }

    public List<Notification> findNotificationsByAsset(String asset) {
        logger.info("Fetching all notifications for {}", asset);
        return repository.findNotificationsByAsset(asset);
    }

}
