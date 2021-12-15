package com.example.usermanagement.web.api.v1.request;


import com.example.usermanagement.web.api.common.request.BaseRequest;
import com.example.usermanagement.web.api.common.validator.PasswordConfirmation;
import com.example.usermanagement.web.api.common.validator.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordConfirmation
public class SimpleChangePasswordRequest extends BaseRequest implements SecurityCredentialsRequest {
    @NotNull(message = "You must send a new password with this request.")
    @ValidPassword
    private String password;
    @NotNull(message = "You must send a new password confirmation with this request.")
    private String passwordConfirmation;
}
