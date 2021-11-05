package com.example.usermanagement.web.api.common.validator;

import com.example.usermanagement.web.api.v1.request.SecurityCredentialsRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, SecurityCredentialsRequest> {
    @Override
    public void initialize(final PasswordConfirmation constraintAnnotation) {
    }

    @Override
    public boolean isValid(SecurityCredentialsRequest securityCredentialsRequest, ConstraintValidatorContext constraintValidatorContext) {
        String password = securityCredentialsRequest.getPassword();
        String passwordConfirmation = securityCredentialsRequest.getPasswordConfirmation();

        boolean toRet = false;
        if (password != null
                && passwordConfirmation != null
                && !"".equals(password)
                && !"".equals(passwordConfirmation)) {
            toRet = password.equals(passwordConfirmation);
        }

        if (!toRet) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate()).addPropertyNode("passwordConfirmation").addConstraintViolation();
        }

        return toRet;
    }
}