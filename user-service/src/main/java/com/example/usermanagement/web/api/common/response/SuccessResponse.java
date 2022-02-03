package com.example.usermanagement.web.api.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse extends BaseResponse {
    private boolean success = true;
}
