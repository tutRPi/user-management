package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.request.BaseRequest;
import com.example.usermanagement.web.api.common.validator.ValidUserId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserIdRequest extends BaseRequest {
    @NotNull(message = "User ID is needed.")
    @ValidUserId
    private Long userId;
}
