package chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequest {
    UUID conversationId;
    UUID senderId;
    UUID receiverId;
    String message;
    LocalDateTime timestamp;
}
