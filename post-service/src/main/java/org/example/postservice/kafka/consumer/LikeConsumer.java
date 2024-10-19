package org.example.postservice.kafka.consumer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.postservice.models.Post;
import org.example.postservice.repositories.PostRespository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeConsumer {

    private final PostRespository postRespository;

    @KafkaListener(topics = "likes-events")
    @Transactional
    public void consumeLikeEvent(LikeEvent likeEvent){
        System.out.println("Consumed like event: " + likeEvent);
        Post post = postRespository.findById(likeEvent.postId()).orElse(null);
        if(post == null)
            return;
        if(likeEvent.eventType().equals("LIKE")){
            post.getLikeIds().add(likeEvent.id());
            log.info("Like added to post with id: " + post.getId());
        } else if (likeEvent.eventType().equals("DISLIKE")) {
            post.getLikeIds().remove(likeEvent.id());
        }
    }
}
