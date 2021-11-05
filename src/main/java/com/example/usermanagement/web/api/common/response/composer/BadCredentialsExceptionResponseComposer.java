package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class BadCredentialsExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<BadCredentialsException> {

    public BadCredentialsExceptionResponseComposer() {
        super(BadCredentialsException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(BadCredentialsException e) {
        return this.buildResponse(ErrorsEnum.BAD_CREDENTIALS);
    }
}
