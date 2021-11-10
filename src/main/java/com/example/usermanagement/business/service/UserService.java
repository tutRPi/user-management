package com.example.usermanagement.business.service;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.Role;
import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public User signUp(User user, ConfirmationToken confirmationToken) {
        // Logic to assign the user role
        RoleByUser roleByUser = new RoleByUser();
        Role role = new Role();
        role.setName(SecurityRole.ROLE_USER.getName());
        roleByUser.setRole(role);
        roleByUser.setUser(user);
        // TODO also add ROLE_2FA_CODE_VERIFICATION?
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        user.setRolesByUserCollection(rolesByUser);

        // set confirmation token
        List<ConfirmationToken> confirmationTokensByUser = new ArrayList<>();
        confirmationTokensByUser.add(confirmationToken);
        user.setConfirmationTokensByUserCollection(confirmationTokensByUser);

        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email, Long userId) {
        return userRepository.existsByEmail(email, userId);
    }
}
