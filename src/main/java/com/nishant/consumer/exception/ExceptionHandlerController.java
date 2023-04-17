package com.nishant.consumer.exception;

import com.google.common.collect.ImmutableMap;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    final static String MESSAGE = "error";

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<Object> badRequestException(CustomBadRequestException exception){
        return new ResponseEntity<>(ImmutableMap.of(MESSAGE, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultException(Exception exception){
        return new ResponseEntity<>(ImmutableMap.of(MESSAGE, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
