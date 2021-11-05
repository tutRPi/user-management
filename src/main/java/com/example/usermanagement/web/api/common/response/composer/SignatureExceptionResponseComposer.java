package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import io.jsonwebtoken.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SignatureExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<SignatureException> {

    public SignatureExceptionResponseComposer() {
        super(SignatureException.class);
    }

    @Override
    public ResponseEntity<BaseResponse> compose(SignatureException e) {
        return this.buildResponse(ErrorsEnum.JWT_SIGNATURE_INVALID);
    }
}
