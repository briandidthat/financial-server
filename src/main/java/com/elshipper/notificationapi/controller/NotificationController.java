package com.elshipper.notificationapi.controller;

import com.elshipper.notificationapi.domain.Notification;
import com.elshipper.notificationapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification saveNotification(@RequestBody @Validated Notification notification) {
        return service.saveNotification(notification);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getAllNotifications() {
        return service.findAllNotifications();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification updateNotification(@PathVariable Integer id, @RequestBody Notification notification) {
        if (!notification.getId().equals(id)) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Path and Object id must match");
        }
        return service.updateNotification(notification);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@PathVariable Integer id) {
        boolean deleted = service.deleteNotification(id);
        if (!deleted) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid notification id");
        }
    }

    @GetMapping("/triggered")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getTriggeredNotifications(@RequestParam String asset) {
        List<Notification> notifications = service.findNotificationsByTriggered(asset);
        if (notifications.size() == 0)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No triggered notifications for that asset");
        return notifications;
    }

    @GetMapping("/asset/")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getNotificationsByAsset(@RequestParam String asset) {
        List<Notification> notifications = service.findNotificationsByAsset(asset);
        if (notifications.size() == 0)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No noitifications set for that asset");
        return notifications;
    }
}
