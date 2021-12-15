package com.example.usermanagement.web.api.v1.response;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTVerificationResponse extends BaseResponse {
    private boolean isJwtValid;
}
