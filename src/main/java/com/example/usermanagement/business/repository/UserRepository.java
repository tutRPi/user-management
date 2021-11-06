package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNmId(Long nmId);

    Optional<User> findByDsEmail(String dsEmail);

    boolean existsByEmail(String email, Long userId);
}
