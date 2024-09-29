package org.example.likeservice.repository;

import org.example.likeservice.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {

    // Récupérer les likes par postId
    List<Like> findByPostId(UUID postId);
}
