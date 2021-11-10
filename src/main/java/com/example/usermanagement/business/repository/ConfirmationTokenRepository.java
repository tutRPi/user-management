package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    List<ConfirmationToken> findByUserId(long userId);
}
