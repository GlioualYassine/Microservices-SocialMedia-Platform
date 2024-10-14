package org.example.userservice.services;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter @Setter
public class FriendModel {
    private UUID id;
    private String username;
    private String email;
}
