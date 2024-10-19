package org.example.notificationservice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.models.Notification;
import org.example.notificationservice.notification.WebSocketService;
import org.example.notificationservice.sevice.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final NotificationService notificationService;
    private final WebSocketService webSocketService;

    @KafkaListener(topics = "notification-websocket-topic")
    public void consumeNotification(NotificationDTO notif) {

        Notification notification = new Notification();
        notification.setId(notif.id());
        notification.setSenderId(notif.senderId());
        notification.setSenderName(notif.senderName());
        notification.setReceiverId(notif.receiverId());
        notification.setReceiverName(notif.receiverName());
        notification.setMessage(notif.message());
        notification.setCreatedAt(notif.createdAt());
        notification.setRead(notif.isRead());
        notification.setType(notif.type());

        notificationService.saveNotification(notification);
        webSocketService.sendNotification(notification);
    }
}
