package org.example.userservice.services;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
public class UserFriendsResponseList {
    private UUID id ;
    private String username;
    List <FriendModel> friends= new ArrayList<>();
}
