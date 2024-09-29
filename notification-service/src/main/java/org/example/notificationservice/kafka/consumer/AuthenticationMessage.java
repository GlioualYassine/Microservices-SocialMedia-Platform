package org.example.notificationservice.kafka.consumer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.notificationservice.email.EmailTemplateName;

public record AuthenticationMessage(
        String fullname,
        String email,
        EmailTemplateName templateName,
        String confirmationUrl,
        String token,
        String subject
) {
}
