package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.BaseResponse;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExceptionResponseComposerManager {
    private final Map<Class, AbstractExceptionResponseComposer> exceptionResponseComposerMap;

    public ExceptionResponseComposerManager(List<AbstractExceptionResponseComposer> exceptionResponseComposerList) {
        this.exceptionResponseComposerMap = exceptionResponseComposerList.stream().collect(
                Collectors.toMap(
                        AbstractExceptionResponseComposer::getWrappedExceptionClass,
                        Function.identity(),
                        (handler1, handler2) -> {
                            return AnnotationAwareOrderComparator.INSTANCE.compare(handler1, handler2) < 0 ? handler1 : handler2;
                        }
                )
        );
    }

    public Optional<ResponseEntity<BaseResponse>> delegateResponseComposition(Exception ex) {
        ResponseEntity<BaseResponse> toRet = null;
        AbstractExceptionResponseComposer composer = this.exceptionResponseComposerMap.get(ex.getClass());
        if (composer != null) {
            toRet = composer.compose(ex);
        }
        return Optional.ofNullable(toRet);
    }
}
