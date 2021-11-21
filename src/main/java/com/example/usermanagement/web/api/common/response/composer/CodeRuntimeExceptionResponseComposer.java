package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class CodeRuntimeExceptionResponseComposer extends AbstractAuthenticationExceptionResponseComposer<CodeRuntimeException> {

    public CodeRuntimeExceptionResponseComposer() {
        super(CodeRuntimeException.class);
    }

    @Override
    public ResponseEntity<ErrorResponse> compose(CodeRuntimeException e) {
        return this.buildResponse(e.getErrorsEnum());
    }
}
