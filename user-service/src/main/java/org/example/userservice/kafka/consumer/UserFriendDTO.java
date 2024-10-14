package org.example.userservice.kafka.consumer;

import java.util.UUID;

public record UserFriendDTO(
        UUID userId,
        UUID friendId
) {
}
