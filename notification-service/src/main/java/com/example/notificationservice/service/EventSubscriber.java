package com.example.notificationservice.service;

import com.example.notificationservice.config.MQConfiguration;
import com.example.notificationservice.dto.SendMailTemplateMessage;
import com.example.notificationservice.service.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
@Slf4j
public class EventSubscriber {

    @Autowired
    EmailService emailService;

    @RabbitListener(queues = MQConfiguration.QUEUE_SEND_MAIL)
    public void receive(SendMailTemplateMessage message) {
        log.info("Received message '{}'", message);
        try {
            emailService.sendMessageUsingThymeleafTemplate(message.getReceiver(), message.getSubject(), message.getTemplateName(), message.getTemplateModel());
            log.info("Successfully sent email to {}", message.getReceiver());
        } catch (IOException | MessagingException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
