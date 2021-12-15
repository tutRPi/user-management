package com.example.usermanagement.web.api.v1.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LockUserRequest extends UserIdRequest {
    @NotNull(message = "User ID is needed.")
    private boolean lock;
}
