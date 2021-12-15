package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserIdRequestTest extends ValidatorTestHelper {

    @MockBean
    UserService userService;

    @BeforeEach
    public void setUp() {
        when(userService.findById(1L)).thenReturn(java.util.Optional.ofNullable(mock(User.class)));
        when(userService.findById(999L)).thenReturn(java.util.Optional.empty());
    }

    @Test
    void userIdRequest_wrongUserId() {
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(999L);

        Set<ConstraintViolation<UserIdRequest>> constraintViolations = validator.validate(userIdRequest);
        ConstraintViolation<UserIdRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(1, constraintViolations.size());
        Assertions.assertEquals("userId", violation.getPropertyPath().toString());
    }

    @Test
    void userIdRequest_userIdNull() {
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(null);

        Set<ConstraintViolation<UserIdRequest>> constraintViolations = validator.validate(userIdRequest);
        ConstraintViolation<UserIdRequest> violation = constraintViolations.iterator().next();
        Assertions.assertEquals(2, constraintViolations.size());
        Assertions.assertEquals("userId", violation.getPropertyPath().toString());
    }

    @Test
    void userIdRequest_valid() {
        UserIdRequest userIdRequest = new UserIdRequest();
        userIdRequest.setUserId(1L);

        Set<ConstraintViolation<UserIdRequest>> constraintViolations = validator.validate(userIdRequest);
        Assertions.assertEquals(0, constraintViolations.size());
    }

}
