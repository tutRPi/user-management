package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.request.BaseRequest;
import com.example.usermanagement.web.api.common.validator.UserEnabled;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@UserEnabled
public class AuthenticationRequest extends BaseRequest {
    @NotEmpty(message = "You must send an username with this request.")
    @Size(min = 5, max = 255)
    @Schema(example = "test@example.com", required = true)
    private String username;

    @NotEmpty(message = "You must send a password with this request.")
    @Size(min = 1, max = 255)
    private String password;
}