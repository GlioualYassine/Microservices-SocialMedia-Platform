package org.example.notificationservice.repository;

import org.example.notificationservice.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification , UUID> {
    List<Notification> findAllByReceiverId(UUID userId);
}
