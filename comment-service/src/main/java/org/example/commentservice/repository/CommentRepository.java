package org.example.commentservice.repository;

import org.example.commentservice.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // Récupérer les commentaires par postId
    List<Comment> findByPostId(UUID postId);
}
