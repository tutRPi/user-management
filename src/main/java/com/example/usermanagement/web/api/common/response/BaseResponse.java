package com.example.usermanagement.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {
    private LocalDateTime timestamp;
    private List<ResponseError> errors;

    public BaseResponse() {
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public void addResponseError(ResponseError responseError) {
        this.errors.add(responseError);
    }

    public void addResponseError(int responseErrorCode, String field, String responseErrorMessage) {
        ResponseError responseError = new ResponseError(responseErrorCode, field, responseErrorMessage);
        this.errors.add(responseError);
    }

    public void addResponseError(int responseErrorCode, String responseErrorMessage) {
        ResponseError responseError = new ResponseError(responseErrorCode, null, responseErrorMessage);
        this.errors.add(responseError);
    }

    public void addResponseError(ErrorsEnum errorsEnum) {
        this.addResponseError(errorsEnum.getCode(), errorsEnum.getMessage());
    }
}