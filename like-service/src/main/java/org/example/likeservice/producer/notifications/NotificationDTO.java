package org.example.likeservice.producer.notifications;


import lombok.Builder;

import java.util.Date;
import java.util.UUID;
@Builder
public record NotificationDTO(
        UUID id ,
        UUID senderId,
        String senderName,
        UUID receiverId,
        String receiverName,
        String message,
        boolean isRead,
        NotificationType type,
        Date createdAt

) {
}
