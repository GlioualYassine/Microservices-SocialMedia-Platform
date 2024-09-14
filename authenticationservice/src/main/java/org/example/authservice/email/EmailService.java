package org.example.authservice.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendEmail(
            String to ,
            String username ,
            EmailTemplateName emailTemplate,
            String confirtmationUrl,
            String activationCode,
            String subject) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.getName();
        }

        //Configure Mail Sender
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirtmationUrl);
        properties.put("activation_code", activationCode);

        Send(to, subject, templateName, mimeMessage, helper, properties);
    }

    @Async
    protected void Send(String to, String subject, String templateName, MimeMessage mimeMessage, MimeMessageHelper helper, Map<String, Object> properties) throws MessagingException {
        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("contact@aynur.com");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendResetPasswordEmail(
            String to ,
            String username ,
            EmailTemplateName emailTemplate,
            String resetUrl,
            String subject) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.getName();
        }

        //Configure Mail Sender
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("link", resetUrl);

        Send(to, subject, templateName, mimeMessage, helper, properties);
    }
}
