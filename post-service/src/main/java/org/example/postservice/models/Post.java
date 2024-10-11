package org.example.postservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    private UUID id ;
    @Column(nullable = false)
    private String content;
    private String imageUrl;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "user_id")),
            @AttributeOverride(name = "username", column = @Column(name = "user_username")),
            @AttributeOverride(name = "email", column = @Column(name = "user_email"))
    })
    private User user;  // Modèle local de l'utilisateur

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    // Liste des IDs de commentaires associés au post
    @ElementCollection
    @CollectionTable(name = "post_comment_ids", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "comment_id")
    private List<UUID> commentIds = new ArrayList<>();

    // Liste des IDs de likes associés au post
    @ElementCollection
    @CollectionTable(name = "post_like_ids", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "like_id")
    private List<UUID> likeIds = new ArrayList<>();



}
