package com.example.usermanagement.web.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@PropertySource(value = {"classpath:application.properties"})
@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.host}")
    String mailServerHost;

    @Value("${spring.mail.port}")
    Integer mailServerPort;

    @Value("${spring.mail.username}")
    String mailServerUsername;

    @Value("${spring.mail.password}")
    String mailServerPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    String mailServerAuth;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    String mailServerSslTrust;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    String mailServerStartTls;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);

        mailSender.setUsername(mailServerUsername);
        mailSender.setPassword(mailServerPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailServerAuth);
        props.put("mail.smtp.starttls.enable", mailServerStartTls);
        props.put("mail.smtp.ssl.trust", mailServerSslTrust);
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

}
