package org.example.postservice.openFeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.example.postservice.models.Comment;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "comment-service"  , url = "http://localhost:8087/api/v1")
public interface CommentServiceClient {
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable UUID commentId) ;

    @GetMapping("/comments/post/{postId}")
    List<CommentResponse> getCommentsByPostId(@PathVariable UUID postId);
}
