package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService cut;

    @Test
    void signUp_success() {
        when(userRepository.save(any())).then(returnsFirstArg());

        User user = new User();
        ConfirmationToken token = new ConfirmationToken();
        User result = cut.signUp(user, token);
        Assertions.assertTrue(result.getRolesByUserCollection().size() >= 1);
    }
}
