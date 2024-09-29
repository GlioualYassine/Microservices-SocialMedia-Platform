package org.example.postservice.Response;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.postservice.models.Comment;
import org.example.postservice.models.User;
import org.example.postservice.openFeignClients.CommentResponse;
import org.example.postservice.openFeignClients.LikeResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder @Getter @Setter
public class PostResponse {
    private UUID id ;

    private String content;
    private User user;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    private List<CommentResponse> comments = new ArrayList<>();
    private List<LikeResponse> likes = new ArrayList<>();
}
