package org.example.notificationservice.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.models.Notification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Envoie une notification privée à l'utilisateur cible.
     *
     * @param notification la notification à envoyer
     */
    public void sendNotification(Notification notification) {
        UUID receiverId = notification.getReceiverId();
        log.info("Sending notification to user with ID: {}", receiverId);

        // Envoi de la notification au destinataire via WebSocket
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),        // ID de l'utilisateur destinataire
                "/notifications",       // Destination privée
                notification                  // Objet Notification
        );

        log.info("Notification sent successfully: {}", notification);
    }
}
