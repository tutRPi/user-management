package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    CustomUserDetailsService cut;


    @Test
    void loadUserByUsername_throwsException() {
        when(userService.findByEmail(any())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            cut.loadUserByUsername("test");
        });
    }

    @Test
    void loadUserByUsername_success() {
        User user = new User();
        user.setEmail("test@user.com");
        when(userService.findByEmail(any())).thenReturn(java.util.Optional.of(user));

        UserDetails result = cut.loadUserByUsername("test");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("test@user.com", result.getUsername());
    }

}
