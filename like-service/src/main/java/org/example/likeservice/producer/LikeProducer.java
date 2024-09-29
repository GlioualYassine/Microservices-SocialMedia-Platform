package org.example.likeservice.producer;

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
public class LikeProducer {
    private final KafkaTemplate<String, LikeEvent> kafkaTemplate;

    public void sendCommentEventToPostSeviceToUpdateLikesList(LikeEvent likeEvent){
        log.info("Sending comment event to post service to update likes list");

        Message<LikeEvent> message = MessageBuilder
                .withPayload(likeEvent)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "likes-events" )
                .build();
        kafkaTemplate.send(message);
    }
}
