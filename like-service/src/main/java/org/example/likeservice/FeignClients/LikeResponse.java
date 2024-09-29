package org.example.likeservice.FeignClients;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record LikeResponse(
        UUID id,
        User userId,
        LocalDateTime createdAt
) {
}
