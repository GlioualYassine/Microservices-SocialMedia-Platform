package org.example.notificationservice.models;

import java.util.Date;
import java.util.UUID;

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
