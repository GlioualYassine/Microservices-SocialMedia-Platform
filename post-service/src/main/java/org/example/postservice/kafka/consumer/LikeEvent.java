package org.example.postservice.kafka.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeEvent (
        UUID id,
        UUID userId,
        UUID postId,
        String eventType,
        LocalDateTime createdAt
){
}
