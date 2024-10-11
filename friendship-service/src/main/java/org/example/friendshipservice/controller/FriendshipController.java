package org.example.friendshipservice.controller;

import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok("Friend request sent");
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam UUID fromUserId, @RequestParam UUID toUserId) {
        friendshipService.acceptFriendRequest(fromUserId, toUserId);
        return ResponseEntity.ok("Friend request accepted");
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<Set<User>> getFriends(@PathVariable UUID userId) {
        Set<User> friends = friendshipService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<Set<User>> getFriendRequests(@PathVariable UUID userId) {
        Set<User> friendRequests = friendshipService.getFriendRequests(userId);
        return ResponseEntity.ok(friendRequests);
    }
}
