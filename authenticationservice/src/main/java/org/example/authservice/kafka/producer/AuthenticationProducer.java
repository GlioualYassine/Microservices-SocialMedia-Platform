package org.example.authservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationProducer {
    private final KafkaTemplate<String,AuthenticationMessage> kafkaTemplate;

    public void sendNotification(AuthenticationMessage message){

        Message<AuthenticationMessage> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, "notification-topic")
                .build();
        kafkaTemplate.send(msg);
    }
}
