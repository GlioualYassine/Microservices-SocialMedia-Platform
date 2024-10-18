package org.example.friendshipservice.kafka.producer.notifications;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@ToString @Builder
public class Notification {
    private UUID id;
    private UUID senderId;
    private String senderName;
    private UUID receiverId;
    private String receiverName;
    private String message;
    private Date createdAt;
    private boolean isRead;
    private NotificationType type;
}
