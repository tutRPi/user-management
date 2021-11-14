package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}
