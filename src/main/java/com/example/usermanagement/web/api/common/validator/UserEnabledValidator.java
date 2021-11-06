package com.example.usermanagement.web.api.common.validator;

import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.v1.request.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UserEnabledValidator implements ConstraintValidator<UserEnabled, AuthenticationRequest> {
    @Autowired
    UserService userService;

    @Override
    public boolean isValid(AuthenticationRequest authenticationRequest, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> match = this.userService.findByEmail(authenticationRequest.getUsername());
        boolean toRet = match.isEmpty();
        if (!toRet) {
            toRet = match.get().isYnEnabled() && !match.get().isYnLocked();
        }

        if (!toRet) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate()).addPropertyNode("username").addConstraintViolation();
        }

        return toRet;
    }
}