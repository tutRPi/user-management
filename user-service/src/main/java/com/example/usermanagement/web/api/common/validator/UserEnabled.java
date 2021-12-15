package com.example.usermanagement.web.api.common.validator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEnabledValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface UserEnabled {
    String message() default "The User is not enabled.";

    Class[] groups() default {};

    Class[] payload() default {};
}
