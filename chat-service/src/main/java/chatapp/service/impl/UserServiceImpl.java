package chatapp.service.impl;


import chatapp.dto.ApiResponse;
import chatapp.dto.UserDTO;
import chatapp.entity.Conversation;
import chatapp.entity.User;
import chatapp.repository.ConversationRepository;
import chatapp.repository.UserRepository;
import chatapp.service.MessageSocketService;
import chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the UserService interface that provides user-related services.
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageSocketService messageSocketService;
    /**
     * Save a user to the system.
     *
     * @param user The User object representing the user to be saved.
     * @return ResponseEntity containing an ApiResponse indicating the result of the operation.
     */
    @Override
    public ResponseEntity<ApiResponse> saveUser(User user) {
        try {
            user = userRepository.save(user);
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Success")
                            .reason("OK")
                            .data(user)
                            .build(),
                    HttpStatus.OK
            );
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Failed")
                            .reason("Email already registered")
                            .data(null)
                            .build(),
                    HttpStatus.OK
            );
        }

    }

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to be found.
     * @return ResponseEntity containing an ApiResponse with the user information if found.
     */
    @Override
    public ResponseEntity<ApiResponse> findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Success")
                            .reason("OK")
                            .data(user)
                            .build(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Failed")
                            .reason("User not found")
                            .data(null)
                            .build(),
                    HttpStatus.OK
            );
        }
    }

    /**
     * Retrieve a list of all users in the system.
     *
     * @return ResponseEntity containing an ApiResponse with a list of User objects representing all users.
     */
    @Override
    public ResponseEntity<ApiResponse> findAllUsers() {
        List<User> list = userRepository.findAll();
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(200)
                        .status("Success")
                        .reason("OK")
                        .data(list)
                        .build(),
                HttpStatus.OK
        );
    }

    /**
     * Retrieve a list of all users except the user with a specific user ID.
     *
     * @param userId The ID of the user to be excluded from the list.
     * @return ResponseEntity containing an ApiResponse with a list of User objects representing all users except the specified user.
     */
    @Override
    public ResponseEntity<ApiResponse> findAllUsersExceptThisUserId(UUID userId) {
        List<User> list = userRepository.findAllUsersExceptThisUserId(userId);
        List<UserDTO>  userDTOList = new ArrayList<>();
        list.forEach(user-> {
            userDTOList.add(
                    UserDTO.builder()
                            .id(user.getUserId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .isOnline(user.isOnline())
                            .build()
            );

        });



        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(200)
                        .status("Success")
                        .reason("OK")
                        .data(userDTOList)
                        .build(),
                HttpStatus.OK
        );
    }

    /**
     * Find or create a conversation ID for a pair of users based on their user IDs.
     *
     * @param user1Id The ID of the first user in the conversation.
     * @param user2Id The ID of the second user in the conversation.
     * @return ResponseEntity containing an ApiResponse with the conversation ID for the user pair.
     */
    @Override
    public ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(UUID user1Id, UUID user2Id) {
        UUID conversationId;
        Optional<User> user1 = userRepository.findById(user1Id);
        Optional<User> user2 = userRepository.findById(user2Id);
        if (user1.isEmpty() || user2.isEmpty()) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Failed")
                            .reason("User not found")
                            .data(null)
                            .build(),
                    HttpStatus.OK
            );
        }

        Optional<Conversation> existingConversation = conversationRepository.findConversationByUsersID(user1.get().getUserId(), user2.get().getUserId());
        if (existingConversation.isPresent()) {
            conversationId = existingConversation.get().getConversationId();
        } else {
            Conversation newConversation = new Conversation();
            newConversation.setUser1(user1.get());
            newConversation.setUser2(user2.get());
            Conversation savedConversation = conversationRepository.save(newConversation);
            conversationId = savedConversation.getConversationId();
        }
        return new ResponseEntity<>(
                ApiResponse.builder()
                        .statusCode(200)
                        .status("Success")
                        .reason("OK")
                        .data(conversationId)
                        .build(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ApiResponse> setUserStatusOnline(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            user.setOnline(true);
            userRepository.save(user);
            messageSocketService.notifyAllUsersNewUserOnline(userId, user.getFirstName()+" "+user.getLastName());
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Success")
                            .reason("OK")
                            .data(user)
                            .build(),
                    HttpStatus.OK
            );
        }
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> setUserStatusOffline(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            user.setOnline(false);
            userRepository.save(user);
            messageSocketService.notifyAllUsersNewUserOffline(userId, user.getFirstName()+" "+user.getLastName());
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .statusCode(200)
                            .status("Success")
                            .reason("OK")
                            .data(user)
                            .build(),
                    HttpStatus.OK
            );
        }
        return null;
    }
}
