package org.example.postservice.kafka.consumer;

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
    @Override
    public String toString() {
        return "CommentEvent{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
