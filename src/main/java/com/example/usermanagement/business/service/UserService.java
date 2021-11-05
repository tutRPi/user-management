package com.example.usermanagement.business.service;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.Role;
import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public User signUp(User user) {
        // Logic to assign the user role
        RoleByUser roleByUser = new RoleByUser();
        Role role = new Role();
        role.setNmId(SecurityRole.ROLE_USER.getId());
        roleByUser.setNmRoleId(role);
        roleByUser.setNmUserId(user);
        // TODO also add ROLE_2FA_CODE_VERIFICATION?
        List<RoleByUser> rolesByUser = new ArrayList<>();
        rolesByUser.add(roleByUser);
        user.setRolesByUserCollection(rolesByUser);
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Integer userId) {
        return userRepository.findByNmId(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByDsEmail(email);
    }

    public boolean existsByEmail(String email, Integer userId) {
        return userRepository.existsByEmail(email, userId);
    }
}
