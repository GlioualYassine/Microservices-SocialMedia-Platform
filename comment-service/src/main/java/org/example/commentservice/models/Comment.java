package org.example.commentservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID postId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private UUID userId;  // ID de l'utilisateur ayant ajouté le commentaire

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
