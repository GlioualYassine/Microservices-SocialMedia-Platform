package org.example.postservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Comment {

    private UUID id;
    private String content;
    private User user;
    private LocalDateTime createdAt;
    private Post post;

}