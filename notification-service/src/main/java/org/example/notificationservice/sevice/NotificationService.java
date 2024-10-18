package org.example.notificationservice.sevice;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.models.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> getAllNotificationOfUser(UUID userId) {
        return notificationRepository.findAllByReceiverId(userId);
    }
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    public void markNotificationAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public void markAllNotificationAsRead(UUID userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }
}
