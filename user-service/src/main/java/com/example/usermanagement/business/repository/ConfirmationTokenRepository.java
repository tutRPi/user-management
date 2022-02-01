package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Query("SELECT c FROM ConfirmationToken c WHERE c.user.id = ?1")
    List<ConfirmationToken> findByUserId(long userId);
}
