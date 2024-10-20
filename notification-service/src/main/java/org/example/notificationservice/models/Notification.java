package org.example.notificationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@ToString
@Entity
public class Notification {
    @Id
    private UUID id;
    private UUID senderId;
    private String senderName;
    private UUID receiverId;
    private String receiverName;
    private String message;
    private Date createdAt;
    private boolean isRead;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
