package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.validator.UniqueEmailUserSignup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDataEmailRequest extends UserDataRequest {
    @NotNull(message = "You must send an email with this request.")
    @Email
    @Size(min = 4, max = 255)
    @UniqueEmailUserSignup
    private String email;
}
