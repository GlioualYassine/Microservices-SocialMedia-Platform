package org.example.friendshipservice.mapper;



import org.example.friendshipservice.dtos.UserDTO;
import org.example.friendshipservice.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .friendRequestsSent(user.getFriendRequestsSent().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .friendRequestsReceived(user.getFriendRequestsReceived().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}