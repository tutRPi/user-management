package com.example.usermanagement.web.api.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailUserSignupValidator.class)
public @interface UniqueEmailUserSignup {
    String message() default "An account with the specified email already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
