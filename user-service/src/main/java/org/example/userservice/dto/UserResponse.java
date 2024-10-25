package org.example.userservice.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Builder
@Data
public class UserResponse {
    private UUID id ;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    private Date birthDate;
    private String imageUrl;


    LocalDateTime createdAt;

    LocalDateTime updatedAt;


    private List<UUID> friends = new ArrayList<>();

    private List<UUID> friendsRequestSent = new ArrayList<>();

    private List<UUID> friendsRequestReceived = new ArrayList<>();
}
