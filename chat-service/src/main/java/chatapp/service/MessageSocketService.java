package chatapp.service;


import chatapp.dto.MessageRequest;

import java.util.UUID;




/**
 * An interface for handling real-time messaging functionality using web sockets.
 */
public interface MessageSocketService {

    /**
     * Notify all users in a public topic that a new user is online.
     *
     * @param userId The ID of the user who is now online.
     * @param userName The name of the user who is now online.
     */
    void notifyAllUsersNewUserOnline(UUID userId, String userName);


    /**
     * Notify all users in a public topic that a user is offline.
     *
     * @param userId The ID of the user who is now offline.
     * @param userName The name of the user who is now offline.
     */
    void notifyAllUsersNewUserOffline(UUID userId, String userName);

    /**
     * Send user conversations to a specific user by their user ID through a web socket.
     *
     * @param userId The ID of the user for whom to send conversations.
     */
    void sendUserConversationByUserId(UUID userId);

    /**
     * Send messages of a specific conversation to the connected users through a web socket.
     *
     * @param conversationId The ID of the conversation for which to send messages.
     */
    void sendMessagesByConversationId(UUID conversationId);

    /**
     * Save a new message using a web socket.
     *
     * @param msg The MessageRequest object containing the message details to be saved.
     */
    void saveMessage(MessageRequest msg);

    /**
     * Delete a conversation by its unique conversation ID using a web socket.
     *
     * @param conversationId The ID of the conversation to be deleted.
     */
    void deleteConversationByConversationId(UUID conversationId);

    /**
     * Delete a message by its unique message ID within a conversation using a web socket.
     *
     * @param conversationId The ID of the conversation to notify its listener.
     * @param messageId      The ID of the message to be deleted.
     */
    void deleteMessageByMessageId(UUID conversationId, UUID messageId);
}
