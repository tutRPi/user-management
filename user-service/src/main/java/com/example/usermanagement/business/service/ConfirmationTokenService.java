package com.example.usermanagement.business.service;

import com.example.usermanagement.business.config.EventPublisherConfiguration;
import com.example.usermanagement.business.dto.SendMailTemplateMessage;
import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.ConfirmationTokenRepository;
import com.example.usermanagement.util.AppSettings;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.UserEmailConfirmTokenController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@Service
public class ConfirmationTokenService {
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageSource emailMessageSource;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${server.port}")
    int serverPort;

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public List<ConfirmationToken> findByUserId(long userId) {
        return confirmationTokenRepository.findByUserId(userId);
    }

    @Transactional
    public Date setConfirmedAt(ConfirmationToken token) {
        User user = token.getUser();
        Date verifiedOn = new Date();
        user.setEmailVerifiedOn(verifiedOn);
        token.setConfirmedAt(verifiedOn);
        userService.save(user);
        confirmationTokenRepository.save(token);
        return verifiedOn;
    }

    public void sendConfirmationLink(String to, ConfirmationToken confirmationToken, HttpServletRequest request) {
        try {
            String link = AppSettings.getAppUrl(request) + Constants.API_VERSION_PATH + UserEmailConfirmTokenController.PATH + "?token=" + confirmationToken.getToken();

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("url", link);

            SendMailTemplateMessage sendMailTemplateMessage = new SendMailTemplateMessage();
            sendMailTemplateMessage.setReceiver(to);
            sendMailTemplateMessage.setSubject(emailMessageSource.getMessage("title.confirmationLink", null, request.getLocale()));
            sendMailTemplateMessage.setTemplateName("confirmationLink.html");
            sendMailTemplateMessage.setTemplateModel(templateModel);
            rabbitTemplate.convertAndSend(EventPublisherConfiguration.QUEUE_SEND_MAIL, sendMailTemplateMessage);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
