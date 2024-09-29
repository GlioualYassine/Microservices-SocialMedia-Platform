package org.example.commentservice.kafka.producer;

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
public class CommentProducer {
    private final KafkaTemplate<String,CommentEvent> kafkaTemplate;

    public void sendCommentEventToPostSeviceToUpdateCommentsList(CommentEvent commentEvent){
        log.info("Sending comment event to post service to update comments list");

        Message<CommentEvent> message = MessageBuilder
                .withPayload(commentEvent)
                .setHeader(
                        KafkaHeaders.TOPIC,
                        "comment-events" )
                .build();
        kafkaTemplate.send(message);
    }
}
