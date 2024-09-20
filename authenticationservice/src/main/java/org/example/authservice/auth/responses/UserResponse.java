package org.example.authservice.auth.responses;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email

) {
}
