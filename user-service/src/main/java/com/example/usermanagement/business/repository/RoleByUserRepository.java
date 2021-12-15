package com.example.usermanagement.business.repository;

import com.example.usermanagement.business.model.RoleByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleByUserRepository extends JpaRepository<RoleByUser, Integer> {
    @Query("SELECT ur FROM RoleByUser ur WHERE ur.user.id = ?1")
    List<RoleByUser> findByUserId(long userId);
}
