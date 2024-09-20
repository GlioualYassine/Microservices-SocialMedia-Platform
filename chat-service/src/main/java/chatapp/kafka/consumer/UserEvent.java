package chatapp.kafka.consumer;

import java.util.UUID;

public record UserEvent(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String status // "online" ou "offline"
) {
}
