package org.example.userservice.repositories;

import org.example.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.id != ?1")
    List<User> findAllExceptUser(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
