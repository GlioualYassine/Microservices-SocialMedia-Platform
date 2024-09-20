package chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    UUID conversationId;
    UUID senderId;
    UUID receiverId;
    String message;
    LocalDateTime timestamp;
}
