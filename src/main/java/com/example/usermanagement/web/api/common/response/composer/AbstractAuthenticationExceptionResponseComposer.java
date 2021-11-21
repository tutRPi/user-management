package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractAuthenticationExceptionResponseComposer<T extends Exception> extends AbstractExceptionResponseComposer<T> {
    public AbstractAuthenticationExceptionResponseComposer(Class<T> wrappedExceptionClass) {
        super(wrappedExceptionClass);
    }

    protected ResponseEntity<ErrorResponse> buildResponse(ErrorsEnum errorsEnum) {
        return this.build(HttpStatus.UNAUTHORIZED, errorsEnum);
    }
}