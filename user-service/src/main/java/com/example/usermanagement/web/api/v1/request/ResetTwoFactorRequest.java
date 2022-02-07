package com.example.usermanagement.web.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetTwoFactorRequest {
    @NotNull(message = "You must send a code with this request.")
    private String code;
}
