package org.example.userservice.kafka.producer;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserDeleteDto(
        UUID id
) {
}
