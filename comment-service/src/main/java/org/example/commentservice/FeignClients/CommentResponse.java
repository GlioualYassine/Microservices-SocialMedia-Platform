package org.example.commentservice.FeignClients;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record CommentResponse(
        UUID id,
        String content,
        User userId,
        LocalDateTime createdAt
) {
}
