package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfirmationToklenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByDsToken(String dsToken);

    List<ConfirmationToken> findByUserId(long userId);
}
