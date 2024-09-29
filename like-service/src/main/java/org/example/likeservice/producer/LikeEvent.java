package org.example.likeservice.producer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record LikeEvent(
        UUID id,
        UUID userId,
        UUID postId,
        String eventType,
        LocalDateTime createdAt
) {
}
