package org.example.friendshipservice.kafka.producer.notifications;

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
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    public void sendNotification(NotificationDTO notification){
        log.info("Sending notification to kafka topic");

        Message <NotificationDTO> message = MessageBuilder
                .withPayload(notification)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "notification-websocket-topic" )
                .build();
        kafkaTemplate.send(message);
    }
}
