package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

    Stream<PasswordResetToken> findAllByExpiresAtLessThan(Instant now);

    void deleteByExpiresAtLessThan(Instant now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiresAt <= ?1")
    void deleteAllExpiredSince(Instant now);
}
