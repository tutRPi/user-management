package com.example.usermanagement.business.service.mail;

import org.springframework.mail.MailSender;

public interface EmailService extends MailSender {
    void sendSimpleMessage(String to, String subject, String text);

    void sendSimpleMessageUsingTemplate(String to, String subject, String... templateModel);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);
}