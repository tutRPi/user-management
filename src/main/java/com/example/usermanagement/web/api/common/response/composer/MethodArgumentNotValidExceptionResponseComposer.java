package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ResponseError;
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
    public ResponseEntity<BaseResponse> compose(MethodArgumentNotValidException e) {
        //TODO: Finish this logic
        BaseResponse toRet = new BaseResponse();
        e.getBindingResult().getFieldErrors().forEach(objectError -> {
            ResponseError responseError = new ResponseError();
            responseError.setCode(775);
            responseError.setField(objectError.getField());
            responseError.setMessage(objectError.getDefaultMessage());
            toRet.addResponseError(responseError);
        });

        return ResponseEntity.badRequest().body(toRet);
    }
}
