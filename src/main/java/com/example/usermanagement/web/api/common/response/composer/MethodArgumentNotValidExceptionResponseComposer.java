package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MethodArgumentNotValidExceptionResponseComposer extends AbstractExceptionResponseComposer<MethodArgumentNotValidException> {

    public MethodArgumentNotValidExceptionResponseComposer() {
        super(MethodArgumentNotValidException.class);
    }

    @Override
    public ResponseEntity<ErrorResponse> compose(MethodArgumentNotValidException e) {
        //TODO: Finish this logic
        ErrorResponse toRet = new ErrorResponse();
//        e.getBindingResult().getFieldErrors().forEach(objectError -> {
//            ErrorResponse errorResponse = new ErrorResponse();
//            errorResponse.setCode(775);
//            errorResponse.setField(objectError.getField());
//            errorResponse.setMessage(objectError.getDefaultMessage());
//            toRet.addResponseError(errorResponse);
//        });

        return ResponseEntity.badRequest().body(toRet);
    }
}
