package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Looking up for a user with email: '" + username + "' ...");
        User user = this.userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: '" + username + "' not found."));
        logger.debug("User with email: '" + username + "' found !");
        CustomUserDetails toRet = new CustomUserDetails(user);
        toRet.setAuthorities(new HashSet<GrantedAuthority>());
        return toRet;
    }
}
