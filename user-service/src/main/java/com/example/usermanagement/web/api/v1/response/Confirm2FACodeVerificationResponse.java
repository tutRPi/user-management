package com.example.usermanagement.web.api.v1.response;

import com.example.usermanagement.web.api.common.response.SuccessResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Confirm2FACodeVerificationResponse extends SuccessResponse {
    private List<String> codes;
}
