package com.example.usermanagement.business.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class EventPublisherConfiguration {

    public static final String QUEUE_SEND_MAIL = "sendMailQueue";
    public static final String QUEUE_USER_ACTIVITY_LOG = "userActivityLogQueue";

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;

    @Bean
    public Queue sendMailQueue() {
        return new Queue(QUEUE_SEND_MAIL);
    }

    @Bean
    public Queue userActivityLogQueue() {
        return new Queue(QUEUE_USER_ACTIVITY_LOG);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = mapperBuilder.build();
        return new Jackson2JsonMessageConverter(mapper);
    }
}
