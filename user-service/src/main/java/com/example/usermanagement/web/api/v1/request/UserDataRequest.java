package com.example.usermanagement.web.api.v1.request;

import com.example.usermanagement.web.api.common.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDataRequest extends BaseRequest {
    @NotNull(message = "You must send a first name with this request.")
    @Size(min = 1, max = 100)
    private String firstName;
    @NotNull(message = "You must send a last name with this request.")
    @Size(min = 1, max = 100)
    private String lastName;
}
