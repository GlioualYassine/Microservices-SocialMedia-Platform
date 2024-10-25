package chatapp.controller;


import chatapp.dto.ApiResponse;
import chatapp.dto.LoginRequest;
import chatapp.entity.User;
import chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller class that handles user-related HTTP requests and interactions.
 * Routes:
 * - POST /user/register: Register a new user in the system.
 * - POST /user/login: Login a user based on their email address.
 * - GET /user/all: Retrieve a list of all users in the system.
 * - GET /user/except/{userId}: Retrieve a list of all users except the user with a specific user ID.
 * - GET /user/conversation/id: Find or create a conversation ID for a pair of users based on their user IDs.
 */
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> findAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Retrieve a list of all users except the user with a specific user ID.
     *
     * @param userId The ID of the user to be excluded from the list.
     * @return ResponseEntity containing an ApiResponse with a list of User objects representing all users except the specified user.
     */
    @GetMapping("/except/{userId}")
    public ResponseEntity<ApiResponse> findAllUsersExceptThisUserId(@PathVariable UUID userId) {
        return userService.findAllUsersExceptThisUserId(userId);
    }

    /**
     * Find or create a conversation ID for a pair of users based on their user IDs.
     *
     * @param user1Id The ID of the first user in the conversation.
     * @param user2Id The ID of the second user in the conversation.
     * @return ResponseEntity containing an ApiResponse with the conversation ID for the user pair.
     */
    @PostMapping("/conversation/id")
    public ResponseEntity<ApiResponse> findConversationIdByUser1IdAndUser2Id(@RequestParam UUID user1Id, @RequestParam UUID user2Id) {
        return userService.findConversationIdByUser1IdAndUser2Id(user1Id, user2Id);
    }
}
