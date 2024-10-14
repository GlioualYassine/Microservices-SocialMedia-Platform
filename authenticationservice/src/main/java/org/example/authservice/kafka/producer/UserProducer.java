package org.example.authservice.kafka.producer;


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
public class UserProducer {
    private final KafkaTemplate<String, UserDTO> kafkaTemplate;

    public void sendUser(UserDTO userDTO){
        log.info("Sending user ");

        Message<UserDTO> message = MessageBuilder
                        .withPayload(userDTO)
                        .setHeader(
                                KafkaHeaders.TOPIC,
                                "user-topic" )
                        .build();
        kafkaTemplate.send(message);
    }

    public void sendUserNode(UserDTO userDTO){
        log.info("Sending user ");

        Message<UserDTO> message = MessageBuilder
                .withPayload(userDTO)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "user-nodes-topic" )
                .build();
        kafkaTemplate.send(message);
    }

    public void NotifyUserAction(UserEvent event){
        log.info("Sending user event ");

        Message<UserEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "user-events" )
                .build();
        kafkaTemplate.send(message);
    }
}
