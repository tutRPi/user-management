package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExpiredJwtExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<ExpiredJwtException> {

    public ExpiredJwtExceptionResponseComposer() {
        super(ExpiredJwtException.class);
    }

    @Override
    public ResponseEntity<ErrorResponse> compose(ExpiredJwtException e) {
        return this.buildResponse(ErrorsEnum.JWT_EXPIRED);
    }
}
