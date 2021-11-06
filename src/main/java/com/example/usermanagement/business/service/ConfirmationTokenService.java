package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.repository.ConfirmationToklenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfirmationTokenService {
    @Autowired
    ConfirmationToklenRepository confirmationToklenRepository;

//    @Transactional
//    public ConfirmationToken saveConfirmationToken(User user) {
//        ConfirmationToken token = new ConfirmationToken();
//        token.setDsToken(UUID.randomUUID().toString());
//        token.setNmUserId(user);
//        token.setDtCreatedOn(new Date());
//        token.setDtExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));
//
//        List<ConfirmationToken> confirmationTokensByUser = new ArrayList<>(user.getConfirmationTokensByUserCollection());
//        user.setConfirmationTokensByUserCollection(confirmationTokensByUser);
//
//        return confirmationToklenRepository.save(token);
//    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationToklenRepository.findByDsToken(token);
    }

    public List<ConfirmationToken> findByUserId(long userId) {
        return confirmationToklenRepository.findByUserId(userId);
    }
}
