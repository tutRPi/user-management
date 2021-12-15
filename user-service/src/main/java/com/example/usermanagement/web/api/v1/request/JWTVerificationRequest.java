package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class JWTVerificationRequest extends BaseRequest {
    @NotNull(message = "You must send a jwt with this request.")
    @Size(min = 1)
    private String jwt;
}
