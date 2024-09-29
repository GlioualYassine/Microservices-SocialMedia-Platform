package org.example.authservice.kafka.producer;

import lombok.Builder;
import org.example.authservice.email.EmailTemplateName;
@Builder
public record AuthenticationMessage(
        String fullname,
        String email,
        EmailTemplateName templateName,
        String confirmationUrl,
        String token,
        String subject
) {
}
