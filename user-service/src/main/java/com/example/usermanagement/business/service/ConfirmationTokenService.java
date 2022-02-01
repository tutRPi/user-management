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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class ConfirmationTokenService {
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    UserService userService;

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
    public Instant setConfirmedAt(ConfirmationToken token) {
        User user = token.getUser();
        Instant verifiedOn = Instant.now();
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

            rabbitTemplate.convertAndSend(EventPublisherConfiguration.QUEUE_SEND_MAIL,
                    new SendMailTemplateMessage(
                            to,
                            request.getLocale().getLanguage(),
                            "title.confirmationLink",
                            "confirmationLink.html",
                            templateModel
                    ));

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
