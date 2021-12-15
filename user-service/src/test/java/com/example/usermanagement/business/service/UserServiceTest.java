package com.example.usermanagement.business.service;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.PasswordResetTokenRepository;
import com.example.usermanagement.business.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

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

    @Test
    void createPasswordResetTokenForUser_existingToken() {
        User user = new User();
        PasswordResetToken passwordResetToken = new PasswordResetToken("TEST TOKEN", user);
        when(passwordResetTokenRepository.findByUser(user)).thenReturn(Optional.ofNullable(passwordResetToken));

        PasswordResetToken result = cut.createPasswordResetTokenForUser(user);
        Assertions.assertSame(passwordResetToken, result);
    }

    @Test
    void createPasswordResetTokenForUser_expiredExistingToken() {
        User user = new User();
        PasswordResetToken passwordResetToken = new PasswordResetToken("TEST TOKEN", user);
        passwordResetToken.setExpiresAt(DateUtils.addMinutes(new Date(), -60));
        when(passwordResetTokenRepository.findByUser(user)).thenReturn(Optional.ofNullable(passwordResetToken));

        PasswordResetToken result = cut.createPasswordResetTokenForUser(user);
        Assertions.assertNotSame(passwordResetToken, result);

        verify(passwordResetTokenRepository, times(1)).delete(passwordResetToken);
    }

    @Test
    void createPasswordResetTokenForUser_noExistingToken() {
        User user = new User();
        when(passwordResetTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        PasswordResetToken result = cut.createPasswordResetTokenForUser(user);
        Assertions.assertNotNull(result);
        verify(passwordResetTokenRepository, times(0)).delete(any());
    }


}
