package com.example.usermanagement.web.api.v1.request;


import com.example.usermanagement.web.api.common.validator.PasswordConfirmation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordConfirmation
public class ChangePasswordRequest extends SimpleChangePasswordRequest implements SecurityCredentialsRequest {
    @NotNull(message = "You must send the current password with this request.")
    @Size(min = 1, max = 255)
    private String currentPassword;
}
