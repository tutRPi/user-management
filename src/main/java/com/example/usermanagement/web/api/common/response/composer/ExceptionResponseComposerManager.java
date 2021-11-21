package com.example.usermanagement.web.api.common.response.composer;

import com.example.usermanagement.web.api.common.response.ErrorResponse;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    public Optional<ResponseEntity<ErrorResponse>> delegateResponseComposition(Exception ex, HttpServletRequest request) {
        ResponseEntity<ErrorResponse> result = null;
        AbstractExceptionResponseComposer composer = this.exceptionResponseComposerMap.get(ex.getClass());
        if (composer != null) {
            result = composer.compose(ex);
            result.getBody().setPath(request.getRequestURI());
        }
        return Optional.ofNullable(result);
    }
}
