package org.example.friendshipservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.friendshipservice.dtos.UserDTO;
import org.example.friendshipservice.model.User;
import org.example.friendshipservice.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestParam UUID fromUserId, @RequestParam UUID toUserId) {
        friendshipService.sendFriendRequest(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/request/{fromUserId}/{toUserId}")
    public ResponseEntity<Boolean> checkExistingFriendRequest(@PathVariable UUID fromUserId, @PathVariable UUID toUserId) {
        return ResponseEntity.ok(friendshipService.checkExistingFriendRequest(fromUserId, toUserId));
    }

    @GetMapping("/request/{fromUserId}/{toUserId}/cancel")
    public ResponseEntity<String> cancelFriendRequest(@PathVariable UUID fromUserId, @PathVariable UUID toUserId) {
        friendshipService.cancelFriendRequest(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam UUID fromUserId, @RequestParam UUID toUserId) {
        friendshipService.acceptFriendRequest(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<Set<User>> getFriends(@PathVariable UUID userId) {
        Set<User> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests/sent/{userId}")
    public ResponseEntity<Set<User>> getFriendRequestsSent(@PathVariable UUID userId) {
        Set<User> friendRequests = friendshipService.getFriendRequestsSent(userId);
        return ResponseEntity.ok(friendRequests);
    }

    @GetMapping("/requests/received/{userId}")
    public ResponseEntity<Set<User>> getFriendRequestsReceived(@PathVariable UUID userId) {
        Set<User> friendRequests = friendshipService.getFriendRequestsReceived(userId);
        return ResponseEntity.ok(friendRequests);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(friendshipService.getUserById(userId));
    }
}
