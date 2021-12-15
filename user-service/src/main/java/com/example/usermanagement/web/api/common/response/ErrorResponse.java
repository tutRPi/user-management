package com.example.usermanagement.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class ErrorResponse implements Serializable {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private int errorCode;
    private String field;
    private List<ResponseError> errorDetails;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.errorDetails = new ArrayList<>();
    }

    public void addResponseError(ResponseError responseError) {
        this.errorDetails.add(responseError);
    }

    public void addResponseError(int responseErrorCode, String field, String responseErrorMessage) {
        ResponseError responseError = new ResponseError(responseErrorCode, field, responseErrorMessage);
        this.errorDetails.add(responseError);
    }
}
