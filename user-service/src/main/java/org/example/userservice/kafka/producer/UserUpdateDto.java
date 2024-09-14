package org.example.userservice.kafka.producer;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserUpdateDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
