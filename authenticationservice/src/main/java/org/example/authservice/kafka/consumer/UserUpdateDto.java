package org.example.authservice.kafka.consumer;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
