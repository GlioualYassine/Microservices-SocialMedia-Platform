package org.example.likeservice.FeignClients;

import lombok.Getter;

import java.util.UUID;

public record User(
        UUID id ,
        String username,
        String email
) {
    public UUID getId() {
        return id;
    }
}
