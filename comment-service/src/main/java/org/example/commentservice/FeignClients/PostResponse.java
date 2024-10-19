package org.example.commentservice.FeignClients;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
public class PostResponse {

    private UUID id ;
    private String content;
    private String imageUrl;


    private User user;  // Modèle local de l'utilisateur

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    // Liste des IDs de commentaires associés au post

    private List<UUID> commentIds = new ArrayList<>();

    // Liste des IDs de likes associés au post

    private List<UUID> likeIds = new ArrayList<>();
}
