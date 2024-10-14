package org.example.friendshipservice.kafka.consumer;

import java.util.UUID;

public record UserDTOConsumer(
        UUID id ,
        String firstName,
        String lastName,
        String username,
        String email
) {
}
