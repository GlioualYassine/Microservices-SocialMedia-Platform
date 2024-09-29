package org.example.postservice.openFeignClients;

import lombok.Builder;
import org.example.postservice.models.User;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record LikeResponse(
        UUID id,
        User userId,
        LocalDateTime createdAt
) {
}
