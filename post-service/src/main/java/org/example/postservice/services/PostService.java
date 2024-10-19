package org.example.postservice.services;

import lombok.RequiredArgsConstructor;

import org.example.postservice.Response.PostResponse;
import org.example.postservice.file.FileStorageService;
import org.example.postservice.file.FileUtils;
import org.example.postservice.models.Comment;
import org.example.postservice.models.Post;
import org.example.postservice.models.User;
import org.example.postservice.openFeignClients.CommentResponse;
import org.example.postservice.openFeignClients.CommentServiceClient;
import org.example.postservice.openFeignClients.LikeServiceClient;
import org.example.postservice.openFeignClients.UserServiceClient;
import org.example.postservice.repositories.PostRespository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileStorageService fileStorageService;
    private final SimpMessagingTemplate messagingTemplate;


    public PostResponse createPost(String content, UUID userId, MultipartFile file) {
        User user = userServiceClient.getUserById(userId);
        Post post = Post.builder()
                .content(content)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        post = postRespository.save(post);

        if (file != null) {
            uploadPostImage(post.getId(), file);
        }

        // Convertir le post en une réponse que le front-end peut utiliser
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .user(user)
                .comments(commentServiceClient.getCommentsByPostId(post.getId()))
                .likes(likeServiceClient.getLikesByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        if (post.getImageUrl() != null)
            postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));

        // Envoyer le message au front-end
        //messagingTemplate.convertAndSend("/topic/posts", postResponse);

        return postResponse;
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
                    if (post.getImageUrl() != null)
                        postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));
                    postResponses.add(postResponse);
                }
        );
        return postResponses;
    }

    public List<PostResponse> getPosts(){
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
                    if (post.getImageUrl() != null)
                        postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));

                    postResponses.add(postResponse);
                }
        );
        return postResponses;
    }
    public PostResponse updatePost(UUID postId, String content){
        Post post = postRespository.findById(postId).orElseThrow();
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
       post =   postRespository.save(post);
       PostResponse postResponse = PostResponse.builder()
               .id(post.getId())
               .content(post.getContent())
               .user(User.builder()
                       .id(post.getUser().getId())
                       .username(post.getUser().getUsername())
                       .email(post.getUser().getEmail())
                       .build())
               .comments(commentServiceClient.getCommentsByPostId(post.getId()))
               .likes(likeServiceClient.getLikesByPostId(post.getId()))
               .createdAt(post.getCreatedAt())
               .updatedAt(post.getUpdatedAt())
               .build();
       if (post.getImageUrl() != null)
           postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));
        return postResponse;


    }

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
                    if (post.getImageUrl() != null)
                        postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));


                    postResponses.add(postResponse);
                }
        );
        return postResponses;
    }

    public void uploadPostImage(UUID postId, MultipartFile file) {
        Post post = postRespository.findById(postId).orElse(null);
        if(post == null)
            return ;
        var postPicture = fileStorageService.saveFile(file,post.getUser().getId());
        post.setImageUrl(postPicture);
        postRespository.save(post);
    }

    public PostResponse getPostById(UUID postId) {
        Post post =  postRespository.findById(postId).orElse(null);
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .user(post.getUser())
                .comments(commentServiceClient.getCommentsByPostId(post.getId()))
                .likes(likeServiceClient.getLikesByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        if (post.getImageUrl() != null)
            postResponse.setImage(FileUtils.readFileFromLocation(post.getImageUrl()));
        return  postResponse;
    }
}
