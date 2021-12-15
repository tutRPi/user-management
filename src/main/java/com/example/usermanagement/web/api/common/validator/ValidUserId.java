package com.example.usermanagement.web.api.common.validator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUserIdValidator.class)
@Documented
public @interface ValidUserId {
    String message() default "The User ID does not exist.";

    Class[] groups() default {};

    Class[] payload() default {};
}
