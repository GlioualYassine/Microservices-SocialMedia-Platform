package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.example.userservice.kafka.producer.UserDeleteDto;
import org.example.userservice.kafka.producer.UserUpdateDto;
import org.example.userservice.kafka.producer.UserProducer;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserProducer userProducer;
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser == null) {
            return null;
        }
        User us =  userRepository.save(user);
        UserUpdateDto userUpdateDto = UserUpdateDto
                .builder()
                .id(us.getId())
                .email(us.getEmail())
                .firstName(us.getFirstName())
                .lastName(us.getLastName())
                .build();
        userProducer.sendUserUpdate(userUpdateDto);
        return us;
    }

    public User deleteUser(UUID id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            userRepository.deleteById(id);
            userProducer.sendUserDelete(
                    UserDeleteDto.builder().id(id).build()
            );
        }
        return existingUser;
    }
}
