package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT (count(u.id) > 0) as exists FROM User u WHERE u.email = ?1 and u.id <> ?2")
    boolean existsByEmail(String email, Long userId);
}
