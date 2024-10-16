package org.example.userservice.kafka.consumer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConsumer {
    private final UserRepository userRepository;

    @KafkaListener(topics = "user-topic")
    public void consumeUserRegisteration(UserDTOConsumer userDTO){
        User user = User
                .builder()
                .id(userDTO.id())
                .username(userDTO.username())
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .build();
        userRepository.save(user);
    }

    @KafkaListener(topics = "user-friend-topic")
    @Transactional
    public void consumeUserFriendship(UserFriendDTO userFriendDTO) {
        userRepository.findById(userFriendDTO.userId())
                .ifPresent(user -> {
                    user.getFriends().add(userFriendDTO.friendId());
                    userRepository.save(user);
                });
        userRepository.findById(userFriendDTO.friendId())
                .ifPresent(user -> {
                    user.getFriends().add(userFriendDTO.userId());
                    userRepository.save(user);
                });
    }

}
