package org.example.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.User;
import org.example.userservice.services.UserFriendsResponseList;
import org.example.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all/{id}")
    public ResponseEntity<List<User>>getAllUsersExceptUser(@PathVariable UUID id){
        return ResponseEntity.ok().body(userService.getAllUsersExceptUserAndFriend(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User>getUserById(@PathVariable UUID id){
        return ResponseEntity.ok().body(userService.getUserById(id));
    }



    @GetMapping("/friends/{id}")
    public ResponseEntity<UserFriendsResponseList>getUserFriends(@PathVariable UUID id){
        return ResponseEntity.ok().body(userService.getConnectedUserFriendships(id));
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<User>getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User>getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PostMapping
    public ResponseEntity<User>createUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User>updateUser(@PathVariable UUID id, @RequestBody User user){
        user.setId(id);
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User>deleteUser(@PathVariable UUID id){
        User user = userService.deleteUser(id);
        return ResponseEntity.ok(user);
    }
}
