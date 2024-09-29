package org.example.commentservice.kafka.producer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CommentEvent(
        UUID id,
        String content,
        UUID userId,
        UUID postId,
        String eventType,
        LocalDateTime createdAt
) {
}
