package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AccountExpiredExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<AccountExpiredException> {

    public AccountExpiredExceptionResponseComposer() {
        super(AccountExpiredException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(AccountExpiredException e) {
        return this.buildResponse(ErrorsEnum.ACCOUNT_EXPIRED);
    }
}
