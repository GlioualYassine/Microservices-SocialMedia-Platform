package org.example.friendshipservice.kafka.consumer;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.friendshipservice.kafka.producer.UserFriendDTO;
import org.example.friendshipservice.model.User;
import org.example.friendshipservice.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {
    private final UserRepository userRepository;

    @KafkaListener(topics = "user-nodes-topic")
    public void consumeUserRegisteration(UserDTOConsumer userDTO){

        User user = User
                .builder()
                .id(userDTO.id())
                .username(userDTO.username())
                .email(userDTO.email())
                .build();
        log.info("Consumed user: " + user);
        userRepository.save(user);
    }


}
