package com.example.usermanagement.web.api.common.controller;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.composer.ExceptionResponseComposerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsControllerAdvice {
    @Autowired
    ExceptionResponseComposerManager exceptionResponseComposerManager;

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception ex) throws Exception {
        // TODO better handling,
        return exceptionResponseComposerManager.delegateResponseComposition(ex).orElseThrow(() -> ex);
    }
}