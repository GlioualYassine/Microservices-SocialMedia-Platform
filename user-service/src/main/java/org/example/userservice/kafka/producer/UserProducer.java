package org.example.userservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {
    private final KafkaTemplate<String,UserUpdateDto> kafkaTemplate;

    public void sendUserUpdate(UserUpdateDto userUpdateDto){

        Message<UserUpdateDto> message = MessageBuilder
                .withPayload(userUpdateDto)
                .setHeader(KafkaHeaders.TOPIC,"user-update-topic")
                .build();


        kafkaTemplate.send(message);
    }

    public void sendUserDelete(UserDeleteDto userDeleteDto){

        Message<UserDeleteDto> message = MessageBuilder
                .withPayload(userDeleteDto)
                .setHeader(KafkaHeaders.TOPIC,"user-delete-topic")
                .build();


        kafkaTemplate.send(message);
    }

}
