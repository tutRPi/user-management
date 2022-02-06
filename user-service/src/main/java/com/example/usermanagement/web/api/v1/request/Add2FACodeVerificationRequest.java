package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class Add2FACodeVerificationRequest extends BaseRequest {
    @NotNull(message = "You must send the current password with this request.")
    @Size(min = 1, max = 255)
    private String currentPassword;
}
