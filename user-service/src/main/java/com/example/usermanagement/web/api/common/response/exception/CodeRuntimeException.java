package com.example.usermanagement.web.api.common.response.exception;

import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import lombok.Getter;

@Getter
public class CodeRuntimeException extends RuntimeException {

    private final ErrorsEnum errorsEnum;

    public CodeRuntimeException(ErrorsEnum errorsEnum) {
        super(errorsEnum.getMessage());
        this.errorsEnum = errorsEnum;
    }
}
