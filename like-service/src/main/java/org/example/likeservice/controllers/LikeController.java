package org.example.likeservice.controllers;

import lombok.RequiredArgsConstructor;

import org.example.likeservice.FeignClients.LikeResponse;
import org.example.likeservice.models.Like;
import org.example.likeservice.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // Ajouter un commentaire à un post
    @PostMapping
    public ResponseEntity<Like> addLike(
            @RequestParam UUID postId,
            @RequestParam UUID userId
    ) {
        Like newLike = likeService.addLike(postId, userId);
        return ResponseEntity.ok(newLike);
    }

    // Récupérer tous les likes d'un post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeResponse>> getLikesByPostId(@PathVariable UUID postId) {
        List<LikeResponse> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<Like> getLikeById(@PathVariable UUID likeId)
    {
        Like comment = likeService.getlikeById(likeId);
        return ResponseEntity.ok(comment);
    }



    // Supprimer un commentaire par ID
    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID likeId) {
        likeService.deleteLike(likeId);
        return ResponseEntity.noContent().build();
    }

//    // Supprimer tous les commentaires d'un post
//    @DeleteMapping("/post/{postId}")
//    public ResponseEntity<Void> deleteCommentsByPostId(@PathVariable UUID postId) {
//        commentService.deleteCommentsByPostId(postId);
//        return ResponseEntity.noContent().build();
//    }
}
