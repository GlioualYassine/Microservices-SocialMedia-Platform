package org.example.friendshipservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFriendProducer {
    private final KafkaTemplate<String,UserFriendDTO> kafkaTemplate ;

    public void sendUserFriendEvent(UserFriendDTO userFriendDTO){
        log.info("Sending user friend event to kafka topic");
        Message<UserFriendDTO> message = MessageBuilder
                .withPayload(userFriendDTO)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "user-friend-topic" )
                .build();
        kafkaTemplate.send(message);
    }


//    public void sendUserFriendRequestEvent(UserFriendDTO userFriendDTO){
//        Message<UserFriendDTO> message = MessageBuilder
//                .withPayload(userFriendDTO)
//                .setHeader(
//                        KafkaHeaders.TOPIC,
//                        "user-friend-request-topic" )
//                .build();
//        kafkaTemplate.send(message);
//    }
}
