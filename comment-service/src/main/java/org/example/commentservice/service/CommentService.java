package org.example.commentservice.service;


import lombok.RequiredArgsConstructor;
import org.example.commentservice.FeignClients.CommentResponse;
import org.example.commentservice.FeignClients.User;
import org.example.commentservice.FeignClients.UserServiceClient;
import org.example.commentservice.kafka.producer.CommentEvent;
import org.example.commentservice.kafka.producer.CommentProducer;
import org.example.commentservice.models.Comment;
import org.example.commentservice.repository.CommentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentProducer commentProducer;
    private final UserServiceClient userServiceClient;

    // Méthode pour ajouter un commentaire (via Kafka)
    public CommentResponse addComment(String commentContent, UUID postId, UUID userId) {
        Comment comment = Comment
                .builder()
                .id(UUID.randomUUID())
                .content(commentContent)
                .postId(postId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        CommentEvent commentEvent = CommentEvent
                .builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .eventType("CREATE")
                .createdAt(comment.getCreatedAt())
                .build();
        commentProducer.sendCommentEventToPostSeviceToUpdateCommentsList(commentEvent);
        User user = userServiceClient.getUserById(userId);
        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(user)
                .createdAt(comment.getCreatedAt())
                .build();


        commentRepository.save(comment);
        return commentResponse;
    }

    // Récupérer tous les commentaires associés à un post
    public List<CommentResponse> getCommentsByPostId(UUID postId) {
        List<Comment> comments =  commentRepository.findByPostId(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .userId(userServiceClient.getUserById(comment.getUserId()))
                    .createdAt(comment.getCreatedAt())
                    .build();
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }

    public void deleteCommentsByPostId(UUID postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        commentProducer.sendCommentEventToPostSeviceToUpdateCommentsList(
                CommentEvent.builder()
                        .postId(postId)
                        .eventType("DELETE_ALL")
                        .build()
        );
        commentRepository.deleteAll(comments);
    }
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentProducer.sendCommentEventToPostSeviceToUpdateCommentsList(
                CommentEvent.builder()
                        .id(commentId)
                        .postId(comment.getPostId())
                        .eventType("DELETE")
                        .build()
        );
        commentRepository.deleteById(commentId);
    }

    public Comment getCommentById(UUID commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment updateComment(UUID commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setContent(content);
            return commentRepository.save(comment);
        }
        return comment ;
    }


    // Méthode Kafka pour écouter les événements CommentEvent
//    @KafkaListener(topics = "comments-topic", groupId = "comment-group")
//    public void handleCommentEvent(CommentEvent commentEvent) {
//        // Créer un nouveau commentaire à partir de l'événement reçu
//        Comment comment = new Comment();
//        comment.setId(commentEvent.getCommentId());
//        comment.setContent(commentEvent.getContent());
//        comment.setPostId(commentEvent.getPostId());
//        comment.setUserId(commentEvent.getUserId());
//
//        // Sauvegarder le commentaire dans la base de données
//        addComment(comment);
//    }
}
