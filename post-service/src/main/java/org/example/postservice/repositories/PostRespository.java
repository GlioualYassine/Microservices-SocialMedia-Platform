package org.example.postservice.repositories;

import org.example.postservice.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRespository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(UUID userId);
}

