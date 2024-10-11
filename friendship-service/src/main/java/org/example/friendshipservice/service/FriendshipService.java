package org.example.friendshipservice.service;

import lombok.RequiredArgsConstructor;
import org.example.friendshipservice.model.Friendship;
import org.example.friendshipservice.model.User;
import org.example.friendshipservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;

    // Send a friend request
    @Transactional
    public void sendFriendRequest(UUID fromUserId, UUID toUserId) {
        // Retrieve the user who is sending the friend request by their ID.
        // Throws an exception if the user is not found.
        User fromUser = userRepository.findById(fromUserId).orElseThrow();

        // Retrieve the user who is receiving the friend request by their ID.
        // Throws an exception if the user is not found.
        User toUser = userRepository.findById(toUserId).orElseThrow();

        // Add the friend request relationship to the sender's list of friend requests.
        fromUser.getFriendRequests().add(toUser);

        // Save the updated sender user entity to the repository.
        userRepository.save(fromUser);
    }

    // Accept a friend request
    @Transactional
    public void acceptFriendRequest(UUID fromUserId, UUID toUserId) {
        // Retrieve the user who is sending the friend request by their ID.
        // Throws an exception if the user is not found.
        User fromUser = userRepository.findById(fromUserId).orElseThrow();

        // Retrieve the user who is receiving the friend request by their ID.
        // Throws an exception if the user is not found.
        User toUser = userRepository.findById(toUserId).orElseThrow();

        // Create a new friendship relationship
        Friendship friendship = Friendship.builder()
                .friend(toUser)
                .since(LocalDateTime.now())
                .status("ACTIVE")
                .build();

        // Add friendship to both users
        fromUser.getFriendships().add(friendship);
        toUser.getFriendships().add(Friendship.builder()
                .friend(fromUser)
                .since(LocalDateTime.now())
                .status("ACTIVE")
                .build());

        // Remove the friend request from the sender's list of friend requests
        fromUser.getFriendRequests().remove(toUser);

        // Save the updated sender user entity to the repository
        userRepository.save(fromUser);

        // Save the updated receiver user entity to the repository
        userRepository.save(toUser);
    }

    // Get all friends of a user
    public Set<User> getFriends(UUID userId) {
        // Retrieve the user by their ID.
        // Throws an exception if the user is not found.
        // Get the user's friendships, map them to their friends, and collect them into a set.
        return userRepository.findById(userId)
            .orElseThrow()
            .getFriendships()
            .stream()
            .map(Friendship::getFriend)// equivalent to .map(friendship -> friendship.getFriend())
            .collect(Collectors.toSet());// to collect the friends into a set
    }

    // Get all friend requests of a user
    public Set<User> getFriendRequests(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getFriendRequests();
    }
}

