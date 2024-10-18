package org.example.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.models.Notification;
import org.example.notificationservice.sevice.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/all")
    public List<Notification> getAllNotification() {
        return notificationService.getAllNotification();
    }
    @GetMapping("/all/{userId}")
    public List<Notification> getAllNotificationOfUser(@PathVariable UUID userId) {
        return notificationService.getAllNotificationOfUser(userId);
    }

    @PostMapping("save")
    public ResponseEntity<Notification> saveNotification(Notification notification) {
        return ResponseEntity.ok(notificationService.saveNotification(notification));
    }

    @PutMapping("/markAsRead/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead( @PathVariable UUID notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markAllAsRead/{userId}")
    public ResponseEntity<Void> markAllNotificationAsRead( @PathVariable UUID userId) {
        notificationService.markAllNotificationAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
