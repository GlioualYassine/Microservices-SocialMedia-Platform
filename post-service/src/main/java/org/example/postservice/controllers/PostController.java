package org.example.postservice.controllers;

import org.example.postservice.Response.PostResponse;
import org.example.postservice.models.Post;
import org.example.postservice.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<Post> createPost(@RequestParam String content, @RequestParam UUID userId , @RequestParam("file") MultipartFile file) {
        Post post = postService.createPost(content, userId,file);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PostMapping(value = "/post/{postId}" , consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPostImage(
            @PathVariable("postId") UUID postId,
            @RequestParam("file") MultipartFile file
    ){
        postService.uploadPostImage(postId, file);
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/user/{userId}")
    public List<PostResponse> getPostsByUser(@PathVariable UUID userId) {
        return postService.getPostsByUser(userId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable UUID postId, @RequestParam String content) {
        PostResponse updatedPost = postService.updatePost(postId, content);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
