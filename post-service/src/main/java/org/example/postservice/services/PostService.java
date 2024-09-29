package org.example.postservice.services;

import lombok.RequiredArgsConstructor;
import org.example.postservice.Response.PostResponse;
import org.example.postservice.models.Comment;
import org.example.postservice.models.Post;
import org.example.postservice.models.User;
import org.example.postservice.openFeignClients.CommentResponse;
import org.example.postservice.openFeignClients.CommentServiceClient;
import org.example.postservice.openFeignClients.LikeServiceClient;
import org.example.postservice.openFeignClients.UserServiceClient;
import org.example.postservice.repositories.PostRespository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRespository postRespository;
    private final UserServiceClient userServiceClient;
    private final CommentServiceClient commentServiceClient;
    private final LikeServiceClient likeServiceClient;
    public Post createPost(String content , UUID userId){

        User user = userServiceClient.getUserById(userId);
        Post post = Post.builder()
                .content(content)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return postRespository.save(post);
    }

    public List<PostResponse> getPostsByUser(UUID userId){
        List<Post> posts =  postRespository.findByUserId(userId);
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach(
                post -> {
                    User user = User.builder()
                            .id(post.getUser().getId())
                            .username(post.getUser().getUsername())
                            .email(post.getUser().getEmail())
                            .build();

                    PostResponse postResponse = PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .user(user)
                            .comments(commentServiceClient.getCommentsByPostId(post.getId()))
                            .likes(likeServiceClient.getLikesByPostId(post.getId()))
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .build();
                    postResponses.add(postResponse);
                }
        );
        return postResponses;
    }

    public Post updatePost(UUID postId, String content){
        Post post = postRespository.findById(postId).orElseThrow();
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
       return  postRespository.save(post);}
    public void deletePost(UUID postId){
        postRespository.deleteById(postId);
    }

    public List<PostResponse> getAllPosts() {
        List<Post> posts =  postRespository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach(
                post -> {
                    User user = User.builder()
                            .id(post.getUser().getId())
                            .username(post.getUser().getUsername())
                            .email(post.getUser().getEmail())
                            .build();
                    PostResponse postResponse = PostResponse.builder()
                            .id(post.getId())
                            .content(post.getContent())
                            .user(user)
                            .comments(commentServiceClient.getCommentsByPostId(post.getId()))
                            .likes(likeServiceClient.getLikesByPostId(post.getId()))
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .build();



                    postResponses.add(postResponse);
                }
        );
        return postResponses;
    }
}
