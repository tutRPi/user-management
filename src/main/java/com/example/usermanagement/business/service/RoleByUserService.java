package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.repository.RoleByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleByUserService {
    @Autowired
    private RoleByUserRepository roleByUserRepository;

    public List<RoleByUser> findByUserId(int userId) {
        return this.roleByUserRepository.findByUserId(userId);
    }
}
