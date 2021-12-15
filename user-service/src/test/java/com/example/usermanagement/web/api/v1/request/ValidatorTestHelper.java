package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.config.ValidatorTestHelperConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ValidatorTestHelperConfiguration.class})
public abstract class ValidatorTestHelper {

    @Autowired
    protected Validator validator;

    protected List<String> getPropertyPaths(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream().map(ConstraintViolation::getPropertyPath).map(Object::toString).collect(Collectors.toList());
    }

    protected List<String> getMessageTemplate(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream().map(ConstraintViolation::getMessageTemplate).map(msg -> msg.replaceAll("([{}])", "")).collect(Collectors.toList());
    }
}