package com.example.usermanagement.business.service.eventbroker;

import org.springframework.stereotype.Service;

@Service
public interface EventSender {
    void sendMessage(String topicName, String message);
}
