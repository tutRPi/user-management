package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractExceptionResponseComposer<T extends Exception> {
    protected Class<T> wrappedExceptionClass;

    public AbstractExceptionResponseComposer(Class<T> wrappedExceptionClass) {
        this.wrappedExceptionClass = wrappedExceptionClass;
    }

    public Class getWrappedExceptionClass() {
        return this.wrappedExceptionClass;
    }

    protected ResponseEntity<BaseResponse> build(HttpStatus httpStatus, ErrorsEnum errorsEnum) {
        BaseResponse toRet = new BaseResponse();
        toRet.addResponseError(errorsEnum);
        return ResponseEntity.status(httpStatus).body(toRet);
    }

    public abstract ResponseEntity<BaseResponse> compose(T exception);
}
