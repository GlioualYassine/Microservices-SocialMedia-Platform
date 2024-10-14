package org.example.friendshipservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String username;
    private String email;

    // UUIDs des utilisateurs pour les relations
    private Set<UUID> friendRequestsSent;
    private Set<UUID> friendRequestsReceived;
}
