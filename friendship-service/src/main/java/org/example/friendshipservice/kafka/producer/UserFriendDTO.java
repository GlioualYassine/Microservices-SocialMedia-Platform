package org.example.friendshipservice.kafka.producer;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserFriendDTO(
        UUID userId,
        UUID friendId
) {
}
