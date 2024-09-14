package org.example.authservice.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.example.authservice.auth.service.AuthenticationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConsumer {
    private final AuthenticationService authenticationService;
    @KafkaListener(topics = "user-update-topic")
    public void ConsumeUserUpdate(UserUpdateDto userUpdateDto){

        authenticationService.updateInfo(userUpdateDto);
    }

    @KafkaListener(topics = "user-delete-topic")
    public void ConsumeUserDelete(UserDeleteDto userDeleteDto){
        authenticationService.deleteUser(userDeleteDto);
    }
}
