package com.example.notificationservice.service.mail;

import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@Primary
public interface EmailService extends MailSender {
    void sendSimpleMessage(String to, String subject, String text);

    void sendSimpleMessageUsingTemplate(String to, String subject, String... templateModel);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

    void sendMessageUsingThymeleafTemplate(String to, String subject, String templateName, Map<String, Object> templateModel) throws IOException, MessagingException;
}