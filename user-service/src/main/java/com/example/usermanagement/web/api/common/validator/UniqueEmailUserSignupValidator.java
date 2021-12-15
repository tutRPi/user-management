package com.example.usermanagement.web.api.common.validator;

import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailUserSignupValidator implements ConstraintValidator<UniqueEmailUserSignup, String> {
    @Autowired
    UserService userService;

    @Override
    public void initialize(final UniqueEmailUserSignup constraintAnnotation) { }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> match = this.userService.findByEmail(email);
        return match.isEmpty();
    }
}
