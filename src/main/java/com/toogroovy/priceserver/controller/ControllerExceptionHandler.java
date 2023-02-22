package com.toogroovy.priceserver.controller;

import com.toogroovy.priceserver.domain.exception.BackendClientException;
import com.toogroovy.priceserver.domain.exception.ExceptionDetails;
import com.toogroovy.priceserver.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Error> handleHttpClientException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> handleResourceNotFoundException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BackendClientException.class)
    public ResponseEntity<Error> handleInternalServerError(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleOutOfRangeException(IllegalArgumentException e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ExceptionDetails> errors = new ArrayList<>();

        LocalDateTime date = LocalDateTime.now();
        for (FieldError fieldError : fieldErrors) {
            String message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            ExceptionDetails details = new ExceptionDetails(date, message, request.getDescription(false));
            errors.add(details);
        }

        return new ResponseEntity(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}