package com.hsbc.codechallenge.exceptions.handler;

import lombok.Getter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    private static String DEFAULT_VALIDATION_MESSAGE = "Validation error";

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return processFieldErrors(ex.getBindingResult().getFieldErrors());
    }

    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        return new Error(BAD_REQUEST.value(),
                DEFAULT_VALIDATION_MESSAGE,
                fieldErrors.stream()
                    .map(SimpleFieldError::new)
                    .collect(Collectors.toList())
                );
    }

    @Getter
    static class Error {
        final int status;
        final String message;
        List<SimpleFieldError> fieldErrors;

        Error(int status, String message, List<SimpleFieldError> fieldErrors) {
            this.status = status;
            this.message = message;
            this.fieldErrors = fieldErrors;
        }
    }

    @Getter
    static class SimpleFieldError {
        String message;
        String field;
        Object rejectedValue;

        SimpleFieldError(FieldError fe) {
            this.message = fe.getDefaultMessage();
            this.field = fe.getField();
            this.rejectedValue = fe.getRejectedValue();
        }
    }

}
