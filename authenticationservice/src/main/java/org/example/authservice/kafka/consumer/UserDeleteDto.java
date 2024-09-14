package org.example.authservice.kafka.consumer;

import java.util.UUID;

public record UserDeleteDto(
        UUID id
) {
}
