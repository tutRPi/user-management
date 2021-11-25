package com.example.usermanagement.web.api.common.controller;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.ResponseError;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@RestControllerAdvice
public class ExceptionsControllerAdvice {

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) throws Exception {

        ResponseEntity<ErrorResponse> result = null;
        if (ex instanceof CodeRuntimeException) {
            result = buildException(((CodeRuntimeException) ex).getErrorsEnum());
        } else if (ex instanceof AccountExpiredException) {
            result = buildException(ErrorsEnum.ACCOUNT_EXPIRED);
        } else if (ex instanceof BadCredentialsException) {
            result = buildException(ErrorsEnum.BAD_CREDENTIALS);
        } else if (ex instanceof CredentialsExpiredException) {
            result = buildException(ErrorsEnum.CREDENTIALS_EXPIRED);
        } else if (ex instanceof DisabledException) {
            result = buildException(ErrorsEnum.ACCOUNT_DISABLED);
        } else if (ex instanceof ExpiredJwtException) {
            result = buildException(ErrorsEnum.JWT_EXPIRED);
        } else if (ex instanceof LockedException) {
            result = buildException(ErrorsEnum.ACCOUNT_LOCKED);
        } else if (ex instanceof MalformedJwtException) {
            result = buildException(ErrorsEnum.JWT_MALFORMED);
        } else if (ex instanceof MethodArgumentNotValidException) {
            ResponseEntity<ErrorResponse> finalResult = buildException(ErrorsEnum.INVALID_ARGUMENTS);
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().forEach(objectError -> {
                ResponseError errorResponse = new ResponseError();
                errorResponse.setCode(775); // TODO change depending on field error
                errorResponse.setField(objectError.getField());
                errorResponse.setMessage(objectError.getDefaultMessage());
                Objects.requireNonNull(finalResult.getBody()).addResponseError(errorResponse);
            });
            result = finalResult;
        } else if (ex instanceof SignatureException) {
            result = buildException(ErrorsEnum.JWT_SIGNATURE_INVALID);
        } else if (ex instanceof UsernameNotFoundException) {
            result = buildException(ErrorsEnum.USERNAME_NOT_FOUND);
        }
        return Optional.ofNullable(result).orElseThrow(() -> ex);
    }

    public static ResponseEntity<ErrorResponse> buildException(ErrorsEnum errorsEnum) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(errorsEnum.getHttpStatus().value());
        response.setError(errorsEnum.getHttpStatus().getReasonPhrase());
        response.setMessage(errorsEnum.getMessage());
        response.setErrorCode(errorsEnum.getCode());
        return ResponseEntity.status(errorsEnum.getHttpStatus()).body(response);
    }
}