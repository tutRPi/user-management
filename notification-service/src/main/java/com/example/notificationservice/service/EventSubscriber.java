package com.example.notificationservice.service;

import com.example.notificationservice.config.MQConfiguration;
import com.example.notificationservice.dto.SendMailTemplateMessage;
import com.example.notificationservice.service.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Locale;

@Service
@Slf4j
public class EventSubscriber {

    @Autowired
    EmailService emailService;

    @Autowired
    MessageSource emailMessageSource;

    @RabbitListener(queues = MQConfiguration.QUEUE_SEND_MAIL)
    public void receive(SendMailTemplateMessage message) {
        log.info("Received message '{}'", message);
        try {
            String subject = emailMessageSource.getMessage(message.getSubjectKey(), null, Locale.forLanguageTag(message.getLanguage()));

            emailService.sendMessageUsingThymeleafTemplate(message.getReceiver(), subject, message.getTemplateName(), message.getTemplateModel());
            log.info("Successfully sent email to {}", message.getReceiver());
        } catch (IOException | MessagingException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
