package com.example.usermanagement.web.api.common.validator;

import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.v1.request.UserIdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class ValidUserIdValidator implements ConstraintValidator<ValidUserId, Long> {

    @Autowired
    UserService userService;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> result = userService.findById(userId);

        return result.isPresent();
    }
}