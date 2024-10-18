package org.example.friendshipservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.friendshipservice.dtos.UserDTO;
import org.example.friendshipservice.kafka.producer.UserFriendDTO;
import org.example.friendshipservice.kafka.producer.UserFriendProducer;
import org.example.friendshipservice.kafka.producer.notifications.Notification;
import org.example.friendshipservice.kafka.producer.notifications.NotificationDTO;
import org.example.friendshipservice.kafka.producer.notifications.NotificationProducer;
import org.example.friendshipservice.kafka.producer.notifications.NotificationType;
import org.example.friendshipservice.mapper.UserMapper;
import org.example.friendshipservice.model.Friendship;
import org.example.friendshipservice.model.User;
import org.example.friendshipservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

    private final UserRepository userRepository;
    private final UserFriendProducer userFriendProducer;
    private final NotificationProducer notificationProducer;


    // Send a friend request
    @Transactional
    public void sendFriendRequest(UUID fromUserId, UUID toUserId) {
        // Retrieve the user who is sending the friend request by their ID.
        // Throws an exception if the user is not found.
        User fromUser = userRepository.findById(fromUserId).orElseThrow();

        // Retrieve the user who is receiving the friend request by their ID.
        // Throws an exception if the user is not found.
        User toUser = userRepository.findById(toUserId).orElseThrow();

        if(fromUser.getFriendRequestsSent().contains(toUser)){
            return;
        }
        // Add the receiving user to the sender's list of friend requests.
        fromUser.getFriendRequestsSent().add(toUser);

        // Add the sender user to the receiving user's list of friend requests.
        toUser.getFriendRequestsReceived().add(fromUser);

        // Save the updated sender user entity to the repository.
        userRepository.save(fromUser);

        NotificationDTO notification =
                 NotificationDTO.builder()
                                .id(UUID.randomUUID())
                                .receiverId(toUserId)
                                .receiverName(toUser.getUsername())
                                .senderName(fromUser.getUsername())
                                .senderId(fromUserId)
                                .isRead(false)
                                .createdAt(new Date())
                                .type(NotificationType.FRIEND_REQUEST_RECEIVED)
                                .message("You have a new friend request from " + fromUser.getUsername())
                                .build();

        // Send a notification to the receiving user.
        notificationProducer.sendNotification(notification);

    }


        @Transactional
        public void acceptFriendRequest(UUID fromUserId, UUID toUserId) {
            log.info("Starting acceptFriendRequest between fromUserId={} and toUserId={}", fromUserId, toUserId);

            // Retrieve sender (fromUser)
            User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> {
                log.error("User with id={} not found", fromUserId);
                return new IllegalArgumentException("User not found: " + fromUserId);
            });
            log.info("Retrieved fromUser: {}", fromUser);

            // Retrieve receiver (toUser)
            User toUser = userRepository.findById(toUserId).orElseThrow(() -> {
                log.error("User with id={} not found", toUserId);
                return new IllegalArgumentException("User not found: " + toUserId);
            });
            log.info("Retrieved toUser: {}", toUser);

            // Create friendship from fromUser to toUser
            Friendship friendship = Friendship.builder()
                    .friend(toUser)
                    .since(LocalDateTime.now())
                    .status("ACTIVE")
                    .build();
            log.debug("Created friendship from {} to {}", fromUser.getUsername(), toUser.getUsername());

            fromUser.getFriendships().add(friendship);
            log.debug("Added friendship to fromUser's list. Current friendships: {}", fromUser.getFriendships());

            // Create the reciprocal friendship (from toUser to fromUser)
            Friendship reciprocalFriendship = Friendship.builder()
                    .friend(fromUser)
                    .since(friendship.getSince())
                    .status("ACTIVE")
                    .build();
            toUser.getFriendships().add(reciprocalFriendship);
            log.debug("Added reciprocal friendship to toUser's list. Current friendships: {}", toUser.getFriendships());

            // Remove friend requests from both users
            boolean removedFromSent = fromUser.getFriendRequestsSent().remove(toUser);
            boolean removedFromReceived = toUser.getFriendRequestsReceived().remove(fromUser);
            log.debug("Removed toUser from fromUser's sent requests: {}", removedFromSent);
            log.debug("Removed fromUser from toUser's received requests: {}", removedFromReceived);

            // Save both users with updated friendships and requests
            log.info("Saving updated fromUser and toUser...");
            userRepository.save(fromUser);
            userRepository.save(toUser);
            log.info("Successfully saved both users.");

            // Send event to notify friendship creation
            UserFriendDTO userFriendEvent = UserFriendDTO.builder()
                    .userId(fromUserId)
                    .friendId(toUserId)
                    .build();
            userFriendProducer.sendUserFriendEvent(userFriendEvent);
            log.info("Friendship event sent: {}", userFriendEvent);

            log.info("Finished processing acceptFriendRequest.");

            NotificationDTO notification =
                    NotificationDTO.builder()
                            .id(UUID.randomUUID())
                            .receiverId(fromUserId)
                            .receiverName(fromUser.getUsername())
                            .senderName(toUser.getUsername())
                            .senderId(toUser.getId())
                            .isRead(false)
                            .createdAt(new Date())
                            .type(NotificationType.FRIEND_REQUEST_ACCEPTED)
                            .message("You have a new friend request from " + fromUser.getUsername())
                            .build();

            // Send a notification to the receiving user.
            notificationProducer.sendNotification(notification);
        }

    @Transactional
    public void rejectFriendRequest(UUID fromUserId, UUID toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElse(null);
        if(fromUser == null){
            return;
        }
        User toUser = userRepository.findById(toUserId).orElse(null);
        if(toUser == null){
            return;
        }
        fromUser.getFriendRequestsReceived().remove(toUser);
        toUser.getFriendRequestsSent().remove(fromUser);
        userRepository.save(fromUser);
        userRepository.save(toUser);

        NotificationDTO notification =
                 NotificationDTO.builder()
                                .id(UUID.randomUUID())
                                .receiverId(fromUserId)
                                .receiverName(fromUser.getUsername())
                                .senderName(toUser.getUsername())
                                .senderId(toUserId)
                                .isRead(false)
                                .createdAt(new Date())
                                .type(NotificationType.FRIEND_REQUEST_REJECTED)
                                .message("Your friend request has been rejected by " + fromUser.getUsername())
                                .build();
        notificationProducer.sendNotification(notification);

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
                .getFriendRequestsReceived();
    }

    // Get all friend requests sent by a user
    public Set<User> getFriendRequestsSent(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getFriendRequestsSent();
    }

    public Boolean checkExistingFriendRequest(UUID fromUserId, UUID toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow();

        User toUser = userRepository.findById(toUserId).orElseThrow();
        // Check if the receiving user is in the sender's list of friend requests
        return fromUser.getFriendRequestsSent().contains(toUser);
    }

    public void cancelFriendRequest(UUID fromUserId, UUID toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow();
        User toUser = userRepository.findById(toUserId).orElseThrow();

        // Remove the receiving user from the sender's list of friend requests
        fromUser.getFriendRequestsSent().remove(toUser);
        // Remove the sender user from the receiving user's list of friend requests
        toUser.getFriendRequestsReceived().remove(fromUser);
        // Save the updated sender user entity to the repository
        userRepository.save(fromUser);
    }

    public Set<User> getFriendRequestsReceived(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow()
                .getFriendRequestsReceived();
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return null;
        }
        return UserMapper.toDTO(user);
    }
}

