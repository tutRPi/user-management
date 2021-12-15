package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Looking up for a user with email: '" + username + "' ...");
        User user = this.userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: '" + username + "' not found."));
        log.debug("User with email: '" + username + "' found !");
        CustomUserDetails toRet = new CustomUserDetails(user);
        toRet.setAuthorities(new HashSet<>());
        return toRet;
    }
}
