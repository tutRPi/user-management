package com.example.usermanagement.web.api.common.validator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConfirmationValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface PasswordConfirmation {
    String message() default "The password and its confirmation do not match.";

    Class[] groups() default {};

    Class[] payload() default {};
}
