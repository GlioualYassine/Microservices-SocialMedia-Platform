package org.example.authservice.controller;

import org.example.authservice.models.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

     @GetMapping("/all")
    public List<User> getAllUsers() {
         return userRepository.findAll();
     }

     @PostMapping
    public User saveUser(User user) {
         return userRepository.save(user);
     }

}
