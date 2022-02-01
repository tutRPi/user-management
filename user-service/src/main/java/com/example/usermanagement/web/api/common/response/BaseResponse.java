package com.example.usermanagement.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {
    private Instant timestamp;

    public BaseResponse() {
        this.timestamp = Instant.now();
    }
}