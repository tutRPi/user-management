package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public abstract class AbstractExceptionResponseComposer<T extends Exception> {
    protected Class<T> wrappedExceptionClass;

    public AbstractExceptionResponseComposer(Class<T> wrappedExceptionClass) {
        this.wrappedExceptionClass = wrappedExceptionClass;
    }

    public Class getWrappedExceptionClass() {
        return this.wrappedExceptionClass;
    }

    protected ResponseEntity<ErrorResponse> build(HttpStatus httpStatus, ErrorsEnum errorsEnum) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(httpStatus.value());
        response.setError(httpStatus.getReasonPhrase());
        response.setMessage(errorsEnum.getMessage());
        response.setCode(errorsEnum.getCode());
        return ResponseEntity.status(httpStatus).body(response);
    }

    public abstract ResponseEntity<ErrorResponse> compose(T exception);
}
