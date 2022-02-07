package com.example.usermanagement.business.service;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.*;
import com.example.usermanagement.business.repository.PasswordResetTokenRepository;
import com.example.usermanagement.business.repository.UserRepository;
import com.example.usermanagement.util.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    public static final int TOKEN_LENGTH = 64;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public User signUp(User user, ConfirmationToken confirmationToken) {
        // Logic to assign the user role
        RoleByUser roleByUser = new RoleByUser();
        Role role = new Role();
        role.setName(SecurityRole.ROLE_USER.getName());
        roleByUser.setRole(role);
        roleByUser.setUser(user);
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        user.setRolesByUserCollection(rolesByUser);

        // set confirmation token
        List<ConfirmationToken> confirmationTokensByUser = new ArrayList<>();
        confirmationTokensByUser.add(confirmationToken);
        user.setConfirmationTokensByUserCollection(confirmationTokensByUser);

        return userRepository.save(user);
    }

    public PasswordResetToken createPasswordResetTokenForUser(User user) {
        Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUser(user);
        if (existingToken.isPresent()) {
            if (existingToken.get().isValid()) {
                return existingToken.get();
            } else {
                passwordResetTokenRepository.delete(existingToken.get());
            }
        }
        String token = RandomStringUtil.getAlphaNumericString(TOKEN_LENGTH);
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        return passwordResetToken;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
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
