package org.example.authservice.kafka.producer;

import java.util.UUID;

public record UserEvent(
         UUID userId,
         String firstName,
         String lastName,
         String email,
         String status // "online" ou "offline"
) {
}
