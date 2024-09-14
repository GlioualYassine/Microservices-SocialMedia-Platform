package org.example.chatservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;  // Inject SimpMessagingTemplate for broadcasting

    @MessageMapping("/user.addUser")
    public void addUser(@Payload User user) {
        userService.saveUser(user);

        // Broadcast the new user information to all users on a public topic
        messagingTemplate.convertAndSend("/topic/public", user);
    }

    @MessageMapping("/user.disconnectUser")
    public void disconnectUser(@Payload User user) {
        userService.disconnect(user);

        // Broadcast the disconnected user information to all users on a public topic
        messagingTemplate.convertAndSend("/topic/public", user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}