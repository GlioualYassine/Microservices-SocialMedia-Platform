package org.example.notificationservice.kafka.consumer;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.notificationservice.email.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationConsumer {
    private final  EmailService emailService ;
    @KafkaListener(topics = "notification-topic")
    @Async
    public void ConsumeAuthenticationMessage(AuthenticationMessage message) throws MessagingException {
        if(message.templateName().name().equals("ACTIVATE_ACCOUNT")){
            emailService.sendEmail(
                    message.email(),
                    message.fullname(),
                    message.templateName(),
                    message.confirmationUrl(),
                    message.token(),
                    message.subject()
            );
        } else if (message.templateName().name().equals("RESET_PASSWORD")){
            emailService.sendResetPasswordEmail(
                    message.email(),
                    message.fullname(),
                    message.templateName(),
                    message.confirmationUrl(),
                    message.subject()
            );
        }

    }
}
