package org.example.likeservice.service;


import lombok.RequiredArgsConstructor;

import org.example.likeservice.FeignClients.*;
import org.example.likeservice.models.Like;
import org.example.likeservice.producer.LikeEvent;
import org.example.likeservice.producer.LikeProducer;
import org.example.likeservice.producer.notifications.NotificationDTO;
import org.example.likeservice.producer.notifications.NotificationProducer;
import org.example.likeservice.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final LikeProducer likeProducer;
    private final UserServiceClient userServiceClient;
    private final PostServiceClient postServiceClient;
    private  final NotificationProducer notificationProducer;
    public Like addLike(UUID postId, UUID userId) {
        Like like = Like
                .builder()
                .id(UUID.randomUUID())
                .postId(postId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        LikeEvent likeEvent = LikeEvent
                .builder()
                .id(like.getId())
                .postId(like.getPostId())
                .userId(like.getUserId())
                .eventType("LIKE")
                .createdAt(like.getCreatedAt())
                .build();
        likeProducer.sendCommentEventToPostSeviceToUpdateLikesList(likeEvent);
        PostResponse post = postServiceClient.getPostById(postId);
        User user = post.getUser() ;
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .id(UUID.randomUUID())
                .senderId(userId)
                .receiverId(user.getId())
                .message("A user liked your post")
                .createdAt(new Date())
                .build();
        notificationProducer.sendNotification(notificationDTO);
        return likeRepository.save(like);
    }

    // Récupérer tous les commentaires associés à un post
    public List<LikeResponse> getLikesByPostId(UUID postId) {
        List<Like> likes =  likeRepository.findByPostId(postId);
        List<LikeResponse> likeResponses = new ArrayList<>();
        likes.forEach(like -> {
            LikeResponse likeResponse = LikeResponse.builder()
                    .id(like.getId())
                    .userId(userServiceClient.getUserById(like.getUserId()))
                    .createdAt(like.getCreatedAt())
                    .build();
            likeResponses.add(likeResponse);
        });
        return likeResponses;
    }

//    public void deleteCommentsByPostId(UUID postId) {
//        List<Comment> comments = commentRepository.findByPostId(postId);
//        commentProducer.sendCommentEventToPostSeviceToUpdateCommentsList(
//                CommentEvent.builder()
//                        .postId(postId)
//                        .eventType("DELETE_ALL")
//                        .build()
//        );
//        commentRepository.deleteAll(comments);
//    }
    public void deleteLike(UUID likeId) {
        Like comment = likeRepository.findById(likeId).orElse(null);
        likeProducer.sendCommentEventToPostSeviceToUpdateLikesList(
                LikeEvent.builder()
                        .id(likeId)
                        .postId(comment.getPostId())
                        .eventType("DISLIKE")
                        .build()
        );
        likeRepository.deleteById(likeId);
    }

    public Like getlikeById(UUID likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }


}
