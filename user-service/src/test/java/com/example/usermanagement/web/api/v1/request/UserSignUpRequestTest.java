package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.*;
import java.util.Set;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSignUpRequestTest extends ValidatorTestHelper {

    @MockBean
    UserService userService;

    @BeforeEach
    public void setUp() {
        when(userService.findByEmail(notNull())).thenReturn(java.util.Optional.empty());
    }

    @Test
    void userSignUpRequest_missingEmail() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        userSignUpRequest.setEmail(null);

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        ConstraintViolation<UserSignUpRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void userSignUpRequest_invalidEmail() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        userSignUpRequest.setEmail("invalidEmailAddress");

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        ConstraintViolation<UserSignUpRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void userSignUpRequest_emailNotUnique() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        when(userService.findByEmail(notNull())).thenReturn(java.util.Optional.ofNullable(mock(User.class)));

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        ConstraintViolation<UserSignUpRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void userSignUpRequest_nullPassword() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        userSignUpRequest.setPassword("");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        Assertions.assertTrue(constraintViolations.size() >= 1);
    }

    @Test
    void userSignUpRequest_invalidPassword() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        // too short
        userSignUpRequest.setPassword("Aa1!");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        ConstraintViolation<UserSignUpRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        // no uppercase
        userSignUpRequest.setPassword("password1!");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        constraintViolations = validator.validate(userSignUpRequest);
        violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        // no lowercase
        userSignUpRequest.setPassword("PASSWORD1!");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        constraintViolations = validator.validate(userSignUpRequest);
        violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        // no number
        userSignUpRequest.setPassword("Password!");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        constraintViolations = validator.validate(userSignUpRequest);
        violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());

        // no special case
        userSignUpRequest.setPassword("Password1");
        userSignUpRequest.setPasswordConfirmation(userSignUpRequest.getPassword());

        constraintViolations = validator.validate(userSignUpRequest);
        violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("password", violation.getPropertyPath().toString());
    }

    @Test
    void userSignUpRequest_passwordDoNotMatch() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();
        userSignUpRequest.setPasswordConfirmation("0therPass!");

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        Assertions.assertTrue(constraintViolations.size() >= 1);
    }


    @Test
    void userSignUpRequest_valid() {
        UserSignUpRequest userSignUpRequest = validUserSignUpRequest();

        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(userSignUpRequest);
        Assertions.assertEquals(0, constraintViolations.size());
    }

    UserSignUpRequest validUserSignUpRequest() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setFirstName("Test");
        userSignUpRequest.setLastName("User");
        userSignUpRequest.setEmail("test@example.com");
        userSignUpRequest.setPassword("P4ssw0rd!123");
        userSignUpRequest.setPasswordConfirmation("P4ssw0rd!123");
        return userSignUpRequest;
    }
}
