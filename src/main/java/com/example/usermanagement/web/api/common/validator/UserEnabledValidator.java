package com.example.usermanagement.web.api.common.validator;

import com.example.usermanagement.business.service.CustomUserDetailsService;
import com.example.usermanagement.web.api.v1.request.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserEnabledValidator implements ConstraintValidator<UserEnabled, AuthenticationRequest> {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean isValid(AuthenticationRequest authenticationRequest, ConstraintValidatorContext constraintValidatorContext) {

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            if (userDetails.isAccountNonLocked() && userDetails.isAccountNonExpired()) {
                return true;
            }
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate()).addPropertyNode("username").addConstraintViolation();
        } catch (UsernameNotFoundException e) {

        }
        return false;
    }
}