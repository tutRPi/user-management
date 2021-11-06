package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.ConfirmationToklenRepository;
import com.example.usermanagement.business.service.mail.EmailService;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.UserEmailConfirmTokenController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@Service
public class ConfirmationTokenService {
    @Autowired
    ConfirmationToklenRepository confirmationTokenRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

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
        User user = token.getNmUserId();
        Date verifiedOn = new Date();
        user.setDtEmailVerifiedOn(verifiedOn);
        token.setDtConfirmedAt(verifiedOn);
        userService.save(user);
        confirmationTokenRepository.save(token);
        return verifiedOn;
    }

    public void sendConfirmationLink(String to, ConfirmationToken confirmationToken) {
        try {
            String host = InetAddress.getLocalHost().getHostName();
            String port = (serverPort != 80) ? ":" + serverPort : "";
            String link = "//" + host + port + Constants.API_VERSION_PATH + UserEmailConfirmTokenController.PATH + "?token=" + confirmationToken.getDsToken();

            emailService.sendSimpleMessage(to, "Confirmation Link", link);

        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
    }
}
