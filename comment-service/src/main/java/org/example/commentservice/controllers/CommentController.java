package org.example.commentservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.commentservice.FeignClients.CommentResponse;
import org.example.commentservice.models.Comment;
import org.example.commentservice.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Ajouter un commentaire à un post
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @RequestParam String content,
            @RequestParam UUID postId,
            @RequestParam UUID userId
    ) {
        CommentResponse newComment = commentService.addComment(content, postId, userId);
        return ResponseEntity.ok(newComment);
    }

    // Récupérer tous les commentaires d'un post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable UUID postId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable UUID commentId)
    {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID commentId, @RequestParam String content) {
        Comment updatedComment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(updatedComment);
    }

    // Supprimer un commentaire par ID
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // Supprimer tous les commentaires d'un post
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deleteCommentsByPostId(@PathVariable UUID postId) {
        commentService.deleteCommentsByPostId(postId);
        return ResponseEntity.noContent().build();
    }
}
