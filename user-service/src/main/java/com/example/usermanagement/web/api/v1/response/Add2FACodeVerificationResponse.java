package com.example.usermanagement.web.api.v1.response;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Add2FACodeVerificationResponse extends BaseResponse {
    private String t2FAQRCodeImageURL;
}
