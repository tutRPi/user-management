package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class CredentialsExpiredExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<CredentialsExpiredException> {

    public CredentialsExpiredExceptionResponseComposer() {
        super(CredentialsExpiredException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(CredentialsExpiredException e) {
        return this.buildResponse(ErrorsEnum.CREDENTIALS_EXPIRED);
    }
}
