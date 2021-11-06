package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.ConfirmationToklenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ConfirmationTokenService {
    @Autowired
    ConfirmationToklenRepository confirmationTokenRepository;

    @Autowired
    UserService userService;

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
        userService.save(user);
        confirmationTokenRepository.updateConfirmedAt(token.getDsToken(), verifiedOn);
        return verifiedOn;
    }
}
