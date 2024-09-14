package org.example.chatserice2.service;

import org.example.chatserice2.entity.UserEntity;
import org.example.chatserice2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }
}
