package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.validator.PasswordConfirmation;
import com.example.usermanagement.web.api.common.validator.UniqueEmailUserSignup;
import com.example.usermanagement.web.api.common.validator.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@PasswordConfirmation
public class UserSignUpRequest extends UserDataRequest implements SecurityCredentialsRequest {
    @NotNull(message = "You must send an email with this request.")
    @Email(message = "Email Address not valid.")
    @Size(min = 5, max = 255)
    @UniqueEmailUserSignup
    @Schema(description = "Email Address of the user.", example = "test@example.com", required = true)
    private String email;
    @NotBlank(message = "You must send a password with this request.")
    @ValidPassword
    private String password;
    @NotBlank(message = "You must send a password confirmation with this request.")
    private String passwordConfirmation;
}
