package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DisabledExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<DisabledException> {

    public DisabledExceptionResponseComposer() {
        super(DisabledException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(DisabledException e) {
        return this.buildResponse(ErrorsEnum.ACCOUNT_DISABLED);
    }
}
