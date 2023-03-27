package com.briandidthat.priceserver.controller;

import com.briandidthat.priceserver.domain.exception.BackendClientException;
import com.briandidthat.priceserver.domain.exception.ExceptionDetails;
import com.briandidthat.priceserver.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleConstraintValidationException(MethodArgumentNotValidException e, WebRequest request) {
        List<ExceptionDetails> violations = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();

        for (FieldError violation : e.getFieldErrors()) {
            String message = violation.getField() + ": " + violation.getDefaultMessage();
            ExceptionDetails details = new ExceptionDetails(date, message, request.getDescription(false));
            violations.add(details);
        }

        System.out.println(violations);
        return new ResponseEntity(violations, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}