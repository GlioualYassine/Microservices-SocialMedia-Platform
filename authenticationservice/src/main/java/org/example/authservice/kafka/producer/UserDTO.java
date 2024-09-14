package org.example.authservice.kafka.producer;

import lombok.Builder;

import java.util.UUID;
 @Builder
public record UserDTO(
         UUID id ,
         String firstName,
         String lastName,
         String username,
         String email
) {
}
