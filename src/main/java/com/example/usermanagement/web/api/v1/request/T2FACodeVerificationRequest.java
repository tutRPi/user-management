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
public class T2FACodeVerificationRequest extends BaseRequest {
    @NotNull(message = "You must send a 2FA code with this request")
    @Size(min = 6, max = 6)
    @Max(999999)
    @Min(000000)
    private String t2FACode;
}
