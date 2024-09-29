package org.example.likeservice.FeignClients;

import java.util.UUID;

public record User(
        UUID id ,
        String username,
        String email
) {
}
