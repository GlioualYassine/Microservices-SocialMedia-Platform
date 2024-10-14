package org.example.userservice.openFeign;

import java.util.Set;
import java.util.UUID;

public record UserDTO(
        UUID id ,
        String username,
        String email,
        Set<UUID> friendRequestsSent,
        Set<UUID> friendRequestsReceived
) {
}
