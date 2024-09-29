package org.example.commentservice.FeignClients;

import java.util.UUID;

public record User(
        UUID id ,
        String username,
        String email
) {
}
