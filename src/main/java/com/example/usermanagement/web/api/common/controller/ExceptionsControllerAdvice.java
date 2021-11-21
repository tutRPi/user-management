package com.example.usermanagement.web.api.common.controller;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.composer.ExceptionResponseComposerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionsControllerAdvice {
    @Autowired
    ExceptionResponseComposerManager exceptionResponseComposerManager;

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) throws Exception {
        // TODO better handling,
        return exceptionResponseComposerManager.delegateResponseComposition(ex, request).orElseThrow(() -> ex);
    }
}