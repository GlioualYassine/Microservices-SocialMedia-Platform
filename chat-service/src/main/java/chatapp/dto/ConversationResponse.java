package chatapp.dto;

import java.sql.Timestamp;
import java.util.UUID;

public interface ConversationResponse {

    UUID getConversationId();

    UUID getOtherUserId();

    String getOtherUserName();

    String getLastMessage();

    Timestamp getLastMessageTimestamp();
}
