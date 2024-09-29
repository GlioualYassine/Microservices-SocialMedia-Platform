package org.example.postservice.kafka.consumer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.postservice.models.Post;
import org.example.postservice.repositories.PostRespository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class commentConsumer {

    private final PostRespository postRespository;
    @Transactional

    @KafkaListener(topics = "comment-events")
    public void handleCommentEvent(CommentEvent commentEvent) {
        System.out.println("Received comment event: " + commentEvent);
        if(commentEvent.eventType().equals("CREATE")) {
            System.out.println("Creating comment with id: " + commentEvent.id());
            Post post = postRespository.findById(commentEvent.postId()).orElseThrow(()-> new RuntimeException("Post not found"));
            post.getCommentIds().add(commentEvent.id());
            postRespository.save(post);

        } else if(commentEvent.eventType().equals("DELETE")) {
            System.out.println("Deleting comment with id: " + commentEvent.id());
            Post post = postRespository.findById(commentEvent.postId()).orElseThrow(()-> new RuntimeException("Post not found"));
            post.getCommentIds().remove(commentEvent.id());
            postRespository.save(post);
        } else if (commentEvent.eventType().equals("DELETE_ALL")) {
            Post post = postRespository.findById(commentEvent.postId()).orElseThrow(()-> new RuntimeException("Post not found"));
            post.getCommentIds().clear();
            postRespository.save(post);
        }

    }
}
